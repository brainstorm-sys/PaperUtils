package io.github.brainstormsys.paperutils.customrecipes;

import io.github.brainstormsys.paperutils.PaperUtils;
import io.github.brainstormsys.paperutils.items.EnderiteLogic;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.FoodProperties;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import javax.naming.Name;
import java.awt.*;
import java.awt.print.Paper;
import java.util.List;

public class Recipes implements Listener {

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

        // ------------------------ ENDERITE ------------------

        NamespacedKey key2 = new NamespacedKey(PaperUtils.getInstance(), "enderite");
        ShapedRecipe recipe3 = new ShapedRecipe(key2, EnderiteLogic.getSword());

        recipe3.shape(
                "C",
                "S",
                "E"
        );

        recipe3.setIngredient('C', new RecipeChoice.ExactChoice(EnderiteLogic.getEnderite()));
        recipe3.setIngredient('S', Material.NETHERITE_SWORD);
        recipe3.setIngredient('E', Material.ENDER_EYE);
        PaperUtils.getInstance().getServer().addRecipe(recipe3);

        // helmet

        NamespacedKey key4 = new NamespacedKey(PaperUtils.getInstance(), "helmet");
        ShapedRecipe recipe4 = new ShapedRecipe(key4, EnderiteLogic.getHelmet());

        recipe4.shape(
                "C",
                "H",
                "E"
        );
        recipe4.setIngredient('C', new RecipeChoice.ExactChoice(EnderiteLogic.getEnderite()));
        recipe4.setIngredient('H', Material.NETHERITE_HELMET);
        recipe4.setIngredient('E', Material.ENDER_EYE);
        PaperUtils.getInstance().getServer().addRecipe(recipe4);

        // chestplate

        NamespacedKey key5 = new NamespacedKey(PaperUtils.getInstance(), "chestplate");
        ShapedRecipe recipe5 = new ShapedRecipe(key5, EnderiteLogic.getChestplate());

        recipe5.shape(
                " E ",
                " C ",
                " P "
        );
        recipe5.setIngredient('E', new RecipeChoice.ExactChoice(EnderiteLogic.getEnderite()));
        recipe5.setIngredient('C', Material.NETHERITE_CHESTPLATE);
        recipe5.setIngredient('P', Material.ENDER_EYE);
        PaperUtils.getInstance().getServer().addRecipe(recipe5);

        // leggings

        NamespacedKey key6 = new NamespacedKey(PaperUtils.getInstance(), "leggings");
        ShapedRecipe recipe6 = new ShapedRecipe(key6, EnderiteLogic.getLeggings());

        recipe6.shape(
                " E ",
                " L ",
                " P "
        );
        recipe6.setIngredient('E', new RecipeChoice.ExactChoice(EnderiteLogic.getEnderite()));
        recipe6.setIngredient('L', Material.NETHERITE_LEGGINGS);
        recipe6.setIngredient('P', Material.ENDER_EYE);
        PaperUtils.getInstance().getServer().addRecipe(recipe6);

        NamespacedKey key7 = new NamespacedKey(PaperUtils.getInstance(), "boots");
        ShapedRecipe recipe7 = new ShapedRecipe(key7, EnderiteLogic.getBoots());

        recipe7.shape(
                " E ",
                " B ",
                " P "
        );
        recipe7.setIngredient('E', new RecipeChoice.ExactChoice(EnderiteLogic.getEnderite()));
        recipe7.setIngredient('B', Material.NETHERITE_BOOTS);
        recipe7.setIngredient('P', Material.ENDER_EYE);
        PaperUtils.getInstance().getServer().addRecipe(recipe7);

        NamespacedKey key8 = new NamespacedKey(PaperUtils.getInstance(), "rawore");
        ShapedRecipe recipe8 = new ShapedRecipe(key8, EnderiteLogic.getEnderite());

        recipe8.shape(
                " O ",
                "OSO",
                " O "
        );
        recipe8.setIngredient('O', new RecipeChoice.ExactChoice(EnderiteLogic.getRawOre()));
        recipe8.setIngredient('S', Material.NETHER_STAR);
        PaperUtils.getInstance().getServer().addRecipe(recipe8);


        NamespacedKey key9 = new NamespacedKey(PaperUtils.getInstance(), "endpickaxe");
        ShapedRecipe recipe9 = new ShapedRecipe(key9, EnderiteLogic.getPickaxe());

        recipe9.shape(
                " E ",
                " X ",
                " P "
        );
        recipe9.setIngredient('E', new RecipeChoice.ExactChoice(EnderiteLogic.getEnderite()));
        recipe9.setIngredient('X', Material.NETHERITE_PICKAXE);
        recipe9.setIngredient('P', Material.ENDER_EYE);
        PaperUtils.getInstance().getServer().addRecipe(recipe9);


        NamespacedKey key10 = new NamespacedKey(PaperUtils.getInstance(), "endaxe");
        ShapedRecipe recipe10 = new ShapedRecipe(key10, EnderiteLogic.getEnderiteAxe());

        recipe10.shape(
                " E ",
                " A ",
                " P "
        );
        recipe10.setIngredient('E', new RecipeChoice.ExactChoice(EnderiteLogic.getEnderite()));
        recipe10.setIngredient('A', Material.NETHERITE_AXE);
        recipe10.setIngredient('P', Material.ENDER_EYE);
        PaperUtils.getInstance().getServer().addRecipe(recipe10);

    }


}
