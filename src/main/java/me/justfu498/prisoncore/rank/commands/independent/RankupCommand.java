package me.justfu498.prisoncore.rank.commands.independent;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.rank.database.RankDatabase;
import me.justfu498.prisoncore.rank.enums.RankMessages;
import me.justfu498.prisoncore.rank.manager.Rank;
import me.justfu498.prisoncore.rank.manager.RankManager;
import me.justfu498.prisoncore.util.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class RankupCommand implements CommandExecutor {

    private final PrisonCore plugin;

    public RankupCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Utils utils = plugin.getUtils();
        Economy economy = PrisonCore.getEconomy();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("prisoncore.rank.rankup")) {
                RankManager manager = plugin.getRank().getRankManager();
                RankDatabase database = plugin.getRank().getRankDatabase();
                Rank nextRank = manager.getNextRank(player);
                if (nextRank != null) {
                    double rankCost = nextRank.getCost();
                    if (economy.getBalance(player) >= rankCost) {
                        for (String line : manager.getCurrentRank(player).getExecuteCommands()) {
                            String[] split = line.split(":");
                            if (split[0].equalsIgnoreCase("[CONSOLE]")) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), split[1].replace("%player%", player.getName()));
                            } else {
                                player.performCommand(split[1]);
                            }
                        }
                        try {
                            database.updatePlayerRank(player, nextRank.getIndex());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        economy.withdrawPlayer(player, rankCost);
                        player.sendMessage(utils.color(RankMessages.RANKUP.toString())
                                .replace("%next_rank%", nextRank.getName()));
                    } else {
                        player.sendMessage(utils.color(RankMessages.NOT_ENOUGH_MONEY.toString())
                                .replace("%cost%", utils.formatShort(rankCost)));
                    }
                } else {
                    player.sendMessage(utils.color(RankMessages.LAST_RANK.toString()));
                }
            } else {
                player.sendMessage(utils.color(RankMessages.NO_PERMISSION.toString()));
            }
        }
        return true;
    }
}
