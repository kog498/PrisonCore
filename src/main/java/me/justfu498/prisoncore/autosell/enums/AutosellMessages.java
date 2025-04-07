package me.justfu498.prisoncore.autosell.enums;

import org.bukkit.configuration.file.FileConfiguration;

public enum AutosellMessages {
    USAGE("messages.usage"),
    NO_PERMISSION("messages.noPermission"),
    NOT_AVAILABLE_SHOP("messages.notAvailableShop"),
    FORMAT_ITEM_IN_LIST("messages.formatItemInList"),
    NOTHING_TO_SELL("messages.nothingToSell"),
    NO_PERM_TO_SELL("messages.noPermToSell"),
    TARGET_NOT_FOUND("messages.targetNotFound"),
    INVALID_NUMBER("messages.invalidNumber"),
    SET_MULTI("messages.setMulti"),
    TARGET_SET_MULTI("messages.targetSetMulti"),
    ADD_MULTI("messages.addMulti"),
    TARGET_ADD_MULTI("messages.targetAddMulti"),
    INVALID_MULTI("messages.invalidMulti"),
    DEL_MULTI("messages.delMulti"),
    TARGET_DEL_MULTI("messages.targetDelMulti"),
    BOOSTER_SELLALL("messages.boosterSellall"),
    SET_BOOSTER("messages.setBooster"),
    TARGET_SET_BOOSTER("messages.targetSetBooster"),
    NOT_HAVE_BOOSTER("messages.notHaveBooster"),
    DEL_BOOSTER("messages.delBooster"),
    TARGET_DEL_BOOSTER("messages.targetDelBooster");

    private final String configPath;
    private String value = "Not loaded! Please contact administrator!";

    AutosellMessages(String configPath) {
        this.configPath = configPath;
    }

    public static void initialize(FileConfiguration config) {
        for (AutosellMessages messages : AutosellMessages.values()) {
            messages.value = config.getString(messages.configPath);
        }
    }

    @Override
    public String toString() {
        return this.value;
    }
}
