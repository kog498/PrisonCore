package me.justfu498.prisoncore;

import me.justfu498.prisoncore.autosell.Autosell;
import me.justfu498.prisoncore.autosell.enums.AutosellMessagesList;
import me.justfu498.prisoncore.autosell.enums.AutosellMessages;
import me.justfu498.prisoncore.autosell.task.ReloadPluginTask;
import me.justfu498.prisoncore.mine.MineCommand;
import me.justfu498.prisoncore.mine.Mines;
import me.justfu498.prisoncore.omnitool.Omnitool;
import me.justfu498.prisoncore.omnitool.enums.OmnitoolListMessages;
import me.justfu498.prisoncore.omnitool.enums.OmnitoolMessages;
import me.justfu498.prisoncore.placeholderapi.PAPIConfig;
import me.justfu498.prisoncore.placeholderapi.PrisonCoreExpansion;
import me.justfu498.prisoncore.prestige.Prestige;
import me.justfu498.prisoncore.prestige.enums.PrestigeMessages;
import me.justfu498.prisoncore.prestige.enums.PrestigeMessagesList;
import me.justfu498.prisoncore.rank.Rank;
import me.justfu498.prisoncore.rank.enums.RankMessages;
import me.justfu498.prisoncore.rank.enums.RankMessagesList;
import me.justfu498.prisoncore.token.Token;
import me.justfu498.prisoncore.token.enums.TokenMessages;
import me.justfu498.prisoncore.token.enums.TokenMessagesList;
import me.justfu498.prisoncore.util.ItemBuilder;
import me.justfu498.prisoncore.util.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class PrisonCore extends JavaPlugin implements CommandExecutor {

    private static Economy economy = null;
    private final Rank rank = new Rank(this);
    private final Prestige prestige = new Prestige(this);
    private final Autosell autosell = new Autosell(this);
    private final Token token = new Token(this);
    private final Omnitool omnitool = new Omnitool(this);
    private final Mines mines = new Mines(this);

    private final PAPIConfig papiConfig = new PAPIConfig(this);

    private final Utils utils = new Utils();
    private final ItemBuilder itemBuilder = new ItemBuilder(this);

    private final ReloadPluginTask reloadPluginTask = new ReloadPluginTask(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        rank.loadRank();
        RankMessages.initialize(rank.getConfig());
        RankMessagesList.initialize(rank.getConfig());
        getCommand("prisoncore").setExecutor(this);

        prestige.loadPrestige();
        PrestigeMessagesList.initialize(prestige.getConfig());
        PrestigeMessages.initialize(prestige.getConfig());

        papiConfig.createConfig();

        autosell.loadAutosell();
        AutosellMessages.initialize(autosell.getConfig());
        AutosellMessagesList.initialize(autosell.getConfig());

        token.loadToken();
        TokenMessages.initialize(token.getConfig());
        TokenMessagesList.initialize(token.getConfig());

        omnitool.loadOmnitool();
        OmnitoolMessages.initialize(omnitool.getConfig());
        OmnitoolListMessages.initialize(omnitool.getConfig());

        loadPlaceholderAPI();

        reloadPluginTask.start();

        mines.load();
        getCommand("mine").setExecutor(new MineCommand(this));
    }

    public FileConfiguration getPAPIConfig() {
        return papiConfig.getConfig();
    }

    public void loadPlaceholderAPI() {
        new PrisonCoreExpansion(this).register();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public Rank getRank() {
        return this.rank;
    }

    public Prestige getPrestige() {
        return this.prestige;
    }

    public Autosell getAutosell() {
        return this.autosell;
    }

    public Token getToken() {
        return this.token;
    }

    public Omnitool getOmnitool() {
        return this.omnitool;
    }

    public Mines getMines() {
        return this.mines;
    }

    public Utils getUtils() {
        return this.utils;
    }

    public ItemBuilder getItemBuilder() {
        return this.itemBuilder;
    }

    public void closeDatabases() {
        try {
            rank.closeConnection();
            prestige.closeConnection();
            token.closeConnection();
            autosell.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("prisoncore.reload")) {
                    rank.getRankConfig().reload();
                    sender.sendMessage(ChatColor.GREEN + "&a&l(!) Reloaded configs!");
                } else {
                    sender.sendMessage(utils.color(RankMessages.NO_PERMISSION.toString()));
                }
            } else if (sender instanceof ConsoleCommandSender) {
                rank.getRankConfig().reload();
                System.out.print("(!) Reloaded configs!");
            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        closeDatabases();
    }
}
