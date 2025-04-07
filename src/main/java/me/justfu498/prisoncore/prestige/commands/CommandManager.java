package me.justfu498.prisoncore.prestige.commands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.prestige.commands.subcommands.*;
import me.justfu498.prisoncore.prestige.database.PrestigeDatabase;
import me.justfu498.prisoncore.prestige.enums.PrestigeMessages;
import me.justfu498.prisoncore.prestige.manager.PrestigeManager;
import me.justfu498.prisoncore.rank.manager.Rank;
import me.justfu498.prisoncore.rank.manager.RankManager;
import me.justfu498.prisoncore.util.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();
    private final PrisonCore plugin;

    public CommandManager(PrisonCore plugin) {
        this.plugin = plugin;
        subCommands.add(new SetCommand(this.plugin));
        subCommands.add(new HelpCommand(this.plugin));
        subCommands.add(new AddCommand(this.plugin));
        subCommands.add(new RemoveCommand(this.plugin));
        subCommands.add(new TopCommand(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Utils utils = plugin.getUtils();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                for (int i = 0; i < getSubCommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                        if (!getSubCommands().get(i).getPermission().equalsIgnoreCase("none")) {
                            if (player.hasPermission(getSubCommands().get(i).getPermission())) {
                                getSubCommands().get(i).executeCommand(sender, args);
                            } else {
                                player.sendMessage(utils.color(PrestigeMessages.NO_PERMISSION.toString()));
                            }
                        } else {
                            getSubCommands().get(i).executeCommand(sender, args);
                        }
                    }
                }
            } else {
                RankManager rankManager = plugin.getRank().getRankManager();
                PrestigeManager prestigeManager = plugin.getPrestige().getPrestigeManager();
                PrestigeDatabase database = plugin.getPrestige().getPrestigeDatabase();
                Economy economy = PrisonCore.getEconomy();
                if (player.hasPermission("prisoncore.prestige")) {
                    Rank nextRank = rankManager.getNextRank(player);
                    int playerPrestige = prestigeManager.getCurrentPrestige(player);
                    int maxPrestige = prestigeManager.getMaxPrestige();
                    double prestigeCost = prestigeManager.getPrestigeCost(player);
                    if (nextRank == null) {
                        if (playerPrestige < maxPrestige) {
                            if (economy.getBalance(player) >= prestigeCost) {
                                economy.withdrawPlayer(player, prestigeCost);
                                try {
                                    database.updatePlayerPrestige(player, playerPrestige + 1);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                for (String line : prestigeManager.getExecuteCommands()) {
                                    String[] split = line.split(":");
                                    if (split[0].equalsIgnoreCase("[CONSOLE]")) {
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), split[1].replace("%player%", player.getName()));
                                    } else {
                                        player.performCommand(split[1].replace("%player%", player.getName()));
                                    }
                                }

                                player.sendMessage(utils.color(PrestigeMessages.PRESTIGE.toString())
                                        .replace("%prestige%", (playerPrestige + 1) + ""));
                            } else {
                                player.sendMessage(utils.color(PrestigeMessages.NOT_ENOUGH_MONEY.toString())
                                        .replace("%cost%", utils.formatShort(prestigeCost)));
                            }
                        } else {
                            player.sendMessage(utils.color(PrestigeMessages.LAST_PRESTIGE.toString()));
                        }
                    } else {
                        player.sendMessage(utils.color(PrestigeMessages.NOT_IN_THE_LAST_RANK.toString()));
                    }
                } else {
                    player.sendMessage(utils.color(PrestigeMessages.NO_PERMISSION.toString()));
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length > 0) {
                for (int i = 0; i < getSubCommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                        getSubCommands().get(i).executeCommand(sender, args);
                    }
                }
            } else {
                for (int i = 0; i < getSubCommands().size(); i++) {
                    System.out.print(getSubCommands().get(i).getSyntax() + " - " + getSubCommands().get(i).getDescription());
                }
            }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }
}
