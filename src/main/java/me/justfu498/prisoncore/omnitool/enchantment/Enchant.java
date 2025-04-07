package me.justfu498.prisoncore.omnitool.enchantment;

public class Enchant {
    private final String rawName;
    private final String displayName;
    private final double cost;
    private final double IncreaseCost;
    private final int maxLevel;
    private final boolean isEnabled;


    public Enchant(String rawName, String displayName, double cost, double increaseCost, int maxLevel, boolean isEnabled) {
        this.rawName = rawName;
        this.displayName = displayName;
        this.cost = cost;
        IncreaseCost = increaseCost;
        this.maxLevel = maxLevel;
        this.isEnabled = isEnabled;
    }

    public String getRawName() {
        return rawName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getCost() {
        return cost;
    }

    public double getIncreaseCost() {
        return IncreaseCost;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
