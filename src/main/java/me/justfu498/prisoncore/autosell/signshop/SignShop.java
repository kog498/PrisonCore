package me.justfu498.prisoncore.autosell.signshop;

import java.util.List;

public class SignShop {
    private final int index;
    private final String nameOfShop;
    private final List<Double> location;

    public SignShop(final int index, final String nameOfShop, final List<Double> location) {
        this.index = index;
        this.nameOfShop = nameOfShop;
        this.location = location;
    }

    public int getIndex() {
        return index;
    }

    public String getNameOfShop() {
        return nameOfShop;
    }

    public List<Double> getLocation() {
        return location;
    }
}
