package me.justfu498.prisoncore.rank;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.rank.commands.CommandManager;
import me.justfu498.prisoncore.rank.commands.independent.ForceRankupCommand;
import me.justfu498.prisoncore.rank.commands.independent.RankListCommand;
import me.justfu498.prisoncore.rank.commands.independent.RankupCommand;
import me.justfu498.prisoncore.rank.config.RankConfig;
import me.justfu498.prisoncore.rank.database.RankDatabase;
import me.justfu498.prisoncore.rank.manager.RankManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.SQLException;

public class Rank {

    private final PrisonCore plugin;

    private RankDatabase rankDatabase;
    private RankConfig rankConfig;
    private RankManager rankManager;

    public Rank(PrisonCore plugin) {
        this.plugin = plugin;
    }

    private void loadDatabase() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            rankDatabase = new RankDatabase(plugin.getDataFolder().getAbsolutePath() + "/rank.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to rank database!");
        }
    }

    public RankDatabase getRankDatabase() {
        return this.rankDatabase;
    }

    public void closeConnection() throws SQLException {
        rankDatabase.closeConnection();
    }

    private void loadConfig() {
        this.rankConfig = new RankConfig(this.plugin);
        rankConfig.createConfig();
    }

    public FileConfiguration getConfig() {
        return this.rankConfig.getConfig();
    }

    public RankConfig getRankConfig() {
        return this.rankConfig;
    }

    private void loadManager() {
        rankManager = new RankManager(this.plugin);
        rankManager.loadRank();
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public void loadCommands() {
        plugin.getCommand("rankup").setExecutor(new RankupCommand(this.plugin));
        plugin.getCommand("ranklist").setExecutor(new RankListCommand(this.plugin));
        plugin.getCommand("rank").setExecutor(new CommandManager(this.plugin));
        plugin.getCommand("forcerankup").setExecutor(new ForceRankupCommand(this.plugin));
    }

    public void loadRank() {
        loadDatabase();
        loadConfig();
        loadManager();
        loadCommands();
    }
}
