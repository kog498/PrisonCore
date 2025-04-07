package me.justfu498.prisoncore.prestige.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.prestige.commands.SubCommand;
import me.justfu498.prisoncore.prestige.enums.PrestigeMessages;
import me.justfu498.prisoncore.prestige.manager.PrestigeManager;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand extends SubCommand {

    private final PrisonCore plugin;

    public AddCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Add player prestige";
    }

    @Override
    public String getSyntax() {
        return "/prestige add <player> <prestige>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.prestige.add";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {

        Utils utils = plugin.getUtils();
        PrestigeManager manager = plugin.getPrestige().getPrestigeManager();

        if (args.length > 2) {
            String playerName = args[1];
            Player target = Bukkit.getServer().getPlayerExact(playerName);

            if (target == null) {
                sender.sendMessage(utils.color(PrestigeMessages.TARGET_NOT_FOUND.toString()));
                return;
            }

            int prestige;
            int currentPrestige = manager.getCurrentPrestige(target);
            try {
                prestige = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(utils.color(PrestigeMessages.INVALID_INPUT.toString()));
                return;
            }

            int newPrestige = currentPrestige + prestige;
            int maxPrestige = manager.getMaxPrestige();

            if (newPrestige > maxPrestige || newPrestige < 0) {
                sender.sendMessage(utils.color(PrestigeMessages.INVALID_PRESTIGE.toString().replace("%max_prestige%", maxPrestige + "")));
                return;
            }

            manager.setPlayerPrestige(target, newPrestige);

            sender.sendMessage(utils.color(PrestigeMessages.ADD_PRESTIGE.toString())
                    .replace("%prestige%", prestige + "")
                    .replace("%player%", target.getName())
                    .replace("%new_prestige%", newPrestige + ""));
            target.sendMessage(utils.color(PrestigeMessages.TARGET_ADD_PRESTIGE.toString())
                    .replace("%prestige%", prestige + "")
                    .replace("%new_prestige%", newPrestige + ""));
        } else {
            sender.sendMessage(utils.color(PrestigeMessages.USAGE.toString()).replace("%usage%", getSyntax()));
        }
    }
}
