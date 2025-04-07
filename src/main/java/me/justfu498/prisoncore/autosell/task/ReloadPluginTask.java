package me.justfu498.prisoncore.autosell.task;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.database.BoosterDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReloadPluginTask {

    private final PrisonCore plugin;

    public ReloadPluginTask(PrisonCore plugin) {
        this.plugin = plugin;
    }

    public void start() {
        List<Player> listOfPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        BoosterDatabase database = plugin.getAutosell().getBoosterDatabase();
        for (Player player : listOfPlayers) {
            try {
                if (database.getPlayerBooster(player) != -1) {
                    SellBoosterTask task = new SellBoosterTask(plugin, player);
                    task.start();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}