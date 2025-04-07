package me.justfu498.prisoncore.rank.enums;

import org.bukkit.configuration.file.FileConfiguration;

public enum RankMessages {
    USAGE("messages.usage"),
    NO_PERMISSION("messages.noPermission"),
    RANKUP("messages.rankup"),
    FORCE_RANKUP("messages.forceRankup"),
    TARGET_FORCE_RANKUP("messages.targetForceRankup"),
    NOT_ENOUGH_MONEY("messages.notEnoughMoney"),
    LAST_RANK("messages.lastRank"),
    LAST_RANK_IN_FORCE("messages.lastRankInForce"),
    TARGET_NOT_FOUND("messages.targetNotFound"),
    RANK_NOT_AVAILABLE("messages.rankNotAvailable"),
    SET_RANK("messages.setRank"),
    TARGET_SET_RANK("messages.targetSetRank"),
    INVALID_NUMBER("messages.invalidNumber"),
    FORMAT_RANK_LIST("messages.formatRankInList"),
    NO_MORE_RANKS("messages.noMoreRanks");

    private final String configPath;
    private String value = "Not loaded! Please contact administrator!";

    RankMessages(String configPath) {
        this.configPath = configPath;
    }

    public static void initialize(FileConfiguration config) {
        for (RankMessages configMessages : RankMessages.values()) {
            configMessages.value = config.getString(configMessages.configPath);
        }
    }

    @Override
    public String toString() {
        return this.value;
    }
}
