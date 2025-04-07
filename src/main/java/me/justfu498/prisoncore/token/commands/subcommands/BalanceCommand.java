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

public class BalanceCommand extends SubCommand {

    private final PrisonCore plugin;

    public BalanceCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Show your balance or player's balance";
    }

    @Override
    public String getSyntax() {
        return "/token balance <player>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.token.balance";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {

        Utils utils = plugin.getUtils();
        TokenDatabase database = plugin.getToken().getTokenDatabase();

        double balance;

        if (args.length > 1) {
            String playerName = args[1];
            Player target = Bukkit.getServer().getPlayerExact(playerName);
            if (target == null) {
                sender.sendMessage(utils.color(TokenMessages.TARGET_NOT_FOUND.toString()));
                return;
            }

            try {
                balance = database.getPlayerToken(target);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            sender.sendMessage(utils.color(TokenMessages.OTHER_BALANCE.toString())
                    .replace("%player%", target.getName())
                    .replace("%token%", utils.formatShort(balance)));
        } else {
            if (sender instanceof Player) {
                try {
                    balance = database.getPlayerToken((Player) sender);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }

                sender.sendMessage(utils.color(TokenMessages.BALANCE.toString())
                        .replace("%token%", utils.formatShort(balance)));
            } else {
                sender.sendMessage(utils.color(TokenMessages.USAGE.toString()).replace("%usage%", getSyntax()));
            }
        }
    }
}
