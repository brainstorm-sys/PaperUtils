package io.github.brainstormsys.paperutils.listeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import io.github.brainstormsys.paperutils.JailData;
import io.github.brainstormsys.paperutils.manager.ItemManager;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class JailListener implements Listener {
    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            Location confirm = JailData.jailedPlayers.get(player.getUniqueId());
            if (player.getLocation().distance(confirm) > 4) {
                player.teleport(confirm);
                player.sendMessage("§4Moving or Teleporting not allowed!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            }
        }

    }

    @EventHandler
    public void OnPlayerPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            player.sendMessage("§4You can't Place Blocks!");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void OnPlayerBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            player.sendMessage("§4You can't Break Blocks!");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void OnPlayerEnterVehicle(VehicleEnterEvent e) {
        if ((e.getEntered() instanceof Player player)) {
            if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
                player.sendMessage("§4You can't enter Vehicles!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void OnPlayerDimensionSwitch(PlayerPortalEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            player.sendMessage("§4You cant enter other dimensions!");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void OnPlayerTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
                e.setCancelled(false);
            } else {
                player.sendMessage("§4You can't teleport!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void OnPlayerCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            player.sendMessage("§4You sneaky! Can't type commands btw.");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void OnPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            Location jailloc = JailData.jailedPlayers.get(player.getUniqueId());
            player.setBedSpawnLocation(jailloc);
        }
    }

    private final JavaPlugin plugin;
    private final ItemManager itemManager;

    public JailListener(JavaPlugin plugin, ItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    @EventHandler
    public void OnPlayerLaunchCrossbow(PlayerLaunchProjectileEvent e) {
        System.out.println("EVENT FIRED");

        Player hurter = e.getPlayer();

        ItemStack item = hurter.getInventory().getItemInMainHand();
        if (itemManager.getItemId(item).equals("crossbow")) {
            System.out.println("Test done (launch event)");

    }
    }

    @EventHandler
    public void jailcrossbowhit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof AbstractArrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        if (!(e.getHitEntity() instanceof Player target)) return;
        ItemStack crossbow = player.getInventory().getItemInMainHand();

        if (!itemManager.getItemId(crossbow).equals("crossbow")) return;
        JailData.jailcall(target);
        player.sendMessage(Component.text("Player Jailed!", NamedTextColor.DARK_RED));
        System.out.println("Test completed");

        JailData.jailcall(target);
        player.sendMessage(Component.text("Player Jailed!", NamedTextColor.DARK_RED));
    }
}
