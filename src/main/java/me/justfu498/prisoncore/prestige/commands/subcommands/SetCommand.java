package me.justfu498.prisoncore.prestige.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.prestige.commands.SubCommand;
import me.justfu498.prisoncore.prestige.enums.PrestigeMessages;
import me.justfu498.prisoncore.prestige.manager.PrestigeManager;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand extends SubCommand {

    private final PrisonCore plugin;

    public SetCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set player's prestige";
    }

    @Override
    public String getSyntax() {
        return "/prestiges set <player> <prestige>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.prestige.set";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        PrestigeManager manager = plugin.getPrestige().getPrestigeManager();
        Utils utils = plugin.getUtils();

        if (args.length > 2) {
            String playerName = args[1];
            Player target = Bukkit.getServer().getPlayerExact(playerName);
            if (target == null) {
                sender.sendMessage(utils.color(PrestigeMessages.TARGET_NOT_FOUND.toString()));
                return;
            }

            int prestige;
            try {
                prestige = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(utils.color(PrestigeMessages.INVALID_INPUT.toString()));
                return;
            }

            if (prestige > manager.getMaxPrestige() || prestige < 0) {
                sender.sendMessage(utils.color(PrestigeMessages.INVALID_PRESTIGE.toString())
                        .replace("%max_prestige%", manager.getMaxPrestige() + ""));
                return;
            }

            manager.setPlayerPrestige(target, prestige);

            sender.sendMessage(utils.color(PrestigeMessages.SET_PRESTIGE.toString())
                    .replace("%player%", target.getName())
                    .replace("%prestige%", prestige + ""));
            target.sendMessage(utils.color(PrestigeMessages.TARGET_SET_PRESTIGE.toString())
                    .replace("%prestige%", prestige + ""));
        } else {
            sender.sendMessage(utils.color(PrestigeMessages.USAGE.toString()).replace("%usage%", getSyntax()));
        }
    }
}
