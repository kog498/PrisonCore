package me.justfu498.prisoncore.rank.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.rank.commands.SubCommand;
import me.justfu498.prisoncore.rank.database.RankDatabase;
import me.justfu498.prisoncore.rank.enums.RankMessages;
import me.justfu498.prisoncore.rank.manager.Rank;
import me.justfu498.prisoncore.rank.manager.RankManager;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SetCommand extends SubCommand {

    private final PrisonCore plugin;

    public SetCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set player's rank";
    }

    @Override
    public String getSyntax() {
        return "/rank set <player> <rank>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.rank.set";
    }

    @Override
    public void playerExecutes(Player player, String[] args) {

        Utils utils = plugin.getUtils();
        RankManager manager = plugin.getRank().getRankManager();
        RankDatabase database = plugin.getRank().getRankDatabase();

        if (args.length > 2) {
            String playerName = args[1];
            Player target = Bukkit.getServer().getPlayerExact(playerName);
            if (target == null) {
                player.sendMessage(utils.color(RankMessages.TARGET_NOT_FOUND.toString()));
                return;
            }

            String rankName = args[2];
            Rank rank = manager.getRank(rankName);
            if (rank == null) {
                player.sendMessage(utils.color(RankMessages.RANK_NOT_AVAILABLE.toString()));
                return;
            }

            try {
                database.updatePlayerRank(target, rank.getIndex());
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            player.sendMessage(utils.color(RankMessages.SET_RANK.toString())
                    .replace("%player%", target.getName())
                    .replace("%rank%", rank.getName()));
            target.sendMessage(utils.color(RankMessages.TARGET_SET_RANK.toString())
                    .replace("%rank%", rank.getName()));
        } else {
            player.sendMessage(utils.color(RankMessages.USAGE.toString())
                    .replace("%usage%", getSyntax()));
        }
    }

    @Override
    public void consoleExecutes(ConsoleCommandSender consoleCommandSender, String[] args) {

        Utils utils = plugin.getUtils();
        RankManager manager = plugin.getRank().getRankManager();
        RankDatabase database = plugin.getRank().getRankDatabase();

        if (args.length > 2) {
            String playerName = args[1];
            Player target = Bukkit.getServer().getPlayerExact(playerName);
            if (target == null) {
                System.out.print(RankMessages.TARGET_NOT_FOUND);
                return;
            }

            String rankName = args[2];
            Rank rank = manager.getRank(rankName);
            if (rank == null) {
                System.out.print(RankMessages.RANK_NOT_AVAILABLE);
                return;
            }

            try {
                database.updatePlayerRank(target, rank.getIndex());
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            System.out.print(RankMessages.SET_RANK.toString()
                    .replace("%player%", target.getName())
                    .replace("%rank%", rank.getName()));
            target.sendMessage(utils.color(RankMessages.TARGET_SET_RANK.toString())
                    .replace("%rank%", rank.getName()));
        } else {
            System.out.print(RankMessages.USAGE.toString()
                    .replace("%usage%", getSyntax()));
        }
    }
}
