package me.justfu498.prisoncore.omnitool.enchantment.enchants;

import me.justfu498.prisoncore.omnitool.enchantment.Enchant;

public class Merchant extends Enchant {
    private final double baseTriggerPercent;
    private final double increaseTriggerPercent;
    private final double increaseSellMulti;

    public Merchant(String rawName, String displayName, double cost, double increaseCost, int maxLevel, boolean isEnabled, double baseTriggerPercent, double increaseTriggerPercent, double increaseSellMulti) {
        super(rawName, displayName, cost, increaseCost, maxLevel, isEnabled);
        this.baseTriggerPercent = baseTriggerPercent;
        this.increaseTriggerPercent = increaseTriggerPercent;
        this.increaseSellMulti = increaseSellMulti;
    }

    public double getBaseTriggerPercent() {
        return baseTriggerPercent;
    }

    public double getIncreaseTriggerPercent() {
        return increaseTriggerPercent;
    }

    public double getCurrentTriggerPercent(int merchantLevel) {
        return getBaseTriggerPercent() + getIncreaseTriggerPercent() * merchantLevel;
    }

    public double getIncreaseSellMulti() {
        return increaseSellMulti;
    }
}
