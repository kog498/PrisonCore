package me.justfu498.prisoncore.rank.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract String getPermission();
    public abstract void playerExecutes(Player player, String[] args);
    public abstract void consoleExecutes(ConsoleCommandSender consoleCommandSender, String[] args);
}
