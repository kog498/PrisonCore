package me.justfu498.prisoncore.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.justfu498.prisoncore.PrisonCore;
import me.justfu498.prisoncore.autosell.database.BoosterDatabase;
import me.justfu498.prisoncore.autosell.database.SellMultiplierDatabase;
import me.justfu498.prisoncore.prestige.manager.PrestigeManager;
import me.justfu498.prisoncore.rank.manager.Rank;
import me.justfu498.prisoncore.rank.manager.RankManager;
import me.justfu498.prisoncore.util.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class PrisonCoreExpansion extends PlaceholderExpansion {

    private final PrisonCore plugin;
    private final Utils utils;

    public PrisonCoreExpansion(PrisonCore plugin) {
        this.plugin = plugin;
        utils = plugin.getUtils();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "prisoncore";
    }

    @Override
    public @NotNull String getAuthor() {
        return "justfu498";
    }

    @Override
    public @NotNull String getVersion() {
        return utils.getPluginVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {

        RankManager rankManager = plugin.getRank().getRankManager();
        Rank nextRank = rankManager.getNextRank(player);
        Economy economy = PrisonCore.getEconomy();

        FileConfiguration config = plugin.getPAPIConfig();

        //Rankup progress bar
        String rankupSymbol = config.getString("rank-prestige.rankup-progress-bar.progress-symbol");
        String rankupFillColor = config.getString("rank-prestige.rankup-progress-bar.progress-filled");
        String rankupNeedColor = config.getString("rank-prestige.rankup-progress-bar.progress-needed");
        String rankupFullProgress = config.getString("rank-prestige.rankup-progress-bar.progress-full");
        String lastRank = config.getString("rank-prestige.rankup-progress-bar.last-rank");

        String nextRankAtLastRank = config.getString("rank-prestige.next-rank-at-last-rank");
        String costAtLastRank = config.getString("rank-prestige.cost-at-last-rank");

        //Total progress bar
        String totalSymbol = config.getString("rank-prestige.total-progress-bar.progress-symbol");
        String totalFillColor = config.getString("rank-prestige.total-progress-bar.progress-filled");
        String totalNeedColor = config.getString("rank-prestige.total-progress-bar.progress-needed");
        String totalRankupFullProgress = config.getString("rank-prestige.total-progress-bar.rankup-progress-full");
        String totalPrestigeFullProgress = config.getString("rank-prestige.total-progress-bar.prestige-progress-full");
        String lastPrestige = config.getString("rank-prestige.total-progress-bar.last-prestige");

        String nextPrestigeAtLastPrestige = config.getString("rank-prestige.next-prestige-at-last-prestige");
        String costAtLastPrestige = config.getString("rank-prestige.cost-at-last-prestige");

        PrestigeManager prestigeManager = plugin.getPrestige().getPrestigeManager();

        SellMultiplierDatabase multiplierDatabase = plugin.getAutosell().getSellMultiDatabase();
        BoosterDatabase boosterDatabase = plugin.getAutosell().getBoosterDatabase();

        String playerNotHaveBooster = config.getString("autosell.player-not-have-booster");
        String durationNotHaveBooster = config.getString("autosell.duration-not-have-booster");

        if (player == null) {
            return "";
        }
        switch (params) {
            case "currentrank" -> {
                return rankManager.getCurrentRank(player).getName();
            }
            case "currentrank_display" -> {
                return rankManager.getCurrentRank(player).getDisplay();
            }
            case "nextrank" -> {
                if (nextRank != null) {
                    return rankManager.getNextRank(player).getName();
                }
                return nextRankAtLastRank;
            }
            case "nextrank_display" -> {
                if (nextRank != null) {
                    return rankManager.getNextRank(player).getDisplay();
                }
                return nextRankAtLastRank;
            }
            case "nextrank_cost" -> {
                if (nextRank != null) {
                    return rankManager.getNextRank(player).getCost() + "";
                }
                return costAtLastRank;
            }
            case "nextrank_cost_formatted" -> {
                if (nextRank != null) {
                    return utils.formatShort(rankManager.getNextRank(player).getCost());
                }
                return costAtLastRank;
            }
            case "nextrank_progress" -> {
                if (nextRank == null) {
                    return "100.0";
                }
                double playerBalance = economy.getBalance(player);
                double rankCost = rankManager.getNextRank(player).getCost();
                if (playerBalance < rankCost) {
                    double value = playerBalance / rankCost * 100;
                    double roundedValue = Math.round(value * 100.0) / 100.0;
                    return roundedValue + "";
                }
                return "100.0";
            }
            case "nextrank_progressbar" -> {
                StringBuilder progressBar = new StringBuilder();
                progressBar.append(String.valueOf(rankupSymbol).repeat(10));

                if (nextRank == null)
                    return lastRank;

                double playerBalance = economy.getBalance(player);
                double rankCost = rankManager.getNextRank(player).getCost();

                double percentage = playerBalance / rankCost * 100;
                double roundedPercentage = Math.round(percentage * 100.0) / 100.0;

                if (roundedPercentage < 10.0) {
                    return rankupNeedColor + progressBar;
                } else if (roundedPercentage < 20.0) {
                    return progressBar
                            .insert(0, rankupFillColor) //&a::::::::::
                            .insert(3, rankupNeedColor) //&a:&c:::::::::
                            .toString();
                } else if (roundedPercentage < 30.0) {
                    return progressBar
                            .insert(0, rankupFillColor)
                            .insert(4, rankupNeedColor) //&a::&c::::::::
                            .toString();
                } else if (roundedPercentage < 40.0) {
                    return progressBar
                            .insert(0, rankupFillColor)
                            .insert(5, rankupNeedColor) //&a:::&c:::::::
                            .toString();
                } else if (roundedPercentage < 50.0) {
                    return progressBar
                            .insert(0, rankupFillColor)
                            .insert(6, rankupNeedColor) //&a::::&c::::::
                            .toString();
                } else if (roundedPercentage < 60.0) {
                    return progressBar
                            .insert(0, rankupFillColor)
                            .insert(7, rankupNeedColor) //&a:::::&c:::::
                            .toString();
                } else if (roundedPercentage < 70.0) {
                    return progressBar
                            .insert(0, rankupFillColor)
                            .insert(8, rankupNeedColor) //&a::::::&c::::
                            .toString();
                } else if (roundedPercentage < 80.0) {
                    return progressBar
                            .insert(0, rankupFillColor)
                            .insert(9, rankupNeedColor) //&a:::::::&c:::
                            .toString();
                } else if (roundedPercentage < 90.0) {
                    return progressBar
                            .insert(0, rankupFillColor)
                            .insert(10, rankupNeedColor) //&a::::::::&c::
                            .toString();
                } else if (roundedPercentage < 100.0) {
                    return progressBar
                            .insert(0, rankupFillColor)
                            .insert(11, rankupNeedColor) //&a:::::::::&c:
                            .toString();
                }
                return rankupFullProgress;
            }

            case "currentprestige" -> {
                return prestigeManager.getCurrentPrestige(player) + "";
            }
            case "currentprestige_display" -> {
                return prestigeManager.getPrestigeDisplay(player);
            }
            case "nextprestige" -> {
                int nextPrestige = prestigeManager.getNextPrestige(player);
                if (nextPrestige <= prestigeManager.getMaxPrestige()) {
                    return nextPrestige + "";
                }
                return nextPrestigeAtLastPrestige;
            }
            case "nextprestige_display" -> {
                int nextPrestige = prestigeManager.getNextPrestige(player);
                if (nextPrestige <= prestigeManager.getMaxPrestige()) {
                    return prestigeManager.getNextPrestigeDisplay(player) + "";
                }
                return nextPrestigeAtLastPrestige;
            }
            case "nextprestige_cost" -> {
                int nextPrestige = prestigeManager.getNextPrestige(player);
                if (nextPrestige <= prestigeManager.getMaxPrestige()) {
                    return prestigeManager.getPrestigeCost(player) + "";
                }
                return costAtLastPrestige;
            }
            case "nextprestige_cost_formatted" -> {
                int nextPrestige = prestigeManager.getNextPrestige(player);
                if (nextPrestige <= prestigeManager.getMaxPrestige()) {
                    return utils.formatShort(prestigeManager.getPrestigeCost(player));
                }
                return costAtLastPrestige;
            }

            case "total_progress" -> {
                double playerBalance = economy.getBalance(player);
                if (nextRank == null) {
                    if (prestigeManager.getCurrentPrestige(player) < prestigeManager.getMaxPrestige()) {
                        double prestigeCost = prestigeManager.getPrestigeCost(player);
                        if (playerBalance < prestigeCost) {
                            double value = playerBalance / prestigeCost * 100;
                            double roundedValue = Math.round(value * 100.0) / 100.0;
                            return roundedValue + "";
                        }
                        return "100.0";
                    }
                    return "100.0";
                }

                double rankCost = rankManager.getNextRank(player).getCost();

                if (playerBalance < rankCost) {
                    double value = playerBalance / rankCost * 100;
                    double roundedValue = Math.round(value * 100.0) / 100.0;
                    return roundedValue + "";
                }
                return "100.0";
            }
            case "total_progressbar" -> {
                StringBuilder progressBar = new StringBuilder();
                progressBar.append(String.valueOf(totalSymbol).repeat(10));

                double playerBalance = economy.getBalance(player);

                double cost;
                double percentage;
                double roundedPercentage;

                if (nextRank == null) {
                    cost = prestigeManager.getPrestigeCost(player);
                    percentage = playerBalance / cost * 100;
                    roundedPercentage = Math.round(percentage * 100.0) / 100.0;
                    if (prestigeManager.getCurrentPrestige(player) < prestigeManager.getMaxPrestige()) {
                        if (roundedPercentage < 10.0) {
                            return totalNeedColor + progressBar;
                        } else if (roundedPercentage < 20.0) {
                            return progressBar
                                    .insert(0, totalFillColor) //&a::::::::::
                                    .insert(3, totalNeedColor) //&a:&c:::::::::
                                    .toString();
                        } else if (roundedPercentage < 30.0) {
                            return progressBar
                                    .insert(0, totalFillColor)
                                    .insert(4, totalNeedColor) //&a::&c::::::::
                                    .toString();
                        } else if (roundedPercentage < 40.0) {
                            return progressBar
                                    .insert(0, totalFillColor)
                                    .insert(5, totalNeedColor) //&a:::&c:::::::
                                    .toString();
                        } else if (roundedPercentage < 50.0) {
                            return progressBar
                                    .insert(0, totalFillColor)
                                    .insert(6, totalNeedColor) //&a::::&c::::::
                                    .toString();
                        } else if (roundedPercentage < 60.0) {
                            return progressBar
                                    .insert(0, totalFillColor)
                                    .insert(7, totalNeedColor) //&a:::::&c:::::
                                    .toString();
                        } else if (roundedPercentage < 70.0) {
                            return progressBar
                                    .insert(0, totalFillColor)
                                    .insert(8, totalNeedColor) //&a::::::&c::::
                                    .toString();
                        } else if (roundedPercentage < 80.0) {
                            return progressBar
                                    .insert(0, totalFillColor)
                                    .insert(9, totalNeedColor) //&a:::::::&c:::
                                    .toString();
                        } else if (roundedPercentage < 90.0) {
                            return progressBar
                                    .insert(0, totalFillColor)
                                    .insert(10, totalNeedColor) //&a::::::::&c::
                                    .toString();
                        } else if (roundedPercentage < 100.0) {
                            return progressBar
                                    .insert(0, totalFillColor)
                                    .insert(11, totalNeedColor) //&a:::::::::&c:
                                    .toString();
                        }
                        return totalPrestigeFullProgress;
                    }
                    return lastPrestige;
                }

                cost = rankManager.getNextRank(player).getCost();
                percentage = playerBalance / cost * 100;
                roundedPercentage = Math.round(percentage * 100.0) / 100.0;

                if (roundedPercentage < 10.0) {
                    return totalNeedColor + progressBar;
                } else if (roundedPercentage < 20.0) {
                    return progressBar
                            .insert(0, totalFillColor) //&a::::::::::
                            .insert(3, totalNeedColor) //&a:&c:::::::::
                            .toString();
                } else if (roundedPercentage < 30.0) {
                    return progressBar
                            .insert(0, totalFillColor)
                            .insert(4, totalNeedColor) //&a::&c::::::::
                            .toString();
                } else if (roundedPercentage < 40.0) {
                    return progressBar
                            .insert(0, totalFillColor)
                            .insert(5, totalNeedColor) //&a:::&c:::::::
                            .toString();
                } else if (roundedPercentage < 50.0) {
                    return progressBar
                            .insert(0, totalFillColor)
                            .insert(6, totalNeedColor) //&a::::&c::::::
                            .toString();
                } else if (roundedPercentage < 60.0) {
                    return progressBar
                            .insert(0, totalFillColor)
                            .insert(7, totalNeedColor) //&a:::::&c:::::
                            .toString();
                } else if (roundedPercentage < 70.0) {
                    return progressBar
                            .insert(0, totalFillColor)
                            .insert(8, totalNeedColor) //&a::::::&c::::
                            .toString();
                } else if (roundedPercentage < 80.0) {
                    return progressBar
                            .insert(0, totalFillColor)
                            .insert(9, totalNeedColor) //&a:::::::&c:::
                            .toString();
                } else if (roundedPercentage < 90.0) {
                    return progressBar
                            .insert(0, totalFillColor)
                            .insert(10, totalNeedColor) //&a::::::::&c::
                            .toString();
                } else if (roundedPercentage < 100.0) {
                    return progressBar
                            .insert(0, totalFillColor)
                            .insert(11, totalNeedColor) //&a:::::::::&c:
                            .toString();
                }
                return totalRankupFullProgress;
            }

            //Autosell Placeholders
            case "autosell_multiplier" -> {
                try {
                    return multiplierDatabase.getPlayerMultiplier(player) + "";
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            case "autosell_booster" -> {
                try {
                    if (boosterDatabase.getPlayerBooster(player) != -1) {
                        return boosterDatabase.getPlayerBooster(player) + "";
                    }
                    return playerNotHaveBooster;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            case "autosell_booster_timeleft_1" -> {
                try {
                    if (boosterDatabase.getPlayerBooster(player) != -1) {
                        return utils.formatTimeByDate(boosterDatabase.getPlayerBoosterDuration(player));
                    }
                    return durationNotHaveBooster;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            case "autosell_booster_timeleft_2" -> {
                try {
                    if (boosterDatabase.getPlayerBooster(player) != -1) {
                        return utils.formatTimeByColon(boosterDatabase.getPlayerBoosterDuration(player));
                    }
                    return durationNotHaveBooster;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
}
