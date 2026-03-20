package io.github.codingmastermanager.paper_plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CustomConfig {

    private static File file;
    private static FileConfiguration customfile;

    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Paper_Plugin").getDataFolder(), "paper-plugin.yml");

        if (!file.exists()){
            try{
                file.createNewFile();

            } catch (IOException e) {

            }
        }
        customfile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customfile;
    }

    public static void save (){
        try {
            customfile.save(file);
        } catch (IOException e) {
            System.out.println("Couldnt' save the file");
        }
    }

    public static void reload(){
        customfile = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveJailedPlayers() {

        FileConfiguration config = CustomConfig.get();

        config.set("jailed", null); // clear old data. (reminder)

        for (UUID uuid : JailData.jailedPlayers.keySet()) {

            Location loc = JailData.jailedPlayers.get(uuid);

            String path = "jailed." + uuid.toString();

            config.set(path + ".world", loc.getWorld().getName());
            config.set(path + ".x", loc.getX());
            config.set(path + ".y", loc.getY());
            config.set(path + ".z", loc.getZ());
        }

        CustomConfig.save();
    }

    public static void loadJailedPlayers() {

        FileConfiguration config = CustomConfig.get();

        if (config.getConfigurationSection("jailed") == null) return;

        for (String key : config.getConfigurationSection("jailed").getKeys(false)) {

            UUID uuid = UUID.fromString(key);

            String world = config.getString("jailed." + key + ".world");
            double x = config.getDouble("jailed." + key + ".x");
            double y = config.getDouble("jailed." + key + ".y");
            double z = config.getDouble("jailed." + key + ".z");

            Location loc = new Location(Bukkit.getWorld(world), x, y, z);

            JailData.jailedPlayers.put(uuid, loc);
        }
    }
}
