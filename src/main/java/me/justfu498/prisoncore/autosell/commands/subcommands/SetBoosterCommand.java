package me.justfu498.prisoncore.autosell.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.commands.SubCommand;
import me.justfu498.prisoncore.autosell.database.BoosterDatabase;
import me.justfu498.prisoncore.autosell.enums.AutosellMessages;
import me.justfu498.prisoncore.autosell.task.SellBoosterTask;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SetBoosterCommand extends SubCommand {

    private final PrisonCore plugin;

    public SetBoosterCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "setbooster";
    }

    @Override
    public String getDescription() {
        return "Give player a sell multiplier booster";
    }

    @Override
    public String getSyntax() {
        return "/autosell setbooster <player> <multiplier> <duration>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.autosell.setbooster";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        BoosterDatabase database = plugin.getAutosell().getBoosterDatabase();
        Utils utils = plugin.getUtils();

        if (args.length > 3) {
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

            int duration;
            try {
                duration = utils.toSecond(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(utils.color(AutosellMessages.INVALID_NUMBER.toString()));
                return;
            }
            if (duration < 0) {
                sender.sendMessage(utils.color(AutosellMessages.INVALID_NUMBER.toString()));
                return;
            }

            try {
                database.updatePlayerBooster(target, multiplier, duration);
                SellBoosterTask task = new SellBoosterTask(plugin, target);
                task.start();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            sender.sendMessage(utils.color(AutosellMessages.SET_BOOSTER.toString())
                    .replace("%player%", target.getName())
                    .replace("%duration%", utils.formatTimeByDate(duration))
                    .replace("%multiplier%", multiplier + ""));
            target.sendMessage(utils.color(AutosellMessages.TARGET_SET_BOOSTER.toString())
                    .replace("%duration%", utils.formatTimeByDate(duration))
                    .replace("%multiplier%", multiplier + ""));
        } else {
            sender.sendMessage(utils.color(AutosellMessages.USAGE.toString()).replace("%usage%", getSyntax()));
        }
    }
}
