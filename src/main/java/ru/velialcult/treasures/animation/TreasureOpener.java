package ru.velialcult.treasures.animation;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class TreasureOpener {

    private final Set<Location> openingTreasures;

    public TreasureOpener() {
        openingTreasures = new HashSet<>();
    }

    public boolean isTreasureOpening(Location location) {
        return openingTreasures.contains(location);
    }

    public void startOpeningTreasure(Location location) {
        openingTreasures.add(location);
    }

    public void finishOpeningTreasure(Location location) {
        openingTreasures.remove(location);
    }

    public Set<Location> getOpeningTreasures() {
        return openingTreasures;
    }
}
