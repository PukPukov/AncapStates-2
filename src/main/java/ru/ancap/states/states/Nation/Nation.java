package ru.ancap.states.states.Nation;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.debug.AncapDebug;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.library.Balance;
import ru.ancap.states.AncapStates;
import ru.ancap.states.event.events.NationDeleteEvent;
import ru.ancap.states.event.events.NationFoundEvent;
import ru.ancap.states.main.AncapStatesDatabaseType;
import ru.ancap.states.message.LStateMessage;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.State;
import ru.ancap.states.states.StateName;
import ru.ancap.states.states.StateType;
import ru.ancap.states.states.Subject;
import ru.ancap.states.states.city.City;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Nation implements State {

    private String id;

    public static Logger log = Bukkit.getLogger();

    public String getID() {
        return this.id;
    }

    private PathDatabase statesDB = AncapStates.getMainDatabase();

    private PathDatabase idDB = AncapStates.getAncapStatesDatabase(AncapStatesDatabaseType.IDLINK_DATABASE);

    public Nation(String id) {
        this.id = id;
    }

    public void create(City creator, String name) {
        new NationFoundEvent(creator, name, this).callEvent();
    }

    public void initialize(City creator, String name) {
        this.statesDB.write("states.nation."+this.id+".balance", new Balance(new HashMap<>()), Balance.SERIALIZE_WORKER);
        this.statesDB.write("states.nation."+this.id+".tax", new Balance(new HashMap<>()), Balance.SERIALIZE_WORKER);
        this.statesDB.write("states.nation."+this.id+".name", name);
        this.statesDB.write("states.nation."+this.id+".capital", creator.getID());
        this.statesDB.write("states.nation."+this.id+".cities", List.of(creator.getID()));
        this.statesDB.write("states.city."+creator.getID()+".nation", this.getID());
    }

    public List<City> getCities() {
        return this.statesDB.readStrings("states.nation."+this.id+".cities", true).stream()
            .map(City::new)
            .toList();
    }

    public String getName() {
        return this.statesDB.readString("states.nation."+this.id+".name");
    }

    public void setName(String name) {
        String oldName = this.statesDB.readString("states.nation."+this.id+".name");
        this.statesDB.write("states.nation."+this.id+".name", name);
        this.idDB.delete("ids.nation_"+oldName);
        this.idDB.write("ids.nation_"+name, this.id);
    }

    public List<AncapStatesPlayer> getMinisters() {
        return this.statesDB.readStrings("states.nation."+this.id+".ministers", true).stream()
            .map(AncapStatesPlayer::findByID)
            .toList();
    }

    public void addMinister(AncapStatesPlayer ancapStatesPlayer) {
        this.statesDB.add("states.nation."+this.id+".ministers", ancapStatesPlayer.id(), true);
    }

    public void removeMinister(AncapStatesPlayer ancapStatesPlayer) {
        this.statesDB.remove("states.nation."+this.id+".ministers", ancapStatesPlayer.id(), true);
    }

    public City getCapital() {
        return new City(this.statesDB.readString("states.nation."+this.id+".capital"));
    }

    public void setCapital(City city) {
        this.statesDB.write("states.nation."+this.id+".capital", city.getID());
    }

    public Balance getBalance() {
        return new Balance(this);
    }

    @Override
    public PathDatabase database() {
        return this.statesDB.inner("states.nation."+this.id);
    }

    @Override
    public Balance balance() {
        return this.getBalance();
    }

    public void setBalance(Balance balance) {
        this.statesDB.write("states.nation."+this.id+".balance", balance, Balance.SERIALIZE_WORKER);
    }

    public String getBoard() {
        return this.statesDB.readString("states.nation."+this.id+".board");
    }

    public void setBoard(String board) {
        this.statesDB.write("states.nation."+this.id+".board", board);
    }

    public List<Hexagon> getTerritories() {
        return this.getCities().stream()
            .flatMap(city -> city.getTerritories().stream())
            .toList();
    }

    public void addCity(City city) {
        city.prepareToJoinInNation();
        this.statesDB.add("states.nation."+this.id+".cities", city.getID(), true);
        this.statesDB.write("states.city."+city.getID()+".nation", this.id);
    }

    public void removeCity(City city) {
        if (this.getCities().size() == 1) {
            CallableMessage message = LStateMessage.NATION_DESTROYED_BY_CORRUPTION(this.getName());
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
    public boolean equals(Object obj) {
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
        AncapStates.TEST = this;
        AncapDebug.debug("before event, exists", this.exists());
        AncapDebug.debug("before event, cities", this.getCities());
        AncapDebug.debug("before event, name", this.getName());
        new NationDeleteEvent(this).callEvent();
    }

    public void delete() {
        this.prepareToDelete();
        this.statesDB.delete("states.nation."+this.id);
    }

    public void prepareToDelete() {
        this.kickAllCities();
    }

    public void kickAllCities() {
        this.getCities().forEach(this::removeCityNoChecks);
    }

    public void setFlag(String flag) {
        this.statesDB.add("states.nation."+this.id+".flags", flag, true);
    }

    public void removeFlag(String flag) {
        this.statesDB.remove("states.nation."+this.id+".flags", flag, true);
    }

    public boolean freeToJoin() {
        return this.statesDB.contains("states.nation."+this.id+".flags", "FREE_TO_JOIN", true);
    }

    public void addInviteTo(City city) {
        this.statesDB.add("states.city."+city.getID()+".invitesFromNations", this.id, true);
        this.statesDB.add("states.nation."+this.id+".invitesToCities", city.getID(), true);
    }

    public void removeInviteTo(City city) {
        this.statesDB.remove("states.city."+city.getID()+".invitesFromNations", this.id, true);
        this.statesDB.remove("states.nation."+this.id+".invitesToCities", city.getID(), true);
    }

    public void setColor(NationColor color) {
        this.statesDB.write("states.nation."+this.id+".color", color.getColor());
    }

    public String getIDString() {
        return this.id;
    }

    @Override
    public StateName name() {
        return new StateName(StateType.NATION, this.getName());
    }

    @Override
    public String type() {
        return StateType.NATION.toString();
    }

    @Override
    public String id() {
        return this.getID();
    }

    @Override
    public void affiliate(@Nullable Subject affiliate) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Subject affiliate() {
        return null;
    }

    public void sendMessage(CallableMessage nationMessage) {
        this.getCities().forEach(city -> city.sendMessage(nationMessage));
    }

    @Override
    public String simpleName() {
        return this.getName();
    }

    public String getColor() {
        String colorString = this.statesDB.readString("states.nation."+this.id+".color");
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
        List<City> cities = this.getCities();
        Balance nationBalance = this.getBalance();
        for (City city : cities) {
            Balance balance = city.getBalance();
            if (balance.have(tax)) {
                balance.remove(tax);
                nationBalance.add(tax);
                city.setBalance(balance);
            } else {
                this.removeCity(city);
                CallableMessage message = LStateMessage.CITY_CANT_PAY_TAXES_AND_KICKED(city.getName());
                this.sendMessage(message);
            }
        }
        this.setBalance(nationBalance);
    }

    public Balance getTax() {
        return this.statesDB.read("states.nation."+this.id+".tax", Balance.SERIALIZE_WORKER);
    }

    public void setTax(Balance balance) {
        this.statesDB.write("states.nation."+this.id+".tax", balance, Balance.SERIALIZE_WORKER);
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
                CallableMessage message = LStateMessage.NATION_DESTROYED_BY_TAXES(name);
                AncapStates.sendMessage(message);
            }
        }
    }

    public void askForRescue() {
        log.info("Nation "+this.getName()+" asking for resque");
        City capital = this.getCapital();
        capital.resqueNation();
    }

    public List<City> getRequestingCities() {
        return this.statesDB.readStrings("states.nation."+this.id+".requestsFromCities", true).stream()
            .map(City::new)
            .toList();
    }

    public List<City> getInvitedCities() {
        return this.statesDB.readStrings("states.nation."+this.id+".invitesToCities", true).stream()
            .map(City::new)
            .toList();
    }

    public List<AncapStatesPlayer> getResidents() {
        List<AncapStatesPlayer> residents = new ArrayList<>();
        List<City> cities = this.getCities();
        for (City city : cities) {
            List<AncapStatesPlayer> cityResidents = city.getResidents();
            residents.addAll(cityResidents);
        }
        return residents;
    }

    public List<String> getFlags() {
        return this.statesDB.readStrings("states.nation."+this.id+".flags", true);
    }
    
}