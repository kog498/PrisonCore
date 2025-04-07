package me.justfu498.prisoncore.omnitool.enchantment.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.mine.Mine;
import me.justfu498.prisoncore.mine.Mines;
import me.justfu498.prisoncore.omnitool.enchantment.EnchantmentManager;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExplosiveListener implements Listener {

    private final PrisonCore plugin;

    public ExplosiveListener(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Utils utils = plugin.getUtils();

        Mines mines = plugin.getMines();

        List<Block> explodedBlocks = new ArrayList<>();

        Mine mine = mines.findMine(event.getBlock().getLocation());
        Player player = event.getPlayer();

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (!utils.isOmnitoolInHand(hand)) {
            return;
        }
        NBTItem nbtItem = new NBTItem(hand);

        if (mine == null) {
            return;
        }

        EnchantmentManager manager = plugin.getOmnitool().getEnchantmentManager();
        if (!manager.getExplosive().isExplosiveTriggered(nbtItem)) {
            return;
        }

        Block west = event.getBlock().getRelative(BlockFace.WEST);
        Block east = event.getBlock().getRelative(BlockFace.EAST);
        Block north = event.getBlock().getRelative(BlockFace.NORTH);
        Block south = event.getBlock().getRelative(BlockFace.SOUTH);

        if (mine.isWithin(west.getLocation())) {
            explodedBlocks.add(west);
        }
        if (mine.isWithin(east.getLocation())) {
            explodedBlocks.add(east);
        }
        if (mine.isWithin(north.getLocation())) {
            explodedBlocks.add(north);
        }
        if (mine.isWithin(south.getLocation())) {
            explodedBlocks.add(south);
        }

        if (!explodedBlocks.isEmpty()) {
            player.playSound(event.getBlock().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            player.playEffect(event.getBlock().getLocation(), Effect.EXPLOSION_LARGE, 0);
        }

        for (Block block : explodedBlocks) {
            if (!event.isCancelled()) {
                fortuneBlock(player, block);
                block.setType(Material.AIR);
            }
        }
    }

    private int getBonus(Player player) {
        int max = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        int min = max / 2;
        int range = max - min + 1;
        int bonus = (int) (Math.random() * range) + min;
        if (bonus < 0) {
            bonus = 1;
        }

        return bonus;
    }

    private boolean hasFortuneEnchant(Player player) {
        return player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
    }

    private void fortuneBlock(Player player, Block block) {
        Utils utils = plugin.getUtils();
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
            String playerBlockName = block.getType().name();
            ItemStack _playerBlock = new ItemStack(Material.getMaterial(playerBlockName), 1, block.getData());
            if (utils.isSimilar(fortuneBlock, _playerBlock)) {
                int bonus = getBonus(player);
                if (hasFortuneEnchant(player)) {
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(playerBlockName), bonus, block.getData()));
                } else {
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(playerBlockName), 1, block.getData()));
                }
            }
        }
    }
}
