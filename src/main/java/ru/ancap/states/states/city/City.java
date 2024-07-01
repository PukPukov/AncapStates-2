package ru.ancap.states.states.city;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.debug.AncapDebug;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.hexagon.common.Point;
import ru.ancap.library.AncapChunk;
import ru.ancap.library.Balance;
import ru.ancap.library.LocationSerializeWorker;
import ru.ancap.states.AncapStates;
import ru.ancap.states.chunk.OutpostChunk;
import ru.ancap.states.chunk.PrivateChunk;
import ru.ancap.states.dynmap.DynmapDescription;
import ru.ancap.states.dynmap.DynmapDrawer;
import ru.ancap.states.event.events.CityDeleteEvent;
import ru.ancap.states.event.events.CityFoundEvent;
import ru.ancap.states.fees.ASFees;
import ru.ancap.states.main.AncapStatesDatabaseType;
import ru.ancap.states.message.LStateMessage;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.Nation.Nation;
import ru.ancap.states.states.State;
import ru.ancap.states.states.StateName;
import ru.ancap.states.states.StateType;
import ru.ancap.states.states.Subject;

import java.util.*;
import java.util.logging.Logger;

public class City implements State {

    private final String id;

    public static Logger log = Bukkit.getLogger();

    private PathDatabase statesDB = AncapStates.getMainDatabase();

    private PathDatabase idDB = AncapStates.getAncapStatesDatabase(AncapStatesDatabaseType.IDLINK_DATABASE);

    public String getID() {
        return this.id;
    }

    public City(String id) {
        this.id = id;
    }

    @Override
    public StateName name() {
        return new StateName(StateType.CITY, this.getName());
    }

    @Override
    public String type() {
        return StateType.CITY.toString();
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public void affiliate(@Nullable Subject affiliate) {
        if (affiliate == null) {
            this.leaveNation();
            return;
        }
        if (!(affiliate instanceof Nation nation)) throw new IllegalStateException();
        else nation.addCity(this);
    }
    
    @Override
    public Subject affiliate() {
        return this.getNation();
    }

    public void sendMessage(CallableMessage message) {
        List<AncapStatesPlayer> players = this.getResidents();
        for (AncapStatesPlayer player : players) {
            player.sendMessage(message);
        }
    }

    @Override
    public String simpleName() {
        return this.getName();
    }

    public void create(AncapStatesPlayer creator, String name) {
        new CityFoundEvent(creator, name, this).callEvent();
    }

    public void initialize(AncapStatesPlayer creator, String name) {
        creator.prepareToJoinInCity();
        String mayorID = creator.id();
        this.statesDB.write("states.player."+creator.id()+".city", this.id);
        this.statesDB.write("states.city."+this.id+".balance", new Balance(new HashMap<>()), Balance.SERIALIZE_WORKER);
        this.statesDB.write("states.city."+this.id+".tax", new Balance(new HashMap<>()), Balance.SERIALIZE_WORKER);
        this.statesDB.write("states.city."+this.id+".name", name);
        AncapDebug.debug("MAYOR DEBUG: writing mayor in City#initialize()", mayorID);
        this.statesDB.write("states.city."+this.id+".mayor", mayorID);
        this.statesDB.write("states.city."+this.id+".residents", List.of(mayorID));
        this.statesDB.write("states.city."+this.id+".home", creator.online().getLocation(), LocationSerializeWorker.INSTANCE);
        Hexagon core = AncapStates.grid.hexagon(creator);
        this.statesDB.write("states.city."+this.id+".hexagons", List.of(core.code()+""));
        this.statesDB.write("states.hexagons."+AncapStates.grid.hexagon(creator).code()+".owner", this.id);
        AncapStates.incrementGlobalPopulation();
    }
    
    public CityEventAPI event() {
        return new CityEventAPI(this);
    }

    public void remove() {
        Bukkit.getScheduler().callSyncMethod(AncapStates.instance(), () -> {
            new CityDeleteEvent(this).callEvent();
            return Void.TYPE;
        });
    }

    public void delete() {
        this.prepareToDelete();
        this.statesDB.delete("states.city."+this.id);
    }

    public void dropNationCapitality() {
        if (!this.isFree()) {
            Nation nation = this.getNation();
            List<City> cities = nation.getCities();
            if (cities.size() < 2) {
                nation.remove();
                CallableMessage message = LStateMessage.NATION_REMOVE(nation.getName());
                AncapStates.sendMessage(message);
            } else {
                City newCapital = cities.get(0);
                if (newCapital.equals(this)) {
                    newCapital = cities.get(1);
                }
                nation.setCapital(newCapital);
            }
        }
    }

    public void prepareToDelete() {
        this.dropNationCapitality();
        this.unclaimAllHexagonsNoChecks();
        this.kickAllResidents();
        this.prepareToJoinInNation();
        this.leaveNation();
    }

    public void kickAllResidents() {
        List<AncapStatesPlayer> players = this.getResidents();
        for (AncapStatesPlayer player : players) this.removeResidentNoChecks(player);
        this.statesDB.write("states.city."+this.id+".residents", List.of());
    }

    public void unclaimAllHexagonsNoChecks() {
        this.getTerritories().forEach(this::removeHexagonNoChecks);
    }

    public String getName() {
        return this.statesDB.readString("states.city."+this.id+".name");
    }

    public void setName(String name) {
        String oldName = this.statesDB.readString("states.city."+this.id+".name");
        this.statesDB.write("states.city."+this.id+".name", name);
        idDB.delete("ids.city_"+oldName);
        idDB.write("ids.city_"+name, this.id);
    }

    public List<AncapStatesPlayer> getResidents() {
        return this.statesDB.readStrings("states.city."+this.id+".residents", true).stream()
            .map(AncapStatesPlayer::findByID).toList();
    }

    public List<AncapStatesPlayer> getAssistants() {
        return this.statesDB.readStrings("states.city."+this.id+".assistants", true).stream()
            .map(AncapStatesPlayer::findByID).toList();
    }

    public void addAssistant(AncapStatesPlayer ancapStatesPlayer) {
        this.statesDB.add("states.city."+this.id+".assistants", ancapStatesPlayer.id(), true);
    }

    public void removeAssistant(AncapStatesPlayer ancapStatesPlayer) {
        this.statesDB.remove("states.city."+this.id+".assistants", ancapStatesPlayer.id(), true);
    }

    public AncapStatesPlayer mayor() {
        return AncapStatesPlayer.findByID(this.statesDB.readString("states.city."+this.id+".mayor"));
    }
    
    @Deprecated
    public AncapStatesPlayer getMayor() {
        return this.mayor();
    }

    public void setMayor(AncapStatesPlayer ancapStatesPlayer) {
        String id = ancapStatesPlayer.id();
        AncapDebug.debug("MAYOR DEBUG: writing mayor in City#setMayor()", id);
        this.statesDB.write("states.city."+this.id+".mayor", id);
    }

    public Balance getBalance() {
        return this.statesDB.read("states.city."+this.id+".balance", Balance.SERIALIZE_WORKER);
    }

    @Override
    public PathDatabase database() {
        return this.statesDB.inner("states.city."+this.id);
    }

    @Override
    public Balance balance() {
        return this.getBalance();
    }

    public void setBalance(Balance balance) {
        this.statesDB.write("states.city."+this.id+".balance", balance, Balance.SERIALIZE_WORKER);
    }

    public AllowLevel getAllowLevel() {
        String allowLevelString = this.statesDB.readString("states.city."+this.id+".allowlevel");
        if (allowLevelString == null) {
            return new AllowLevel(4);
        }
        int allowLevel = Integer.parseInt(allowLevelString);
        return new AllowLevel(allowLevel);
    }

    public void setAllowLevel(AllowLevel allowLevel) {
        this.statesDB.write("states.city."+this.id+".allowlevel", String.valueOf(allowLevel.getInt()));
    }

    public Nation getNation() {
        String nationID = this.statesDB.readString("states.city." + this.id + ".nation");
        if (nationID == null) {
            return null;
        }
        return new Nation(nationID);
    }

    public void setNation(Nation nation) {
        if (nation == null) {
            this.statesDB.delete("states.city."+this.id+".nation");
        } else {
            this.statesDB.write("states.city."+this.id+".nation", nation.getIDString());
        }
    }

    public String getBoard() {
        return this.statesDB.readString("states.city."+this.id+".board");
    }

    public void setBoard(String board) {
        this.statesDB.write("states.city."+this.id+".board", board);
    }

    public List<Hexagon> getTerritories() {
        return this.statesDB.readStrings("states.city."+this.id+".hexagons", true).stream()
            .map(Long::valueOf)
            .map(code -> AncapStates.grid.hexagon(code))
            .toList();
    }

    public CityInfo getInfo() {
        return new CityInfo(this);
    }

    public boolean exists() {
        return this.statesDB.isSet("states.city."+this.id+".name");
    }

    public void addResident(AncapStatesPlayer player) {
        player.prepareToJoinInCity();
        this.statesDB.add("states.city."+this.id+".residents", player.id(), true);
        this.statesDB.write("states.player."+player.id()+".city", this.id);
        AncapStates.incrementGlobalPopulation();
    }

    public void removeResident(AncapStatesPlayer player) {
        this.prepareToRemoveResident(player);
        this.statesDB.remove("states.city."+this.id+".residents", player.id(), true);
        AncapStates.decrementGlobalPopulation();
    }

    public void prepareToRemoveResident(AncapStatesPlayer player) {
        player.prepareToLeaveCity();
        player.removeCityJoining();
        if (this.getResidents().size() == 1) this.remove();
    }

    public void removeResidentNoChecks(AncapStatesPlayer player) {
        this.statesDB.remove("states.city."+this.id+".residents", player.id(), true);
        this.statesDB.delete("states.player."+player.id()+".city");
    }

    public void addInviteTo(AncapStatesPlayer player) {
        this.statesDB.add("states.city."+this.id+".invitesToPlayers", player.id(), true);
        this.statesDB.add("states.player."+player.id()+".invitesFromCities", this.getID(), true);
    }

    public void removeInviteTo(AncapStatesPlayer player) {
        this.statesDB.remove("states.city."+this.id+".invitesToPlayers", player.id(), true);
        this.statesDB.remove("states.player."+player.id()+".invitesFromCities", this.id, true);
    }

    public void addRequestTo(Nation nation) {
        this.statesDB.add("states.city."+this.id+".requestsToNations", nation.getIDString(), true);
        this.statesDB.add("states.nation."+nation.getIDString()+".requestsFromCities", this.id, true);
    }

    public void removeRequestTo(Nation nation) {
        this.statesDB.remove("states.city."+this.id+".requestsToNations", nation.getIDString(), true);
        this.statesDB.remove("states.nation."+nation.getIDString()+".requestsFromCities", this.id, true);
    }

    public void declineRequestFrom(AncapStatesPlayer player) {
        this.statesDB.remove("states.city."+this.id+".requestsFromPlayers", player.id(), true);
        this.statesDB.remove("states.player."+player.id()+".requestsToCities", this.id, true);
    }

    public void declineInviteFrom(Nation nation) {
        this.statesDB.remove("states.city."+this.id+".invitesFromNations", nation.getIDString(), true);
        this.statesDB.remove("states.nation."+nation.getIDString()+".invitesToCities", this.id, true);
    }

    public void setFlag(String flag) {
        this.statesDB.add("states.city."+this.id+".flags", flag, true);
    }

    public void removeFlag(String flag) {
        this.statesDB.remove("states.city."+this.id+".flags", flag, true);
    }

    public boolean freeToJoin() {
        return this.haveFlag("FREE_TO_JOIN");
    }

    public boolean haveFlag(String flag) {
        return this.statesDB.contains("states.city."+this.id+".flags", flag, true);
    }

    @Override
    public String toString() {
        return "City{"+this.getID()+"}";
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == null) return false;
        if (!City.class.isAssignableFrom(obj.getClass())) return false;
        City other = (City) obj;
        return Objects.equals(other.getID(), this.getID());
    }

    public void setLimit(AncapStatesPlayer limited, int limit) {
        this.statesDB.write("states.city."+this.id+".limit.personal."+limited.id(), String.valueOf(limit));
    }

    public void setLimit(LimitType limitType, int limit) {
        this.statesDB.write("states.city."+this.id+".limit."+limitType.toString(), String.valueOf(limit));
    }

    public void addPrivateChunk(AncapStatesPlayer player, AncapChunk ancapChunk) {
        this.statesDB.write("states.city."+this.id+".chunks."+ancapChunk.toString(), player.id());
    }
    public void removePrivateChunk(AncapChunk ancapChunk) {
        this.statesDB.delete("states.city."+this.id+".chunks."+ancapChunk.toString());
    }

    public boolean canClaimNewOutpostChunk() {
        return this.getOutpostChunks().size()<this.getResidents().size()*3;
    }

    public List<AncapChunk> getOutpostChunks() {
        return this.statesDB.inner("states.chunks").keys().stream()
            .map(OutpostChunk::new)
            .filter(chunk -> Objects.equals(chunk.getOwner(), this))
            .map(OutpostChunk::id)
            .map(AncapChunk::new)
            .toList();
    }

    public void addOutpostChunk(AncapChunk ancapChunk) {
        this.statesDB.write("states.chunks."+ancapChunk.toString(), this.id);
    }

    public void removeOutpostChunk(AncapChunk ancapChunk) {
        this.statesDB.delete("states.chunks."+ancapChunk.toString());
    }

    public boolean haveHexagonClaimingFee() {
        Balance balance = this.getBalance();
        return balance.getDiamond()>=0.5;
    }

    public boolean canAttach(Hexagon hexagon) {
        for (Hexagon neighbor : hexagon.neighbors(1)) {
            if (Objects.equals(AncapStates.cityMap().getCity(neighbor), this)) return true;
        }
        return false;
    }

    public void grabHexagonClaimingFee() {
        Balance balance = this.getBalance();
        balance.remove(ASFees.HEXAGON_CLAIM);
        this.setBalance(balance);
    }

    public void addHexagon(Hexagon hexagon) {
        this.statesDB.write("states.hexagons."+hexagon.code()+".owner", this.id);
        this.statesDB.add("states.city."+this.id+".hexagons", ""+hexagon.code(), true);
    }

    public Hexagon getHomeHexagon() {
        return AncapStates.grid.hexagon(this.statesDB.read("states.city."+this.id+".home", LocationSerializeWorker.INSTANCE));
    }

    public void giveHexagonUnclaimingRepayment() {
        Balance balance = this.getBalance();
        balance.add(Balance.DIAMOND, 0.1);
    }

    public void removeHexagon(Hexagon hexagon) {
        if (this.getHomeHexagon().code() == hexagon.code()) this.remove();
        this.removeHexagonNoChecks(hexagon);
    }

    public void removeHexagonNoChecks(Hexagon hexagon) {
        this.statesDB.delete("states.hexagons."+hexagon.code()+".owner");
        this.statesDB.remove("states.city."+this.id+".hexagons", ""+hexagon.code(), true);
    }

    public List<AncapStatesPlayer> getRequestingPlayers() {
        return this.statesDB.readStrings("states.city."+this.getID()+".requestsFromPlayers", true).stream()
            .map(AncapStatesPlayer::findByID).toList();
    }

    public int getRemoteness(AncapStatesPlayer player) {
        City playerCity = player.getCity();
        AncapStatesPlayer cityLeader = this.mayor();
        if (player.equals(cityLeader)) {
            return 1;
        }
        if (playerCity != null) {
            if (playerCity.equals(this)) {
                if(player.isAssistant()) {
                    return 2;
                }
                return 3;
            }
            Nation nation = this.getNation();
            Nation playerNation = playerCity.getNation();
            if (nation != null && nation.equals(playerNation)) {
                if (player.isLeader()) {
                    return 4;
                }
                if (player.isMinister()) {
                    return 5;
                }
                return 6;
            }
        }
        if (player.isLicentiate()) {
            return 7;
        }
        return 8;
    }

    public boolean isFree() {
        return this.statesDB.readString("states.city."+this.id+".nation") == null;
    }

    public boolean haveNationCreatingFee() {
        return this.getBalance().getNetherite() >= 1;
    }

    public void grabNationCreationFee() {
        Balance balance = this.getBalance();
        balance.remove(ASFees.NATION_CREATION);
        this.setBalance(balance);
    }

    public void prepareToJoinInNation() {
        this.cancelAllInvitesToNation();
        this.cancelAllRequestsToNation();
    }

    public void cancelAllRequestsToNation() {
        this.statesDB.readStrings("states.city."+this.id+".requestsToNations", true).stream()
            .map(Nation::new)
            .forEach(this::removeRequestTo);
    }

    public void cancelAllInvitesToNation() {
        this.statesDB.readStrings("states.city."+this.id+".invitesFromNations", true).stream()
            .map(Nation::new)
            .forEach(this::declineInviteFrom);
    }

    public boolean isInvitedTo(Nation nation) {
        return this.statesDB.contains("states.city."+this.id+".invitesFromNations", nation.getIDString(), true);
    }

    public void leaveNation() {
        Nation nation = this.getNation();
        if (nation == null) return;
        this.dropNationCapitality();
        this.statesDB.delete("states.city."+this.id+".nation");
        this.statesDB.remove("states.nation."+nation.getIDString()+".cities", this.id, true);
    }

    public void leaveNationNoChecks() {
        Nation nation = this.getNation();
        if (nation == null) {
            return;
        }
        this.statesDB.delete("states.city."+this.id+".nation");
        this.statesDB.remove("states.nation."+nation.getIDString()+".cities", this.id, true);
    }

    public boolean isRequestingTo(Nation nation) {
        return this.statesDB.contains("states.city."+this.id+".requestsToNations", nation.getIDString(), true);
    }

    public Location getHome() {
        return this.statesDB.read("states.city."+this.id+".home", LocationSerializeWorker.INSTANCE);
    }

    public List<PrivateChunk> getPrivateChunks() {
        return this.statesDB.inner("states.city."+this.id+".chunks").keys().stream()
            .map(id -> new PrivateChunk(this, id))
            .toList();
    }

    public boolean isInviting(AncapStatesPlayer player) {
        return player.isInvitedTo(this);
    }
    
    public String getIcon() {
        String color = this.getColor();
        String icon = "blueflag";
        if (this.isFree()) {
            icon = "greenflag";
        } else if (this.mayor().isLeader()) {
            icon = "king";
        }
        return icon;
    }

    public void draw() {
        String color = this.getColor();
        String icon = this.getIcon();
        DynmapDrawer drawer = new DynmapDrawer();
        Set<Hexagon> territoriesSet = new HashSet<>(this.getTerritories());
        drawer.drawFigure(territoriesSet, color, this.getDescription());
        drawer.draw(new Point(this.getHome().getBlockX(), this.getHome().getBlockZ()), icon, this.getDescription());
    }

    public DynmapDescription getDescription() {
        return this.getInfo().toDynmapFormat();
    }

    public String getColor() {
        if (this.isFree()) {
            return "#575757";
        }
        Nation nation = this.getNation();
        return nation.getColor();
    }

    public AncapChunk getChunk(Location loc) {
        Chunk chunk = loc.getChunk();
        return new AncapChunk(chunk.getX()+";"+chunk.getZ());
    }

    public PrivateChunk getPrivateChunk(Location loc) {
        AncapChunk chunk = this.getChunk(loc);
        return new PrivateChunk(this, chunk.getID());
    }

    public boolean isIntegratedIn(Nation nation) {
        return this.getNation() != null & this.getNation().equals(nation);
    }

    public void transferMoney(Nation recipient, double amount, String type) {
        Balance cityBalance = this.getBalance();
        Balance recipientBalance = recipient.getBalance();
        cityBalance.remove(type, amount);
        recipientBalance.add(type, amount);
        recipient.setBalance(recipientBalance);
        this.setBalance(cityBalance);
    }

    public void collectTaxes() {
        log.info("Collecting "+this.getName()+" taxes");
        Balance tax = this.getTax();
        List<AncapStatesPlayer> residents = this.getResidents();
        Balance cityBalance = this.getBalance();
        for (AncapStatesPlayer resident : residents) {
            Balance balance = resident.getBalance();
            if (balance.have(tax)) {
                balance.remove(tax);
                cityBalance.add(tax);
                resident.setBalance(balance);
            } else {
                this.removeResident(resident);
                CallableMessage message = LStateMessage.PLAYER_CANT_PAY_TAXES_AND_KICKED(resident.getName());
                this.sendMessage(message);
            }
        }
        this.setBalance(cityBalance);
    }

    public Balance getTax() {
        return this.statesDB.read("states.city."+this.id+".tax", Balance.SERIALIZE_WORKER);
    }

    public void setTax(Balance balance) {
        this.statesDB.write("states.city."+this.id+".tax", balance, Balance.SERIALIZE_WORKER);
    }

    public Balance getMaintenanceTax() {
        double fee = 0.5;
        return new Balance(0, this.getTerritories().size()*fee, 0);
    }

    public void grabTaxes() {
        log.info("Grabbing "+this.getName()+" taxes");
        Balance tax = this.getMaintenanceTax();
        Balance balance = this.getBalance();
        if (balance.have(tax)) {
            balance.remove(tax);
            this.setBalance(balance);
        } else {
            this.askForResque();
            balance = this.getBalance();
            if (balance.have(tax)) {
                balance.remove(tax);
                this.setBalance(balance);
            } else {
                this.remove();
            }
        }
    }

    public void askForResque() {
        AncapStatesPlayer mayor = this.mayor();
        mayor.resqueCity();
    }

    public void reclaimHomeHexagon() {
        this.statesDB.write("states.hexagons."+this.getHomeHexagon().code()+".owner", this.id);
    }

    public void resqueNation() {
        Nation nation = this.getNation();
        if (nation == null) {
            return;
        }
        log.info("City "+this.getName()+" trying to rescue "+nation.getName());
        Balance balance = new Balance(0, 0, 0.5D);
        if (!this.getBalance().have(balance)) {
            log.info("City "+this.getName()+" haven't enough money to rescue"+nation.getName());
            return;
        }
        log.info("City "+this.getName()+" rescuing "+nation.getName());
        this.transferMoney(nation, balance);
    }

    public List<AncapStatesPlayer> getInvitedPlayers() {
        return this.statesDB.readStrings("states.city."+this.getID()+".invitesToPlayers", true).stream()
            .map(AncapStatesPlayer::findByID).toList();
    }

    public List<String> getFlags() {
        return this.statesDB.readStrings("states.city."+this.id+".flags", true);
    }
    
}