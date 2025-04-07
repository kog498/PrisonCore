package me.justfu498.prisoncore.token.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.token.commands.SubCommand;
import me.justfu498.prisoncore.token.database.TokenDatabase;
import me.justfu498.prisoncore.token.enums.TokenMessagesList;
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
        return "Show top player token";
    }

    @Override
    public String getSyntax() {
        return "/token top";
    }

    @Override
    public String getPermission() {
        return "prisoncore.token.top";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        Utils utils = plugin.getUtils();
        TokenDatabase database = plugin.getToken().getTokenDatabase();

        for (String line : TokenMessagesList.LEADERBOARD_HEADER.toStringList()) {
            sender.sendMessage(utils.color(line));
        }

        int index = 1;

        for (String line : TokenMessagesList.LEADERBOARD_BODY.toStringList()) {
            try {
                line = line
                        .replace("%player-top" + index + "%", database.getTopPlayerName(index))
                        .replace("%token-top" + index + "%", utils.formatShort(database.getTopPlayerToken(index)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sender.sendMessage(utils.color(line));
            index++;
        }

        for (String line : TokenMessagesList.LEADERBOARD_FOOTER.toStringList()) {
            sender.sendMessage(utils.color(line));
        }
    }
}
