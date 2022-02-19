package states.States.Nation;

import AncapLibrary.API.SMassiveAPI;
import AncapLibrary.Economy.Balance;
import AncapLibrary.Library.AncapState;
import AncapLibrary.Message.Message;
import library.Hexagon;
import org.bukkit.Bukkit;
import states.Database.Database;
import states.Dynmap.DynmapDrawer;
import states.Main.AncapStates;
import states.Message.StateMessage;
import states.Player.AncapStatesPlayer;
import states.States.City.City;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Nation implements AncapState {

    private String id;

    public static Logger log = Bukkit.getLogger();

    public String getID() {
        return this.id;
    }

    private Database statesDB = Database.STATES_DATABASE;

    private Database idDB = Database.IDLINK_DATABASE;

    public Nation(String id) {
        this.id = id;
    }

    public void create(City creator, String name) {
        statesDB.write("states.nation."+this.id+".name", name);
        statesDB.write("states.nation."+this.id+".capital", creator.getID());
        statesDB.write("states.nation."+this.id+".cities", creator.getID());
        statesDB.write("states.city."+creator.getID()+".nation", this.getID());
        DynmapDrawer.redrawDynmap();
    }

    public City[] getCities() {
        String[] ids = SMassiveAPI.toMassive(statesDB.getString("states.nation."+this.id+".cities"));
        City[] cities = new City[ids.length];
        for (int i = 0; i<ids.length; i++) {
            cities[i] = new City(ids[i]);
        }
        return cities;
    }

    public String getName() {
        return statesDB.getString("states.nation."+this.id+".name");
    }

    public void setName(String name) {
        String oldName = statesDB.getString("states.nation."+this.id+".name");
        statesDB.write("states.nation."+this.id+".name", name);
        idDB.write("ids.nation_"+oldName, null);
        idDB.write("ids.nation_"+name, this.id);
    }

    public AncapStatesPlayer[] getMinisters() {
        String[] names = SMassiveAPI.toMassive(statesDB.getString("states.nation."+this.id+".ministers"));
        AncapStatesPlayer[] ministers = new AncapStatesPlayer[names.length];
        for (int i = 0; i<names.length; i++) {
            ministers[i] = new AncapStatesPlayer(names[i]);
        }
        return ministers;
    }

    public void addMinister(AncapStatesPlayer ancapStatesPlayer) {
        statesDB.write("states.nation."+this.id+".ministers", SMassiveAPI.add(statesDB.getString("states.nation."+this.id+".ministers"), ancapStatesPlayer.getID()));
    }

    public void removeMinister(AncapStatesPlayer ancapStatesPlayer) {
        statesDB.write("states.nation."+this.id+".ministers", SMassiveAPI.remove(statesDB.getString("states.nation."+this.id+".ministers"), ancapStatesPlayer.getID()));
    }

    public City getCapital() {
        return new City(statesDB.getString("states.nation."+this.id+".capital"));
    }

    public void setCapital(City city) {
        statesDB.write("states.nation."+this.id+".capital", city.getID());
    }

    public Balance getBalance() {
        return new Balance(this);
    }

    public void setBalance(Balance balance) {
        statesDB.write("states.nation."+this.id+".balance.iron", String.valueOf(balance.getIron()));
        statesDB.write("states.nation."+this.id+".balance.diamond", String.valueOf(balance.getDiamond()));
        statesDB.write("states.nation."+this.id+".balance.netherite", String.valueOf(balance.getNetherite()));
    }

    public String getBoard() {
        return statesDB.getString("states.nation."+this.id+".board");
    }

    public void setBoard(String board) {
        statesDB.write("states.nation."+this.id+".board", board);
    }

    public Hexagon[] getTerritories() {
        City[] cities = this.getCities();
        ArrayList<Hexagon> hexagons = new ArrayList<>();
        for (int i = 0; i<cities.length; i++) {
            Hexagon[] cityHexagons = cities[i].getTerritories();
            hexagons.addAll(List.of(cityHexagons));
        }
        return hexagons.toArray(new Hexagon[0]);
    }

    public void addCity(City city) {
        city.prepareToJoinInNation();
        statesDB.write("states.nation."+this.id+".cities", SMassiveAPI.add(statesDB.getString("states.nation."+this.id+".cities"), city.getID()));
        statesDB.write("states.city."+city.getID()+".nation", this.id);
    }

    public void removeCity(City city) {
        if (this.getCities().length == 1) {
            Message message = StateMessage.NATION_DESTROYED_BY_CORRUPTION(this.getName());
            AncapStates.sendMessage(message);
            this.removeCityNoChecks(city);
            this.remove();
            return;
        }
        city.leaveNation();
    }

    public void removeCityNoChecks(City city) {
        city.leaveNationNoChecks();
    }

    @Override
    public String toString() {
        return "Nation{"+this.getID()+"}";
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Nation.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        Nation other = (Nation) obj;
        return other.toString().equals(this.toString());
    }

    public boolean isInviting(City city) {
        return city.isInvitedTo(this);
    }

    public NationInfo getInfo() {
        return new NationInfo(this);
    }

    public boolean exists() {
        return statesDB.isSet("states.nation."+this.id);
    }

    public void remove() {
        this.prepareToDelete();
        statesDB.write("states.nation."+this.id, null);
    }

    public void prepareToDelete() {
        this.kickAllCities();
    }

    public void kickAllCities() {
        City[] cities = this.getCities();
        for (int i = 0; i<cities.length; i++) {
            this.removeCityNoChecks(cities[i]);
        }
    }

    public void setFlag(String flag) {
        statesDB.write("states.nation."+this.id+".flags", SMassiveAPI.add(statesDB.getString("states.nation."+this.id+".flags"), flag));
    }

    public void removeFlag(String flag) {
        statesDB.write("states.nation."+this.id+".flags", SMassiveAPI.remove(statesDB.getString("states.nation."+this.id+".flags"), flag));
    }

    public boolean freeToJoin() {
        return SMassiveAPI.contain(statesDB.getString("states.nation."+this.id+".flags"), "FREE_TO_JOIN");
    }

    public void addInviteTo(City city) {
        statesDB.write("states.city."+city.getID()+".invitesFromNations", SMassiveAPI.add(statesDB.getString("states.city."+city.getID()+".invitesFromNations"), this.id));
        statesDB.write("states.nation."+this.id+".invitesToCities", SMassiveAPI.add(statesDB.getString("states.nation."+this.id+".invitesToCities"), city.getID()));
    }

    public void removeInviteTo(City city) {
        statesDB.write("states.city."+city.getID()+".invitesFromNations", SMassiveAPI.remove(statesDB.getString("states.city."+city.getID()+".invitesFromNations"), this.id));
        statesDB.write("states.nation."+this.id+".invitesToCities", SMassiveAPI.remove(statesDB.getString("states.nation."+this.id+".invitesToCities"), city.getID()));
    }

    public void setColor(NationColor color) {
        statesDB.write("states.nation."+this.id+".color", color.getColor());
    }

    public String getIDString() {
        return this.id;
    }

    public void sendMessage(Message nationMessage) {
        City[] cities = this.getCities();
        for (City city : cities) {
            city.sendMessage(nationMessage);
        }
    }

    public String getCitiesString() {
        return statesDB.getString("states.nation."+this.id+".cities");
    }

    public String getMinistersString() {
        return statesDB.getString("states.nation."+this.id+".ministers");
    }

    public String getColor() {
        String colorString = statesDB.getString("states.nation."+this.id+".color");
        if (colorString != null) {
            return colorString;
        }
        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = 0.66f;
        final float luminance = 0.66f;
        Color color = Color.getHSBColor(hue, saturation, luminance);
        colorString = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        statesDB.write("states.nation."+this.id+".color", colorString);
        return colorString;
    }

    public void collectTaxes() {
        log.info("Collecting "+this.getName()+" taxes");
        Balance tax = this.getTax();
        City[] cities = this.getCities();
        Balance nationBalance = this.getBalance();
        for (City city : cities) {
            Balance balance = city.getBalance();
            if (balance.have(tax)) {
                balance.remove(tax);
                nationBalance.add(tax);
                city.setBalance(balance);
            } else {
                this.removeCity(city);
                Message message = StateMessage.CITY_CANT_PAY_TAXES_AND_KICKED(city.getName());
                this.sendMessage(message);
            }
        }
        this.setBalance(nationBalance);
    }

    public Balance getTax() {
        String netheriteString = statesDB.getString("states.nation."+this.id+".tax.netherite");
        if (netheriteString == null) {
            netheriteString = "0";
        }
        double netherite = Double.parseDouble(netheriteString);
        String diamondString = statesDB.getString("states.nation."+this.id+".tax.diamond");
        if (diamondString == null) {
            diamondString = "0";
        }
        double diamond = Double.parseDouble(diamondString);
        String ironString = statesDB.getString("states.nation."+this.id+".tax.iron");
        if (ironString == null) {
            ironString = "0";
        }
        double iron = Double.parseDouble(ironString);
        return new Balance(iron, diamond, netherite);
    }

    public void setTax(Balance balance) {
        statesDB.write("states.nation."+this.id+".tax.netherite", String.valueOf(balance.getNetherite()));
        statesDB.write("states.nation."+this.id+".tax.diamond", String.valueOf(balance.getDiamond()));
        statesDB.write("states.nation."+this.id+".tax.iron", String.valueOf(balance.getIron()));
    }

    public void grabTaxes() {
        log.info("Grabbing "+this.getName()+" taxes");
        double fee = 0.4;
        Balance tax = new Balance(0, 0, fee);
        Balance balance = this.getBalance();
        if (balance.have(tax)) {
            balance.remove(tax);
            this.setBalance(balance);
        } else {
            this.askForRescue();
            balance = this.getBalance();
            if (balance.have(tax)) {
                balance.remove(tax);
                this.setBalance(balance);
            } else {
                String name = this.getName();
                this.remove();
                Message message = StateMessage.NATION_DESTROYED_BY_TAXES(name);
                AncapStates.sendMessage(message);
            }
        }
    }

    private void askForRescue() {
        log.info("Nation "+this.getName()+" asking for resque");
        City capital = this.getCapital();
        capital.resqueNation();
    }

    public City[] getRequestingCities() {
        String[] requestingCitiesNames = SMassiveAPI.toMassive(statesDB.getString("states.nation."+this.getID()+".requestsFromCities"));
        City[] cities = new City[requestingCitiesNames.length];
        for (int i = 0; i<requestingCitiesNames.length; i++) {
            cities[i] = new City(requestingCitiesNames[i]);
        }
        return cities;
    }

    public City[] getInvitedCities() {
        String[] invitingCitiesNames = SMassiveAPI.toMassive(statesDB.getString("states.nation."+this.getID()+".invitesToCities"));
        City[] cities = new City[invitingCitiesNames.length];
        for (int i = 0; i<invitingCitiesNames.length; i++) {
            cities[i] = new City(invitingCitiesNames[i]);
        }
        return cities;
    }

    public AncapStatesPlayer[] getResidents() {
        List<AncapStatesPlayer> residents = new ArrayList<>();
        City[] cities = this.getCities();
        for (City city : cities) {
            AncapStatesPlayer[] cityResidents = city.getResidents();
            for (AncapStatesPlayer cityResident : cityResidents) {
                residents.add(cityResident);
            }
        }
        return residents.toArray(new AncapStatesPlayer[0]);
    }

    public String[] getFlags() {
        return SMassiveAPI.toMassive(statesDB.getString("states.nation."+this.id+".flags"));
    }

    @Override
    public void setMeta(String field, String str) {
        statesDB.write("states.nation."+this.getID()+"."+field, str);
    }

    @Override
    public String getMeta(String field) {
        return statesDB.getString("states.nation."+this.getID()+"."+field);
    }
}
