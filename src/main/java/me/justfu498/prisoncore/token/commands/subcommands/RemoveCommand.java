package me.justfu498.prisoncore.token.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.token.commands.SubCommand;
import me.justfu498.prisoncore.token.database.TokenDatabase;
import me.justfu498.prisoncore.token.enums.TokenMessages;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class RemoveCommand extends SubCommand {

    private final PrisonCore plugin;

    public RemoveCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove tokens from player's balance";
    }

    @Override
    public String getSyntax() {
        return "/token remove <player> <amount>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.token.remove";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {

        Utils utils = plugin.getUtils();
        TokenDatabase database = plugin.getToken().getTokenDatabase();

        if (args.length > 2) {
            String playerName = args[1];
            Player target = Bukkit.getServer().getPlayerExact(playerName);

            if (target == null) {
                sender.sendMessage(utils.color(TokenMessages.TARGET_NOT_FOUND.toString()));
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(utils.color(TokenMessages.INVALID_NUMBER.toString()));
                return;
            }

            double newBalance;
            try {
                newBalance = database.getPlayerToken(target) - amount;
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            if (newBalance < 0) {
                newBalance = 0;
            }

            try {
                database.updatePlayerToken(target, newBalance);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            sender.sendMessage(utils.color(TokenMessages.REMOVE_TOKEN.toString())
                    .replace("%token%", utils.formatShort(amount))
                    .replace("%player%", target.getName())
                    .replace("%new_balance%", utils.formatShort(newBalance)));
            target.sendMessage(utils.color(TokenMessages.TARGET_REMOVE_TOKEN.toString())
                    .replace("%token%", utils.formatShort(amount))
                    .replace("%new_balance%", utils.formatShort(newBalance)));
        } else {
            sender.sendMessage(utils.color(TokenMessages.USAGE.toString()).replace("%usage%", getSyntax()));
        }
    }
}
