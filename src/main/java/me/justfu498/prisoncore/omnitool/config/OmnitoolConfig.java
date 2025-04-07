package me.justfu498.prisoncore.omnitool.config;

import me.justfu498.prisoncore.PrisonCore;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class OmnitoolConfig {
    private File file;
    private FileConfiguration config;

    private final PrisonCore plugin;

    public OmnitoolConfig(PrisonCore plugin) {
        this.plugin = plugin;
    }

    public void createConfig() {
        file = new File(this.plugin.getDataFolder(), "omnitool.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            this.plugin.saveResource("omnitool.yml", false);
        }
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
