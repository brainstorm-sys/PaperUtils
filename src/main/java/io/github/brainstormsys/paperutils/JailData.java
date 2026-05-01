package io.github.brainstormsys.paperutils;

import io.github.brainstormsys.paperutils.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class JailData {
    public static HashMap<UUID, Location> jailedPlayers = new HashMap<>();
    public static Location jailLocation;

    public static void saveAll() {
        FileConfiguration config = ConfigManager.get("data");

        config.set("jailed", null);
        for (UUID uuid : jailedPlayers.keySet()) {
            Location loc = jailedPlayers.get(uuid);
            String path = "jailed." + uuid.toString();
            config.set(path + ".world", loc.getWorld().getName());
            config.set(path + ".x", loc.getX());
            config.set(path + ".y", loc.getY());
            config.set(path + ".z", loc.getZ());
        }

        if (jailLocation != null) {
            config.set("jail.world", jailLocation.getWorld().getName());
            config.set("jail.x", jailLocation.getX());
            config.set("jail.y", jailLocation.getY());
            config.set("jail.z", jailLocation.getZ());
        }

        ConfigManager.save("data");
    }

    public static void loadAll() {
        FileConfiguration config = ConfigManager.get("data");

        ConfigurationSection jailedSection = config.getConfigurationSection("jailed");
        if (jailedSection != null) {
            for (String key : jailedSection.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                World world = Bukkit.getWorld(config.getString("jailed." + key + ".world", ""));
                if (world != null) {
                    double x = config.getDouble("jailed." + key + ".x");
                    double y = config.getDouble("jailed." + key + ".y");
                    double z = config.getDouble("jailed." + key + ".z");
                    jailedPlayers.put(uuid, new Location(world, x, y, z));
                }
            }
        }

        String worldName = config.getString("jail.world");
        if (worldName != null) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                double x = config.getDouble("jail.x");
                double y = config.getDouble("jail.y");
                double z = config.getDouble("jail.z");
                jailLocation = new Location(world, x, y, z);
            }
        }
    }
}