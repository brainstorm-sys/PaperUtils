package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import io.github.brainstormsys.paperutils.listeners.EnderiteListener;
import io.github.brainstormsys.paperutils.listeners.JailWandListener;
import io.github.brainstormsys.paperutils.listeners.MethListener;
import io.github.brainstormsys.paperutils.manager.Commands;
import io.github.brainstormsys.paperutils.manager.ConfigManager;
import io.github.brainstormsys.paperutils.manager.ItemManager;
import io.github.brainstormsys.paperutils.manager.Recipe;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperUtils extends JavaPlugin implements Listener {

    public static Recipe recipe;
    public static ItemManager itemManager;
    public static Commands commands;
    public static PaperUtils instance;

    @Override
    public void onEnable() {
        instance = this;
        CommandAPI.onEnable();

        new ConfigManager(this);
        itemManager = new ItemManager(this);
        recipe = new Recipe(this);
        commands = new Commands(this, itemManager);

        JailData.loadAll();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new JailWandListener(itemManager), this);
        getServer().getPluginManager().registerEvents(new MethListener(itemManager), this);
        getServer().getPluginManager().registerEvents(new EnderiteListener(itemManager), this);

        commands.register();
        recipe.register();

        saveDefaultConfig();
    }

    public static PaperUtils getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIPaperConfig(this).silentLogs(true));
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        JailData.saveAll();
        Bukkit.removeRecipe(new NamespacedKey(this, "amythist_to_crystal"));
    }
}