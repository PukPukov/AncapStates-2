package states.Migration;

import org.bukkit.configuration.file.FileConfiguration;
import states.API.SMassiveAPI;
import states.States.AncapStates;
import states.City.City;
import states.ID.ID;

public class LegacyCity {

    private static final FileConfiguration oldDB = AncapStates.getInstance().getConfig();

    private String name;
    private String residents;
    private String board;
    private String leader;
    private String assistants;
    private String nation;
    private String allowLevel;
    private String home;

    public LegacyCity(String name) {
        this.name = name;
        this.residents = oldDB.getString("states.city."+name+".residents");
        this.board = oldDB.getString("states.city."+name+".board");
        this.leader = oldDB.getString("states.city."+name+".leader");
        this.assistants = oldDB.getString("states.city."+name+".deputats");
        this.nation = oldDB.getString("states.city."+name+".nation");
        this.allowLevel = oldDB.getString("states.city."+name+".allowLevel");
        this.home = oldDB.getString("states.city."+name+".home");
    }

    public String getName() {
        return this.name;
    }

    public LegacyPlayer[] getResidents() {
        if (this.residents == null || this.residents.equals("")) {
            return new LegacyPlayer[0];
        }
        String[] residentsNames = SMassiveAPI.toMassive(this.residents);
        LegacyPlayer[] residents = new LegacyPlayer[residentsNames.length];
        for (int i = 0; i<residentsNames.length; i++) {
            residents[i] = new LegacyPlayer(residentsNames[i]);
        }
        return residents;
    }

    public String getBoard() {
        if (this.board == null || this.board.equals("") || this.board.equals("Сообщение не установлено")) {
            return "Сообщение не установлено";
        }
        return this.board;
    }

    public LegacyPlayer getLeader() {
        return new LegacyPlayer(this.leader);
    }

    public LegacyPlayer[] getAssistants() {
        if (this.assistants == null || this.assistants.equals("")) {
            return new LegacyPlayer[0];
        }
        String[] assistantsNames = SMassiveAPI.toMassive(this.assistants);
        LegacyPlayer[] assistants = new LegacyPlayer[assistantsNames.length];
        for (int i = 0; i<assistantsNames.length; i++) {
            assistants[i] = new LegacyPlayer(assistantsNames[i]);
        }
        return assistants;
    }

    public LegacyNation getNation() {
        if (this.nation == null || this.nation.equals("")) {
            return null;
        }
        return new LegacyNation(this.nation);
    }

    public int getAllowLevel() {
        if (this.allowLevel == null || this.allowLevel.equals("")) {
            return 4;
        }
        return Integer.parseInt(this.allowLevel);
    }

    public String getHome() {
        return this.home;
    }

    public City getMigratedCity() {
        return new City(ID.getCityID(this.name));
    }
}
