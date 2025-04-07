package me.justfu498.prisoncore.token.commands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.token.commands.subcommands.*;
import me.justfu498.prisoncore.token.enums.TokenMessages;
import me.justfu498.prisoncore.token.enums.TokenMessagesList;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private final PrisonCore plugin;

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(PrisonCore plugin) {
        this.plugin = plugin;
        subCommands.add(new AddCommand(this.plugin));
        subCommands.add(new RemoveCommand(this.plugin));
        subCommands.add(new BalanceCommand(this.plugin));
        subCommands.add(new SetCommand(this.plugin));
        subCommands.add(new HelpCommand(this.plugin));
        subCommands.add(new WithdrawCommand(this.plugin));
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
                                getSubCommands().get(i).executeCommand(player, args);
                            } else {
                                player.sendMessage(utils.color(TokenMessages.NO_PERMISSION.toString()));
                            }
                        } else {
                            getSubCommands().get(i).executeCommand(player, args);
                        }
                    }
                }
            } else {
                if (player.hasPermission("prisoncore.help.admin")) {
                    for (String line : TokenMessagesList.HELP_ADMIN.toStringList()) {
                        player.sendMessage(utils.color(line));
                    }
                } else {
                    for (String line : TokenMessagesList.HELP_PLAYER.toStringList()) {
                        player.sendMessage(utils.color(line));
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;
            if (args.length > 0) {
                for (int i = 0; i < getSubCommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                        getSubCommands().get(i).executeCommand(consoleCommandSender, args);
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
