package me.justfu498.prisoncore.omnitool.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.util.Utils;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OmnitoolLevelUp implements Listener {

    private final PrisonCore plugin;
    private final FileConfiguration config;
    private final Utils utils;

    public OmnitoolLevelUp(PrisonCore plugin) {
        this.plugin = plugin;
        config = plugin.getOmnitool().getConfig();
        utils = plugin.getUtils();
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack playerOmnitool = player.getInventory().getItemInMainHand();

        NBTItem nbtItem = new NBTItem(playerOmnitool);
        if (!nbtItem.hasNBTData()) return;

        String info = nbtItem.getString("info");
        if (!info.equals("omnitool")) return;

        double xpGained = nbtItem.getDouble("xpGained");
        xpGained++;
        nbtItem.setDouble("xpGained", xpGained);

        //Except exploding events
        int brokenBlock = nbtItem.getInteger("brokenBlock");
        brokenBlock++;
        nbtItem.setInteger("brokenBlock", brokenBlock);

        //Level up processing
        int level = nbtItem.getInteger("level");
        double xpNeeded = nbtItem.getDouble("xpNeeded");
        boolean isLevelUp = xpGained >= xpNeeded;
        while (xpGained >= xpNeeded) {
            xpGained = xpGained - xpNeeded;
            xpNeeded = getNextLevelCost(level);
            level++;

            nbtItem.setDouble("xpGained", xpGained);
            nbtItem.setDouble("xpNeeded", xpNeeded);
            nbtItem.setInteger("level", level);

            nbtItem.applyNBT(playerOmnitool);
            ItemStack updatedOmnitool = getItemUpdated(player, playerOmnitool, nbtItem);
            player.getInventory().setItemInMainHand(updatedOmnitool);
        }

        if (!isLevelUp) {
            nbtItem.applyNBT(playerOmnitool);
            ItemStack updatedOmnitool = getItemUpdated(player, playerOmnitool, nbtItem);
            player.getInventory().setItemInMainHand(updatedOmnitool);
        }
    }

    public double getNextLevelCost(int currentLevel) {
        String mainPath = "omnitool-settings";
        String costExpression = config.getString(mainPath + ".increase-cost-expression");
        double baseCost = config.getDouble(mainPath + ".base-xp-cost");
        Expression exp = new ExpressionBuilder(costExpression)
                .variables("baseCost", "currentLevel")
                .build()
                .setVariable("baseCost", baseCost)
                .setVariable("currentLevel", currentLevel);
        return exp.evaluate();
    }

    public ItemStack getItemUpdated(Player player, ItemStack playerOmnitool, NBTItem nbtItem) {
        String mainPath = "item-format";
        List<String> lore = new ArrayList<>();
        for (String line : config.getStringList(mainPath + ".lore")) {
            lore.add(line
                    .replace("%level%", nbtItem.getInteger("level") + "")
                    .replace("%xp_gained%", nbtItem.getDouble("xpGained") + "")
                    .replace("%xp_needed%", nbtItem.getDouble("xpNeeded") + "")
                    .replace("%level_progress%", utils.getProgress(nbtItem.getDouble("xpGained"), nbtItem.getDouble("xpNeeded")) + "")
                    .replace("%broken_block%", utils.formatShort(nbtItem.getInteger("brokenBlock")))
                    .replace("%player%", player.getName())
                    .replace("%efficiency_level%", nbtItem.getInteger("efficiency") + "")
                    .replace("%fortune_level%", nbtItem.getInteger("fortune") + "")
                    .replace("%explosive_level%", nbtItem.getInteger("explosive") + "")
                    .replace("%merchant_level%", nbtItem.getInteger("merchant") + ""));
        }
        ItemMeta im = playerOmnitool.getItemMeta();
        im.setLore(plugin.getUtils().color(lore));
        playerOmnitool.setItemMeta(im);
        return playerOmnitool;
    }


}
