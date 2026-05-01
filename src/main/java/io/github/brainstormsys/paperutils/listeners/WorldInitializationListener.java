package io.github.brainstormsys.paperutils.listeners;

import io.github.brainstormsys.paperutils.populators.EnderitePopulator;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class WorldInitializationListener implements Listener {

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        if (event.getWorld().getEnvironment() == World.Environment.THE_END) {
            event.getWorld().getPopulators().add(new EnderitePopulator());
        }
    }

}
