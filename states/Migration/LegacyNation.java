package states.Migration;

import org.bukkit.configuration.file.FileConfiguration;
import states.API.SMassiveAPI;
import states.States.AncapStates;
import states.ID.ID;
import states.Nation.Nation;

public class LegacyNation {

    private static final FileConfiguration oldDB = AncapStates.getInstance().getConfig();

    private String name;
    private String capital;
    private String board;
    private String ministers;
    private String color;
    private String cities;

    public LegacyNation(String name) {
        this.name = name;
        this.capital = oldDB.getString("states.nation."+this.name+".capital");
        this.board = oldDB.getString("states.nation."+this.name+".board");
        this.ministers = oldDB.getString("states.nation."+this.name+".ministers");
        this.color = oldDB.getString("states.nation."+this.name+".color");
        this.cities = oldDB.getString("states.nation."+this.name+".cities");
    }

    public String getName() {
        return this.name;
    }

    public String getBoard() {
        if (this.board == null || this.board.equals("") || this.board.equals("Сообщение не установлено")) {
            return "Сообщение не установлено";
        }
        return this.board;
    }

    public LegacyCity getCapital() {
        return new LegacyCity(this.capital);
    }

    public LegacyPlayer[] getMinisters() {
        if (this.ministers == null || this.ministers.equals("")) {
            return new LegacyPlayer[0];
        }
        String[] ministersNames = SMassiveAPI.toMassive(this.ministers);
        LegacyPlayer[] ministers = new LegacyPlayer[ministersNames.length];
        for (int i = 0; i<ministersNames.length; i++) {
            ministers[i] = new LegacyPlayer(ministersNames[i]);
        }
        return ministers;
    }

    public String getColor() {
        return this.color;
    }

    public LegacyCity[] getCities() {
        if (this.cities == null || this.cities.equals("")) {
            return new LegacyCity[0];
        }
        String[] citiesNames = SMassiveAPI.toMassive(this.cities);
        LegacyCity[] cities = new LegacyCity[citiesNames.length];
        for (int i = 0; i<citiesNames.length; i++) {
            cities[i] = new LegacyCity(citiesNames[i]);
        }
        return cities;
    }

    public Nation getMigratedNation() {
        return new Nation(ID.getNationID(this.name));
    }
}
