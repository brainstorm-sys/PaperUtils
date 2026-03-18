package io.github.codingmastermanager.paper_plugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("§aThe server has Started!");
        //getServer().getPluginManager().registerEvents(new XPBottleDestroyerPro(), this);
        CommandAPI.onEnable();
        SpawnCommand.spawncomm();
        JailCommand.jailcomm();
        getServer().getPluginManager().registerEvents(new JailListener(), this);
        //testCommand.register();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        CustomConfig.setup();
        CustomConfig.get().options().copyDefaults(true);
        CustomConfig.save();
        CustomConfig.get().addDefault("taco", "rice");
        CustomConfig.loadJailedPlayers();

    }

    @Override
    public void onLoad(){
        CommandAPI.onLoad(
                new CommandAPIPaperConfig(this)
                        .silentLogs(true)
                        .fallbackToLatestNMS(false)
                        .missingExecutorImplementationMessage("An error occured")
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        CommandAPI.onDisable();
        CustomConfig.saveJailedPlayers();

    }


}
