package states.Migration;

import org.bukkit.configuration.file.FileConfiguration;
import states.States.AncapStates;
import states.Player.AncapPlayer;

public class LegacyPlayer {

    private static final FileConfiguration oldDB = AncapStates.getInstance().getConfig();

    private String name;
    private String city;

    public LegacyPlayer(String name) {
        this.name = name;
        this.city = oldDB.getString("states.player."+name+".city");
    }

    public String getName() {
        return this.name;
    }

    public LegacyCity getCity() {
        return new LegacyCity(this.city);
    }

    public AncapPlayer getMigratedPlayer() {
        return new AncapPlayer(this.name);
    }
}
