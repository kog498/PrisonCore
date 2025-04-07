package me.justfu498.prisoncore.autosell.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.commands.SubCommand;
import me.justfu498.prisoncore.autosell.database.BoosterDatabase;
import me.justfu498.prisoncore.autosell.enums.AutosellMessages;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class DelBoosterCommand extends SubCommand {

    private final PrisonCore plugin;

    public DelBoosterCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "delbooster";
    }

    @Override
    public String getDescription() {
        return "Remove player's sell multiplier booster";
    }

    @Override
    public String getSyntax() {
        return "/autosell delbooster <player>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.autosell.delbooster";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        BoosterDatabase database = plugin.getAutosell().getBoosterDatabase();
        Utils utils = plugin.getUtils();

        if (args.length > 1) {
            Player target = Bukkit.getServer().getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(utils.color(AutosellMessages.TARGET_NOT_FOUND.toString()));
                return;
            }

            try {
                if (database.getPlayerBooster(target) == -1) {
                    sender.sendMessage(utils.color(AutosellMessages.NOT_HAVE_BOOSTER.toString()).replace("%player%", target.getName()));
                    return;
                }
                database.updatePlayerBooster(target, 1);
                sender.sendMessage(utils.color(AutosellMessages.DEL_BOOSTER.toString())
                        .replace("%player%", target.getName()));
                target.sendMessage(utils.color(AutosellMessages.TARGET_DEL_BOOSTER.toString()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage(utils.color(AutosellMessages.USAGE.toString()).replace("%usage%", getSyntax()));
        }
    }
}
