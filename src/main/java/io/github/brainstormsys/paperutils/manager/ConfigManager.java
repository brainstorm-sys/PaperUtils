package io.github.brainstormsys.paperutils.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static Plugin plugin;
    private static final Map<String, FileConfiguration> configs = new HashMap<>();
    private static final Map<String, File> configFiles = new HashMap<>();

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public static FileConfiguration get(String name) {
        if (configs.containsKey(name)) return configs.get(name);
        return load(name);
    }

    public static FileConfiguration load(String name) {
        File file = new File(plugin.getDataFolder(), name + ".yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (plugin.getResource(name + ".yml") != null) {
                plugin.saveResource(name + ".yml", false);
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().severe("Failed to create " + name + ".yml");
                }
            }
        }
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        configs.put(name, cfg);
        configFiles.put(name, file);
        return cfg;
    }

    public static void save(String name) {
        FileConfiguration cfg = configs.get(name);
        File file = configFiles.get(name);
        if (cfg == null || file == null) return;
        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save " + name + ".yml");
        }
    }

    public static void reload(String name) {
        configFiles.remove(name);
        configs.remove(name);
        load(name);
    }

    public static void reloadAll() {
        for (String name : new HashMap<>(configs).keySet()) {
            reload(name);
        }
    }

    public static void set(String name, String path, Object val) {
        get(name).set(path, val);
        save(name);
    }

    public static Object getVal(String name, String path) {
        return get(name).get(path);
    }

    public static Object getVal(String name, String path, Object def) {
        return get(name).get(path, def);
    }

    public static boolean has(String name, String path) {
        return get(name).contains(path);
    }
}