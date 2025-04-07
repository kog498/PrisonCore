package me.justfu498.prisoncore.autosell.signshop;

import me.justfu498.prisoncore.PrisonCore;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SignShopManager {
    private final List<SignShop> signShopList = new ArrayList<>();

    private final PrisonCore plugin;

    public SignShopManager(PrisonCore plugin) {
        this.plugin = plugin;
    }

    public List<SignShop> getSignShopList() {
        return signShopList;
    }

    public void clearSign() {
        signShopList.clear();
    }

    public void loadSignShop() {
        FileConfiguration signs = plugin.getAutosell().getSignConfig();

        if (!signShopList.isEmpty()) {
            clearSign();
        }

        for (String key : signs.getConfigurationSection("signs").getKeys(false)) {
            signShopList.add(new SignShop(Integer.parseInt(key),
                    signs.getString("signs." + key + ".shop"),
                    signs.getDoubleList("signs" + key + ".location")));
        }
    }
}
