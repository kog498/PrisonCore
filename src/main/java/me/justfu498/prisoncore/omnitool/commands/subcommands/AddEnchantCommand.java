package me.justfu498.prisoncore.omnitool.commands.subcommands;

import de.tr7zw.nbtapi.NBTItem;
import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.omnitool.commands.SubCommand;
import me.justfu498.prisoncore.omnitool.enchantment.EnchantmentManager;
import me.justfu498.prisoncore.omnitool.enums.OmnitoolMessages;
import me.justfu498.prisoncore.util.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class AddEnchantCommand extends SubCommand {

    private final PrisonCore plugin;

    public AddEnchantCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "addenchant";
    }

    @Override
    public String getDescription() {
        return "Enchant and upgrade player omnitool";
    }

    @Override
    public String getSyntax() {
        return "/omnitool addenchant <enchant> <level>";
    }

    @Override
    public String getPermission() {
        return "prisoncore.omnitool.addenchant";
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        Utils utils = plugin.getUtils();
        EnchantmentManager manager = plugin.getOmnitool().getEnchantmentManager();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 2) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (!utils.isOmnitoolInHand(hand)) {
                    player.sendMessage(utils.color(OmnitoolMessages.NOT_HOLD.toString()));
                    return;
                }
                NBTItem nbtItem = new NBTItem(hand);

                String enchant = args[1];
                String level = args[2];
                int currentLevel;
                int addLevel = getDigit(level);
                if (enchant.equalsIgnoreCase(manager.getEfficiency().getRawName())) {
                    if (addLevel <= 0) {
                        player.sendMessage(utils.color(OmnitoolMessages.INVALID_NUMBER.toString()));
                        return;
                    }
                    currentLevel = nbtItem.getInteger("efficiency");
                    int newLevel = currentLevel + addLevel;

                    if (currentLevel == manager.getEfficiency().getMaxLevel()) {
                        player.sendMessage(utils.color(OmnitoolMessages.ENCH_MAX_LEVEL.toString()));
                        return;
                    }
                    else if (newLevel > manager.getEfficiency().getMaxLevel()) {
                        newLevel = manager.getEfficiency().getMaxLevel();
                    }

                    nbtItem.setInteger("efficiency", newLevel);
                    nbtItem.applyNBT(hand);
                    ItemMeta im = hand.getItemMeta();
                    im.addEnchant(Enchantment.DIG_SPEED, nbtItem.getInteger("efficiency"), true);
                    hand.setItemMeta(im);
                    ItemStack itemStack = getItemUpdated(nbtItem.getString("owner"), hand, nbtItem);
                    player.getInventory().setItemInMainHand(itemStack);
                } else if (enchant.equalsIgnoreCase(manager.getFortune().getRawName())){
                    if (addLevel <= 0) {
                        player.sendMessage(utils.color(OmnitoolMessages.INVALID_NUMBER.toString()));
                        return;
                    }
                    currentLevel = nbtItem.getInteger("fortune");
                    int newLevel = currentLevel + addLevel;

                    if (currentLevel == manager.getFortune().getMaxLevel()) {
                        player.sendMessage(utils.color(OmnitoolMessages.ENCH_MAX_LEVEL.toString()));
                        return;
                    }
                    else if (newLevel > manager.getFortune().getMaxLevel()) {
                        newLevel = manager.getFortune().getMaxLevel();
                    }

                    nbtItem.setInteger("fortune", newLevel);
                    nbtItem.applyNBT(hand);
                    ItemMeta im = hand.getItemMeta();
                    im.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, nbtItem.getInteger("fortune"), true);
                    hand.setItemMeta(im);
                    ItemStack itemStack = getItemUpdated(nbtItem.getString("owner"), hand, nbtItem);
                    player.getInventory().setItemInMainHand(itemStack);
                } else if (enchant.equalsIgnoreCase(manager.getExplosive().getRawName())) {
                    if (addLevel <= 0) {
                        player.sendMessage(utils.color(OmnitoolMessages.INVALID_NUMBER.toString()));
                        return;
                    }
                    currentLevel = nbtItem.getInteger("explosive");
                    int newLevel = currentLevel + addLevel;

                    if (currentLevel == manager.getExplosive().getMaxLevel()) {
                        player.sendMessage(utils.color(OmnitoolMessages.ENCH_MAX_LEVEL.toString()));
                        return;
                    } else if (newLevel > manager.getExplosive().getMaxLevel()) {
                        newLevel = manager.getFortune().getMaxLevel();
                    }

                    nbtItem.setInteger("explosive", newLevel);
                    nbtItem.applyNBT(hand);
                    ItemStack itemStack = getItemUpdated(nbtItem.getString("owner"), hand, nbtItem);
                    player.getInventory().setItemInMainHand(itemStack);
                } else if (enchant.equalsIgnoreCase(manager.getMerchant().getRawName())){
                    if (addLevel <= 0) {
                        player.sendMessage(utils.color(OmnitoolMessages.INVALID_NUMBER.toString()));
                        return;
                    }
                    currentLevel = nbtItem.getInteger("merchant");
                    int newLevel = currentLevel + addLevel;

                    if (currentLevel == manager.getMerchant().getMaxLevel()) {
                        player.sendMessage(utils.color(OmnitoolMessages.ENCH_MAX_LEVEL.toString()));
                        return;
                    }
                    else if (newLevel > manager.getMerchant().getMaxLevel()) {
                        newLevel = manager.getMerchant().getMaxLevel();
                    }

                    nbtItem.setInteger("merchant", newLevel);
                    nbtItem.applyNBT(hand);
                    ItemStack itemStack = getItemUpdated(nbtItem.getString("owner"), hand, nbtItem);
                    player.getInventory().setItemInMainHand(itemStack);
                } else {
                    player.sendMessage(utils.color(OmnitoolMessages.INVALID_ENCHANT.toString()));
                }
            }
        } else {
            sender.sendMessage(utils.color(OmnitoolMessages.ONLY_PLAYER.toString()));
        }
    }

    public int getDigit(String digit) {
        int res;
        try {
            res = Integer.parseInt(digit);
        } catch (NumberFormatException e) {
            res = -1;
        }
        return res;
    }

    public ItemStack getItemUpdated(String playerName, ItemStack playerOmnitool, NBTItem nbtItem) {
        FileConfiguration config = plugin.getOmnitool().getConfig();
        Utils utils = plugin.getUtils();
        String mainPath = "item-format";
        List<String> lore = new ArrayList<>();
        for (String line : config.getStringList(mainPath + ".lore")) {
            lore.add(line
                    .replace("%level%", nbtItem.getInteger("level") + "")
                    .replace("%xp_gained%", nbtItem.getDouble("xpGained") + "")
                    .replace("%xp_needed%", nbtItem.getDouble("xpNeeded") + "")
                    .replace("%level_progress%", utils.getProgress(nbtItem.getDouble("xpGained"), nbtItem.getDouble("xpNeeded")) + "")
                    .replace("%broken_block%", utils.formatShort(nbtItem.getInteger("brokenBlock")))
                    .replace("%player%", playerName)
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
