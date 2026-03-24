package io.github.brainstorm.paperutils;

import dev.jorel.commandapi.CommandAPICommand;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JailWandItem implements Listener {
    public static ItemStack getJailWand(){
        ItemStack jailwand = new ItemStack(Material.BLAZE_ROD);
        jailwand.setData(DataComponentTypes.ITEM_MODEL, Key.key("arbiters_crossbow", "jail_rod"));//arbiters_crossbow:jail_rod
        ItemMeta meta = jailwand.getItemMeta();
        meta.displayName(Component.text("Jail Wand", NamedTextColor.GRAY));
        jailwand.setItemMeta(meta);

        return jailwand;
    }

    public static void gibitemjailwand(){
        new CommandAPICommand("gibjailwand")
                .executesPlayer((player, commandArguments) -> {
                    player.give(getJailWand());
                })
                .register();
    }

    private static final Map<UUID, Integer> clickCount = new HashMap<>();
    private static final Map<UUID, Location> lastLocation = new HashMap<>();
    private final Key jailwandKey = Key.key("arbiters_crossbow", "jail_rod");
    private final PaperPlugin plugin;

    public JailWandItem(PaperPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = e.getPlayer();
        ItemStack wand = player.getInventory().getItemInMainHand();
        Key model = wand.getData(DataComponentTypes.ITEM_MODEL);
        if (!model.equals(jailwandKey)) return;

        Location blocloc = e.getClickedBlock().getLocation();
        int clicks = clickCount.getOrDefault(player.getUniqueId(), 0) + 1;
        clickCount.put(player.getUniqueId(), clicks);

        lastLocation.put(player.getUniqueId(), blocloc);
        player.sendMessage(Component.text("Jail Location" + clicks + "/3 set!", NamedTextColor.YELLOW));
        if (clicks >= 3){
            Location loc = e.getClickedBlock().getLocation();
            PaperPlugin.jailLocation = loc;
            player.sendMessage(Component.text("Jail location set!", NamedTextColor.GREEN));
            clickCount.remove(player.getUniqueId());
            lastLocation.remove(player.getUniqueId());

            PaperPlugin.jailLocation = loc;
            CustomConfig.saveJailLocation(PaperPlugin.jailLocation);
        }
    }
}
