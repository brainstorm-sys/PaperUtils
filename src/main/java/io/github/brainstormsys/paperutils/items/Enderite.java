package io.github.brainstormsys.paperutils.items;

import io.github.brainstormsys.paperutils.PaperUtils;
import io.github.brainstormsys.paperutils.manager.ItemManager;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;

import java.util.List;

public class Enderite {

    private final ItemManager itemManager;

    public Enderite(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void register() {
        itemManager.register(
                "enderite",
                Material.ANCIENT_DEBRIS,
                "arbiters_crossbow:enderite",
                MiniMessage.miniMessage().deserialize("<gradient:#B43FEF:#6978F3><bold><italic:false>Enderite</italic:false></bold></gradient>"),
                meta -> {
                    meta.lore(List.of(Component.empty(), Component.text("Enderite!")));
                    meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                });

        itemManager.register(
                "ender_blade",
                Material.NETHERITE_SWORD,
                "arbiters_crossbow:ender_blade",
                MiniMessage.miniMessage().deserialize("<gradient:#B43FEF:#6978F3><bold><italic:false>Enderblade</italic:false></bold></gradient>"),
                meta -> {
                    meta.lore(List.of(
                            Component.empty(),
                            Component.text("Right-Click to Teleport!", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false),
                            Component.text("Cooldown: 10 seconds", NamedTextColor.GRAY),
                            Component.text("Greater Heights!").decoration(TextDecoration.ITALIC, false)
                    ));
                    meta.addEnchant(Enchantment.SHARPNESS, 10, true);
                    meta.addEnchant(Enchantment.UNBREAKING, 10, true);
                    meta.addEnchant(Enchantment.MENDING, 1, true);
                    meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
                },
                ItemManager.DataComponent.of(DataComponentTypes.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.itemAttributes()
                        .addModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(new NamespacedKey(PaperUtils.getInstance(), "enderite_damage"), 9.0, AttributeModifier.Operation.ADD_NUMBER), EquipmentSlotGroup.MAINHAND)
                        .addModifier(Attribute.BLOCK_INTERACTION_RANGE, new AttributeModifier(new NamespacedKey(PaperUtils.getInstance(), "enderite_reach"), 15, AttributeModifier.Operation.ADD_NUMBER), EquipmentSlotGroup.MAINHAND)
                        .addModifier(Attribute.ATTACK_SPEED, new AttributeModifier(new NamespacedKey(PaperUtils.getInstance(), "enderite_speed"), -0.50, AttributeModifier.Operation.ADD_NUMBER), EquipmentSlotGroup.MAINHAND)
                        .build()));

        itemManager.register(
                "enderite_helmet",
                Material.NETHERITE_HELMET,
                "minecraft:enderite_helmet",
                MiniMessage.miniMessage().deserialize("<gradient:#B43FEF:#6978F3><bold><!italic>Enderite Helmet</!italic></bold></gradient>"),
                meta -> meta.lore(List.of(Component.empty(), Component.text("Grants Night Vision").decoration(TextDecoration.ITALIC, false))),
                ItemManager.DataComponent.of(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD)
                        .assetId(Key.key("minecraft", "enderite"))
                        .build()));
    }
}