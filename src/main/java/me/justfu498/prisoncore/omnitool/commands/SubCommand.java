package me.justfu498.prisoncore.omnitool.commands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract String getPermission();
    public abstract void executeCommand(CommandSender sender, String[] args);
}
