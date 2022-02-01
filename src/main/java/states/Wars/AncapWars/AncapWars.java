package states.Wars.AncapWars;

import org.bukkit.plugin.java.JavaPlugin;
import states.Player.AncapPlayer;
import states.Main.AncapStates;
import states.Wars.ForbiddenStatementsManagers.EffectsManager;
import states.Wars.ForbiddenStatementsManagers.InventoryManager;
import states.Wars.WarPlayers.AncapWarrior;

public class AncapWars extends JavaPlugin {

    private static InventoryManager inventoryManager = new InventoryManager();

    private static EffectsManager effectsManager = new EffectsManager();

    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public static EffectsManager getEffectsManager() {
        return effectsManager;
    }

    public static AncapWarrior[] getOnlinePlayers() {
        AncapPlayer[] players = AncapStates.getOnlinePlayers();
        AncapWarrior[] warriors = new AncapWarrior[players.length];
        for (int i = 0; i<players.length; i++) {
            warriors[i] = new AncapWarrior(players[i]);
        }
        return warriors;
    }
}
