package me.justfu498.prisoncore.autosell.shopmanager;

import java.util.List;

public class Shop {
    private final int index;
    private final String name;
    private final String display;
    private final List<String> items;

    public Shop(final int index, final String name, final String display, final List<String> items) {
        this.index = index;
        this.name = name;
        this.display = display;
        this.items = items;
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

    public List<String> getItems() {
        return items;
    }
}
