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

public class AddMultiCommand extends SubCommand {

    private final PrisonCore plugin;

    public AddMultiCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "addmulti";
    }

    @Override
    public String getDescription() {
        return "Add sell multiplier to player account";
    }

    @Override
    public String getSyntax() {
        return "/autosell addmulti <player> <multiplier>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.autosell.addmulti";
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
            try {
                newMultiplier = smultiDatabase.getPlayerMultiplier(target) + multiplier;
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            try {
                smultiDatabase.updatePlayerMultiplier(target, newMultiplier);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            sender.sendMessage(utils.color(AutosellMessages.ADD_MULTI.toString())
                    .replace("%player%", target.getName())
                    .replace("%multiplier%", multiplier + "")
                    .replace("%new_multiplier%", newMultiplier + ""));
            target.sendMessage(utils.color(AutosellMessages.TARGET_ADD_MULTI.toString())
                    .replace("%multiplier%", multiplier + "")
                    .replace("%new_multiplier%", newMultiplier + ""));
        } else {
            sender.sendMessage(utils.color(AutosellMessages.USAGE.toString()).replace("%usage%", getSyntax()));
        }
    }
}
