package me.justfu498.prisoncore.mine;

import me.justfu498.prisoncore.PrisonCore;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class Mines {
    private final PrisonCore plugin;

    private final File file;
    private final YamlConfiguration config;

    private final Set<Mine> mines = new HashSet<>();

    public Mines(PrisonCore plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "mines.yml");
        config = new YamlConfiguration();
    }

    public void load() {
        try {
            if (!file.exists())
                file.createNewFile();
            config.load(file);

        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        // TODO load mines
        mines.clear();

        if (config.isSet("Mines")) {
            for (Map<?, ?> rawMineMap : config.getMapList("Mines"))
                mines.add(Mine.deserialize((Map<String, Object>) rawMineMap));

            System.out.println("Loaded mines: " + getMinesNames());
        }
    }

    public void save() {
        List<Map<String, Object>> serializedRegions = new ArrayList<>();

        for (Mine mine : mines)
            serializedRegions.add(mine.serialize());

        config.set("Mines", serializedRegions);

        try {
            config.save(file);

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    // TODO getMine(Location location)
    public Mine findMine(Location location) {
        for (Mine mine : mines) {
            if (mine.isWithin(location)) {
                return mine;
            }
        }
        return null;
    }
    public Mine findMine(String name) {
        for (Mine mine : mines)
            if (mine.getName().equalsIgnoreCase(name))
                return mine;

        return null;
    }

    public void saveMine(String name, Location primaryLocation, Location secondaryLocation) {
        mines.add(new Mine(name, primaryLocation, secondaryLocation));

        save();
    }

    public Set<Mine> getMines() {
        return Collections.unmodifiableSet(mines);
    }

    public Set<String> getMinesNames() {
        Set<String> names = new HashSet<>();

        for (Mine mine : mines)
            names.add(mine.getName());

        return Collections.unmodifiableSet(names);
    }
}
