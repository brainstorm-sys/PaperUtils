package io.github.brainstormsys.paperutils.items;

import io.github.brainstormsys.paperutils.manager.ItemManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class Weapons {

    public final ItemManager itemManager;

    public Weapons(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void register() {
        itemManager.register(
                "arbiters_crossbow",
                Material.CROSSBOW,
                "arbiters_crossbow:crossbow",
                MiniMessage.miniMessage().deserialize("<gradient:#8b0000:#ff0000><bold><italic:false>Arbiter's Crossbow</italic:false></bold></gradient>"),
                meta -> meta.addEnchant(Enchantment.QUICK_CHARGE, 3, true)
        );
    }
}