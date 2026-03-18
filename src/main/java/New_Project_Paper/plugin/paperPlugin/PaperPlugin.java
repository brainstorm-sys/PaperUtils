package New_Project_Paper.plugin.paperPlugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("§aThe server has Started!");
        //getServer().getPluginManager().registerEvents(new XPBottleDestroyerPro(), this);
        CommandAPI.onEnable();
        spawnCommand.spawncomm();
        jailCommand.jailcomm();
        getServer().getPluginManager().registerEvents(new jailListener(), this);
        //testCommand.register();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        customConfig.setup();
        customConfig.get().options().copyDefaults(true);
        customConfig.save();
        customConfig.get().addDefault("taco", "rice");
        customConfig.loadJailedPlayers();

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
        customConfig.saveJailedPlayers();

    }


}
