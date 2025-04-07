package me.justfu498.prisoncore.omnitool.commands.subcommands;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.omnitool.commands.SubCommand;
import me.justfu498.prisoncore.omnitool.enums.OmnitoolMessages;
import me.justfu498.prisoncore.util.ItemBuilder;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand extends SubCommand {

    private final PrisonCore plugin;

    public GiveCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Give player an omnitool";
    }

    @Override
    public String getSyntax() {
        return "/omnitool give <player>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.omnitool.give";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {

        Utils utils = plugin.getUtils();

        if (args.length > 1) {
            String playerName = args[1];
            Player target = Bukkit.getServer().getPlayerExact(playerName);
            if (target == null) {
                sender.sendMessage(utils.color(OmnitoolMessages.TARGET_NOT_FOUND.toString()));
                return;
            }
            ItemBuilder itemBuilder = plugin.getItemBuilder();
            target.getInventory().addItem(itemBuilder.getOmnitool(target));
            sender.sendMessage(utils.color(OmnitoolMessages.GIVE.toString()).replace("%player%", target.getName()));
            target.sendMessage(utils.color(OmnitoolMessages.TARGET_GIVE.toString()));
        } else {
            sender.sendMessage(utils.color(OmnitoolMessages.USAGE.toString()).replace("%usage%", getSyntax()));
        }
    }
}
