package me.justfu498.prisoncore.omnitool.enchantment;

import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.omnitool.enchantment.enchants.Efficiency;
import me.justfu498.prisoncore.omnitool.enchantment.enchants.Explosive;
import me.justfu498.prisoncore.omnitool.enchantment.enchants.Fortune;
import me.justfu498.prisoncore.omnitool.enchantment.enchants.Merchant;
import me.justfu498.prisoncore.omnitool.enchantment.listeners.ExplosiveListener;
import org.bukkit.configuration.file.FileConfiguration;

public class EnchantmentManager {
    private Efficiency efficiency;
    private Fortune fortune;
    private Explosive explosive;
    private Merchant merchant;

    private final PrisonCore plugin;
    private final FileConfiguration config;

    public EnchantmentManager(PrisonCore plugin) {
        this.plugin = plugin;
        config = plugin.getOmnitool().getConfig();
    }

    public String getRawName(int key) {
        return config.getString("enchants." + key + ".rawName");
    }

    public String getDisplayName(int key) {
        return config.getString("enchants." + key + ".displayName");
    }

    public double getCost(int key) {
        return config.getDouble("enchants." + key + ".cost");
    }

    public double getIncreaseCost(int key) {
        return config.getDouble("enchants." + key + ".increase-cost-by");
    }

    public int getMaxLevel(int key) {
        return config.getInt("enchants." + key + ".max-level");
    }

    public boolean getEnabled(int key) {
        return config.getBoolean("enchants." + key + ".enabled");
    }

    public String getEnchantPath(int key) {
        return "enchants." + key;
    }

    public Efficiency getEfficiency() {
        return efficiency;
    }

    public Fortune getFortune() {
        return fortune;
    }

    public Explosive getExplosive() {
        return explosive;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void loadEnchants() {
        efficiency = new Efficiency(getRawName(1),
                getDisplayName(1),
                getCost(1),
                getIncreaseCost(1),
                getMaxLevel(1),
                getEnabled(1));
        System.out.print("Successfully loaded efficiency enchantment!");

        fortune = new Fortune(getRawName(2),
                getDisplayName(2),
                getCost(2),
                getIncreaseCost(2),
                getMaxLevel(2),
                getEnabled(2));
        System.out.print("Successfully loaded fortune enchantment!");

        explosive = new Explosive(getRawName(3),
                getDisplayName(3),
                getCost(3),
                getIncreaseCost(3),
                getMaxLevel(3),
                getEnabled(3),
                config.getDouble(getEnchantPath(3) + ".trigger-percentage.base"),
                config.getDouble(getEnchantPath(3) + ".trigger-percentage.increase-per-level"));
        plugin.getServer().getPluginManager().registerEvents(new ExplosiveListener(plugin), plugin);
        System.out.println("Successfully loaded explosive enchantment!");

        merchant = new Merchant(getRawName(6),
                getDisplayName(6),
                getCost(6),
                getIncreaseCost(6),
                getMaxLevel(6),
                getEnabled(6),
                config.getDouble(getEnchantPath(6) + ".trigger-percentage.base"),
                config.getDouble(getEnchantPath(6) + ".trigger-percentage.increase-per-level"),
                config.getDouble(getEnchantPath(6) + ".increase-sell-multiplier"));
        System.out.print("Successfully loaded merchant enchantment!");
    }

}
