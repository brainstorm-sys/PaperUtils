package io.github.brainstormsys.paperutils.populators;

import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class EnderitePopulator extends BlockPopulator {

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        int placed = 0;
        int attempts = 0;

        while (placed < 2 && attempts < 2000) {
            attempts++;

            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            int y = 10 + random.nextInt(60);

            if (limitedRegion.getType(x, y, z) == Material.END_STONE) {
                limitedRegion.setType(x, y, z, Material.DIAMOND_ORE); // Idk how u gonn change this to Enderite but here this is the populator you need. should hypotethically work didnt test it
                placed++;
            }
        }
    }
}
