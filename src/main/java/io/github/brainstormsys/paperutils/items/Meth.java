package io.github.brainstormsys.paperutils.items;

import io.github.brainstormsys.paperutils.manager.ItemManager;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

public class Meth {

    private final ItemManager itemManager;
    private final Key EMPTY_SOUND = Key.key("minecraft", "intentionally_empty");

    public Meth(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void register() {
        FoodProperties food = FoodProperties.food()
                .canAlwaysEat(true)
                .nutrition(0)
                .saturation(0)
                .build();

        Consumable consumable = Consumable.consumable()
                .consumeSeconds(1.0f)
                .animation(ItemUseAnimation.BOW)
                .sound(EMPTY_SOUND)
                .hasConsumeParticles(false)
                .build();

        itemManager.register(
                "low_purity",
                Material.DRIED_KELP,
                "arbiters_crossbow:low_purity",
                Component.text("Un-pure Crystals", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                meta -> meta.lore(List.of(
                        Component.empty(),
                        Component.text("Purity: 60%", NamedTextColor.BLUE),
                        Component.text("Cook more to get better", NamedTextColor.DARK_GRAY)
                )),
                ItemManager.DataComponent.of(DataComponentTypes.FOOD, food),
                ItemManager.DataComponent.of(DataComponentTypes.CONSUMABLE, consumable));

        itemManager.register(
                "lowmid_purity",
                Material.DRIED_KELP,
                "arbiters_crossbow:lowmid_purity",
                Component.text("Un-pure Crystals", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                meta -> meta.lore(List.of(
                        Component.empty(),
                        Component.text("Purity: 80%", NamedTextColor.BLUE),
                        Component.text("Cook more to get better", NamedTextColor.DARK_GRAY)
                )),
                ItemManager.DataComponent.of(DataComponentTypes.FOOD, food),
                ItemManager.DataComponent.of(DataComponentTypes.CONSUMABLE, consumable));

        itemManager.register(
                "mid_purity",
                Material.DRIED_KELP,
                "arbiters_crossbow:mid_purity",
                Component.text("Un-pure Crystals", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                meta -> meta.lore(List.of(
                        Component.empty(),
                        Component.text("Purity: 90%", NamedTextColor.BLUE),
                        Component.text("Cook more to get better", NamedTextColor.DARK_GRAY)
                )),
                ItemManager.DataComponent.of(DataComponentTypes.FOOD, food),
                ItemManager.DataComponent.of(DataComponentTypes.CONSUMABLE, consumable));

        itemManager.register(
                "crystal",
                Material.DRIED_KELP,
                "arbiters_crossbow:crystal",
                MiniMessage.miniMessage().deserialize("<gradient:#3A7C85:#67E2F3><bold><italic:false>Sweet Crystals</italic:false></bold></gradient>"),
                meta -> meta.lore(List.of(
                        Component.empty(),
                        Component.text("Purity: 99.3%", NamedTextColor.BLUE),
                        Component.text("Walt likes this", NamedTextColor.DARK_GRAY)
                )),
                ItemManager.DataComponent.of(DataComponentTypes.FOOD, food),
                ItemManager.DataComponent.of(DataComponentTypes.CONSUMABLE, consumable));
    }
}