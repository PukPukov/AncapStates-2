package states.Wars.AncapWars;

import library.Hexagon;
import org.bukkit.plugin.java.JavaPlugin;
import states.Wars.ForbiddenStatementsManagers.EffectsManager;
import states.Wars.ForbiddenStatementsManagers.InventoryManager;

public class AncapWars extends JavaPlugin {

    private static InventoryManager inventoryManager = new InventoryManager();

    private static EffectsManager effectsManager = new EffectsManager();

    private static final int pluginId = 13818;

    public static boolean isAtWar(Hexagon hexagon) {
        return false;
    }

    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public static EffectsManager getEffectsManager() {
        return effectsManager;
    }
}
