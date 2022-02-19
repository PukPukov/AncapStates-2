package states.Wars.AncapWars;

import org.bukkit.plugin.java.JavaPlugin;
import states.Player.AncapStatesPlayer;
import states.Main.AncapStates;
import states.Wars.ForbiddenStatementsManagers.EffectsManager;
import states.Wars.ForbiddenStatementsManagers.InventoryManager;
import states.Wars.WarPlayers.AncapStatesWarrior;

public class AncapWars extends JavaPlugin {

    private static InventoryManager inventoryManager = new InventoryManager();

    private static EffectsManager effectsManager = new EffectsManager();

    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public static EffectsManager getEffectsManager() {
        return effectsManager;
    }

    public static AncapStatesWarrior[] getOnlinePlayers() {
        AncapStatesPlayer[] players = AncapStates.getPlayerMap().getOnlinePlayers();
        AncapStatesWarrior[] warriors = new AncapStatesWarrior[players.length];
        for (int i = 0; i<players.length; i++) {
            warriors[i] = new AncapStatesWarrior(players[i]);
        }
        return warriors;
    }
}
