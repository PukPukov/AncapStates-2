package states.Player;

import AncapLibrary.API.SMassiveAPI;
import AncapLibrary.Economy.Balance;
import AncapLibrary.Message.Message;
import AncapLibrary.Player.AncapPlayer;
import AncapLibrary.Timer.Heartbeat.Exceptions.Chunk.AncapChunk;
import Database.Database;
import library.Hexagon;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import states.Chunk.OutpostChunk;
import states.Chunk.PrivateChunk;
import states.Config.Config;
import states.Main.AncapStates;
import states.Message.ErrorMessage;
import states.Message.StateMessage;
import states.States.City.City;
import states.States.City.LimitType;
import states.States.Nation.Nation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class AncapStatesPlayer extends AncapPlayer {

    private Database statesDB = AncapStates.getMainDatabase();
    public static Logger log = Bukkit.getLogger();

    public AncapStatesPlayer(Player player) {
        super(player);
    }

    public AncapStatesPlayer(String name) {
        super(name);
    }

    public City getCity() {
        String cityID = statesDB.getString("states.player."+this.getID()+".city");
        if (cityID == null) {
            return null;
        }
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
        statesDB.write("states.city."+city.getID()+".requestsFromPlayers", SMassiveAPI.add(statesDB.getString("states.city."+city.getID()+".requestsFromPlayers"), this.getID()));
        statesDB.write("states.player."+this.getID()+".requestsToCities", SMassiveAPI.add(statesDB.getString("states.player."+this.getID()+".requestsToCities"), city.getID()));
    }

    public void removeRequestTo(City city) {
        statesDB.write("states.city."+city.getID()+".requestsFromPlayers", SMassiveAPI.remove(statesDB.getString("states.city."+city.getID()+".requestsFromPlayers"), this.getID()));
        statesDB.write("states.player."+this.getID()+".requestsToCities", SMassiveAPI.remove(statesDB.getString("states.player."+this.getID()+".requestsToCities"), city.getID()));
    }

    public boolean isFree() {
        return !statesDB.isSet("states.player."+this.getID()+".city");
    }

    public boolean haveCityCreationFee() {
        return this.getBalance().getNetherite()>=1;
    }

    public boolean isMayor() {
        City city = this.getCity();
        return new AncapStatesPlayer(statesDB.getString("states.city."+city.getID()+".mayor")).equals(this);
    }

    public void grabCityCreationFee() {
        Config values = Config.VALUES_CONFIGURATION;
        Balance balance = this.getBalance();
        int fee = Integer.parseInt(values.getString("fees.city_creation_fee"));
        balance.removeNetherite(fee);
        this.setBalance(balance);
    }

    public boolean isInvitedTo(City city) {
        return SMassiveAPI.contain(statesDB.getString("states.player."+this.getID()+".invitesFromCities"), city.getID());
    }

    public boolean isAskingToJoinIn(City city) {
        return SMassiveAPI.contain(statesDB.getString("states.city."+city.getID()+".requestsFromPlayers"), this.getID());
    }

    public void leaveCity() {
        this.dropCityMayoring();
        City city = this.getCity();
        city.removeResident(this);
    }

    public void dropCityMayoring() {
        if (!this.isFree()) {
            City city = this.getCity();
            AncapStatesPlayer[] players = city.getResidents();
            if (players.length == 1) {
                city.remove();
                Message message = StateMessage.CITY_REMOVE(city.getName());
                AncapStates.sendMessage(message);
            } else {
                AncapStatesPlayer newMayor = players[0];
                if (newMayor.equals(this)) {
                    newMayor = players[1];
                }
                city.setMayor(newMayor);
            }
        }
    }

    public boolean isAssistant() {
        City city = this.getCity();
        return SMassiveAPI.contain(statesDB.getString("states.city."+city.getID()+".assistants"), this.getID());
    }

    public boolean isResidentOf(City city) {
        return SMassiveAPI.contain(statesDB.getString("states.city."+city.getID()+".residents"), this.getID());
    }

    public boolean canTeleportToCitySpawn() {
        return this.getBalance().getDiamond()>Integer.parseInt(Config.VALUES_CONFIGURATION.getString("fees.citySpawnTeleport")) || this.haveFreeTeleport();
    }

    public boolean haveFreeTeleport() {
        return !statesDB.isSet("states.player."+this.getID()+".freeTeleportUsed");
    }

    public void citySpawn() {
        City city = this.getCity();
        Location loc = city.getHome();
        if (this.haveFreeTeleport()) {
            statesDB.write("states.player."+this.getID()+".freeTeleportUsed", "true");
        } else {
            this.grabCityTeleportFee();
        }
        this.getPlayer().teleport(loc);
    }

    public void grabCityTeleportFee() {
        Balance balance = this.getBalance();
        int fee = Integer.parseInt(Config.VALUES_CONFIGURATION.getString("fees.citySpawnTeleport"));
        balance.removeDiamond(fee);
        this.setBalance(balance);
    }

    @Override
    public String toString() {
        return "AncapStatesPlayer{"+this.getID()+"}";
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
        return other.getID().equals(this.getID());
    }

    public boolean canClaimPrivateChunks() {
        return true;
    }

    public AncapChunk getChunk() {
        Chunk chunk = this.getPlayer().getLocation().getChunk();
        return new AncapChunk(chunk.getX()+";"+chunk.getZ());
    }

    public PrivateChunk getPrivateChunk() {
        City city = this.getCity();
        AncapChunk chunk = this.getChunk();
        return new PrivateChunk(city, chunk.getID());
    }

    public City getCityAtPosition() {
        Location loc = this.getPlayer().getLocation();
        return AncapStates.getCityMap().getCity(loc);
    }

    public PrivateChunk[] getPrivateChunks() {
        if (this.isFree()) {
            return new PrivateChunk[0];
        }
        PrivateChunk[] cityPrivateChunks = this.getCity().getPrivateChunks();
        ArrayList<PrivateChunk> chunks = new ArrayList<>();
        ArrayList<PrivateChunk> playerChunks = new ArrayList<>();
        chunks.addAll(List.of(cityPrivateChunks));
        for (int i = 0; i<chunks.size(); i++) {
            if (chunks.get(i).getOwner().equals(this)) {
                playerChunks.add(chunks.get(i));
            }
        }
        return playerChunks.toArray(new PrivateChunk[0]);
    }

    public boolean canClaimNewPrivateChunk() {
        if (this.isFree()) {
            return false;
        }
        City city = this.getCity();
        PrivateChunk[] chunks = this.getPrivateChunks();
        LimitType type = new LimitType("resident");
        if (this.isAssistant()) {
            type = new LimitType("assistants");
        }
        String rangLimitString = statesDB.getString("states.city."+city.getID()+".limit."+type);
        String personalLimitString = statesDB.getString("states.city."+city.getID()+".limit.personal."+this.getID());
        int rangLimit = 3;
        int personalLimit = 0;
        if (rangLimitString != null) {
            rangLimit = Integer.parseInt(rangLimitString);
        }
        if (personalLimitString != null) {
            personalLimit = Integer.parseInt(personalLimitString);
        }
        if (chunks.length >= rangLimit && chunks.length >= personalLimit) {
            return false;
        }
        return true;
    }

    public OutpostChunk getOutpostChunk() {
        return new OutpostChunk(this.getPlayer());
    }

    public void addFriend(AncapStatesPlayer friend) {
        statesDB.write("states.player."+this.getID()+".friends", SMassiveAPI.add(statesDB.getString("states.player."+this.getID()+".friends"), friend.getID()));
    }

    public void removeFriend(AncapStatesPlayer friend) {
        statesDB.write("states.player."+this.getID()+".friends", SMassiveAPI.remove(statesDB.getString("states.player."+this.getID()+".friends"), friend.getID()));
    }

    public AncapStatesPlayerInfo getInfo() {
        return new AncapStatesPlayerInfo(this);
    }

    public boolean isLeader() {
        return this.getCity().getNation().getCapital().getMayor().equals(this);
    }

    public void prepareToJoinInCity() {
        this.revokeAllRequestsToCities();
        this.cancelAllInvitesFromCities();
    }

    public void cancelAllInvitesFromCities() {
        String[] invitesFromCities = SMassiveAPI.toMassive(statesDB.getString("states.player."+this.getID()+".invitesFromCities"));
        for (int i = 0; i<invitesFromCities.length; i++) {
            City city = new City(invitesFromCities[i]);
            this.declineInviteFrom(city);
        }
    }

    public void revokeAllRequestsToCities() {
        String[] requestsFromCities = SMassiveAPI.toMassive(statesDB.getString("states.player."+this.getID()+".requestsFromCities"));
        for (int i = 0; i<requestsFromCities.length; i++) {
            City city = new City(requestsFromCities[i]);
            this.declineInviteFrom(city);
        }
    }

    public boolean isMinister() {
        Nation nation = this.getCity().getNation();
        return SMassiveAPI.contain(statesDB.getString("states.nation."+nation.getIDString()+".ministers"), this.getID());
    }

    public boolean isCitizenOf(Nation nation) {
        City[] cities = nation.getCities();
        for (City city : cities) {
            AncapStatesPlayer[] players = city.getResidents();
            for (AncapStatesPlayer resident : players) {
                if (this.equals(resident)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLicentiate() {
        boolean b = this.getPlayer().hasPermission("ancapstates.licentiate");
        return b;
    }

    public void declineInviteFrom(City city) {
        statesDB.write("states.player."+this.getID()+".invitesFromCities", SMassiveAPI.remove(statesDB.getString("states.player."+this.getID()+".invitesFromCities"), city.getID()));
        statesDB.write("states.city."+city.getID()+".invitesToPlayers", SMassiveAPI.remove(statesDB.getString("states.city."+city.getID()+".invitesToPlayers"), this.getID()));
    }

    public AncapStatesPlayer[] getFriends() {
        String[] names = SMassiveAPI.toMassive(statesDB.getString("states.player."+this.getID()+".friends"));
        AncapStatesPlayer[] players = new AncapStatesPlayer[names.length];
        for (int i = 0; i<names.length; i++) {
            players[i] = new AncapStatesPlayer(names[i]);
        }
        return players;
    }

    public City[] getInviting() {
        String[] names = SMassiveAPI.toMassive(statesDB.getString("states.player."+this.getID()+".invitesFromCities"));
        City[] cities = new City[names.length];
        for (int i = 0; i<names.length; i++) {
            cities[i] = new City(names[i]);
        }
        return cities;
    }

    public City[] getRequesting() {
        String[] names = SMassiveAPI.toMassive(statesDB.getString("states.player."+this.getID()+".requestsToCities"));
        City[] cities = new City[names.length];
        for (int i = 0; i<names.length; i++) {
            cities[i] = new City(names[i]);
        }
        return cities;
    }

    public String getFriendsNames() {
        return statesDB.getString("states.player."+this.getID()+".friends");
    }

    public String getInvitingNames() {
        return statesDB.getString("states.player."+this.getID()+".invitesFromCities");
    }


    public String getRequestingNames() {
        return statesDB.getString("states.player."+this.getID()+".requestsToCities");
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
        if (board == null) {
            board = "Сообщение не установлено";
        }
        this.getPlayer().sendTitle("Ты зашёл в город §b"+city.getName(), "§o"+board, 30, 30, 30);
    }

    private void sendWildernessTitle() {
        this.getPlayer().sendTitle("Ты забрёл в ничейные земли","Осторожно, тут опасно.", 30, 30, 30);
    }

    public void transferMoney(AncapStatesPlayer recipient, double amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance recipientBalance = recipient.getBalance();
        if (type.equals("iron")) {
            playerBalance.removeIron(amount);
            recipientBalance.addIron(amount);
        }
        if (type.equals("netherite")) {
            playerBalance.removeNetherite(amount);
            recipientBalance.addNetherite(amount);
        }
        if (type.equals("diamond")) {
            playerBalance.removeDiamond(amount);
            recipientBalance.addDiamond(amount);
        }
        recipient.setBalance(recipientBalance);
        this.setBalance(playerBalance);
    }

    public void depositCity(City city, int amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance cityBalance = city.getBalance();
        if (type.equals("iron")) {
            playerBalance.removeIron(amount);
            cityBalance.addIron(amount);
        }
        if (type.equals("netherite")) {
            playerBalance.removeNetherite(amount);
            cityBalance.addNetherite(amount);
        }
        if (type.equals("diamond")) {
            playerBalance.removeDiamond(amount);
            cityBalance.addDiamond(amount);
        }
        city.setBalance(cityBalance);
        this.setBalance(playerBalance);
    }

    public void depositNation(Nation nation, int amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance nationBalance = nation.getBalance();
        if (type.equals("iron")) {
            playerBalance.removeIron(amount);
            nationBalance.addIron(amount);
        }
        if (type.equals("netherite")) {
            playerBalance.removeNetherite(amount);
            nationBalance.addNetherite(amount);
        }
        if (type.equals("diamond")) {
            playerBalance.removeDiamond(amount);
            nationBalance.addDiamond(amount);
        }
        nation.setBalance(nationBalance);
        this.setBalance(playerBalance);
    }

    public void withdrawCity(City city, int amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance cityBalance = city.getBalance();
        if (type.equals("iron")) {
            playerBalance.addIron(amount);
            cityBalance.removeIron(amount);
        }
        if (type.equals("netherite")) {
            playerBalance.addNetherite(amount);
            cityBalance.removeNetherite(amount);
        }
        if (type.equals("diamond")) {
            playerBalance.addDiamond(amount);
            cityBalance.removeDiamond(amount);
        }
        city.setBalance(cityBalance);
        this.setBalance(playerBalance);
    }

    public void withdrawNation(Nation nation, int amount, String type) {
        Balance playerBalance = this.getBalance();
        Balance nationBalance = nation.getBalance();
        if (type.equals("iron")) {
            playerBalance.addIron(amount);
            nationBalance.removeIron(amount);
        }
        if (type.equals("netherite")) {
            playerBalance.addNetherite(amount);
            nationBalance.removeNetherite(amount);
        }
        if (type.equals("diamond")) {
            playerBalance.addDiamond(amount);
            nationBalance.removeDiamond(amount);
        }
        nation.setBalance(nationBalance);
        this.setBalance(playerBalance);
    }

    public boolean isFriendOf(AncapStatesPlayer player) {
        String friendsNames = this.getFriendsNames();
        if (SMassiveAPI.contain(friendsNames, player.getID())) {
            return true;
        }
        return false;
    }

    public void resqueCity() {
        City city = this.getCity();
        if (city == null) {
            return;
        }
        log.info("Player "+this.getName()+" trying to rescue "+city.getName());
        Balance balance = new Balance(0, 0, 0.5D);
        if (!this.getBalance().have(balance)) {
            log.info("Player "+this.getName()+" haven't enough money to rescue "+city.getName());
            return;
        }
        log.info("Player "+this.getName()+" rescuing "+city.getName());
        this.transferMoney(city, balance);
    }

    public void checkCityMove() {
        City city = this.getCityAtPosition();
        City cityLast = AncapStates.getCityMap().getPositionsMap().get(this.getID());
        if (!Objects.equals(city,cityLast)) {
            AncapStates.getCityMap().getPositionsMap().put(this.getID(), city);
            this.sendJoinTitle(city);
        }
    }

    public Hexagon getHexagon() {
        return AncapStates.grid.getHexagon(this);
    }

    public boolean canInteract(Location loc) {
        try {
            City city = AncapStates.getCityMap().getCity(loc);
            if (city == null) {
                return true;
            }
            PrivateChunk chunk = city.getPrivateChunk(loc);
            int remoteness = city.getRemoteness(this);
            int allowLevel = city.getAllowLevel().getInt();
            Message message = ErrorMessage.CANT_INTERACT_THIS_BLOCK;
            if (chunk.getOwner() == null) {
                if (remoteness>allowLevel) {
                    this.sendMessage(message);
                    return false;
                }
                return true;
            }
            if (remoteness>allowLevel) {
                this.sendMessage(message);
                return false;
            }
            if (chunk.getOwner() != null && !chunk.getOwner().equals(this) && !chunk.getOwner().isFriendOf(this)) {
                this.sendMessage(message);
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
