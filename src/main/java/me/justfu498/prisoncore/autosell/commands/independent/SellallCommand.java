package me.justfu498.prisoncore.autosell.commands.independent;

import de.tr7zw.nbtapi.NBTItem;
import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.database.BoosterDatabase;
import me.justfu498.prisoncore.autosell.database.SellMultiplierDatabase;
import me.justfu498.prisoncore.autosell.enums.AutosellMessagesList;
import me.justfu498.prisoncore.autosell.enums.AutosellMessages;
import me.justfu498.prisoncore.autosell.shopmanager.Shop;
import me.justfu498.prisoncore.autosell.shopmanager.ShopManager;
import me.justfu498.prisoncore.omnitool.enchantment.EnchantmentManager;
import me.justfu498.prisoncore.omnitool.enums.OmnitoolMessages;
import me.justfu498.prisoncore.util.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SellallCommand implements CommandExecutor {

    private final PrisonCore plugin;

    public SellallCommand(PrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Utils utils = plugin.getUtils();
        ShopManager manager = plugin.getAutosell().getShopManager();
        Economy economy = PrisonCore.getEconomy();
        SellMultiplierDatabase smultiDatabase = plugin.getAutosell().getSellMultiDatabase();
        BoosterDatabase boosterDatabase = plugin.getAutosell().getBoosterDatabase();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("prisoncore.autosell.sellall")) {
                player.sendMessage(utils.color(AutosellMessages.NO_PERMISSION.toString()));
                return true;
            }

            if (args.length > 0) {
                Shop shop = manager.getShop(args[0]);
                if (shop == null) {
                    player.sendMessage(utils.color(AutosellMessages.NOT_AVAILABLE_SHOP.toString()));
                    return true;
                }

                if (!player.hasPermission("prisoncore.autosell.sellall." + shop.getName())) {
                    player.sendMessage(utils.color(AutosellMessages.NO_PERM_TO_SELL.toString()));
                    return true;
                }

                double receivedMoney = 0;
                boolean hasItemToSell = false;
                List<String> nameOfItems = new ArrayList<>();
                List<Integer> amountOfItems = new ArrayList<>();

                for (ItemStack playerItem : player.getInventory().getStorageContents()) {
                    if (playerItem != null) {
                        for (String sellItem : shop.getItems()) {
                            String[] split;
                            ItemStack itemStack;
                            String itemType;
                            short itemData;
                            double itemValue;
                            if (sellItem.contains(":")) {
                                split = sellItem.split(":");
                                itemType = split[0];
                                String[] split2 = split[1].split(";");
                                itemData = Short.parseShort(split2[0]);
                                itemValue = Double.parseDouble(split2[1]);
                            } else {
                                split = sellItem.split(";");
                                itemType = split[0];
                                itemData = 0;
                                itemValue = Double.parseDouble(split[1]);
                            }
                            itemStack = new ItemStack(Material.getMaterial(itemType), 1, itemData);
                            boolean isOldItem = false;
                            if (utils.isSimilar(playerItem, itemStack)) {
                                int itemAmount = playerItem.getAmount();
                                String itemName = playerItem.getType().name();
                                receivedMoney += itemAmount * itemValue;
                                hasItemToSell = true;
                                if (nameOfItems.size() == 0) {
                                    nameOfItems.add(itemName);
                                    amountOfItems.add(itemAmount);
                                } else {
                                    for (int i = 0; i < nameOfItems.size(); i++) {
                                        if (itemName.equalsIgnoreCase(nameOfItems.get(i))) {
                                            amountOfItems.set(i, amountOfItems.get(i) + itemAmount);
                                            isOldItem = true;
                                            break;
                                        }
                                    }
                                    if (!isOldItem) {
                                        nameOfItems.add(itemName);
                                        amountOfItems.add(itemAmount);
                                    }
                                }
                                player.getInventory().remove(playerItem);
                            }
                        }
                    }
                }

                if (hasItemToSell) {
                    List<String> itemSold = new ArrayList<>();
                    for (int i = 0; i < nameOfItems.size(); i++) {
                        itemSold.add(AutosellMessages.FORMAT_ITEM_IN_LIST.toString()
                                .replace("%item%", utils.capitalizeFirstLetter(nameOfItems.get(i).replace("_", " ")))
                                .replace("%amount%", utils.formatShort(amountOfItems.get(i))));
                    }

                    double playerSellMulti;
                    try {
                        playerSellMulti = smultiDatabase.getPlayerMultiplier(player);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return true;
                    }
                    double extraMoney = playerSellMulti * receivedMoney - receivedMoney;
                    double totalMoney = receivedMoney + extraMoney;

                    for (String line : AutosellMessagesList.SELLALL.toStringList()) {
                        if (line.equalsIgnoreCase("%items%")) {
                            for (String item : itemSold) {
                                player.sendMessage(utils.color(item));
                            }
                        } else {
                            player.sendMessage(utils.color(line)
                                    .replace("%shop%", shop.getName())
                                    .replace("%money_received%", utils.formatShort(receivedMoney))
                                    .replace("%multiplier%", playerSellMulti + "")
                                    .replace("%total_money%", utils.formatShort(totalMoney)));
                        }
                    }
                    economy.depositPlayer(player, totalMoney);

                    //Merchant
                    ItemStack hand = player.getInventory().getItemInMainHand();
                    if (utils.isOmnitoolInHand(hand)) {
                        NBTItem nbtItem = new NBTItem(hand);
                        EnchantmentManager enchantmentManager = plugin.getOmnitool().getEnchantmentManager();
                        if (isMerchantTriggered(nbtItem)) {
                            double merchantMoney = receivedMoney * enchantmentManager.getMerchant().getIncreaseSellMulti();
                            economy.depositPlayer(player, merchantMoney);
                            player.sendMessage(utils.color(OmnitoolMessages.MERCHANT_TRIGGER.toString()).replace("%money%", utils.formatShort(merchantMoney)));
                        }
                    }

                    //Booster
                    try {
                        if (boosterDatabase.getPlayerBooster(player) != -1) {
                            double boosterMoney = receivedMoney * boosterDatabase.getPlayerBooster(player);
                            economy.depositPlayer(player, boosterMoney);
                            player.sendMessage(utils.color(AutosellMessages.BOOSTER_SELLALL.toString())
                                    .replace("%money%", utils.formatShort(boosterMoney))
                                    .replace("%multiplier%", boosterDatabase.getPlayerBooster(player) + ""));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    player.sendMessage(utils.color(AutosellMessages.NOTHING_TO_SELL.toString()));
                }
            } else {
                player.sendMessage(utils.color(AutosellMessages.USAGE.toString())
                        .replace("%usage%", "/sellall <shop>"));
            }
        }
        return true;
    }

    public boolean isMerchantTriggered(NBTItem omnitool) {
        EnchantmentManager enchantmentManager = plugin.getOmnitool().getEnchantmentManager();
        double percentToTrigger = enchantmentManager.getMerchant().getCurrentTriggerPercent(omnitool.getInteger("merchant"));
        double min = enchantmentManager.getMerchant().getBaseTriggerPercent();
        double max = 1;
        double range = max - min + 1;
        double res = Math.random() * range + min;
        return percentToTrigger >= res;
    }
}