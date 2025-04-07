package me.justfu498.prisoncore.prestige.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.prestige.commands.SubCommand;
import me.justfu498.prisoncore.prestige.database.PrestigeDatabase;
import me.justfu498.prisoncore.prestige.enums.PrestigeMessagesList;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class TopCommand extends SubCommand {

    private final PrisonCore plugin;

    public TopCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "top";
    }

    @Override
    public String getDescription() {
        return "Show player top";
    }

    @Override
    public String getSyntax() {
        return "/prestige top";
    }

    @Override
    public String getPermission() {
        return "prisoncore.prestige.top";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        Utils utils = plugin.getUtils();
        PrestigeDatabase database = plugin.getPrestige().getPrestigeDatabase();

        for (String line : PrestigeMessagesList.LEADERBOARD_HEADER.toStringList()) {
            sender.sendMessage(utils.color(line));
        }

        int index = 1;

        for (String line : PrestigeMessagesList.LEADERBOARD_BODY.toStringList()) {
            try {
                line = line
                        .replace("%player-top" + index + "%", database.getTopPlayerName(index))
                        .replace("%prestige-top" + index + "%", database.getTopPlayerPrestige(index) + "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sender.sendMessage(utils.color(line));
            index++;
        }

        for (String line : PrestigeMessagesList.LEADERBOARD_FOOTER.toStringList()) {
            sender.sendMessage(utils.color(line));
        }
    }
}
