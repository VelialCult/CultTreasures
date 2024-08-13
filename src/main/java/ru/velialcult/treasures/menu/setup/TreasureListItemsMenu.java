package ru.velialcult.treasures.menu.setup;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.treasures.treasure.Treasure;
import ru.velialcult.treasures.menu.buttons.ScrollDownButton;
import ru.velialcult.treasures.menu.buttons.ScrollUpButton;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TreasureListItemsMenu {

    protected static void generateInventory(Player player, Treasure treasure) {

        SuppliedItem closeButton = new SuppliedItem(() ->
                                                            s -> VersionAdapter.getSkullBuilder()
                                                                    .setDisplayName("§6Закрыть меню")
                                                                    .setLore(new ArrayList<>(Arrays.asList(
                                                                            "§7Нажмите, чтобы закрыть данное",
                                                                            "§7меню."
                                                                    )))
                                                                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M1ZmQ0MTJiY2VjNDhmMjk4MDIyOWY0NGFkNzg1ZTlkMjQ3ZjY2YjZhOTFlZWY3YTk4ZDc2NmJkMTFkNGExOSJ9fX0=")
                                                                    .build(), click -> {
            TreasureSetupMenu.generateInventory(player, treasure);
            return true;
        });

        SuppliedItem addItems = new SuppliedItem(() ->
                                                         s -> VersionAdapter.getSkullBuilder()
                                                                 .setDisplayName("§6Добавить предметы")
                                                                 .setLore(VersionAdapter.TextUtil().setReplaces(new ArrayList<>(Arrays.asList(
                                                                         "",
                                                                         "§7В данном меню вы можете добавить предметы,",
                                                                         "§7Которые будут выпадать в сокровищнице",
                                                                         "",
                                                                         "§8▹ §eНажмите, чтобы добавить"
                                                                 ))))
                                                                 .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjlmMjc3NzNmMDhkY2M2ODU0NzNhMjFmNjQ3NmZlYjZhYWQ4OWFjMjRhMTNmMDRlZjYzNjkyYjlhYzlmZWE5MCJ9fX0=")
                                                                 .build(), click -> {
            TreasureAddDropItemsMenu.generateInventory(player, treasure);
            return true;
        });

        List<Item> items = treasure.getItems().entrySet().stream().map(map -> {
            double chance = map.getValue();
            return new SuppliedItem( () ->
                                             s -> VersionAdapter.getItemBuilder()
                                                     .setItem(map.getKey().clone())
                                                     .setLore(new ArrayList<>(Arrays.asList(
                                                     "",
                                                     " §8▪ §fТекущая вероятность выпадения: §e" + chance,
                                                     "",
                                                     "§8▹ §eНажмите, чтобы изменить вероятность"
                                             ))).build(),
                                     click -> {
                                         ChangeChanceItemMenu.generateInventory(player, treasure, map.getKey());
                                         return true;
                                     });
        }).collect(Collectors.toList());

        Gui gui = ScrollGui.items()
                .setStructure(
                        "f f f f f f f f f",
                        "f x x x x x x x f",
                        "f x x x x x x x f",
                        "f x x x x x x x f",
                        "d f f f f f f f u",
                        ". a . . c . . . . ")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('u', new ScrollUpButton())
                .addIngredient('d', new ScrollDownButton())
                .addIngredient('f', XMaterial.GRAY_STAINED_GLASS_PANE.parseItem())
                .addIngredient('c', closeButton)
                .addIngredient('u', addItems)
                .setContent(items)
                .build();
        Window window = Window.single()
                .setViewer(player)
                .setTitle("§8Список предметов сокровищницы")
                .setGui(gui)
                .build();
        window.open();
    }
}
