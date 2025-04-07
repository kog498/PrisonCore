package me.justfu498.prisoncore.placeholderapi;

import me.justfu498.prisoncore.PrisonCore;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PAPIConfig {
    private File file;
    private FileConfiguration config;

    private final PrisonCore plugin;

    public PAPIConfig(PrisonCore plugin) {
        this.plugin = plugin;
    }

    public void createConfig() {
        file = new File(plugin.getDataFolder(), "placeholderapi.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("placeholderapi.yml", false);
        }
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            System.out.print("Can't load placeholderapi.yml!");
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
