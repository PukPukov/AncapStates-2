package states.Wars.WarPlayers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import states.Player.AncapPlayer;
import states.Wars.AncapWars.AncapWars;
import states.Wars.ForbiddenStatementsManagers.EffectsManager;
import states.Wars.ForbiddenStatementsManagers.ForbiddenStatementsThread;

import java.util.Collection;

public class AncapWarrior extends AncapPlayer {

    public AncapWarrior(String string) {
        super(string);
    }

    public AncapWarrior(Player player) {
        super(player);
    }

    public void prepareToWar() {
        ForbiddenStatementsThread thread = new ForbiddenStatementsThread(this);
        thread.start();
    }

    public void clearForbiddenOnWarsItems() {
        Player p = this.getPlayer();
        PlayerInventory inventory = p.getInventory();
        ItemStack[] armor = inventory.getArmorContents();
        ItemStack[] items = inventory.getContents();
        armor = AncapWars.getInventoryManager().getClearedItemStacks(armor);
        items = AncapWars.getInventoryManager().getClearedItemStacks(items);
        inventory.setArmorContents(armor);
        inventory.setContents(items);
    }

    public void clearForbiddenEffects() {
        Player p = this.getPlayer();
        PotionEffect[] effects = p.getActivePotionEffects().toArray(new PotionEffect[0]);
        PotionEffect[] effectsToRemove = AncapWars.getEffectsManager().getEffectsToRemoveFrom(effects);
        for (PotionEffect effect : effectsToRemove) {
            p.removePotionEffect(effect.getType());
        }
    }
}
