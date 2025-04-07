package me.justfu498.prisoncore.autosell.enums;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public enum AutosellMessagesList {
    SELLALL("messages.sellall"),
    HELP_ADMIN("messages.help.helpAdmin"),
    HELP_PLAYER("messages.help.helpPlayer");

    private final String configPath;
    private List<String> value = Arrays.asList("Not Loaded! Please contact administrator!");

    AutosellMessagesList(String configPath) {
        this.configPath = configPath;
    }

    public static void initialize(FileConfiguration config) {
        for (AutosellMessagesList listMessages : AutosellMessagesList.values()) {
            listMessages.value = config.getStringList(listMessages.configPath);
        }
    }

    public List<String> toStringList() {
        return this.value;
    }
}
