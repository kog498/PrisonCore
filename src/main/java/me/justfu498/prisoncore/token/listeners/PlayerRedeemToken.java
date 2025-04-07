package me.justfu498.prisoncore.token.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.token.database.TokenDatabase;
import me.justfu498.prisoncore.token.enums.TokenMessages;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class PlayerRedeemToken implements Listener {

    private final PrisonCore plugin;

    public PlayerRedeemToken(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Utils utils = plugin.getUtils();
        TokenDatabase database = plugin.getToken().getTokenDatabase();

        Player player = event.getPlayer();

        if (utils.hasOffhand()) {
            if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.isCancelled()) {
                event.setCancelled(false);
            }

            ItemStack hand = player.getItemInHand();
            if (hand == null || hand.getType() == Material.AIR) return;

            NBTItem nbtItem = new NBTItem(hand);
            if (!nbtItem.hasNBTData()) return;

            String info = nbtItem.getString("info");
            if (!info.equals("tokenitem")) return;

            double amount = 0;
            if (hand.getAmount() == 1) {
                amount = nbtItem.getDouble("amount");
            } else if (hand.getAmount() > 1) {
                amount = nbtItem.getDouble("amount") * hand.getAmount();
            }

            player.setItemInHand(new ItemStack(Material.AIR));
            player.updateInventory();

            double newBalance;
            try {
                newBalance = database.getPlayerToken(player) + amount;
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            try {
                database.updatePlayerToken(player, newBalance);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            player.sendMessage(utils.color(TokenMessages.REDEEM_TOKEN.toString())
                    .replace("%token%", utils.formatShort(amount))
                    .replace("%new_balance%", utils.formatShort(newBalance)));
        }
    }
}
