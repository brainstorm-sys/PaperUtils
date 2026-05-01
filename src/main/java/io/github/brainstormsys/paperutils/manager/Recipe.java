package io.github.brainstormsys.paperutils.manager;

import io.github.brainstormsys.paperutils.PaperUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class Recipe {

    private final JavaPlugin plugin;

    public Recipe(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        NamespacedKey key = new NamespacedKey(plugin, "amythist_to_crystal");

        BlastingRecipe recipe = new BlastingRecipe(
                key,
                CrystalMeth.getlowpurity(),
                Material.AMETHYST_SHARD,
                1.0f,
                140

        );

        Bukkit.addRecipe(recipe);

        BlastingRecipe recipe1 = new BlastingRecipe(
                new NamespacedKey(plugin, "crystalstage2"),
                CrystalMeth.lowmidpurity(),
                new RecipeChoice.ExactChoice(CrystalMeth.getlowpurity()),
                1.0f,
                140
        );

        Bukkit.addRecipe(recipe1);

        BlastingRecipe recipe2 = new BlastingRecipe(
                new NamespacedKey(plugin, "crystalstage3"),
                CrystalMeth.midpurity(),
                new RecipeChoice.ExactChoice(CrystalMeth.lowmidpurity()),
                1.0f,
                140
        );

        Bukkit.addRecipe(recipe2);

        BlastingRecipe recipe3 = new BlastingRecipe(
                new NamespacedKey(plugin, "crystalstage4"),
                CrystalMeth.getmeth(),
                new RecipeChoice.ExactChoice(CrystalMeth.midpurity()),
                1.0f,
                140
        );

        Bukkit.addRecipe(recipe3);

        ShapedRecipe recipe4 = new ShapedRecipe(new NamespacedKey(PaperUtils.getInstance(), "burger");, getBurger());

        recipe4.shape(
                "B",
                "M",
                "B"
        );

        recipe4.setIngredient('B', Material.BREAD);
        recipe4.setIngredient('M', Material.COOKED_BEEF);

        PaperUtils.getInstance().getServer().addRecipe(recipe4);


        // ---------------- SUSHI -------------------------------



        NamespacedKey key1 = new NamespacedKey(PaperUtils.getInstance(), "sushi");
        ShapedRecipe recipe5 = new ShapedRecipe(key1, getSushi());

        recipe5.shape(
                "SSS",
                "FAF",
                "SSS"

        );

        recipe5.setIngredient('S', Material.DRIED_KELP);
        recipe5.setIngredient('F', new RecipeChoice.MaterialChoice(
                Material.SALMON,
                Material.COD,
                Material.TROPICAL_FISH,
                Material.COOKED_SALMON,
                Material.COOKED_COD
        ));

        recipe5.setIngredient('A', Material.BEETROOT);
        PaperUtils.getInstance().getServer().addRecipe(recipe5);

        // ------------------------ ENDERITE ------------------

        NamespacedKey key2 = new NamespacedKey(PaperUtils.getInstance(), "enderite");
        ShapedRecipe recipe6 = new ShapedRecipe(key2, EnderiteLogic.getSword());

        recipe6.shape(
                "C",
                "S",
                "E"
        );

        recipe6.setIngredient('C', new RecipeChoice.ExactChoice(EnderiteLogic.getEnderite()));
        recipe6.setIngredient('S', Material.NETHERITE_SWORD);
        recipe6.setIngredient('E', Material.ENDER_EYE);
        PaperUtils.getInstance().getServer().addRecipe(recipe6);

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
    }



}
