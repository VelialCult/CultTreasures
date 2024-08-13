package ru.velialcult.treasures.loader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import ru.velialcult.library.bukkit.file.FileRepository;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.treasures.CultTreasures;
import ru.velialcult.treasures.treasure.KeyItem;
import ru.velialcult.treasures.treasure.Treasure;

import java.util.*;

public class TreasureLoader {

    private final CultTreasures plugin;

    public TreasureLoader(CultTreasures plugin) {
        this.plugin = plugin;
    }

    public List<Treasure> loadTreasure() {
        FileConfiguration config = FileRepository.getByName(plugin, "treasures.yml").getConfiguration();
        List<Treasure> treasures = new ArrayList<>();

        for (String key : config.getConfigurationSection("treasures").getKeys(false)) {
            String displayName = VersionAdapter.TextUtil().colorize(config.getString("treasures." + key + ".displayName", ""));
            List<String> lore = VersionAdapter.TextUtil().colorize(config.getStringList("treasures." + key + ".lore"));

            Map<ItemStack, Double> itemStackDoubleMap = new HashMap<>();
            for (String item : config.getConfigurationSection("treasures." + key + ".items").getKeys(false)) {
                ItemStack itemStack = ItemStack.deserialize(config.getConfigurationSection("treasures." + key + ".items." + item + ".item").getValues(true));
                double chance = config.getDouble("treasures." + key + ".items." + item + ".chance");
                itemStackDoubleMap.put(itemStack, chance);
            }
            KeyItem keyItem;
            if (config.contains("treasures." + key + ".keyItem")) {
                keyItem = new KeyItem(UUID.fromString(key),
                        config.getString("treasures." + key + ".keyItem.displayName"),
                        config.getStringList("treasures." + key + ".keyItem.lore"),
                        config.getString("treasures." + key + ".keyItem.material"),
                        config.getString("treasures." + key + ".keyItem.url"));
            } else {
                keyItem = new KeyItem(UUID.fromString(key));
            }
             Treasure treasure = new Treasure(UUID.fromString(key),lore, displayName, itemStackDoubleMap, keyItem);
            treasures.add(treasure);
        }

        return treasures;
    }
}
