package io.github.codingmastermanager.paper_plugin;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.minecraft.world.item.BowItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class JailListener implements Listener {
    @EventHandler
    public void movementdetect(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            Location confirm = JailData.jailedPlayers.get(player.getUniqueId());
            if (player.getLocation().distance(confirm) > 1) {
                player.teleport(confirm);
                player.sendMessage("§4Moving or Teleporting not allowed!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            }
        }

    }

    @EventHandler
    public void placedetect(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            player.sendMessage("§4You can't Place Blocks!");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void breakdetect(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            player.sendMessage("§4You can't Break Blocks!");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void vehicleenter(VehicleEnterEvent e) {
        if ((e.getEntered() instanceof Player player)) {
            if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
                player.sendMessage("§4You can't enter Vehicles!");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void dimensionenter(PlayerPortalEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            player.sendMessage("§4You cant enter other dimensions!");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void teleport(PlayerTeleportEvent e) {
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
    public void cmd(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            player.sendMessage("§4You sneaky! Can't type commands btw.");
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void respawnloc(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (JailData.jailedPlayers.containsKey(player.getUniqueId())) {
            Location jailloc = JailData.jailedPlayers.get(player.getUniqueId());
            player.setBedSpawnLocation(jailloc);
        }
    }

    private final JavaPlugin plugin;
    private final NamespacedKey key;
    private final Key crossbowKey = Key.key("purple_crossbow:crossbow");

    public JailListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "custom_crossbow");
    }

    @EventHandler
    public void jailcrossbow(PlayerLaunchProjectileEvent e) {
        Player shooter = e.getPlayer();
        ItemStack crossbow = e.getItemStack();

        Key model = crossbow.getData(DataComponentTypes.ITEM_MODEL);
        System.out.println("Crossbow has the model: " + model);

        if (!model.equals(crossbowKey)) return;
        if (!(e.getProjectile() instanceof Arrow arrow)) return;

        arrow.getPersistentDataContainer().set(key, PersistentDataType.STRING, "purple_crossbow");
    }

    public void jailcrossbowhit(ProjectileHitEvent e) {
        System.out.println("A projectile hit");
        if (!(e.getEntity() instanceof Arrow arrow)) return;
        System.out.println("the projectile is an arrow");
        if (!(arrow.getShooter() instanceof Player player)) return;
        if (!arrow.getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;
        System.out.println("The projectile has the PDC value set");
        if (!(e.getHitEntity() instanceof Player)) return;
        System.out.println("Player hit!");

        player.sendMessage("test done vro");
    }
}
