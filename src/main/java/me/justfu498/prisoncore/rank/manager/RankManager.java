package me.justfu498.prisoncore.rank.manager;

import me.justfu498.prisoncore.PrisonCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RankManager {
    private final List<Rank> rankList = new ArrayList<>();

    private final PrisonCore plugin;

    public RankManager(PrisonCore plugin) {
        this.plugin = plugin;
    }

    public List<Rank> getRankList() {
        return this.rankList;
    }

    public Rank getRank(int index) {
        return getRankList().stream().filter(rank -> rank.getIndex() == index)
                .findFirst().orElse(null);
    }

    public Rank getRank(String name) {
        return getRankList().stream().filter(rank -> rank.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public Rank getCurrentRank(Player player) {
        int currentRank;
        try {
            currentRank = plugin.getRank().getRankDatabase().getPlayerRank(player);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return getRank(currentRank);
    }

    public Rank getNextRank(Player player) {
        int currentRank;
        try {
            currentRank = plugin.getRank().getRankDatabase().getPlayerRank(player);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return getRank(currentRank + 1);
    }

    public void clearRank() {
        rankList.clear();
    }

    public void loadRank() {
        FileConfiguration ranks = this.plugin.getRank().getConfig();

        if (!rankList.isEmpty()) {
            clearRank();
        }

        int totalRanks = 0;
        for (String key : ranks.getConfigurationSection("ranks").getKeys(false)) {
            totalRanks++;
            rankList.add(new Rank(Integer.parseInt(key),
                    ranks.getString("ranks." + key + ".name"),
                    ranks.getString("ranks." + key + ".display"),
                    ranks.getDouble("ranks." + key + ".cost"),
                    ranks.getStringList("ranks." + key + ".execute-cmds")));
        }

        System.out.print("Successfully loaded " + totalRanks + " ranks, enjoy!");
    }
}
