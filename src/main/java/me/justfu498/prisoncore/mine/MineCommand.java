package me.justfu498.prisoncore.mine;

import me.justfu498.prisoncore.PrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MineCommand implements CommandExecutor {

    private final PrisonCore plugin;

    public MineCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    // Homework: Move into PlayerCache
    private final Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");

            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /mine <pos1|pos2|save <name>|paste <name>>");

            return true;
        }

        final Mines mines = plugin.getMines();

        final Player player = (Player) sender;
        final String param = args[0].toLowerCase();

        final Tuple<Location, Location> selection = selections.getOrDefault(player.getUniqueId(), new Tuple<>(null, null));

        switch (param) {
            case "pos1" -> {
                selection.setFirst(player.getLocation());
                sender.sendMessage("§8[§a✔§8] §7First location set!");
                selections.put(player.getUniqueId(), selection);
            }
            case "pos2" -> {
                selection.setSecond(player.getLocation());
                sender.sendMessage("§8[§a✔§8] §7Second location set!");
                selections.put(player.getUniqueId(), selection);
            }
            case "save" -> {
                if (selection.getFirst() == null || selection.getSecond() == null) {
                    sender.sendMessage("§8[§c❌§8] §7Please select both positions first using /mine pos1 and /mine pos2");

                    return true;
                }
                if (args.length != 2) {
                    sender.sendMessage("§8[§6🛑§8] §7Usage: /mine save <name>");

                    return true;
                }
                final String name = args[1];
                if (mines.findMine(name) != null) {
                    sender.sendMessage(ChatColor.RED + "Region by this name already exists.");

                    return true;
                }
                mines.saveMine(name, selection.getFirst(), selection.getSecond());
                sender.sendMessage("§8[§a✔§8] §7Schematic saved!");
            }
            case "list" -> sender.sendMessage(ChatColor.GOLD + "Installed mines: " + String.join(", ", mines.getMinesNames()));
            case "current" -> {
                final Mine standingIn = mines.findMine(player.getLocation());
                sender.sendMessage(ChatColor.GOLD + "You are standing in mine: "
                        + (standingIn == null ? "none" : standingIn.getName()));
            }
            default -> sender.sendMessage("§8[§6🛑§8] §7Usage: /mine <pos1|pos2|save <name>|paste <name>>");
        }

        return true;
    }

    private static class Tuple<A, B> {
        private A first;
        private B second;

        public Tuple(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public void setFirst(A first) {
            this.first = first;
        }

        public B getSecond() {
            return second;
        }

        public void setSecond(B second) {
            this.second = second;
        }
    }
}
