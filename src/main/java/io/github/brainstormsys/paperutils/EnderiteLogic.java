package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPICommand;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EnderiteLogic implements Listener {

    public static final Key ENDERITE_MODEL = Key.key("arbiters_crossbow", "enderite");
    public static final Key SWORD_MODEL = Key.key("arbiters_crossbow","ender_blade");

    public static ItemStack getEnderite(){

        ItemStack enderite = new ItemStack(Material.ANCIENT_DEBRIS);
        enderite.setData(DataComponentTypes.ITEM_MODEL, ENDERITE_MODEL);


        ItemMeta meta = enderite.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#B43FEF:#6978F3><bold><italic:false>Enderite<italic:false></bold></gradient>"
        ));
        meta.lore(List.of(
                Component.empty(),
                Component.text("Enderite!")
        ));

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        enderite.setItemMeta(meta);
        return enderite;
    }

    public static ItemStack getSword(){
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        sword.setData(DataComponentTypes.ITEM_MODEL, SWORD_MODEL);

        sword.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.itemAttributes()
                        .addModifier(Attribute.ATTACK_DAMAGE,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "enderite_damage"),
                                        9.0,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.MAINHAND
                                )
                        .addModifier(Attribute.BLOCK_INTERACTION_RANGE,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "enderite_reach"),
                                        15,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.MAINHAND
                                )
                        .addModifier(Attribute.ATTACK_SPEED,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "enderite_speed"),
                                        -0.50,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.MAINHAND
                                )

        );

        ItemMeta meta = sword.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#B43FEF:#6978F3><bold><italic:false>Enderblade</italic:false></bold></gradient>"
        ));

        meta.lore(List.of(
                Component.empty(),
                Component.text("Right-Click to Teleport!", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("Cooldown: 10 seconds", NamedTextColor.GRAY),
                Component.text("Greater Heights!")
                        .decoration(TextDecoration.ITALIC, false)
        ));

        meta.addEnchant(Enchantment.SHARPNESS, 10, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);

        sword.setItemMeta(meta);
        return sword;
    }

    public static void enderComs(){
        new CommandAPICommand("endergive")
                .withPermission("paperplugin.moderators")
                .executesPlayer((player, args) -> {
                    player.give(getEnderite());

                })
                .register();

    }






    // ============ SWORD LOGIC =============//

    public static final int COOLDOWN_TICKS = 20 * 10;
    // js to define shit
    public EnderiteLogic(PaperUtils plugin){
    }
    //

    public static boolean isEnderSword(ItemStack item){
        if(item == null) return false;
        Key model = item.getData(DataComponentTypes.ITEM_MODEL);
        if(model == null) return false;
        return model.equals(SWORD_MODEL);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock() == null) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!isEnderSword(item)) return;

        if(player.hasCooldown(Material.NETHERITE_SWORD)) return;

        Block block = e.getClickedBlock();
        Location tpLoc = block.getLocation().add(0.5, 1, 0.5);
        tpLoc.setYaw(player.getLocation().getYaw());
        tpLoc.setPitch(player.getLocation().getPitch());

        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getLocation().add(0, 1, 0), 50, 0.3, 0.5, 0.3, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        player.teleport(tpLoc);

        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, tpLoc.add(0, 1,0), 40, 0.3, 0.5, 0.3, 0.1);
        player.getWorld().playSound(tpLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        player.setCooldown(Material.NETHERITE_SWORD, COOLDOWN_TICKS);

    }

    // ============ SWORD LOGIC =============//


    // ============ HELMET LOGIC ============//

    public static final Key HELMET_MODEL = Key.key("minecraft","enderite_helmet");

    public static ItemStack getHelmet(){
        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        helmet.setData(DataComponentTypes.ITEM_MODEL, HELMET_MODEL);

        helmet.setData(DataComponentTypes.EQUIPPABLE,
                Equippable.equippable(EquipmentSlot.HEAD)
                        .assetId(Key.key("minecraft","enderite"))
                        .build()
                );

        ItemMeta meta = helmet.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#B43FEF:#6978F3><bold><!italic>Enderite Helmet</!italic></bold></gradient>"
        ));

        meta.lore(List.of(
                Component.empty(),
                Component.text("Grants Night Vision")
                        .decoration(TextDecoration.ITALIC ,false)
        ));

        helmet.setItemMeta(meta);
        return helmet;
    }


    // ============ HELMET LOGIC ============//

}
