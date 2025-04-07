package me.justfu498.prisoncore.autosell.shopmanager;

import me.justfu498.prisoncore.PrisonCore;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {
    private final List<Shop> shopList = new ArrayList<>();

    private final PrisonCore plugin;

    public ShopManager(PrisonCore plugin) {
        this.plugin = plugin;
    }

    public List<Shop> getShopList() {
        return this.shopList;
    }

    public Shop getShop(String shopName) {
        return getShopList().stream().filter(shop -> shop.getName().equalsIgnoreCase(shopName))
                .findFirst().orElse(null);
    }

    public void clearShop() {
        shopList.clear();
    }

    public void loadShop() {
        FileConfiguration shops = plugin.getAutosell().getConfig();

        if (!shopList.isEmpty()) {
            clearShop();
        }

        int totalShops = 0;
        for (String key : shops.getConfigurationSection("shops").getKeys(false)) {
            totalShops++;
            shopList.add(new Shop(Integer.parseInt(key),
                    shops.getString("shops." + key + ".name"),
                    shops.getString("shops." + key + ".display"),
                    shops.getStringList("shops." + key + ".items")));
        }
        System.out.print("Successfully loaded " + totalShops + " shops, enjoy!");
    }
}
