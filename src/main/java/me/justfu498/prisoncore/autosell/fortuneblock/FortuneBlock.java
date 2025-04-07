package me.justfu498.prisoncore.autosell.fortuneblock;

import me.justfu498.prisoncore.PrisonCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class FortuneBlock {
    private double multiplier;
    private List<String> blackListWorlds;
    private List<String> blockList;

    private final PrisonCore plugin;

    public FortuneBlock(PrisonCore plugin) {
        this.plugin = plugin;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public List<String> getBlackListWorlds() {
        return blackListWorlds;
    }

    public List<String> getBlockList() {
        return blockList;
    }

    public void loadFortuneBlock() {
        FileConfiguration config = plugin.getAutosell().getConfig();

        try {
            multiplier = Double.parseDouble(config.getString("fortune-block.multiplier"));
        } catch (NumberFormatException e) {
            System.out.print("Error! Multiplier value is not a number!");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
        }

        blackListWorlds = config.getStringList("fortune-block.blacklist-worlds");
        blockList = config.getStringList("fortune-block.blocks");

        System.out.print("Loaded fortune blocks!");
    }
}
