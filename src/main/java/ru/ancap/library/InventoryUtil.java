package ru.ancap.library;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
    
    public static void drop(int totalAmount, Material material, Location location) {
        int maxStackSize = material.getMaxStackSize();
        
        int fullStacks = totalAmount / maxStackSize; // This will be the number of full stacks to drop
        int remainder = totalAmount % maxStackSize; // This will be the size of the partial stack, if any
        
        if (fullStacks > 0) {
            ItemStack fullStack = new ItemStack(material, maxStackSize);
            for (int i = 0; i < fullStacks; i++) location.getWorld().dropItemNaturally(location, fullStack);
        }
        
        if (remainder > 0) {
            ItemStack remainderStack = new ItemStack(material, remainder);
            location.getWorld().dropItemNaturally(location, remainderStack);
        }
    }
    
}