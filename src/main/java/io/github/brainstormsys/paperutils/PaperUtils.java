package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.util.UUID;

public final class PaperUtils extends JavaPlugin implements Listener {

    public static PaperUtils instance;
    public static Location jailLocation; //ez lmao var
    //test configurations

    private static final String PACK_URL =
            "https://github.com/brainstorm-sys/PaperUtils/releases/download/v1.0/ResourcePack.zip";
    private static final Component PROMPT = Component.text(
            "You have to download the ResourcePack in order to join the server",
            NamedTextColor.YELLOW
    );

    @Override
    public void onEnable() {

        instance = this;
        // Plugin startup logic
        this.getComponentLogger().info("<red>The server has Started!");
        CommandAPI.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new JailListener(this), this);
        getServer().getPluginManager().registerEvents(new JailWandItem(this), this);
        getLogger().info("ForceResourcePack enabled!");

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        getServer().getScheduler().runTaskLater(this, () -> {
            if(player.isOnline()) {
                try {
                    ResourcePackInfo pack = ResourcePackInfo.resourcePackInfo()
                            .uri(URI.create(PACK_URL))
                            .id(UUID.randomUUID())
                            .computeHashAndBuild().get();

                    ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
                            .packs(pack)
                            .prompt(PROMPT)
                            .required(true)
                            .build();

                player.sendResourcePacks(request);

            }
        }, 20L);


    }

    @EventHandler
    public void onPlayerStatus(PlayerResourcePackStatusEvent e){
        Player player = e.getPlayer();
        switch (e.getStatus()){
            case DECLINED:
            case FAILED_DOWNLOAD:
                player.kick(
                        Component.text("You must need to accept the resourcepack to play on this server!",
                                NamedTextColor.RED
                                )

                );
                break;

            case SUCCESSFULLY_LOADED:
                getLogger().info(player.getName() + "loaded the ResorcePack");
                break;

            case ACCEPTED:
                getLogger().info(player.getName() + "is downloading the reosurcepack..");
                break;

            default:
                break;
        }


    }



}