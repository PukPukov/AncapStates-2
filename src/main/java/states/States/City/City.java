package states.States.City;

import AncapLibrary.API.SMassiveAPI;
import AncapLibrary.Economy.Balance;
import AncapLibrary.Location.AncapLocation;
import AncapLibrary.Message.Message;
import AncapLibrary.Timer.Heartbeat.Exceptions.Chunk.AncapChunk;
import Database.Database;
import library.Hexagon;
import library.Point;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import states.Chunk.OutpostChunk;
import states.Chunk.PrivateChunk;
import states.Config.Config;
import states.Dynmap.DynmapDescription;
import states.Dynmap.DynmapDrawer;
import states.Main.AncapStates;
import states.Main.AncapStatesDatabaseType;
import states.Message.StateMessage;
import states.Player.AncapStatesPlayer;
import states.States.AncapState;
import states.States.Nation.Nation;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

public class City implements AncapState {

    private final String id;

    public static Logger log = Bukkit.getLogger();

    private Database statesDB = AncapStates.getMainDatabase();

    private Database idDB = AncapStates.getAncapStatesDatabase(AncapStatesDatabaseType.IDLINK_DATABASE);

    public String getID() {
        return this.id;
    }

    public City(String id) {
        this.id = id;
    }

    public void sendMessage(Message message) {
        AncapStatesPlayer[] players = this.getResidents();
        for (AncapStatesPlayer player : players) {
            player.sendMessage(message);
        }
    }

    public void create(AncapStatesPlayer creator, String name) {
        creator.prepareToJoinInCity();
        String mayorName = creator.getID();
        String home = new AncapLocation(creator.getPlayer().getLocation()).toString();
        this.statesDB.write("states.player."+creator.getID()+".city", this.id);
        statesDB.write("states.city."+this.id+".name", name);
        statesDB.write("states.city."+this.id+".mayor", mayorName);
        statesDB.write("states.city."+this.id+".residents", mayorName);
        statesDB.write("states.city."+this.id+".home", home);
        statesDB.write("states.hexagons."+AncapStates.grid.getHexagon(creator).toString()+".owner", this.id);
        DynmapDrawer.redrawDynmap();
    }

    public void remove() {
        this.dropNationCapitality();
        this.prepareToDelete();
        statesDB.write("states.city."+this.id, null);
    }

    public void dropNationCapitality() {
        if (!this.isFree()) {
            Nation nation = this.getNation();
            City[] cities = nation.getCities();
            if (cities.length == 1) {
                nation.remove();
                Message message = StateMessage.NATION_REMOVE(nation.getName());
                AncapStates.sendMessage(message);
            } else {
                City newCapital = cities[0];
                if (newCapital.equals(this)) {
                    newCapital = cities[1];
                }
                nation.setCapital(newCapital);
            }
        }
    }

    public void prepareToDelete() {
        this.unclaimAllHexagons();
        this.kickAllResidents();
        this.prepareToJoinInNation();
        this.leaveNation();
    }

    public void kickAllResidents() {
        AncapStatesPlayer[] players = this.getResidents();
        for (int i = 0; i<players.length; i++) {
            this.removeResidentNoChecks(players[i]);
        }
    }

    public void unclaimAllHexagons() {
        Hexagon[] hexagons = this.getTerritories();
        for (int i = 0; i<hexagons.length; i++) {
            this.removeHexagon(hexagons[i]);
        }
    }

    public String getName() {
        return statesDB.getString("states.city."+this.id+".name");
    }

    public void setName(String name) {
        String oldName = statesDB.getString("states.city."+this.id+".name");
        statesDB.write("states.city."+this.id+".name", name);
        idDB.write("ids.city_"+oldName, null);
        idDB.write("ids.city_"+name, this.id);
    }

    public AncapStatesPlayer[] getResidents() {
        String[] names = SMassiveAPI.toMassive(statesDB.getString("states.city."+this.id+".residents"));
        AncapStatesPlayer[] players = new AncapStatesPlayer[names.length];
        for (int i = 0; i<names.length; i++) {
            players[i] = new AncapStatesPlayer(names[i]);
        }
        return players;
    }

    public AncapStatesPlayer[] getAssistants() {
        String[] names = SMassiveAPI.toMassive(statesDB.getString("states.city."+this.id+".assistants"));
        AncapStatesPlayer[] assistants = new AncapStatesPlayer[names.length];
        for (int i = 0; i<names.length; i++) {
            assistants[i] = new AncapStatesPlayer(names[i]);
        }
        return assistants;
    }

    public void addAssistant(AncapStatesPlayer ancapStatesPlayer) {
        statesDB.write("states.city."+this.id+".assistants", SMassiveAPI.add(statesDB.getString("states.city."+this.id+".assistants"), ancapStatesPlayer.getID()));
    }

    public void removeAssistant(AncapStatesPlayer ancapStatesPlayer) {
        statesDB.write("states.city."+this.id+".assistants", SMassiveAPI.remove(statesDB.getString("states.city."+this.id+".assistants"), ancapStatesPlayer.getID()));
    }

    public AncapStatesPlayer getMayor() {
        String name = statesDB.getString("states.city."+this.id+".mayor");
        AncapStatesPlayer mayor = new AncapStatesPlayer(name);
        return mayor;
    }

    public void setMayor(AncapStatesPlayer ancapStatesPlayer) {
        statesDB.write("states.city."+this.id+".mayor", ancapStatesPlayer.getID());
    }

    public Balance getBalance() {
        return new Balance(this);
    }

    public void setBalance(Balance balance) {
        statesDB.write("states.city."+this.id+".balance.iron", String.valueOf(balance.getIron()));
        statesDB.write("states.city."+this.id+".balance.diamond", String.valueOf(balance.getDiamond()));
        statesDB.write("states.city."+this.id+".balance.netherite", String.valueOf(balance.getNetherite()));
        return;
    }

    public AllowLevel getAllowLevel() {
        String allowLevelString = statesDB.getString("states.city."+this.id+".allowlevel");
        if (allowLevelString == null) {
            return new AllowLevel(4);
        }
        int allowLevel = Integer.parseInt(allowLevelString);
        return new AllowLevel(allowLevel);
    }

    public void setAllowLevel(AllowLevel allowLevel) {
        statesDB.write("states.city."+this.id+".allowlevel", String.valueOf(allowLevel.getInt()));
    }

    public Nation getNation() {
        String nationID = statesDB.getString("states.city." + this.id + ".nation");
        if (nationID == null) {
            return null;
        }
        return new Nation(nationID);
    }

    public void setNation(Nation nation) {
        if (nation == null) {
            statesDB.write("states.city."+this.id+".nation", null);
        } else {
            statesDB.write("states.city."+this.id+".nation", nation.getIDString());
        }
    }

    public String getBoard() {
        return statesDB.getString("states.city."+this.id+".board");
    }

    public void setBoard(String board) {
        statesDB.write("states.city."+this.id+".board", board);
    }

    public Hexagon[] getTerritories() {
        String[] hexagonCodes = statesDB.getKeys("states.hexagons");
        ArrayList<Hexagon> allHexagons = new ArrayList<>();
        ArrayList<Hexagon> hexagons = new ArrayList<>();
        for (int i = 0; i<hexagonCodes.length; i++) {
            allHexagons.add(AncapStates.getInstance().getGrid().getHexagon(hexagonCodes[i]));
        }
        for (int i = 0; i<allHexagons.size(); i++) {
            if (Objects.equals(AncapStates.getCityMap().getCity(allHexagons.get(i)), this)) {
                hexagons.add(allHexagons.get(i));
            }
        }
        return hexagons.toArray(new Hexagon[0]);
    }

    public CityInfo getInfo() {
        return new CityInfo(this);
    }

    public boolean exists() {
        return statesDB.isSet("states.city."+this.id+".name");
    }

    public void addResident(AncapStatesPlayer player) {
        player.prepareToJoinInCity();
        statesDB.write("states.city."+this.id+".residents", SMassiveAPI.add(statesDB.getString("states.city."+this.id+".residents"), player.getID()));
        statesDB.write("states.player."+player.getID()+".city", this.id);
    }

    public void removeResident(AncapStatesPlayer player) {
        if (this.getResidents().length == 1) {
            Message message = StateMessage.CITY_DESTROYED_BY_CORRUPTION(this.getName());
            AncapStates.sendMessage(message);
            this.removeResidentNoChecks(player);
            this.remove();
            return;
        }
        statesDB.write("states.city."+this.id+".residents", SMassiveAPI.remove(statesDB.getString("states.city."+this.id+".residents"), player.getID()));
        statesDB.write("states.player."+player.getID()+".city", null);
    }

    public void removeResidentNoChecks(AncapStatesPlayer player) {
        statesDB.write("states.city."+this.id+".residents", SMassiveAPI.remove(statesDB.getString("states.city."+this.id+".residents"), player.getID()));
        statesDB.write("states.player."+player.getID()+".city", null);
    }

    public void addInviteTo(AncapStatesPlayer player) {
        statesDB.write("states.city."+this.id+".invitesToPlayers", SMassiveAPI.add(statesDB.getString("states.city."+this.id+".invitesToPlayers"), player.getID()));
        statesDB.write("states.player."+player.getID()+".invitesFromCities", SMassiveAPI.add(statesDB.getString("states.player."+player.getID()+".invitesFromCities"), this.getID()));
    }

    public void removeInviteTo(AncapStatesPlayer player) {
        statesDB.write("states.city."+this.id+".invitesToPlayers", SMassiveAPI.remove(statesDB.getString("states.city."+this.id+".invitesToPlayers"), player.getID()));
        statesDB.write("states.player."+player.getID()+".invitesFromCities", SMassiveAPI.remove(statesDB.getString("states.player."+player.getID()+".invitesFromCities"), this.id));
    }

    public void addRequestTo(Nation nation) {
        statesDB.write("states.city."+this.id+".requestsToNations", SMassiveAPI.add(statesDB.getString("states.city."+this.id+".requestsToNations"), nation.getIDString()));
        statesDB.write("states.nation."+nation.getIDString()+".requestsFromCities", SMassiveAPI.add(statesDB.getString("states.nation."+nation.getIDString()+".requestsFromCities"), this.id));
    }

    public void removeRequestTo(Nation nation) {
        statesDB.write("states.city."+this.id+".requestsToNations", SMassiveAPI.remove(statesDB.getString("states.city."+this.id+".requestsToNations"), nation.getIDString()));
        statesDB.write("states.nation."+nation.getIDString()+".requestsFromCities", SMassiveAPI.remove(statesDB.getString("states.nation."+nation.getIDString()+".requestsFromCities"), this.id));
    }

    public void declineRequestFrom(AncapStatesPlayer player) {
        statesDB.write("states.city."+this.id+".requestsFromPlayers", SMassiveAPI.remove(statesDB.getString("states.city."+this.id+".requestsFromPlayers"), player.getID()));
        statesDB.write("states.player."+player.getID()+".requestsToCities", SMassiveAPI.remove(statesDB.getString("states.player."+player.getID()+".requestsToCities"), this.id));
    }

    public void declineInviteFrom(Nation nation) {
        statesDB.write("states.city."+this.id+".invitesFromNations", SMassiveAPI.remove(statesDB.getString("states.city."+this.id+".invitesFromNations"), nation.getIDString()));
        statesDB.write("states.nation."+nation.getIDString()+".invitesToCities", SMassiveAPI.remove(statesDB.getString("states.nation."+nation.getIDString()+".invitesToCities"), this.id));
    }

    public void setFlag(String flag) {
        statesDB.write("states.city."+this.id+".flags", SMassiveAPI.add(statesDB.getString("states.city."+this.id+".flags"), flag));
    }

    public void removeFlag(String flag) {
        statesDB.write("states.city."+this.id+".flags", SMassiveAPI.remove(statesDB.getString("states.city."+this.id+".flags"), flag));
    }

    public boolean freeToJoin() {
        return this.haveFlag("FREE_TO_JOIN");
    }

    public boolean haveFlag(String flag) {
        return SMassiveAPI.contain(statesDB.getString("states.city."+this.id+".flags"), flag);
    }

    @Override
    public String toString() {
        return "City{"+this.getID()+"}";
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == null) {
            return false;
        }
        if (!City.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        City other = (City) obj;
        return other.toString().equals(this.toString());
    }

    public void setLimit(AncapStatesPlayer limited, int limit) {
        statesDB.write("states.city."+this.id+".limit.personal."+limited.getID(), String.valueOf(limit));
    }

    public void setLimit(LimitType limitType, int limit) {
        statesDB.write("states.city."+this.id+".limit."+limitType.toString(), String.valueOf(limit));
    }

    public void addPrivateChunk(AncapStatesPlayer player, AncapChunk ancapChunk) {
        statesDB.write("states.city."+this.id+".chunks."+ancapChunk.toString(), player.getID());
    }
    public void removePrivateChunk(AncapChunk ancapChunk) {
        statesDB.write("states.city."+this.id+".chunks."+ancapChunk.toString(), null);
    }

    public boolean canClaimNewOutpostChunk() {
        return this.getOutpostChunks().length<this.getResidents().length*3;
    }

    public AncapChunk[] getOutpostChunks() {
        String[] outpostChunks = statesDB.getKeys("states.chunks");
        ArrayList<String> list = new ArrayList<>();
        for (String chunk : outpostChunks) {
            if (new OutpostChunk(chunk).getOwner().equals(this)) {
                list.add(chunk);
            }
        }
        AncapChunk[] chunks = new AncapChunk[list.size()];
        for (int i = 0; i<list.size(); i++) {
            chunks[i] = new AncapChunk(list.get(i));
        }
        return chunks;
    }

    public void addOutpostChunk(AncapChunk ancapChunk) {
        statesDB.write("states.chunks."+ancapChunk.toString(), this.id);
    }

    public void removeOutpostChunk(AncapChunk ancapChunk) {
        statesDB.write("states.chunks."+ancapChunk.toString(), null);
    }

    public boolean haveHexagonClaimingFee() {
        int fee = Integer.parseInt(Config.VALUES_CONFIGURATION.getString("fees.hexagon_claiming_fee"));
        Balance balance = this.getBalance();
        return balance.getDiamond()>=fee;
    }

    public boolean canAttach(Hexagon hexagon) {
        Hexagon[] hexagons = this.getTerritories();
        for (int i = 0; i<hexagons.length; i++) {
            if (hexagons[i].isNeighborOf(hexagon)) {
                return true;
            }
        }
        return false;
    }

    public void grabHexagonClaimingFee() {
        Balance balance = this.getBalance();
        int fee = Integer.parseInt(Config.VALUES_CONFIGURATION.getString("fees.hexagon_claiming_fee"));
        balance.removeDiamond(fee);
        this.setBalance(balance);
    }

    public void addHexagon(Hexagon hexagon) {
        statesDB.write("states.hexagons."+hexagon.toString()+".owner", this.id);
    }

    public Hexagon getHomeHexagon() {
        return AncapStates.grid.getHexagon(new AncapLocation(statesDB.getString("states.city."+this.id+".home")).getLocation());
    }

    public void giveHexagonUnclaimingRepayment() {
        int repayment = Integer.parseInt(Config.VALUES_CONFIGURATION.getString("fees.hexagon_unclaiming_repayment"));
        Balance balance = this.getBalance();
        balance.addDiamond(repayment);
    }

    public void removeHexagon(Hexagon hexagon) {
        statesDB.write("states.hexagons."+hexagon.toString()+".owner", null);
    }

    public AncapStatesPlayer[] getRequestingPlayers() {
        String[] requestingPlayerNames = SMassiveAPI.toMassive(statesDB.getString("states.city."+this.getID()+".requestsFromPlayers"));
        AncapStatesPlayer[] players = new AncapStatesPlayer[requestingPlayerNames.length];
        for (int i = 0; i<requestingPlayerNames.length; i++) {
            players[i] = new AncapStatesPlayer(requestingPlayerNames[i]);
        }
        return players;
    }

    public int getRemoteness(AncapStatesPlayer player) {
        City playerCity = player.getCity();
        AncapStatesPlayer cityLeader = this.getMayor();
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
        return statesDB.getString("states.city."+this.id+".nation") == null;
    }

    public boolean haveNationCreatingFee() {
        return this.getBalance().getNetherite() >= 9;
    }

    public void grabNationCreationFee() {
        int fee = Integer.parseInt(Config.VALUES_CONFIGURATION.getString("fees.nation_creation_fee"));
        Balance balance = this.getBalance();
        balance.removeNetherite(fee);
        this.setBalance(balance);
    }

    public void prepareToJoinInNation() {
        this.cancelAllInvitesToNation();
        this.cancelAllRequestsToNation();
    }

    public void cancelAllRequestsToNation() {
        String[] requestedNationsIDs = SMassiveAPI.toMassive(statesDB.getString("states.city."+this.id+".requestsToNations"));
        for (int i = 0; i<requestedNationsIDs.length; i++) {
            Nation nation = new Nation(requestedNationsIDs[i]);
            this.removeRequestTo(nation);
        }
    }

    public void cancelAllInvitesToNation() {
        String[] invitedNationsIDs = SMassiveAPI.toMassive(statesDB.getString("states.city."+this.id+".invitesFromNations"));
        for (int i = 0; i<invitedNationsIDs.length; i++) {
            Nation nation = new Nation(invitedNationsIDs[i]);
            this.declineInviteFrom(nation);
        }
    }

    public boolean isInvitedTo(Nation nation) {
        return SMassiveAPI.contain(statesDB.getString("states.city."+this.id+".invitesFromNations"), nation.getIDString());
    }

    public void leaveNation() {
        Nation nation = this.getNation();
        if (nation == null) {
            return;
        }
        this.dropNationCapitality();
        statesDB.write("states.city."+this.id+".nation", null);
        statesDB.write("states.nation."+nation.getIDString()+".cities", SMassiveAPI.remove(statesDB.getString("states.nation."+nation.getIDString()+".cities"), this.id));
    }

    public void leaveNationNoChecks() {
        Nation nation = this.getNation();
        if (nation == null) {
            return;
        }
        statesDB.write("states.city."+this.id+".nation", null);
        statesDB.write("states.nation."+nation.getIDString()+".cities", SMassiveAPI.remove(statesDB.getString("states.nation."+nation.getIDString()+".cities"), this.id));
    }

    public boolean isRequestingTo(Nation nation) {
        return SMassiveAPI.contain(statesDB.getString("states.city."+this.id+".requestsToNations"), nation.getIDString());
    }

    public Location getHome() {
        return new AncapLocation(statesDB.getString("states.city."+this.id+".home")).getLocation();
    }

    public PrivateChunk[] getPrivateChunks() {
        String[] ids = statesDB.getKeys("states.city."+this.id+".chunks");
        PrivateChunk[] chunks = new PrivateChunk[ids.length];
        for (int i = 0; i<ids.length; i++) {
            chunks[i] = new PrivateChunk(this, ids[i]);
        }
        return chunks;
    }

    public boolean isInviting(AncapStatesPlayer player) {
        return player.isInvitedTo(this);
    }

    public String getResidentsNames() {
        return statesDB.getString("states.city."+this.id+".residents");
    }

    public String getAssistantsNames() {
        return statesDB.getString("states.city."+this.id+".assistants");
    }

    public void draw() {
        String color = this.getColor();
        String icon = "blueflag";
        if (this.isFree()) {
            icon = "greenflag";
        } else if (this.getMayor().isLeader()) {
            icon = "king";
        }
        DynmapDrawer drawer = new DynmapDrawer();
        drawer.drawFigure(this.getTerritories(), color, this.getDescription());
        drawer.draw(new Point(this.getHome().getBlockX(), this.getHome().getBlockZ()), icon, this.getDescription());
    }

    private DynmapDescription getDescription() {
        return this.getInfo().toDynmapFormat();
    }

    private String getColor() {
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
        if (type.equals("iron")) {
            cityBalance.removeIron(amount);
            recipientBalance.addIron(amount);
        }
        if (type.equals("netherite")) {
            cityBalance.removeNetherite(amount);
            recipientBalance.addNetherite(amount);
        }
        if (type.equals("diamond")) {
            cityBalance.removeDiamond(amount);
            recipientBalance.addDiamond(amount);
        }
        recipient.setBalance(recipientBalance);
        this.setBalance(cityBalance);
    }

    public void collectTaxes() {
        log.info("Collecting "+this.getName()+" taxes");
        Balance tax = this.getTax();
        AncapStatesPlayer[] residents = this.getResidents();
        Balance cityBalance = this.getBalance();
        for (AncapStatesPlayer resident : residents) {
            Balance balance = resident.getBalance();
            if (balance.have(tax)) {
                balance.remove(tax);
                cityBalance.add(tax);
                resident.setBalance(balance);
            } else {
                this.removeResident(resident);
                Message message = StateMessage.PLAYER_CANT_PAY_TAXES_AND_KICKED(resident.getName());
                this.sendMessage(message);
            }
        }
        this.setBalance(cityBalance);
    }

    public Balance getTax() {
        String netheriteString = statesDB.getString("states.city."+this.id+".tax.netherite");
        if (netheriteString == null) {
            netheriteString = "0";
        }
        double netherite = Double.parseDouble(netheriteString);
        String diamondString = statesDB.getString("states.city."+this.id+".tax.diamond");
        if (diamondString == null) {
            diamondString = "0";
        }
        double diamond = Double.parseDouble(diamondString);
        String ironString = statesDB.getString("states.city."+this.id+".tax.iron");
        if (ironString == null) {
            ironString = "0";
        }
        double iron = Double.parseDouble(ironString);
        return new Balance(iron, diamond, netherite);
    }

    public void setTax(Balance balance) {
        statesDB.write("states.city."+this.id+".tax.netherite", String.valueOf(balance.getNetherite()));
        statesDB.write("states.city."+this.id+".tax.diamond", ""+balance.getDiamond());
        statesDB.write("states.city."+this.id+".tax.iron", String.valueOf(balance.getIron()));
    }

    public Balance getMaintenanceTax() {
        double fee = 0.5;
        return new Balance(0, this.getTerritories().length*fee, 0);
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
                String name = this.getName();
                this.remove();
                Message message = StateMessage.CITY_DESTROYED_BY_TAXES(name);
                AncapStates.sendMessage(message);
            }
        }
    }

    public void askForResque() {
        AncapStatesPlayer mayor = this.getMayor();
        mayor.resqueCity();
    }

    public void reclaimHomeHexagon() {
        statesDB.write("states.hexagons."+this.getHomeHexagon().toString()+".owner", this.id);
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

    public AncapStatesPlayer[] getInvitedPlayers() {
        String[] invitingPlayersNames = SMassiveAPI.toMassive(statesDB.getString("states.city."+this.getID()+".invitesToPlayers"));
        AncapStatesPlayer[] players = new AncapStatesPlayer[invitingPlayersNames.length];
        for (int i = 0; i<invitingPlayersNames.length; i++) {
            players[i] = new AncapStatesPlayer(invitingPlayersNames[i]);
        }
        return players;
    }

    public String[] getFlags() {
        return SMassiveAPI.toMassive(statesDB.getString("states.city."+this.id+".flags"));
    }

    @Override
    public void setMeta(String field, String str) {
        statesDB.write("states.city."+this.getID()+"."+field, str);
    }

    @Override
    public String getMeta(String field) {
        return statesDB.getString("states.city."+this.getID()+"."+field);
    }
}
