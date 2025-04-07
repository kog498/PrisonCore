package me.justfu498.prisoncore.autosell;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.commands.independent.SellallCommand;
import me.justfu498.prisoncore.autosell.config.AutosellConfig;
import me.justfu498.prisoncore.autosell.config.SignShopConfig;
import me.justfu498.prisoncore.autosell.database.BoosterDatabase;
import me.justfu498.prisoncore.autosell.database.SellMultiplierDatabase;
import me.justfu498.prisoncore.autosell.fortuneblock.FortuneBlock;
import me.justfu498.prisoncore.autosell.listeners.BreakBlock;
import me.justfu498.prisoncore.autosell.listeners.PlayerJoinBooster;
import me.justfu498.prisoncore.autosell.shopmanager.ShopManager;
import me.justfu498.prisoncore.autosell.signshop.SignShopManager;
import me.justfu498.prisoncore.autosell.commands.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.SQLException;

public class Autosell {
    private SellMultiplierDatabase sellMultiplierDatabase;
    private BoosterDatabase boosterDatabase;

    private AutosellConfig autosellConfig;
    private SignShopConfig signShopConfig;
    private ShopManager shopManager;
    private SignShopManager signShopManager;
    private FortuneBlock fortuneBlock;

    private final PrisonCore plugin;

    public Autosell(PrisonCore plugin) {
        this.plugin = plugin;
    }

    private void loadDatabase() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            sellMultiplierDatabase = new SellMultiplierDatabase(plugin.getDataFolder().getAbsolutePath() + "/smultidata.db");
            boosterDatabase = new BoosterDatabase(plugin.getDataFolder().getAbsolutePath() + "/sboosterdata.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to sell multiplier database!");
        }
    }

    public SellMultiplierDatabase getSellMultiDatabase() {
        return sellMultiplierDatabase;
    }

    public BoosterDatabase getBoosterDatabase() {
        return boosterDatabase;
    }

    public void closeConnection() throws SQLException {
        sellMultiplierDatabase.closeConnection();
        boosterDatabase.closeConnection();
    }

    public void loadConfig() {
        autosellConfig = new AutosellConfig(this.plugin);
        autosellConfig.createConfig();
        signShopConfig = new SignShopConfig(this.plugin);
        signShopConfig.createConfig();
    }

    public FileConfiguration getConfig() {
        return autosellConfig.getConfig();
    }

    public FileConfiguration getSignConfig() {
        return signShopConfig.getConfig();
    }

    public void loadManagers() {
        shopManager = new ShopManager(this.plugin);
        shopManager.loadShop();
        signShopManager = new SignShopManager(this.plugin);
        signShopManager.loadSignShop();
        fortuneBlock = new FortuneBlock(this.plugin);
        fortuneBlock.loadFortuneBlock();
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public SignShopManager getSignShopManager() {
        return signShopManager;
    }

    public FortuneBlock getFortuneBlock() {
        return fortuneBlock;
    }

    public void loadCommands() {
        plugin.getCommand("sellall").setExecutor(new SellallCommand(plugin));
        plugin.getCommand("autosell").setExecutor(new CommandManager(plugin));
    }

    public void loadListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new BreakBlock(this.plugin), this.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinBooster(this.plugin), this.plugin);
    }

    public void loadAutosell() {
        loadDatabase();
        loadConfig();
        loadManagers();
        loadCommands();
        loadListeners();
    }
}
