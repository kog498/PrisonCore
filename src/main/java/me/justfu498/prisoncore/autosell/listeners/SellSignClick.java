package me.justfu498.prisoncore.autosell.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SellSignClick implements Listener {

    @EventHandler
    public void onClickSign(PlayerInteractEvent e) {

        Block block = e.getClickedBlock();
        Player player = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (block.getType().equals(Material.SIGN) || block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)) {
                player.performCommand("sellall a");
            }
        }
    }
}
