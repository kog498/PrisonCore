package me.justfu498.prisoncore.autosell.listeners;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BreakBlock implements Listener {

    private final PrisonCore plugin;

    public BreakBlock(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {

        Utils utils = plugin.getUtils();

        Player player = e.getPlayer();

        Block playerBlock = e.getBlock();
        //Cancelled by World Guard
        if (e.isCancelled()) {
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (!utils.isOmnitoolInHand(hand)) {
            return;
        }

        for (String blockInList : plugin.getAutosell().getFortuneBlock().getBlockList()) {
            ItemStack fortuneBlock;
            if (blockInList.contains(":")) {
                String[] split = blockInList.split(":");
                String blockName = split[0];
                byte blockData = Byte.parseByte(split[1]);
                fortuneBlock = new ItemStack(Material.getMaterial(blockName), 1, blockData);
            } else {
                fortuneBlock = new ItemStack(Material.getMaterial(blockInList), 1, (byte) 0);
            }
            String playerBlockName = playerBlock.getType().name();
            ItemStack _playerBlock = new ItemStack(Material.getMaterial(playerBlockName), 1, playerBlock.getData());
            if (utils.isSimilar(fortuneBlock, _playerBlock)) {
                e.setDropItems(false);
                int bonus = getBonus(player);
                if (hasFortuneEnchant(player)) {
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(playerBlockName), bonus, playerBlock.getData()));
                } else {
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(playerBlockName), 1, playerBlock.getData()));
                }
            }
        }
    }

    public int getBonus(Player player) {
        int max = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        int min = max / 2;
        int range = max - min + 1;
        int bonus = (int) (Math.random() * range) + min;
        if (bonus < 0) {
            bonus = 1;
        }

        return bonus;
    }

    public boolean hasFortuneEnchant(Player player) {
        return player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
    }
}
