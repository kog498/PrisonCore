package me.justfu498.prisoncore.autosell.listeners;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.database.BoosterDatabase;
import me.justfu498.prisoncore.autosell.task.SellBoosterTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoinBooster implements Listener {

    private final PrisonCore plugin;

    public PlayerJoinBooster(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BoosterDatabase database = plugin.getAutosell().getBoosterDatabase();
        Player player = event.getPlayer();
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
