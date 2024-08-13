package ru.velialcult.treasures.loader;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import ru.velialcult.library.bukkit.utils.location.LocationUtil;
import ru.velialcult.treasures.CultTreasures;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreasureBlockLocationsLoader {

    private final CultTreasures plugin;

    public TreasureBlockLocationsLoader(CultTreasures plugin) {
        this.plugin = plugin;
    }

    public List<Location> loadLocations() {
        FileConfiguration config = plugin.getConfig();

        return new ArrayList<>(config.getStringList("settings.locations").stream().map(LocationUtil::stringToLocation).collect(Collectors.toList()));
    }
}
