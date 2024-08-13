package ru.velialcult.treasures.treasure;

import org.bukkit.inventory.ItemStack;
import ru.velialcult.library.bukkit.inventory.LoreHolder;

import java.util.*;

public class Treasure implements LoreHolder {

    private final UUID uuid;
    private Map<ItemStack, Double> items;
    private List<String> lore;
    private String displayName;
    private KeyItem keyItem;

    public Treasure(UUID uuid, List<String> lore, String displayName, Map<ItemStack, Double> items, KeyItem keyItem) {
        this.uuid = uuid;
        this.lore = lore;
        this.displayName = displayName;
        this.items = items;
        this.keyItem = keyItem;
    }

    public Treasure(UUID uuid) {
        this(uuid, new ArrayList<>(), "", new HashMap<>(), new KeyItem(uuid));
    }

    public KeyItem getKeyItem() {
        return keyItem;
    }

    public void setKeyItem(KeyItem keyItem) {
        this.keyItem = keyItem;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setItems(Map<ItemStack, Double> items) {
        this.items = items;
    }

    public Map<ItemStack, Double> getItems() {
        return this.items;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(List<String> list) {
        this.lore = list;
    }
}
