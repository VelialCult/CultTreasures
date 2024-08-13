package ru.velialcult.treasures.manager;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.velialcult.library.bukkit.utils.PlayerUtil;
import ru.velialcult.library.bukkit.utils.items.ItemUtil;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.library.java.text.ReplaceData;
import ru.velialcult.treasures.animation.TreasureOpener;
import ru.velialcult.treasures.file.MessagesFile;
import ru.velialcult.treasures.treasure.Treasure;
import ru.velialcult.treasures.animation.TreasureAnimation;
import ru.velialcult.treasures.random.ItemRandomizer;

import java.util.*;

public class TreasureManager {
    private final List<Treasure> treasures;
    private final KeyManager keyManager;
    private final TreasureBlockLocationManager treasureBlockLocationManager;
    private final TreasureOpener treasureOpener;
    private final MessagesFile messagesFile;

    public TreasureManager(KeyManager keyManager,
                           TreasureBlockLocationManager treasureBlockLocationManager,
                           TreasureOpener treasureOpener,
                           MessagesFile messagesFile) {
        this.keyManager = keyManager;
        this.treasureBlockLocationManager = treasureBlockLocationManager;
        this.treasureOpener = treasureOpener;
        this.messagesFile = messagesFile;
        this.treasures = new ArrayList<>();
    }

    public boolean isKey(ItemStack itemStack) {
        for (Treasure treasure : treasures) {
            ItemStack keyItem = treasure.getKeyItem().createItemStack();
            return ItemUtil.areSimilar(keyItem, itemStack);
        }
        return false;
    }

    public Treasure getTreasureByUUID(UUID uuid) {
        return treasures.stream()
                .filter(treasure -> treasure.getUuid().equals(uuid))
                .findAny()
                .orElse(null);
    }

    public void openTreasure(Player player, Treasure treasure, Block block) {
        if (treasureBlockLocationManager.getTreasureBlockLocations().contains(block.getLocation())) {
            if (treasureOpener.isTreasureOpening(block.getLocation())) {
                player.closeInventory();
                VersionAdapter.MessageUtils().sendMessage(player, messagesFile.getFileOperations().getString("messages.treasure.already-opening"));
                return;
            }

            if (keyManager.hasKey(player, treasure)) {
                keyManager.useKey(player, treasure);
                ItemRandomizer itemRandomizer = new ItemRandomizer();
                Map<ItemStack, Double> items = treasure.getItems();
                items.forEach(itemRandomizer::addItem);
                ItemStack reward = itemRandomizer.getRandomItem();
                player.closeInventory();
                treasureOpener.startOpeningTreasure(block.getLocation());
                TreasureAnimation.playOpenAnimation(block.getLocation(), treasure, reward, player);
            } else {
                VersionAdapter.MessageUtils().sendMessage(player, messagesFile.getFileOperations().getString("messages.treasure.dont-have-keys",
                                                                                                             new ReplaceData("{treasure}", treasure.getDisplayName())
                ));
                player.closeInventory();
                Vector direction = player.getLocation().subtract(block.getLocation()).toVector();
                direction.normalize();
                player.setVelocity(direction.multiply(1.5));
            }
        }
    }

    public List<Treasure> getTreasures() {
        return treasures;
    }
}
