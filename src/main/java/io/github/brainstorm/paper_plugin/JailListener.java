package io.github.brainstorm.paper_plugin;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
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
    private final Key crossbowKey = Key.key("arbiters_crossbow:crossbow");

    public JailListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "arbiters_crossbow");
    }

    @EventHandler
    public void jailcrossbow(PlayerLaunchProjectileEvent e) {
        System.out.println("EVENT FIRED");

        Player hurter = e.getPlayer();

        Key key = hurter.getInventory().getItemInMainHand().getData(DataComponentTypes.ITEM_MODEL);
        if (Key.key("arbiters_crossbow", "crossbow").equals(key)) {
            System.out.println("Test done (launch event)");

    }
    }
       // if(!crossbow.hasItemMeta())return;
       // ItemMeta metacheck = crossbow.getItemMeta();
      //  NamespacedKey key = CustomItem.PURPLE_CROSSBOW_KEY;


     //   if(e.getProjectile() instanceof Arrow){
     //       Object proj = e.getProjectile();
     //       AbstractArrow arrow = (AbstractArrow) proj; // Temp set
     //       arrow.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
     //       System.out.println("TAGGED SUCCESSFULLY");
     //   }



    @EventHandler
    public void jailcrossbowhit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof AbstractArrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        if (!(e.getHitEntity() instanceof Player target)) return;
        ItemStack crossbow = player.getInventory().getItemInMainHand();

        Key model = crossbow.getData(DataComponentTypes.ITEM_MODEL);

        if (!model.equals(crossbowKey)) return;
        JailData.jailcall(target);
        player.sendMessage(Component.text("Player Jailed!", NamedTextColor.DARK_RED));
        System.out.println("Test completed");

        JailData.jailcall(target);
        player.sendMessage(Component.text("Player Jailed!", NamedTextColor.DARK_RED));

    /*        ItemStack crossbow = player.getInventory().getItemInMainHand();

        Key model = crossbow.getData(DataComponentTypes.ITEM_MODEL);

        if (!model.equals(crossbowKey)) return;
        JailData.jailcall(target);
        player.sendMessage(Component.text("Player Jailed!", NamedTextColor.DARK_RED)); */
    }
}
