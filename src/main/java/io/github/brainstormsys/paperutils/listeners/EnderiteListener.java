package io.github.brainstormsys.paperutils.listeners;

import io.github.brainstormsys.paperutils.manager.ItemManager;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EnderiteListener implements Listener {

    private final ItemManager itemManager;

    public EnderiteListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    private static final Key SWORD_MODEL = Key.key("arbiters_crossbow", "ender_blade");
    private static final int COOLDOWN_TICKS = 200;

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        if (block == null) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!itemManager.isItem(item,"ender_blade")) return;

        if (player.hasCooldown(Material.NETHERITE_SWORD)) return;

        Location tpLoc = block.getLocation().add(0.5, 1, 0.5);
        tpLoc.setYaw(player.getLocation().getYaw());
        tpLoc.setPitch(player.getLocation().getPitch());

        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getLocation().add(0, 1, 0), 50, 0.3, 0.5, 0.3, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        player.teleport(tpLoc);

        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, tpLoc.clone().add(0, 1, 0), 40, 0.3, 0.5, 0.3, 0.1);
        player.getWorld().playSound(tpLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        player.setCooldown(Material.NETHERITE_SWORD, COOLDOWN_TICKS);
    }
}