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

public class SetMultiCommand extends SubCommand {

    private final PrisonCore plugin;

    public SetMultiCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "setmulti";
    }

    @Override
    public String getDescription() {
        return "Set player's sell multiplier";
    }

    @Override
    public String getSyntax() {
        return "/autosell setmulti <player> <multiplier>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.autosell.setmulti";
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

            try {
                smultiDatabase.updatePlayerMultiplier(target, multiplier);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            sender.sendMessage(utils.color(AutosellMessages.SET_MULTI.toString())
                    .replace("%player%", target.getName())
                    .replace("%multiplier%", multiplier + ""));
            target.sendMessage(utils.color(AutosellMessages.TARGET_SET_MULTI.toString())
                    .replace("%multiplier%", multiplier + ""));
        } else {
            sender.sendMessage(utils.color(AutosellMessages.USAGE.toString()).replace("%usage%", getSyntax()));
        }
    }
}
