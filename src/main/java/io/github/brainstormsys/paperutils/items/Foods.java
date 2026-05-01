package io.github.brainstormsys.paperutils.items;

import io.github.brainstormsys.paperutils.manager.ItemManager;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.FoodProperties;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

public class Foods {

    public final ItemManager itemManager;

    public Foods(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void register() {
        itemManager.register(
                "burger",
                Material.COOKED_BEEF,
                "arbiters_crossbow:burger",
                MiniMessage.miniMessage().deserialize("<gradient:#F5861D:#F4822E:#F37E3F:#F37A50:#F27661:#F17272><bold>Burger</bold></gradient>"),
                meta -> meta.lore(List.of(Component.empty(), Component.text("Burger!!"))),
                ItemManager.DataComponent.of(DataComponentTypes.FOOD, FoodProperties.food()
                        .nutrition(9)
                        .saturation(13)
                        .build()));

        itemManager.register(
                "sushi",
                Material.COOKED_BEEF,
                "arbiters_crossbow:sushi",
                MiniMessage.miniMessage().deserialize("<gradient:#17A90F:#93AE52><bold>Sushi</bold></gradient>"),
                meta -> meta.lore(List.of(Component.empty(), Component.text("Asian sushi"))),
                ItemManager.DataComponent.of(DataComponentTypes.FOOD, FoodProperties.food()
                        .nutrition(6)
                        .saturation(10)
                        .build()));
    }
}