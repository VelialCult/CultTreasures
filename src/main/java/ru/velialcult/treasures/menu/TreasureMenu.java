package ru.velialcult.treasures.menu;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.velialcult.library.bukkit.utils.InventoryUtil;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.library.java.text.ReplaceData;
import ru.velialcult.treasures.CultTreasures;
import ru.velialcult.treasures.file.InventoriesFile;
import ru.velialcult.treasures.manager.KeyManager;
import ru.velialcult.treasures.manager.TreasureManager;
import ru.velialcult.treasures.menu.buttons.ScrollDownButton;
import ru.velialcult.treasures.menu.buttons.ScrollUpButton;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreasureMenu {

    public static void generateInventory(Player player, Block block) {

        InventoriesFile inventoriesFile = CultTreasures.getInstance().getInventoriesFile();
        TreasureManager treasureManager = CultTreasures.getInstance().getTreasureManager();
        KeyManager keyManager = CultTreasures.getInstance().getKeyManager();

        char itemsChar = inventoriesFile.getFileOperations().getString("inventories.main-menu.items.default.char").charAt(0);

        String[] structure =  inventoriesFile.getFileOperations().getList("inventories.main-menu.structure").toArray(new String[0]);

        List<Item> items = treasureManager.getTreasures().stream()
                .map(treasure -> new AutoUpdateItem(20, () ->
                        s -> VersionAdapter.getItemBuilder()
                                .setItem(treasure.getKeyItem().createItemStack())
                                .setDisplayName(inventoriesFile.getFileOperations().getString("inventories.main-menu.items.default.displayName",
                                                                                      new ReplaceData("{treasure}", treasure.getDisplayName())
                            ))
                                .setLore(inventoriesFile.getFileOperations().getList("inventories.main-menu.items.default.lore",
                                                                             new ReplaceData("{keys}", keyManager.getKeys(player, treasure)),
                                                                             new ReplaceData("{lore}", String.join("\n", treasure.getLore()))))
                        .build()) {
                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        treasureManager.openTreasure(player, treasure, block);
                    }

                }).collect(Collectors.toList());

        Gui.Builder<ScrollGui<Item>, ScrollGui.Builder<Item>> builder = ScrollGui.items()
                .setStructure(structure)
                .addIngredient(itemsChar, Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('u', new ScrollUpButton())
                .addIngredient('d', new ScrollDownButton())
                .setContent(items);

        Map<Character, SuppliedItem> customItemList = InventoryUtil.createItems(inventoriesFile.getConfig(),
                                                                     "inventories.main-menu.items",
                                                                     (event, path) -> {
            List<String> commands = inventoriesFile.getConfig().getStringList(path + ".actionOnClick");
            for (String command : commands) {
                if (command.startsWith("[message]")) {
                    String message = command.replace("[message]", "");
                    VersionAdapter.MessageUtils().sendMessage(player, message);
                }

                if (command.startsWith("[execute]")) {
                    String executeCommand = command.replace("[execute]", "");
                    Bukkit.dispatchCommand(player, executeCommand);
                }
                if (command.equals("[close]")) {
                    player.closeInventory();
                }
            }
        });

        InventoryUtil.setItems(builder, customItemList);

        Window window = Window.single()
                .setViewer(player)
                .setTitle(inventoriesFile.getFileOperations().getString("inventories.main-menu.title"))
                .setGui(builder.build())
                .build();
        window.open();
    }
}
