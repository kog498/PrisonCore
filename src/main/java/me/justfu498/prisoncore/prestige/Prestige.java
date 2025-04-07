package me.justfu498.prisoncore.prestige;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.prestige.config.PrestigeConfig;
import me.justfu498.prisoncore.prestige.database.PrestigeDatabase;
import me.justfu498.prisoncore.prestige.manager.PrestigeManager;
import me.justfu498.prisoncore.prestige.commands.CommandManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.SQLException;

public class Prestige {

    private final PrisonCore plugin;

    public Prestige(PrisonCore plugin) {
        this.plugin = plugin;
    }

    private PrestigeDatabase prestigeDatabase;
    private PrestigeConfig prestigeConfig;
    private PrestigeManager prestigeManager;

    private void loadDatabase() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            prestigeDatabase = new PrestigeDatabase(plugin.getDataFolder().getAbsolutePath() + "/prestige.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to prestige database!");
        }
    }

    public PrestigeDatabase getPrestigeDatabase() {
        return this.prestigeDatabase;
    }

    public PrestigeManager getPrestigeManager() {
        return prestigeManager;
    }

    public void closeConnection() throws SQLException {
        prestigeDatabase.closeConnection();
    }

    private void loadConfig() {
        prestigeConfig = new PrestigeConfig(this.plugin);
        prestigeConfig.createConfig();
    }

    public FileConfiguration getConfig() {
        return prestigeConfig.getConfig();
    }

    public void reloadConfig() {
        prestigeConfig.reload();
    }

    public void loadManager() {
        prestigeManager = new PrestigeManager(this.plugin);
        prestigeManager.loadPrestige();
    }

    public void loadCommands() {
        plugin.getCommand("prestige").setExecutor(new CommandManager(this.plugin));
    }

    public void loadPrestige() {
        loadDatabase();
        loadConfig();
        loadManager();
        loadCommands();
    }
}
