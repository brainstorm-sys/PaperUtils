package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPICommand;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CrystalMeth extends JavaPlugin implements Listener {
    public CrystalMeth(PaperUtils plugin) {
    }


    public static ItemStack getmeth(){
        ItemStack meth = new ItemStack(Material.HONEY_BOTTLE);
        meth.setData(DataComponentTypes.ITEM_MODEL, Key.key("arbiters_crossbow", "crystal"));
        ItemMeta meta = meth.getItemMeta();
        meta.displayName(Component.text("Sweet Crystals", NamedTextColor.BLUE));
        meth.setItemMeta(meta);
        return meth;
    }
}
