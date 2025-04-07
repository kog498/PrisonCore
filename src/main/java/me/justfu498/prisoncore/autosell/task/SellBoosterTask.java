package me.justfu498.prisoncore.autosell.task;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.database.BoosterDatabase;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SellBoosterTask extends Thread {
    private final Player player;

    private final PrisonCore plugin;

    public SellBoosterTask(PrisonCore plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void run() {
        BoosterDatabase database = plugin.getAutosell().getBoosterDatabase();
        Utils utils = plugin.getUtils();
        try {
            while (database.getPlayerBoosterDuration(player) != 0) {
                if (!player.isOnline()) {
                    this.interrupt();
                    return;
                }
                database.updatePlayerBooster(player, database.getPlayerBoosterDuration(player) - 1);
                sleep(1000);
            }
            player.sendMessage(utils.color("&cYour booster is expired!"));
            database.removePlayer(player);
            this.interrupt();
        } catch (SQLException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}