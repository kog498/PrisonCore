package me.justfu498.prisoncore.autosell.commands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.commands.subcommands.*;
import me.justfu498.prisoncore.autosell.enums.AutosellMessagesList;
import me.justfu498.prisoncore.autosell.enums.AutosellMessages;
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
        subCommands.add(new SetMultiCommand(this.plugin));
        subCommands.add(new AddMultiCommand(this.plugin));
        subCommands.add(new DelMultiCommand(this.plugin));
        subCommands.add(new SetBoosterCommand(this.plugin));
        subCommands.add(new DelBoosterCommand(this.plugin));
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
                                player.sendMessage(utils.color(AutosellMessages.NO_PERMISSION.toString()));
                            }
                        } else {
                            getSubCommands().get(i).executeCommand(player, args);
                        }
                    }
                }
            } else {
                if (player.hasPermission("prisoncore.help.admin")) {
                    for (String line : AutosellMessagesList.HELP_ADMIN.toStringList()) {
                        player.sendMessage(utils.color(line));
                    }
                } else {
                    for (String line : AutosellMessagesList.HELP_PLAYER.toStringList()) {
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