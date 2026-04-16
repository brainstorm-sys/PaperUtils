package io.github.brainstormsys.paperutils;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.FoodProperties;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class BurgerLogic implements Listener {

    public static final Key BURGER_KEY = Key.key("arbiters_crossbow", "burger");
    public static final Key SUSHI_KEY = Key.key("arbiters_crossbow", "sushi");

    public static ItemStack getBurger(){
        ItemStack burger = new ItemStack(Material.COOKED_BEEF);
        burger.setData(DataComponentTypes.ITEM_MODEL, BURGER_KEY);

        burger.setData(DataComponentTypes.FOOD,
                FoodProperties.food()
                        .nutrition(9)
                        .saturation(13)
                        .build()
                );


        ItemMeta meta = burger.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#F5861D:#F4822E:#F37E3F:#F37A50:#F27661:#F17272><bold>Burger</bold></gradient>"
        ));

        meta.lore(List.of(
                Component.empty(),
                Component.text("Burger!!")
        ));

        burger.setItemMeta(meta);
        return burger;

    }

    public static ItemStack getSushi(){

        ItemStack sushi = new ItemStack(Material.COOKED_BEEF);
        sushi.setData(DataComponentTypes.ITEM_MODEL, SUSHI_KEY);

        sushi.setData(DataComponentTypes.FOOD,
                FoodProperties.food()
                        .nutrition(6)
                        .saturation(10)
                        .build()

        );

        ItemMeta meta = sushi.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#17A90F:#93AE52><bold>Sushi</bold></gradient>"
        ));


        meta.lore(List.of(
                Component.empty(),
                Component.text("Asian sushi")

        ));

        sushi.setItemMeta(meta);
        return sushi;
    }

    public static void registerRecipe(){
        NamespacedKey key = new NamespacedKey(PaperUtils.getInstance(), "burger");
        ShapedRecipe recipe = new ShapedRecipe(key, getBurger());

        recipe.shape(
                "B",
                "M",
                "B"
        );

        recipe.setIngredient('B', Material.BREAD);
        recipe.setIngredient('M', Material.COOKED_BEEF);

        PaperUtils.getInstance().getServer().addRecipe(recipe);


        // ---------------- SUSHI -------------------------------



        NamespacedKey key1 = new NamespacedKey(PaperUtils.getInstance(), "sushi");
        ShapedRecipe recipe1 = new ShapedRecipe(key1, getSushi());

        recipe1.shape(
                "SSS",
                "FAF",
                "SSS"

        );

        recipe1.setIngredient('S', Material.DRIED_KELP);
        recipe1.setIngredient('F', new RecipeChoice.MaterialChoice(
                Material.SALMON,
                Material.COD,
                Material.TROPICAL_FISH,
                Material.COOKED_SALMON,
                Material.COOKED_COD
        ));

        recipe1.setIngredient('A', Material.BEETROOT);
        PaperUtils.getInstance().getServer().addRecipe(recipe1);

    }
}
