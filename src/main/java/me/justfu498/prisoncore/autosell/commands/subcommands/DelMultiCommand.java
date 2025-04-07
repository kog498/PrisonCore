package me.justfu498.prisoncore.autosell.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.commands.SubCommand;
import me.justfu498.prisoncore.autosell.database.SellMultiplierDatabase;
import me.justfu498.prisoncore.autosell.enums.AutosellMessages;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class DelMultiCommand extends SubCommand {

    private final PrisonCore plugin;

    public DelMultiCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "delmulti";
    }

    @Override
    public String getDescription() {
        return "Remove sell multiplier from player account";
    }

    @Override
    public String getSyntax() {
        return "/autosell delmulti <player> <multiplier>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.autosell.delmulti";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        SellMultiplierDatabase smultiDatabase = plugin.getAutosell().getSellMultiDatabase();
        Utils utils = plugin.getUtils();

        if (args.length > 2) {
            Player target = Bukkit.getServer().getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(utils.color(AutosellMessages.TARGET_NOT_FOUND.toString()));
                return;
            }

            double multiplier;
            try {
                multiplier = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(utils.color(AutosellMessages.INVALID_NUMBER.toString()));
                return;
            }

            if (multiplier <= 0) {
                sender.sendMessage(utils.color(AutosellMessages.INVALID_NUMBER.toString()));
                return;
            }

            double newMultiplier;
            double playerMultiplier;
            try {
                playerMultiplier = smultiDatabase.getPlayerMultiplier(target);
                newMultiplier = playerMultiplier - multiplier;
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            if (newMultiplier < 1.0) {
                sender.sendMessage(utils.color(AutosellMessages.INVALID_MULTI.toString()).replace("%multiplier%", playerMultiplier + ""));
                return;
            }

            try {
                smultiDatabase.updatePlayerMultiplier(target, newMultiplier);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            sender.sendMessage(utils.color(AutosellMessages.DEL_MULTI.toString())
                    .replace("%player%", target.getName())
                    .replace("%multiplier%", multiplier + "")
                    .replace("%new_multiplier%", newMultiplier + ""));
            target.sendMessage(utils.color(AutosellMessages.TARGET_DEL_MULTI.toString())
                    .replace("%multiplier%", multiplier + "")
                    .replace("%new_multiplier%", newMultiplier + ""));
        } else {
            sender.sendMessage(utils.color(AutosellMessages.USAGE.toString()).replace("%usage%", getSyntax()));
        }
    }
}
