package me.justfu498.prisoncore.rank.manager;

import java.util.List;

public class Rank {
    private final int index;
    private final String name;
    private final String display;
    private final double cost;
    private final List<String> executeCommands;

    public Rank(final int index, final String name, final String display, final double cost, final List<String> executeCommands) {
        this.index = index;
        this.name = name;
        this.display = display;
        this.cost = cost;
        this.executeCommands = executeCommands;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }

    public double getCost() {
        return cost;
    }

    public List<String> getExecuteCommands() {
        return executeCommands;
    }
}
