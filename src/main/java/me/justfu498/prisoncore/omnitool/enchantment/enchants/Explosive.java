package me.justfu498.prisoncore.omnitool.enchantment.enchants;

import de.tr7zw.nbtapi.NBTItem;
import me.justfu498.prisoncore.omnitool.enchantment.Enchant;

public class Explosive extends Enchant {
    private final double baseTriggerPercent;
    private final double increaseTriggerPercent;

    public Explosive(String rawName, String displayName, double cost, double increaseCost, int maxLevel, boolean isEnabled, double baseTriggerPercent, double increaseTriggerPercent) {
        super(rawName, displayName, cost, increaseCost, maxLevel, isEnabled);
        this.baseTriggerPercent = baseTriggerPercent;
        this.increaseTriggerPercent = increaseTriggerPercent;
    }

    public double getBaseTriggerPercent() {
        return baseTriggerPercent;
    }

    public double getIncreaseTriggerPercent() {
        return increaseTriggerPercent;
    }

    public double getCurrentTriggerPercent(int level) {
        return getBaseTriggerPercent() + getIncreaseTriggerPercent() * level;
    }

    public boolean isExplosiveTriggered(NBTItem nbtItem) {
        double min = getBaseTriggerPercent();
        double max = 1;
        double range = max - min + 1;
        double res = Math.random() * range + min;
        return getCurrentTriggerPercent(nbtItem.getInteger("explosive")) >= res;
    }
}
