package ru.velialcult.treasures.treasure;

import ru.velialcult.library.bukkit.inventory.LoreHolder;

import java.util.ArrayList;
import java.util.List;

public class TreasureKeyLore implements LoreHolder {

    private List<String> lore;

    public TreasureKeyLore(List<String> lore) {
        this.lore = lore;
    }

    public TreasureKeyLore() {
        this(new ArrayList<>());
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
