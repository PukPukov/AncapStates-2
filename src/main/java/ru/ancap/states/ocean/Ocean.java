package ru.ancap.states.ocean;

import org.bukkit.block.Biome;

public class Ocean {

    public static boolean isOcean(Biome biome) {
        return biome == Biome.OCEAN ||
                biome == Biome.COLD_OCEAN ||
                biome == Biome.DEEP_OCEAN ||
                biome == Biome.LUKEWARM_OCEAN ||
                biome == Biome.FROZEN_OCEAN ||
                biome == Biome.WARM_OCEAN ||
                biome == Biome.DEEP_COLD_OCEAN ||
                biome == Biome.DEEP_FROZEN_OCEAN ||
                biome == Biome.DEEP_LUKEWARM_OCEAN ||
                biome == Biome.ICE_SPIKES;
    }

}
