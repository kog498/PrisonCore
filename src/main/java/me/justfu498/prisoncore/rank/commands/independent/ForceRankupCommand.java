package me.justfu498.prisoncore.rank.commands.independent;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.rank.database.RankDatabase;
import me.justfu498.prisoncore.rank.enums.RankMessages;
import me.justfu498.prisoncore.rank.manager.Rank;
import me.justfu498.prisoncore.rank.manager.RankManager;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ForceRankupCommand implements CommandExecutor {

    private final PrisonCore plugin;

    public ForceRankupCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Utils utils = plugin.getUtils();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("prisoncore.rank.forcerankup")) {
                if (args.length > 0) {
                    String playerName = args[0];
                    Player target = Bukkit.getServer().getPlayerExact(playerName);
                    if (target == null) {
                        player.sendMessage(utils.color(RankMessages.TARGET_NOT_FOUND.toString()));
                        return true;
                    }
                    RankManager manager = plugin.getRank().getRankManager();
                    RankDatabase database = plugin.getRank().getRankDatabase();
                    Rank nextRank = manager.getNextRank(target);
                    if (nextRank == null) {
                        player.sendMessage(utils.color(RankMessages.LAST_RANK_IN_FORCE.toString()));
                        return true;
                    }
                    for (String line : manager.getCurrentRank(target).getExecuteCommands()) {
                        String[] split = line.split(":");
                        if (split[0].equalsIgnoreCase("[CONSOLE]")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), split[1].replace("%player%", target.getName()));
                        } else {
                            target.performCommand(split[1]);
                        }
                    }
                    try {
                        database.updatePlayerRank(target, nextRank.getIndex());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(utils.color(RankMessages.FORCE_RANKUP.toString())
                            .replace("%player%", target.getName())
                            .replace("%rank%", nextRank.getName()));
                    target.sendMessage(utils.color(RankMessages.TARGET_FORCE_RANKUP.toString())
                            .replace("%rank%", nextRank.getName()));
                } else {
                    player.sendMessage(utils.color(RankMessages.USAGE.toString()).replace("%usage%", "/forcerankup <player>"));
                }
            } else {
                player.sendMessage(utils.color(RankMessages.NO_PERMISSION.toString()));
            }
        }
        return true;
    }
}
