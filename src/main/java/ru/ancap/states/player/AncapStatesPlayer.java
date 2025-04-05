package ru.ancap.states.player;

import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.codehaus.plexus.util.ExceptionUtils;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.debug.AncapDebug;
import ru.ancap.framework.communicate.communicator.util.CMMSerializer;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.library.AncapChunk;
import ru.ancap.library.Balance;
import ru.ancap.states.AncapStates;
import ru.ancap.states.chunk.OutpostChunk;
import ru.ancap.states.chunk.PrivateChunk;
import ru.ancap.states.dynmap.DynmapDrawer;
import ru.ancap.states.fees.ASFees;
import ru.ancap.states.id.ID;
import ru.ancap.states.states.Nation.Nation;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.city.LimitType;
import ru.ancap.states.util.DummyException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AncapStatesPlayer extends AncapPlayer {

    private final PathDatabase statesDB = AncapStates.getMainDatabase();
    public static Logger log = Bukkit.getLogger();

    public AncapStatesPlayer(String id) {
        super(id);
    }
    
    protected AncapStatesPlayer(String id, String name) {
        super(id, name);
    }
    
    public static AncapStatesPlayer link(String id) {
        return new AncapStatesPlayer(id);
    }
    
    public static AncapStatesPlayer get(Player player) {
        return new AncapStatesPlayer(Identifier.of(player), player.getName());
    }
    
    public static AncapStatesPlayer findByName(String name) throws PlayerNotFoundException {
        var link = AncapStatesPlayer.link(name.toLowerCase());
        if (!link.created()) throw new PlayerNotFoundException();
        return link;
    } 
    
    public static AncapStatesPlayer findByID(String id) {
        var player = new AncapStatesPlayer(id);
        if (!player.created()) throw new IllegalStateException("Couldn't find AncapStatesPlayer for id "+id);
        return player;
    }

    public static AncapStatesPlayer findByNameFor(String name, AncapStatesPlayer caller) {
        try {
            return AncapStatesPlayer.findByName(name);
        } catch (PlayerNotFoundException e) {
            caller.sendMessage(new LAPIMessage(
                AncapStates.class, "error.no-such-player",
                new Placeholder("nick", name)
            ));
        }
        throw new IllegalStateException();
    }

    public City getCity() {
        String cityID = this.statesDB.readString("states.player."+this.id()+".city");
        if (cityID == null) return null;
        return new City(cityID);
    }

    public Nation getNation() {
        City city = this.getCity();
        if (city == null) {
            return null;
        }
        if (city.isFree()) {
            return null;
        }
        return city.getNation();
    }

    public void addRequestTo(City city) {
        this.statesDB.add("states.city."+city.getID()+".requestsFromPlayers", this.id());
        this.statesDB.add("states.player."+this.id()+".requestsToCities", city.getID());
    }

    public void removeRequestTo(City city) {
        this.statesDB.remove("states.city."+city.getID()+".requestsFromPlayers", this.id());
        this.statesDB.remove("states.player."+this.id()+".requestsToCities", city.getID());
    }

    public boolean isFree() {
        return !statesDB.isSet("states.player."+this.id()+".city");
    }

    public boolean haveCityCreationFee() {
        return this.getBalance().getIron()>=32;
    }

    public boolean isMayor() {
        City city = this.getCity();
        return AncapStatesPlayer.findByID(this.statesDB.readString("states.city."+city.getID()+".mayor")).equals(this);
    }

    public void grabCityCreationFee() {
        Balance balance = this.getBalance();
        balance.remove(ASFees.CITY_CREATION);
        this.setBalance(balance);
    }

    public boolean isInvitedTo(City city) {
        return this.statesDB.contains("states.player."+this.id()+".invitesFromCities", city.getID());
    }

    public boolean isAskingToJoinIn(City city) {
        return this.statesDB.contains("states.city."+city.getID()+".requestsFromPlayers", this.id());
    }

    public void leaveCity() {
        this.dropCityMayoring();
        City city = this.getCity();
        city.removeResident(this);
    }

    public void dropCityMayoring() {
        if (!this.isFree()) {
            City city = this.getCity();
            List<AncapStatesPlayer> players = city.getResidents();
            if (players.size() == 1) {
                city.remove();
            } else {
                AncapStatesPlayer newMayor = players.get(0);
                if (newMayor.equals(this)) {
                    newMayor = players.get(1);
                }
                AncapDebug.debug(
                    "MAYOR DEBUG: calling setMayor() from dropCityMayoring()",
                    "current mayor", city.mayor().id(), "new mayor", newMayor.id(), "\n",
                    ExceptionUtils.getStackTrace(new DummyException())
                );
                city.setMayor(newMayor);
            }
        }
    }

    public boolean isAssistant() {
        City city = this.getCity();
        return this.statesDB.contains("states.city."+city.getID()+".assistants", this.id(), true);
    }

    public boolean isResidentOf(City city) {
        return this.statesDB.contains("states.city."+city.getID()+".residents", this.id(), true);
    }

    public boolean canTeleportToCitySpawn() {
        return this.getBalance().getDiamond()>=0.5 || this.haveFreeTeleport() || this.online().hasPermission("ru.ancap.states.free-city-spawn");
    }

    public boolean haveFreeTeleport() {
        return !this.statesDB.isSet("states.player."+this.id()+".freeTeleportUsed");
    }

    public void tryCitySpawn() throws NotEnoughMoneyException {
        City city = this.getCity();
        Location loc = city.getHome();
        String teleportReason;
        if (!this.online().hasPermission("ru.ancap.states.free-city-spawn")) {
            if (this.haveFreeTeleport()) {
                this.statesDB.write("states.player."+this.id()+".freeTeleportUsed", "true");
                teleportReason = "by-first-free";
            } else {
                if (this.getBalance().get(Balance.DIAMOND) < 0.5) throw new NotEnoughMoneyException();
                this.grabCityTeleportFee();
                teleportReason = "for-paid";
            }
        } else teleportReason = "for-free-by-permission";
        this.sendMessage(new LAPIMessage(AncapStates.class, "teleported-to-city-spawn."+teleportReason));
        this.online().teleport(loc);
    }

    public void grabCityTeleportFee() {
        Balance balance = this.getBalance();
        balance.remove(ASFees.CITY_TELEPORT);
        this.setBalance(balance);
    }

    @Override
    public String toString() {
        return "AncapStatesPlayer{"+this.id()+"}";
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == null) {
            return false;
        }
        if (!AncapStatesPlayer.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        AncapStatesPlayer other = (AncapStatesPlayer) obj;
        return other.id().equals(this.id());
    }

    public boolean canClaimPrivateChunks() {
        return true;
    }

    public AncapChunk getChunk() {
        Chunk chunk = this.online().getLocation().getChunk();
        return new AncapChunk(chunk.getX()+";"+chunk.getZ());
    }

    public PrivateChunk getPrivateChunk() {
        City city = this.getCity();
        AncapChunk chunk = this.getChunk();
        return new PrivateChunk(city, chunk.getID());
    }

    @Nullable
    public City getCityAtPosition() {
        Location loc = this.online().getLocation();
        return AncapStates.cityMap().getCity(loc);
    }

    public List<PrivateChunk> getPrivateChunks() {
        if (this.isFree()) {
            return List.of();
        }
        List<PrivateChunk> cityPrivateChunks = this.getCity().getPrivateChunks();
        ArrayList<PrivateChunk> chunks = new ArrayList<>();
        ArrayList<PrivateChunk> playerChunks = new ArrayList<>();
        chunks.addAll(cityPrivateChunks);
        for (int i = 0; i<chunks.size(); i++) {
            if (chunks.get(i).getOwner().equals(this)) {
                playerChunks.add(chunks.get(i));
            }
        }
        return playerChunks;
    }

    public boolean canClaimNewPrivateChunk() {
        if (this.isFree()) {
            return false;
        }
        City city = this.getCity();
        List<PrivateChunk> chunks = this.getPrivateChunks();
        LimitType type = new LimitType("resident");
        if (this.isAssistant()) {
            type = new LimitType("assistants");
        }
        String rangLimitString = this.statesDB.readString("states.city."+city.getID()+".limit."+type);
        String personalLimitString = this.statesDB.readString("states.city."+city.getID()+".limit.personal."+this.id());
        int rangLimit = 3;
        int personalLimit = 0;
        if (rangLimitString != null) {
            rangLimit = Integer.parseInt(rangLimitString);
        }
        if (personalLimitString != null) {
            personalLimit = Integer.parseInt(personalLimitString);
        }
        if (chunks.size() >= rangLimit && chunks.size() >= personalLimit) {
            return false;
        }
        return true;
    }

    public OutpostChunk getOutpostChunk() {
        return new OutpostChunk(this.online());
    }

    public void addFriend(AncapStatesPlayer friend) {
        this.statesDB.add("states.player."+this.id()+".friends", friend.id(), true);
    }

    public void removeFriend(AncapStatesPlayer friend) {
        this.statesDB.add("states.player."+this.id()+".friends", friend.id(), true);
    }

    public AncapStatesPlayerInfo getInfo() {
        return new AncapStatesPlayerInfo(this);
    }

    public boolean isLeader() {
        return this.getCity().getNation().getCapital().mayor().equals(this);
    }

    public void prepareToJoinInCity() {
        this.revokeAllRequestsToCities();
        this.cancelAllInvitesFromCities();
    }

    public void cancelAllInvitesFromCities() {
        List<String> invitesFromCities = this.statesDB.readStrings("states.player."+this.id()+".invitesFromCities", true);
        for (String invite : invitesFromCities) {
            City city = new City(invite);
            this.declineInviteFrom(city);
        }
    }

    public void revokeAllRequestsToCities() {
        List<String> requestsFromCities = this.statesDB.readStrings("states.player."+this.id()+".requestsFromCities", true);
        for (String request : requestsFromCities) {
            City city = new City(request);
            this.declineInviteFrom(city);
        }
    }

    public boolean isMinister() {
        Nation nation = this.getCity().getNation();
        return this.statesDB.contains("states.nation."+nation.getIDString()+".ministers", this.id(), true);
    }

    public boolean isCitizenOf(Nation nation) {
        List<City> cities = nation.getCities();
        for (City city : cities) {
            List<AncapStatesPlayer> players = city.getResidents();
            for (AncapStatesPlayer resident : players) {
                if (this.equals(resident)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLicentiate() {
        return this.online().hasPermission("ancapstates.licentiate");
    }

    public void declineInviteFrom(City city) {
        this.statesDB.remove("states.player."+this.id()+".invitesFromCities", city.getID(), true);
        this.statesDB.remove("states.city."+city.getID()+".invitesToPlayers", this.id(), true);
    }

    public List<AncapStatesPlayer> getFriends() {
        return this.statesDB.readStrings("states.player."+this.id()+".friends", true).stream()
            .map(AncapStatesPlayer::findByID)
            .toList();
    }

    public List<City> getInviting() {
        return this.statesDB.readStrings("states.player."+this.id()+".invitesFromCities", true).stream()
            .map(City::new).toList();
    }

    public List<City> getRequesting() {
        return this.statesDB.readStrings("states.player."+this.id()+".requestsToCities", true).stream()
            .map(City::new).toList();
    }

    public String getFriendsNames() {
        return String.join(", ", this.statesDB.readStrings("states.player."+this.id()+".friends", true));
    }

    public String getInvitingNames() {
        return String.join(", ", this.statesDB.readStrings("states.player."+this.id()+".invitesFromCities", true));
    }

    public String getRequestingNames() {
        return String.join(", ", this.statesDB.readStrings("states.player."+this.id()+".requestsToCities", true));
    }

    public void sendJoinTitle(City city) {
        if (city == null) {
            this.sendWildernessTitle();
            return;
        }
        this.sendCityJoinTitle(city);
    }

    private void sendCityJoinTitle(City city) {
        String board = city.getBoard();
        String identifier = Identifier.of(this.online());
        if (board == null) {
            board = new LAPIMessage(AncapStates.class, "board.not-setted").call(identifier);
        }
        board = "<italic>"+board+"</italic>";
        this.online().showTitle(Title.title(
            CMMSerializer.serialize(new LAPIMessage(
                AncapStates.class, "notify.entered-city",
                new Placeholder("name", city.getName())
            ).call(identifier)), 
            CMMSerializer.serialize(new Message(board).call(identifier)),
            Title.Times.times(
                Duration.of(1500, ChronoUnit.MILLIS),
                Duration.of(1500, ChronoUnit.MILLIS),
                Duration.of(1500, ChronoUnit.MILLIS)
            )
        ));
    }

    private void sendWildernessTitle() {
        String identifier = Identifier.of(this.online());
        this.online().showTitle(Title.title(
            CMMSerializer.serialize(new LAPIMessage(AncapStates.class, "notify.entered-wilderness.title").call(identifier)),
            CMMSerializer.serialize(new LAPIMessage(AncapStates.class, "notify.entered-wilderness.subtitle").call(identifier)),
            Title.Times.times(
                Duration.of(1500, ChronoUnit.MILLIS),
                Duration.of(1500, ChronoUnit.MILLIS),
                Duration.of(1500, ChronoUnit.MILLIS)
            )
        ));
    }

    public void transferMoney(AncapStatesPlayer recipient, double amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance recipientBalance = recipient.getBalance();
        playerBalance.remove(type, amount);
        recipientBalance.add(type, amount);
        recipient.setBalance(recipientBalance);
        this.setBalance(playerBalance);
    }

    public void depositCity(City city, int amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance cityBalance = city.getBalance();
        playerBalance.remove(type, amount);
        cityBalance.add(type, amount);
        city.setBalance(cityBalance);
        this.setBalance(playerBalance);
    }

    public void depositNation(Nation nation, int amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance nationBalance = nation.getBalance();
        playerBalance.remove(type, amount);
        nationBalance.add(type, amount);
        nation.setBalance(nationBalance);
        this.setBalance(playerBalance);
    }

    public void withdrawCity(City city, int amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance cityBalance = city.getBalance();
        playerBalance.add(type, amount);
        cityBalance.remove(type, amount);
        city.setBalance(cityBalance);
        this.setBalance(playerBalance);
    }

    public void withdrawNation(Nation nation, int amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance nationBalance = nation.getBalance();
        playerBalance.add(type, amount);
        nationBalance.remove(type, amount);
        nation.setBalance(nationBalance);
        this.setBalance(playerBalance);
    }

    public boolean isFriendOf(AncapStatesPlayer player) {
        return this.getFriends().stream().anyMatch(friend -> friend.equals(player));
    }

    public void resqueCity() {
        City city = this.getCity();
        if (city == null) return;
        log.info("Player "+this.getName()+" trying to rescue "+city.getName());
        Balance balance = new Balance(Map.of("netherite", 0.5));
        if (!this.getBalance().have(balance)) {
            log.info("Player "+this.getName()+" haven't enough money to rescue "+city.getName());
            return;
        }
        log.info("Player "+this.getName()+" rescuing "+city.getName());
        this.transferMoney(city, balance);
    }

    public Hexagon getHexagon() {
        return AncapStates.grid.hexagon(this);
    }

    public boolean canInteract(Location location) {
        boolean canBasically = this.canInteract0(location);
        if (!canBasically) {
            var online = this.online();
            if (online != null && online.isOp()) {
                online.sendMessage("Взаимодействие с защищённым блоком разрешено правами оператора");
                return true;
            } else {
                return false;
            }
        } else return true;
    }
    
    private boolean canInteract0(Location loc) {
        try {
            if (loc.getWorld().getName().equals("world_the_end") || 
                loc.getWorld().getName().equals("world_nether")) return true;
            City city = AncapStates.cityMap().getCity(loc);
            if (city == null) {
                return true;
            }
            PrivateChunk chunk = city.getPrivateChunk(loc);
            int remoteness = city.getRemoteness(this);
            int allowLevel = city.getAllowLevel().getInt();
            if (chunk.getOwner() == null) {
                return remoteness <= allowLevel;
            }
            if (remoteness>allowLevel) {
                return false;
            }
            if (chunk.getOwner() != null && !chunk.getOwner().equals(this) && !chunk.getOwner().isFriendOf(this)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean canInteract(@Nullable City entered) {
        if (entered == null) return true;
        return this.canInteract(entered.getHome());
    }

    public void prepareToLeaveCity() {
        City city = this.getCity();
        if (city == null) return;
        for (PrivateChunk privateChunk : this.getPrivateChunks()) {
            city.removePrivateChunk(privateChunk.asAncapChunk());
        }
    }

    public void removeCityJoining() {
        this.statesDB.delete("states.player."+this.id()+".city");
    }

    public boolean isMayorOf(City city) {
        if (!this.isResidentOf(city)) return false;
        return this.isMayor();
    }

    public Nation createTestNation() {
        Balance bigBalance = new Balance(10000, 10000, 10000);
        City city = this.createTestCity();
        String nationName = this.getName()+"Nation";
        Nation nation = new Nation(ID.getNationID(nationName));
        nation.create(city, nationName);
        nation.setBalance(bigBalance);
        DynmapDrawer.redrawDynmap();
        return nation;
    }

    public City createTestCity() {
        Balance bigBalance = new Balance(10000, 10000, 10000);
        this.setBalance(bigBalance);
        String cityName = this.getName()+"City";
        City city = new City(ID.getCityID(cityName));
        city.create(this, cityName);
        for (Hexagon neighbor : this.getHexagon().neighbors(2)) city.addHexagon(neighbor);
        city.setBalance(bigBalance);
        DynmapDrawer.redrawDynmap();
        return city;
    }
    
}