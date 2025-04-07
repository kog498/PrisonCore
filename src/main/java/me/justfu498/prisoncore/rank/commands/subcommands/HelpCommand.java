package me.justfu498.prisoncore.rank.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.rank.commands.SubCommand;
import me.justfu498.prisoncore.rank.enums.RankMessagesList;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
        return "/rank help";
    }

    @Override
    public String getPermission() {
        return "none";
    }

    @Override
    public void playerExecutes(Player player, String[] args) {

        Utils utils = plugin.getUtils();

        if (player.hasPermission("prisoncore.rank.helpAdmin")) {
            for (String line : RankMessagesList.HELP_ADMIN.toStringList()) {
                player.sendMessage(utils.color(line));
            }
            return;
        }

        for (String line : RankMessagesList.HELP_PLAYER.toStringList()) {
            player.sendMessage(utils.color(line));
        }
    }

    @Override
    public void consoleExecutes(ConsoleCommandSender consoleCommandSender, String[] args) {

    }
}
