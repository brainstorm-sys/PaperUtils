package io.github.brainstormsys.paperutils.listeners;

import io.github.brainstormsys.paperutils.JailData;
import io.github.brainstormsys.paperutils.manager.ItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JailWandListener implements Listener {

    private final ItemManager itemManager;
    private final Map<UUID, Integer> clickCount = new HashMap<>();

    public JailWandListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock() == null) return;

        Player player = e.getPlayer();
        ItemStack wand = player.getInventory().getItemInMainHand();
        String id = itemManager.getItemId(wand);

        if (id == null || !id.equals("jail_rod")) return;

        Location blocloc = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);
        int clicks = clickCount.getOrDefault(player.getUniqueId(), 0) + 1;
        clickCount.put(player.getUniqueId(), clicks);

        player.sendMessage(Component.text("Jail Location point " + clicks + "/3 set!", NamedTextColor.YELLOW));

        if (clicks >= 3) {
            JailData.jailLocation = blocloc;
            JailData.saveAll();
            player.sendMessage(Component.text("Jail location successfully set!", NamedTextColor.GREEN));
            clickCount.remove(player.getUniqueId());
        }
    }
}