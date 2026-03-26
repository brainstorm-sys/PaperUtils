package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperUtils extends JavaPlugin {

    public static PaperUtils instance;
    public static Location jailLocation; //ez lmao var
    @Override
    public void onEnable() {

        instance = this;
        // Plugin startup logic
        this.getComponentLogger().info("<red>The server has Started!");
        CommandAPI.onEnable();
        getServer().getPluginManager().registerEvents(new JailListener(this), this);
        getServer().getPluginManager().registerEvents(new JailWandItem(this), this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();


        CustomConfig.setup();
        CustomConfig.get().options().copyDefaults(true);
        CustomConfig.save();
        CustomConfig.get().addDefault("taco", "rice");
        PaperUtils.jailLocation = CustomConfig.loadJailLocation();
        CustomConfig.loadJailedPlayers();;

    }

    public static PaperUtils getInstance(){
        return instance;
    }

    @Override
    public void onLoad(){
        System.out.println("NEW_FILE");
        CommandAPI.onLoad(
                new CommandAPIPaperConfig(this)
                        .silentLogs(true)
                        .fallbackToLatestNMS(false)
                        .missingExecutorImplementationMessage("An error occured")
        );

        SpawnCommand.spawncomm();
        JailCommand.jailcomm();
        CustomItem.itemcomm();
        JailWandItem.gibitemjailwand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        CommandAPI.onDisable();
        CustomConfig.saveJailedPlayers();
        CustomConfig.saveJailLocation(PaperUtils.jailLocation);

    }


}