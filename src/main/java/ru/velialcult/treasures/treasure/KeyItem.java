package ru.velialcult.treasures.treasure;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import ru.velialcult.library.bukkit.inventory.LoreHolder;
import ru.velialcult.library.core.VersionAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KeyItem implements LoreHolder {

    private final UUID treacureUUID;
    private String displayName;
    private List<String> lore;
    private String material;
    private String url;

    public KeyItem(UUID treacureUUID, String displayName, List<String> lore, String material, String url) {
        this.treacureUUID = treacureUUID;
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.url = url;
    }

    public KeyItem(UUID treacureUUID) {
        this(treacureUUID, "", new ArrayList<>(), "GRASS", "");
    }

    public ItemStack createItemStack() {
        ItemStack itemStack;
        if (material.equalsIgnoreCase("head")) {
            itemStack =  VersionAdapter.getSkullBuilder()
                    .setLore(lore)
                    .setTexture(url)
                    .setDisplayName(displayName)
                    .build();
        } else {
            itemStack =  VersionAdapter.getItemBuilder()
                    .setLore(lore)
                    .setDisplayName(displayName)
                    .setType(XMaterial.matchXMaterial(material).stream()
                                     .findFirst()
                                     .orElse(XMaterial.GRASS).parseMaterial())
                    .build();
        }

        return itemStack;
    }

    public UUID getTreacureUUID() {
        return treacureUUID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
