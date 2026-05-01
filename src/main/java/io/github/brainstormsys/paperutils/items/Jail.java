package io.github.brainstormsys.paperutils.items;

import io.github.brainstormsys.paperutils.manager.ItemManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

public class Jail {

    private final ItemManager itemManager;

    public Jail(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void register() {
        itemManager.register(
                "jail_wand",
                Material.BLAZE_ROD,
                "arbiters_crossbow:jail_rod",
                MiniMessage.miniMessage().deserialize("<gradient:#555555:#292828><bold><italic:false>Jail Wand</italic:false></bold></gradient>"),
                meta -> {}
        );
    }
}