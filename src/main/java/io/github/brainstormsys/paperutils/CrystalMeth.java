package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPICommand;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.Name;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class CrystalMeth implements Listener {
    public CrystalMeth(PaperUtils plugin) {
    }
            // MID_PURITY mid_purity
    // --------------------------------------------------------------------------------------------------
    public static final Key LOW_PURITY = Key.key("arbiters_crossbow", "low_purity");
    public static final Key LOWMID_PURITY = Key.key("arbiters_crossbow", "lowmid_purity");
    public static final Key MID_PURITY = Key.key("arbiters_crossbow", "mid_purity");
    public static final Key CRYSTAL_MODEL = Key.key("arbiters_crossbow", "crystal");
    // --------------------------------------------------------------------------------------------------
    public static final Key SNIFF_SOUND = Key.key("arbiters_crossbow", "crystal_sniff");
    public static final Key EMPTY = Key.key("minecraft", "intentionally_empty");
    public static ItemStack getlowpurity(){
        ItemStack lowpurity = new ItemStack(Material.DRIED_KELP);
        lowpurity.setData(DataComponentTypes.ITEM_MODEL, LOW_PURITY);

        lowpurity.setData(DataComponentTypes.FOOD,
                FoodProperties.food()
                        .canAlwaysEat(true)
                        .nutrition(0)
                        .saturation(0)
                        .build()

        );

        lowpurity.setData(DataComponentTypes.CONSUMABLE,
                Consumable.consumable()
                        .consumeSeconds(1.0f)
                        .animation(ItemUseAnimation.BOW)//arbiters_crossbow:crystal_sniff
                        .sound(EMPTY)
                        .hasConsumeParticles(false)
                        .build()
                );

        ItemMeta meta = lowpurity.getItemMeta();
        meta.displayName(Component.text("Un-pure Crystals", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
        );
        meta.lore(List.of(
                Component.empty(),
                Component.text("Purity: 60%", NamedTextColor.BLUE),
                Component.text("Cook more to get better", NamedTextColor.DARK_GRAY)
        ));
        lowpurity.setItemMeta(meta);
        return lowpurity;
    }

    public static ItemStack lowmidpurity(){
        ItemStack lowmidpurity = new ItemStack(Material.DRIED_KELP);
        lowmidpurity.setData(DataComponentTypes.ITEM_MODEL, LOWMID_PURITY);

        lowmidpurity.setData(DataComponentTypes.FOOD,
                FoodProperties.food()
                        .canAlwaysEat(true)
                        .nutrition(0)
                        .saturation(0)
                        .build()

        );

        lowmidpurity.setData(DataComponentTypes.CONSUMABLE,
                Consumable.consumable()
                        .consumeSeconds(1.0f)
                        .animation(ItemUseAnimation.BOW)//arbiters_crossbow:crystal_sniff
                        .sound(EMPTY)
                        .hasConsumeParticles(false)
                        .build()
        );

        ItemMeta meta = lowmidpurity.getItemMeta();
        meta.displayName(Component.text("Un-pure Crystals", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
        );
        meta.lore(List.of(
                Component.empty(),
                Component.text("Purity: 80%", NamedTextColor.BLUE),
                Component.text("Cook more to get better", NamedTextColor.DARK_GRAY)
        ));
        lowmidpurity.setItemMeta(meta);
        return lowmidpurity;
    }

    public static ItemStack midpurity(){
        ItemStack midpurity = new ItemStack(Material.DRIED_KELP);
        midpurity.setData(DataComponentTypes.ITEM_MODEL, MID_PURITY);

        midpurity.setData(DataComponentTypes.FOOD,
                FoodProperties.food()
                        .canAlwaysEat(true)
                        .nutrition(0)
                        .saturation(0)
                        .build()

        );

        midpurity.setData(DataComponentTypes.CONSUMABLE,
                Consumable.consumable()
                        .consumeSeconds(1.0f)
                        .animation(ItemUseAnimation.BOW)
                        .sound(EMPTY)
                        .hasConsumeParticles(false)
                        .build()
        );

        ItemMeta meta = midpurity.getItemMeta();
        meta.displayName(Component.text("Un-pure Crystals", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
        );
        meta.lore(List.of(
                Component.empty(),
                Component.text("Purity: 90%", NamedTextColor.BLUE),
                Component.text("Cook more to get better", NamedTextColor.DARK_GRAY)
        ));
        midpurity.setItemMeta(meta);
        return midpurity;
    }

    public static ItemStack getmeth(){
        ItemStack meth = new ItemStack(Material.DRIED_KELP);
        meth.setData(DataComponentTypes.ITEM_MODEL, CRYSTAL_MODEL);

        meth.setData(DataComponentTypes.FOOD,
                FoodProperties.food()
                        .canAlwaysEat(true)
                        .nutrition(0)
                        .saturation(0)
                        .build()

                );

        meth.setData(DataComponentTypes.CONSUMABLE,
                Consumable.consumable()
                        .consumeSeconds(1.0f)
                        .animation(ItemUseAnimation.BOW)
                        .sound(EMPTY)
                        .hasConsumeParticles(false)
                        .build()
        );

        ItemMeta meta = meth.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#3A7C85:#67E2F3><bold><italic:false>Sweet Crystals</italic:false><bold></gradient>"
        ));
        meta.lore(List.of(
                Component.empty(),
                Component.text("Purity: 99.3%", NamedTextColor.BLUE),
                Component.text("Walt likes this", NamedTextColor.DARK_GRAY)
        ));
        meth.setItemMeta(meta);
        return meth;
    }


}
