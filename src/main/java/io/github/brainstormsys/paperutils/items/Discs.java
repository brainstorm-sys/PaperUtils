package io.github.brainstormsys.paperutils.items;

import io.github.brainstormsys.paperutils.manager.ItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

public class Discs {

    public final ItemManager itemManager;

    public Discs(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void register() {
        itemManager.register(
                "rickroll_disc",
                Material.MUSIC_DISC_CAT,
                "arbiters_crossbow:rickroll2",
                MiniMessage.miniMessage().deserialize("test"),
                meta -> meta.lore(List.of(
                        Component.empty(),
                        Component.text("Custom shit test - lebron")
                ))
        );
    }
}