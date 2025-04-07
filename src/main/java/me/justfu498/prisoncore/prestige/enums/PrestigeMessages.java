package me.justfu498.prisoncore.prestige.enums;

import org.bukkit.configuration.file.FileConfiguration;

public enum PrestigeMessages {
    USAGE("messages.usage"),
    NO_PERMISSION("messages.noPermission"),
    PRESTIGE("messages.prestige"),
    NOT_IN_THE_LAST_RANK("messages.notInTheLastRank"),
    LAST_PRESTIGE("messages.lastPrestige"),
    NOT_ENOUGH_MONEY("messages.notEnoughMoney"),
    TARGET_NOT_FOUND("messages.targetNotFound"),
    INVALID_INPUT("messages.invalidInput"),
    SET_PRESTIGE("messages.setPrestige"),
    INVALID_PRESTIGE("messages.invalidPrestige"),
    TARGET_SET_PRESTIGE("messages.targetSetPrestige"),
    ADD_PRESTIGE("messages.addPrestige"),
    TARGET_ADD_PRESTIGE("messages.targetAddPrestige"),
    REMOVE_PRESTIGE("messages.removePrestige"),
    TARGET_REMOVE_PRESTIGE("messages.targetRemovePrestige");

    private final String configPath;
    private String value = "Not loaded! Please contact administrator!";

    PrestigeMessages(String configPath) {
        this.configPath = configPath;
    }

    public static void initialize(FileConfiguration config) {
        for (PrestigeMessages configMessages : PrestigeMessages.values()) {
            configMessages.value = config.getString(configMessages.configPath);
        }
    }

    @Override
    public String toString() {
        return this.value;
    }
}
