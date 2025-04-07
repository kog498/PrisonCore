package me.justfu498.prisoncore.omnitool;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.omnitool.commands.CommandManager;
import me.justfu498.prisoncore.omnitool.config.OmnitoolConfig;
import me.justfu498.prisoncore.omnitool.enchantment.EnchantmentManager;
import me.justfu498.prisoncore.omnitool.listeners.OmnitoolLevelUp;
import org.bukkit.configuration.file.FileConfiguration;

public class Omnitool {

    private final PrisonCore plugin;

    private OmnitoolConfig omnitoolConfig;
    private EnchantmentManager enchantmentManager;

    public Omnitool(PrisonCore plugin) {
        this.plugin = plugin;
    }

    private void loadConfig() {
        omnitoolConfig = new OmnitoolConfig(plugin);
        omnitoolConfig.createConfig();
    }

    public FileConfiguration getConfig() {
        return omnitoolConfig.getConfig();
    }

    private void loadCommands() {
        plugin.getCommand("omnitool").setExecutor(new CommandManager(plugin));
    }

    private void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new OmnitoolLevelUp(plugin), plugin);
    }

    public void loadManagers() {
        enchantmentManager = new EnchantmentManager(plugin);
        enchantmentManager.loadEnchants();
    }

    public EnchantmentManager getEnchantmentManager() {
        return enchantmentManager;
    }

    public void loadOmnitool() {
        loadConfig();
        loadCommands();
        loadListeners();
        loadManagers();
    }
}
