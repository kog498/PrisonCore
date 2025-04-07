package me.justfu498.prisoncore.token;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.token.commands.CommandManager;
import me.justfu498.prisoncore.token.config.TokenConfig;
import me.justfu498.prisoncore.token.database.TokenDatabase;
import me.justfu498.prisoncore.token.listeners.PlayerRedeemToken;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.SQLException;

public class Token {

    private final PrisonCore plugin;

    private TokenDatabase tokenDatabase;
    private TokenConfig tokenConfig;

    public Token(PrisonCore plugin) {
        this.plugin = plugin;
    }

    private void loadDatabase() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            tokenDatabase = new TokenDatabase(plugin.getDataFolder().getAbsolutePath() + "/token.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to token database!");
        }
    }

    public TokenDatabase getTokenDatabase() {
        return tokenDatabase;
    }

    public void closeConnection() throws SQLException {
        tokenDatabase.closeConnection();
    }

    private void loadConfig() {
        tokenConfig = new TokenConfig(plugin);
        tokenConfig.createConfig();
    }

    public FileConfiguration getConfig() {
        return tokenConfig.getConfig();
    }

    public void loadCommands() {
        plugin.getCommand("token").setExecutor(new CommandManager(plugin));
    }

    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerRedeemToken(this.plugin), this.plugin);
    }

    public void loadToken() {
        loadDatabase();
        loadConfig();
        loadCommands();
        loadListeners();
    }
}
