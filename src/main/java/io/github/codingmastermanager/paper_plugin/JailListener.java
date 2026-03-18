package io.github.codingmastermanager.paper_plugin;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class JailListener implements Listener {
    @EventHandler
    public void movementdetect(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())){
            Location confirm = JailData.jailedPlayers.get(player.getUniqueId());
            if (player.getLocation().distance(confirm) > 1 ){
                player.teleport(confirm);
                player.sendMessage("§4Moving or Teleporting not allowed!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            }
        }

    }

    @EventHandler
    public void placedetect(BlockPlaceEvent e){
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())){
            player.sendMessage("§4You can't Place Blocks!");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void breakdetect(BlockBreakEvent e){
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())){
            player.sendMessage("§4You can't Break Blocks!");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void vehicleenter(VehicleEnterEvent e){
        if ((e.getEntered() instanceof Player player)){
            if(JailData.jailedPlayers.containsKey(player.getUniqueId())){
                player.sendMessage("§4You can't enter Vehicles!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void dimensionenter(PlayerPortalEvent e){
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())){
            player.sendMessage("§4You cant enter other dimensions!");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void teleport(PlayerTeleportEvent e){
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())){
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
                e.setCancelled(false);
            }
            else {
                player.sendMessage("§4You can't teleport!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cmd(PlayerCommandPreprocessEvent e){
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())){
            player.sendMessage("§4You sneaky! Can't type commands btw.");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void respawnloc(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())){
            Location jailloc = JailData.jailedPlayers.get(player.getUniqueId());
            player.setBedSpawnLocation(jailloc);
        }
    }
}
