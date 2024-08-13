package ru.velialcult.treasures.manager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.velialcult.library.bukkit.utils.PlayerUtil;
import ru.velialcult.treasures.treasure.Treasure;
import ru.velialcult.treasures.data.PlayerData;

public class KeyManager {

    private final PlayerManager playerManager;

    public KeyManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public int getKeys(Player player, Treasure treasure) {
        ItemStack itemStack = treasure.getKeyItem().createItemStack();
        return PlayerUtil.getItemsAmount(player, itemStack);
    }

    public void giveKey(Player player, Treasure treasure, int amount) {
        ItemStack itemStack = treasure.getKeyItem().createItemStack().clone();
        itemStack.setAmount(amount);
        PlayerUtil.giveItem(player, itemStack);
    }

    public void takeKeys(Player player, Treasure treasure, int amount) {
        ItemStack itemStack = treasure.getKeyItem().createItemStack().clone();
        itemStack.setAmount(amount);
        PlayerUtil.removeItems(player, itemStack);
    }

    public boolean hasKey(Player player, Treasure treasure) {
        return getKeys(player, treasure) > 0;
    }

    public void useKey(Player player, Treasure treasure) {
        int currentAmount = getKeys(player, treasure);

        if (currentAmount > 0) {
            PlayerData playerData = playerManager.getPlayerData(player.getUniqueId());

            playerData.incrementOpenedTreasures(treasure);

            takeKeys(player, treasure, 1);
        }
    }
}
