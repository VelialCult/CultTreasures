package ru.velialcult.treasures.saver;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import ru.velialcult.library.bukkit.utils.ConfigurationUtil;
import ru.velialcult.library.bukkit.utils.location.LocationUtil;
import ru.velialcult.treasures.CultTreasures;

import java.util.List;
import java.util.stream.Collectors;

public class TreasureBlockLocationSaver {

    private final CultTreasures plugin;

    public TreasureBlockLocationSaver(CultTreasures plugin) {
        this.plugin = plugin;
    }

    public void save(List<Location> locations) {
        FileConfiguration config = plugin.getConfig();

        config.set("settings.locations", locations.stream().map(LocationUtil::locationToString).collect(Collectors.toList()));

        ConfigurationUtil.saveFile(config, plugin.getDataFolder().getAbsolutePath(), "config.yml");
    }
}
