package me.justfu498.prisoncore.prestige.enums;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public enum PrestigeMessagesList {
    HELP_ADMIN("messages.help.helpAdmin"),
    HELP_PLAYER("messages.help.helpPlayer"),
    LEADERBOARD_HEADER("messages.leaderboard.header"),
    LEADERBOARD_BODY("messages.leaderboard.body"),
    LEADERBOARD_FOOTER("messages.leaderboard.footer");

    private final String configPath;
    private List<String> value = Arrays.asList("Not Loaded! Please contact administrator!");

    PrestigeMessagesList(String configPath) {
        this.configPath = configPath;
    }

    public static void initialize(FileConfiguration config) {
        for (PrestigeMessagesList prestigeConfigMessagesList : PrestigeMessagesList.values()) {
            prestigeConfigMessagesList.value = config.getStringList(prestigeConfigMessagesList.configPath);
        }
    }

    public List<String> toStringList() {
        return this.value;
    }
}
