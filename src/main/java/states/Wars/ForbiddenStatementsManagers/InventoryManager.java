package states.Wars.ForbiddenStatementsManagers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class InventoryManager {

    public static final ItemStack emptyItemStack = new ItemStack(Material.AIR);

    public ItemStack[] getClearedItemStacks(ItemStack[] armor) {
        for (int i = 0; i<armor.length; i++) {
            if (this.isForbidden(armor[i])) {
                armor[i] = emptyItemStack;
            }
        }
        return armor;
    }

    public boolean isForbidden(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return false;
        }
        Material type = itemStack.getType();
        if (!itemStack.getEnchantments().isEmpty()) {
            return true;
        }
        if (this.isItemForbidden(type)) {
            return true;
        }
        if (this.isPotion(type)) {
            if (this.isPotionForbidden(itemStack)) {
                return true;
            }
            return false;
        }
        if (this.isArrow(itemStack)) {
            if (this.isArrowForbidden(itemStack)) {
                return true;
            }
            return false;
        }
        if (this.isHelmet(type)) {
            if (this.isHelmetForbidden(type)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isHelmet(Material type) {
        return type == Material.LEATHER_HELMET ||
                type == Material.CHAINMAIL_HELMET ||
                type == Material.GOLDEN_HELMET ||
                type == Material.IRON_HELMET ||
                type == Material.DIAMOND_HELMET ||
                type == Material.NETHERITE_HELMET;
    }

    public boolean isHelmetForbidden(Material type) {
        return type != Material.LEATHER_HELMET;
    }

    public boolean isItemForbidden(Material type) {
        return type == Material.END_CRYSTAL;
    }

    public boolean isArrowForbidden(ItemStack itemStack) {
        Material type = itemStack.getType();
        if (type == Material.ARROW || type == Material.SPECTRAL_ARROW) {
            return false;
        }
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        return this.isPotionMetaForbidden(meta);
    }

    public boolean isArrow(ItemStack itemStack) {
        Material type = itemStack.getType();
        return type == Material.ARROW ||
                type == Material.SPECTRAL_ARROW ||
                type == Material.TIPPED_ARROW;
    }

    public boolean isPotionForbidden(ItemStack itemStack) {
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        return this.isPotionMetaForbidden(meta);
    }

    public boolean isPotionMetaForbidden(PotionMeta meta) {
        if (!meta.getCustomEffects().isEmpty()) {
            return true;
        }
        if (meta.getBasePotionData().getType().getEffectType() != PotionEffectType.HEAL) {
            return true;
        }
        return false;
    }

    public boolean isPotion(Material type) {
        return type == Material.POTION ||
                type == Material.SPLASH_POTION ||
                type == Material.LINGERING_POTION;
    }
}
