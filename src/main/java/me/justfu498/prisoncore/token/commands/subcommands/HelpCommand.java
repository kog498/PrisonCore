package me.justfu498.prisoncore.token.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.token.commands.SubCommand;
import me.justfu498.prisoncore.token.enums.TokenMessagesList;
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
        return "Show help list";
    }

    @Override
    public String getSyntax() {
        return "/token help";
    }

    @Override
    public String getPermission() {
        return "none";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {

        Utils utils = plugin.getUtils();

        if (sender.hasPermission("prisoncore.help.admin")) {
            for (String line : TokenMessagesList.HELP_ADMIN.toStringList()) {
                sender.sendMessage(utils.color(line));
            }
        } else {
            for (String line : TokenMessagesList.HELP_PLAYER.toStringList()) {
                sender.sendMessage(utils.color(line));
            }
        }
    }
}
