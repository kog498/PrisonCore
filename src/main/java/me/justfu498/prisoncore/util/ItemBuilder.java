package me.justfu498.prisoncore.util;

import de.tr7zw.nbtapi.NBTItem;
import me.justfu498.prisoncore.PrisonCore;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final PrisonCore plugin;
    private final Utils utils;

    public ItemBuilder(PrisonCore plugin) {
        this.plugin = plugin;
        utils = plugin.getUtils();
    }

    public ItemStack getTokenItem(Player player, double amount) {
        FileConfiguration config = plugin.getToken().getConfig();

        String material = config.getString("token-item.material");
        String name = config.getString("token-item.name");
        List<String> lore = new ArrayList<>();
        for (String line : config.getStringList("token-item.lore")) {
            lore.add(line
                    .replace("%player%", player.getName())
                    .replace("%amount%", utils.formatShort(amount)));
        }

        ItemStack stack = new ItemStack(Material.getMaterial(material));
        ItemMeta im = stack.getItemMeta();
        im.setDisplayName(utils.color(name));
        im.setLore(utils.color(lore));
        stack.setItemMeta(im);

        NBTItem nbtItem = new NBTItem(stack);
        nbtItem.setDouble("amount", amount);
        nbtItem.setString("info", "tokenitem");

        return nbtItem.getItem();
    }

    public ItemStack getOmnitool(Player player) {
        FileConfiguration config = plugin.getOmnitool().getConfig();

        String mainPath = "item-format";
        String material = config.getString(mainPath + ".material");
        String name = config.getString(mainPath + ".name");
        List<String> lore = new ArrayList<>();
        double xpNeeded = config.getDouble("omnitool-settings.base-xp-cost");
        for (String line : config.getStringList(mainPath + ".lore")) {
            lore.add(line
                    .replace("%level%", "0")
                    .replace("%xp_gained%", "0.0")
                    .replace("%xp_needed%", utils.formatShort(xpNeeded))
                    .replace("%level_progress%", utils.getProgress(0.0, xpNeeded) + "")
                    .replace("%broken_block%", "0")
                    .replace("%player%", player.getName())
                    .replace("%efficiency_level%", "0")
                    .replace("%fortune_level%", "0")
                    .replace("%explosive_level%", "0")
                    .replace("%merchant_level%", "0"));
        }
        boolean unbreakable = config.getBoolean(mainPath + ".unbreakable");

        ItemStack stack = new ItemStack(Material.getMaterial(material));
        ItemMeta im = stack.getItemMeta();
        im.setDisplayName(utils.color(name));
        im.setLore(utils.color(lore));
        if (unbreakable) {
            im.setUnbreakable(true);
            im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(im);

        NBTItem nbtItem = new NBTItem(stack);
        nbtItem.setString("info", "omnitool");
        nbtItem.setInteger("level", 0);
        nbtItem.setDouble("xpGained", 0.0);
        nbtItem.setDouble("xpNeeded", xpNeeded);
        nbtItem.setDouble("progress", 0.0);
        nbtItem.setInteger("brokenBlock", 0);
        nbtItem.setString("owner", player.getName());

        nbtItem.setInteger("efficiency", 0);
        nbtItem.setInteger("fortune", 0);
        nbtItem.setInteger("explosive", 0);
        nbtItem.setInteger("merchant", 0);

        return nbtItem.getItem();
    }
}
