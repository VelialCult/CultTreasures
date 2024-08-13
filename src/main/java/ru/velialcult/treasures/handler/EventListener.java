package ru.velialcult.treasures.handler;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.velialcult.treasures.manager.TreasureBlockLocationManager;
import ru.velialcult.treasures.manager.TreasureManager;
import ru.velialcult.treasures.menu.TreasureMenu;

public class EventListener implements Listener {

    private final TreasureBlockLocationManager treasureBlockLocationManager;
    private final TreasureManager treasureManager;

    public EventListener(TreasureBlockLocationManager treasureBlockLocationManager,
                         TreasureManager treasureManager) {
        this.treasureBlockLocationManager = treasureBlockLocationManager;
        this.treasureManager = treasureManager;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block != null) {
                if (treasureBlockLocationManager.getTreasureBlockLocations().contains(block.getLocation())) {
                    TreasureMenu.generateInventory(player, block);
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        if (treasureManager.isKey(itemStack)) event.setCancelled(true);
    }
}
