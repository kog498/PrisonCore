package me.justfu498.prisoncore.token.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.token.commands.SubCommand;
import me.justfu498.prisoncore.token.database.TokenDatabase;
import me.justfu498.prisoncore.token.enums.TokenMessages;
import me.justfu498.prisoncore.util.ItemBuilder;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class WithdrawCommand extends SubCommand {

    private final PrisonCore plugin;

    public WithdrawCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "withdraw";
    }

    @Override
    public String getDescription() {
        return "Change tokens in your account to physical tokens";
    }

    @Override
    public String getSyntax() {
        return "/token withdraw <amount>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.token.withdraw";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {

        Utils utils = plugin.getUtils();
        ItemBuilder itemBuilder = plugin.getItemBuilder();
        TokenDatabase database = plugin.getToken().getTokenDatabase();

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length > 1) {
                double amount;
                try {
                    amount = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(utils.color(TokenMessages.INVALID_NUMBER.toString()));
                    return;
                }

                double newBalance;
                try {
                    newBalance = database.getPlayerToken(player) - amount;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }

                if (newBalance < 0) {
                    sender.sendMessage(utils.color(TokenMessages.NOT_ENOUGH_TOKEN.toString()));
                    return;
                }

                try {
                    database.updatePlayerToken(player, newBalance);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }

                player.getInventory().addItem(itemBuilder.getTokenItem(player, amount));
                sender.sendMessage(utils.color(TokenMessages.WITHDRAW_TOKEN.toString())
                        .replace("%token%", utils.formatShort(amount))
                        .replace("%new_balance%", utils.formatShort(newBalance)));
            } else {
                sender.sendMessage(utils.color(TokenMessages.USAGE.toString()).replace("%usage%", getSyntax()));
            }
        } else {
            sender.sendMessage(utils.color(TokenMessages.ONLY_PLAYER.toString()));
        }
    }
}
