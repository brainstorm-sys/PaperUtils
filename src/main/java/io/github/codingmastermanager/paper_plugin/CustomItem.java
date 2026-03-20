package io.github.codingmastermanager.paper_plugin;

import dev.jorel.commandapi.CommandAPICommand;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItem extends JavaPlugin {
    public static ItemStack getcrossbow(){
        final ItemStack crossbow = ItemStack.of(Material.CROSSBOW);

        crossbow.setData(DataComponentTypes.ITEM_MODEL, Key.key("purple_crossbow", "crossbow"));
        crossbow.setData(DataComponentTypes.ITEM_NAME, Component.text("Arbiter's Crossbow", NamedTextColor.GOLD));
        crossbow.setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HAND).build());

        return crossbow;
    }

    public static void itemcomm(){
        new CommandAPICommand("gibitem")
                .executesPlayer((player, commandArguments) -> {
                    player.give(getcrossbow());
                })
                .register();
    }
}
