package me.justfu498.prisoncore.token.enums;

import org.bukkit.configuration.file.FileConfiguration;

public enum TokenMessages {
    USAGE("messages.usage"),
    NO_PERMISSION("messages.noPermission"),
    TARGET_NOT_FOUND("messages.targetNotFound"),
    INVALID_NUMBER("messages.invalidNumber"),
    ADD_TOKEN("messages.addToken"),
    TARGET_ADD_TOKEN("messages.targetAddToken"),
    REMOVE_TOKEN("messages.removeToken"),
    TARGET_REMOVE_TOKEN("messages.targetRemoveToken"),
    BALANCE("messages.balance"),
    OTHER_BALANCE("messages.otherBalance"),
    SET_TOKEN("messages.setToken"),
    TARGET_SET_TOKEN("messages.targetSetToken"),
    NOT_ENOUGH_TOKEN("messages.notEnoughToken"),
    WITHDRAW_TOKEN("messages.withdrawToken"),
    ONLY_PLAYER("messages.onlyPlayer"),
    REDEEM_TOKEN("messages.redeemToken");

    private final String configPath;
    private String value = "Not loaded! Please contact administrator!";

    TokenMessages(String configPath) {
        this.configPath = configPath;
    }

    public static void initialize(FileConfiguration config) {
        for (TokenMessages configMessages : TokenMessages.values()) {
            configMessages.value = config.getString(configMessages.configPath);
        }
    }

    @Override
    public String toString() {
        return this.value;
    }
}
