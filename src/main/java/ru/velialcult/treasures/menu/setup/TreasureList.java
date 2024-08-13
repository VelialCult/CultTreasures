package ru.velialcult.treasures.menu.setup;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.treasures.CultTreasures;
import ru.velialcult.treasures.treasure.Treasure;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TreasureList {

    public static void generateInventory(Player player) {

        TreasureManager treasureManager = CultTreasures.getInstance().getTreasureManager();

        SuppliedItem closeButton = new SuppliedItem(() ->
                                                            s -> VersionAdapter.getSkullBuilder()
                                                                    .setDisplayName("§6Закрыть меню")
                                                                    .setLore(new ArrayList<>(Arrays.asList(
                                                                            "§7Нажмите, чтобы закрыть данное",
                                                                            "§7меню."
                                                                    )))
                                                                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M1ZmQ0MTJiY2VjNDhmMjk4MDIyOWY0NGFkNzg1ZTlkMjQ3ZjY2YjZhOTFlZWY3YTk4ZDc2NmJkMTFkNGExOSJ9fX0=")
                                                                    .build(), click -> {
            player.closeInventory();
            return true;
        });

        SuppliedItem addTreasure = new SuppliedItem(() ->
                                                            s -> VersionAdapter.getSkullBuilder()
                                                                    .setDisplayName("§6Добавить сокровищницу")
                                                                    .setLore(new ArrayList<>(Arrays.asList(
                                                                            "",
                                                                            "§7Используйте данную кнопку, чтобы перейти",
                                                                            "§7К созданию новой сокровищницы.",
                                                                            "",
                                                                            "§8▹ §eНажмите, чтобы перейти"
                                                                    )))
                                                                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjlmMjc3NzNmMDhkY2M2ODU0NzNhMjFmNjQ3NmZlYjZhYWQ4OWFjMjRhMTNmMDRlZjYzNjkyYjlhYzlmZWE5MCJ9fX0=")
                                                                    .build(), click -> {
            Treasure treasure = new Treasure(UUID.randomUUID());
            treasureManager.getTreasures().add(treasure);
            TreasureSetupMenu.generateInventory(player, treasure);
            return true;
        });

        List<Item> items = treasureManager.getTreasures().stream()
                .map(treasure -> new AutoUpdateItem(20, () ->
                        s -> VersionAdapter.getItemBuilder()
                                .setType(XMaterial.CHEST.parseMaterial())
                                .setDisplayName("§fСокровищница §e" + treasure.getUuid())
                                .setLore(new ArrayList<>(List.of(
                                        "",
                                        " §8▹ §eНажмите, чтобы перейти к настройке сокровищницы"
                                )))
                                .build()) {
                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        TreasureSetupMenu.generateInventory(player, treasure);
                    }
                }).collect(Collectors.toList());

        Gui gui = ScrollGui.items()
                .setStructure(
                        "f f f f f f f f f",
                        "f x x x x x x x f",
                        "f x x x x x x x f",
                        "f x x x x x x x f",
                        "d f f f f f f f u",
                        ". . . . c . a . . ")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('u', new ScrollUpButton())
                .addIngredient('d', new ScrollDownButton())
                .addIngredient('f', XMaterial.GRAY_STAINED_GLASS_PANE.parseItem())
                .addIngredient('c', closeButton)
                .addIngredient('a', addTreasure)
                .setContent(items)
                .build();
        Window window = Window.single()
                .setViewer(player)
                .setTitle("§8Список сокровищниц")
                .setGui(gui)
                .build();
        window.open();
    }
}
