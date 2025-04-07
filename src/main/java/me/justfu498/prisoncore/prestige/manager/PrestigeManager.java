package me.justfu498.prisoncore.prestige.manager;

import me.justfu498.prisoncore.PrisonCore;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class PrestigeManager {
    private int maxPrestige;
    private String display;
    private double baseCost;
    private String costExpression;
    private List<String> executeCommands;

    private final PrisonCore plugin;

    public PrestigeManager(PrisonCore plugin) {
        this.plugin = plugin;
    }

    public int getMaxPrestige() {
        return maxPrestige;
    }

    public String getDisplay() {
        return display;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public String getCostExpression() {
        return costExpression;
    }

    public List<String> getExecuteCommands() {
        return executeCommands;
    }

    public int getCurrentPrestige(Player player) {
        int currentPrestige;
        try {
            currentPrestige = plugin.getPrestige().getPrestigeDatabase().getPlayerPrestige(player);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return currentPrestige;
    }

    public int getNextPrestige(Player player) {
        return getCurrentPrestige(player) + 1;
    }

    public double getPrestigeCost(Player player) {
        Expression exp = new ExpressionBuilder(getCostExpression())
                .variables("baseCost", "currentPrestige")
                .build()
                .setVariable("baseCost", getBaseCost())
                .setVariable("currentPrestige", getCurrentPrestige(player));
        return exp.evaluate();
    }

    public String getPrestigeDisplay(Player player) {
        return getDisplay().replace("%prestige%", getCurrentPrestige(player) + "");
    }

    public String getNextPrestigeDisplay(Player player) {
        return getDisplay().replace("%prestige%", (getCurrentPrestige(player) + 1) + "");
    }

    public void setPlayerPrestige(Player player, int value) {
        try {
            plugin.getPrestige().getPrestigeDatabase().updatePlayerPrestige(player, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadPrestige() {
        FileConfiguration config = this.plugin.getPrestige().getConfig();

        try {
            maxPrestige = Integer.parseInt(config.getString("settings.max-prestige"));
        } catch (NumberFormatException e) {
            System.out.print("Error! Max prestige value is not a number");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
        }

        System.out.print("Successfully loaded " + maxPrestige + " prestiges, enjoy!");

        display = config.getString("settings.display");

        try {
            baseCost = Double.parseDouble(config.getString("settings.base-cost"));
        } catch (NumberFormatException e) {
            System.out.print("Error! Base cost value is not a number");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
        }

        costExpression = config.getString("settings.increase-cost-expression");
        executeCommands = config.getStringList("settings.execute-cmds");
    }
}
