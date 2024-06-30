package ru.ancap.states.util;

import org.bukkit.Material;

public class Materials {
    
    public static boolean isBoat(Material type) { return switch (type) {
        case OAK_BOAT,      OAK_CHEST_BOAT,
             SPRUCE_BOAT,   SPRUCE_CHEST_BOAT,
             BIRCH_BOAT,    BIRCH_CHEST_BOAT,
             JUNGLE_BOAT,   JUNGLE_CHEST_BOAT,
             ACACIA_BOAT,   ACACIA_CHEST_BOAT,
             DARK_OAK_BOAT, DARK_OAK_CHEST_BOAT,
             CHERRY_BOAT,   CHERRY_CHEST_BOAT    -> true;
        default -> false;
    }; }
    
}