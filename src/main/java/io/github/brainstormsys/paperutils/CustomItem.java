package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPICommand;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItem extends JavaPlugin {

    private JavaPlugin plugin;
    public static ItemStack getcrossbow(){
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        crossbow.setData(DataComponentTypes.ITEM_MODEL, Key.key("arbiters_crossbow", "crossbow"));
        crossbow.addEnchantment(Enchantment.QUICK_CHARGE, 3);
        ItemMeta meta = crossbow.getItemMeta();
        meta.displayName(Component.text("Arbiter's Crossbow", NamedTextColor.DARK_RED));
        crossbow.setItemMeta(meta);
        return crossbow;
    }

    public static void itemcomm(){
        new CommandAPICommand("gibitem")
                .withPermission("paperplugin.moderators")
                .executesPlayer((player, commandArguments) -> {
                    player.give(getcrossbow());
                })
                .register();
    }
}
