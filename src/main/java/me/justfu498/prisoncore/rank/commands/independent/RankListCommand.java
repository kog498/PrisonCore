package me.justfu498.prisoncore.rank.commands.independent;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.rank.enums.RankMessagesList;
import me.justfu498.prisoncore.rank.enums.RankMessages;
import me.justfu498.prisoncore.rank.manager.Rank;
import me.justfu498.prisoncore.rank.manager.RankManager;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankListCommand implements CommandExecutor {

    private final PrisonCore plugin;

    public RankListCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Utils utils = plugin.getUtils();
        RankManager manager = plugin.getRank().getRankManager();
        List<Rank> rankList = new ArrayList<>();
        List<String> rankLine = new ArrayList<>();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("prisoncore.rank.ranklist")) {
                int page;
                if (args.length == 0) {
                    page = 1;
                    for (int i = 1; i <= 11; i++) {
                        if (manager.getRank(i) != null) {
                            rankList.add(manager.getRank(i));
                        } else {
                            break;
                        }
                    }
                } else {
                    try {
                        page = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(utils.color(RankMessages.INVALID_NUMBER.toString()));
                        return true;
                    }
                    if (page == 1) {
                        for (int i = 1; i <= 11; i++) {
                            if (manager.getRank(i) != null) {
                                rankList.add(manager.getRank(i));
                            } else {
                                break;
                            }
                        }
                    } else if (page > 1) {
                        for (int i = page * 10 - 9; i <= page * 10 + 1; i++) {
                            if (manager.getRank(i) != null) {
                                rankList.add(manager.getRank(i));
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (rankList.size() == 0) {
                    player.sendMessage(utils.color(RankMessages.NO_MORE_RANKS.toString()));
                    return true;
                }

                for (int i = 0; i < rankList.size() - 1; i++) {
                    rankLine.add(RankMessages.FORMAT_RANK_LIST.toString()
                            .replace("%rank%", rankList.get(i).getName())
                            .replace("%next_rank%", rankList.get(i + 1).getName())
                            .replace("%cost%", utils.formatShort(rankList.get(i + 1).getCost())));
                }

                for (String line : RankMessagesList.RANK_LIST.toStringList()) {
                    if (line.equalsIgnoreCase("%ranks%")) {
                        for (String rank : rankLine) {
                            player.sendMessage(utils.color(rank));
                        }
                    } else {
                        player.sendMessage(utils.color(line).replace("%page%", (page + 1) + ""));
                    }
                }
            } else {
                player.sendMessage(utils.color(RankMessages.NO_PERMISSION.toString()));
            }
        }
        return true;
    }
}
