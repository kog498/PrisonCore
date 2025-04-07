package me.justfu498.prisoncore.prestige.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.prestige.commands.SubCommand;
import me.justfu498.prisoncore.prestige.enums.PrestigeMessagesList;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {

    private final PrisonCore plugin;

    public HelpCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show this help list";
    }

    @Override
    public String getSyntax() {
        return "/prestige help";
    }

    @Override
    public String getPermission() {
        return "none";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        Utils utils = plugin.getUtils();
        if (sender.hasPermission("prisoncore.prestige.admin.help")) {
            for (String line : PrestigeMessagesList.HELP_ADMIN.toStringList()) {
                sender.sendMessage(utils.color(line));
            }
            return;
        }

        for (String line : PrestigeMessagesList.HELP_PLAYER.toStringList()) {
            sender.sendMessage(utils.color(line));
        }
    }
}
