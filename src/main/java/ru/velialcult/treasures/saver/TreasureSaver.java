package ru.velialcult.treasures.saver;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import ru.velialcult.library.bukkit.file.FileRepository;
import ru.velialcult.library.bukkit.utils.ConfigurationUtil;
import ru.velialcult.treasures.CultTreasures;
import ru.velialcult.treasures.treasure.Treasure;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TreasureSaver {

    private final CultTreasures plugin;

    public TreasureSaver(CultTreasures plugin) {
        this.plugin = plugin;
    }

    public void save(List<Treasure> treasureList) {
        FileConfiguration config = FileRepository.getByName(plugin, "treasures.yml").getConfiguration();
        for (Treasure treasure : treasureList) {
            config.set("treasures." + treasure.getUuid() + ".items", null);
            Map<ItemStack, Double> itemStackDoubleMap = treasure.getItems();
            for (Map.Entry<ItemStack, Double> entry : itemStackDoubleMap.entrySet()) {
                UUID uniqueKey = UUID.randomUUID();
                config.set("treasures." + treasure.getUuid() + ".displayName", treasure.getDisplayName());
                config.set("treasures." + treasure.getUuid() + ".lore", treasure.getLore());
                config.set("treasures." + treasure.getUuid() + ".items." + uniqueKey + ".item", entry.getKey().serialize());
                config.set("treasures." + treasure.getUuid() + ".items." + uniqueKey + ".chance", entry.getValue());
                config.set("treasures." + treasure.getUuid() + ".keyItem.displayName", treasure.getKeyItem().getDisplayName());
                config.set("treasures." + treasure.getUuid() + ".keyItem.lore", treasure.getKeyItem().getLore());
                config.set("treasures." + treasure.getUuid() + ".keyItem.material", treasure.getKeyItem().getMaterial());
                config.set("treasures." + treasure.getUuid() + ".keyItem.url", treasure.getKeyItem().getUrl());
            }
        }

        ConfigurationUtil.saveFile(config, plugin.getDataFolder().getAbsolutePath(), "treasures.yml");
    }
}
