package io.github.brainstormsys.paperutils;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.JukeboxPlayable;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomMusicDiscs{

    public static final Key DISC_MODEL = Key.key("arbiters_crossbow", "rickroll2");

    public static ItemStack getDisc(){
        ItemStack disc = new ItemStack(Material.MUSIC_DISC_CAT);
        disc.setData(DataComponentTypes.ITEM_MODEL, DISC_MODEL);

        ItemMeta meta = disc.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "test"
        ));

        meta.lore(List.of(
                Component.empty(),
                Component.text("Custom shit test - lebron")

        ));

        disc.setItemMeta(meta);
        return disc;
    }

}
