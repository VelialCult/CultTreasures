package ru.velialcult.treasures.random;

import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemRandomizer {
    private final Map<ItemStack, Double> map = new HashMap<>();
    private final Random random;

    public ItemRandomizer() {
        this.random = new Random();
    }

    public void addItem(ItemStack item, double chance) {
        if (chance <= 0) return;
        map.put(item, chance);
    }

    public void addItems(Map<ItemStack, Double> items) {
        for (Map.Entry<ItemStack, Double> entry : items.entrySet()) {
            addItem(entry.getKey(), entry.getValue());
        }
    }

    public ItemStack getRandomItem() {
        double totalWeight = 0.0;
        for (double weight : map.values()) {
            totalWeight += weight;
        }

        double value = random.nextDouble() * totalWeight;
        for (Map.Entry<ItemStack, Double> entry : map.entrySet()) {
            value -= entry.getValue();
            if (value <= 0.0) {
                return entry.getKey();
            }
        }

        return null;
    }
}
