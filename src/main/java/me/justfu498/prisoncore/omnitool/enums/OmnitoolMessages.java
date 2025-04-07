package me.justfu498.prisoncore.omnitool.enums;

import org.bukkit.configuration.file.FileConfiguration;

public enum OmnitoolMessages {
    USAGE("messages.usage"),
    NO_PERMISSION("messages.noPermission"),
    GIVE("messages.give"),
    TARGET_GIVE("messages.targetGive"),
    TARGET_NOT_FOUND("messages.targetNotFound"),
    NOT_HOLD("messages.notHold"),
    INVALID_NUMBER("messages.invalidNumber"),
    INVALID_ENCHANT("messages.invalidEnchant"),
    ONLY_PLAYER("messages.onlyPlayer"),
    ENCH_MAX_LEVEL("messages.enchMaxLevel"),
    MERCHANT_TRIGGER("messages.merchantTrigger");

    private final String configPath;
    private String value = "Not loaded! Please contact administrator!";

    OmnitoolMessages(String configPath) {
        this.configPath = configPath;
    }

    public static void initialize(FileConfiguration config) {
        for (OmnitoolMessages messages : OmnitoolMessages.values()) {
            messages.value = config.getString(messages.configPath);
        }
    }

    @Override
    public String toString() {
        return this.value;
    }
}
