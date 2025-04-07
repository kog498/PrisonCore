package me.justfu498.prisoncore.rank.enums;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public enum RankMessagesList {
    HELP_ADMIN("messages.help.helpAdmin"),
    HELP_PLAYER("messages.help.helpPlayer"),
    RANK_LIST("messages.rankList");

    private final String configPath;
    private List<String> value = Arrays.asList("Not Loaded! Please contact administrator!");

    RankMessagesList(String configPath) {
        this.configPath = configPath;
    }

    public static void initialize(FileConfiguration config) {
        for (RankMessagesList rankConfigMessagesList : RankMessagesList.values()) {
            rankConfigMessagesList.value = config.getStringList(rankConfigMessagesList.configPath);
        }
    }

    public List<String> toStringList() {
        return this.value;
    }
}
