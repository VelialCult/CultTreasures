package ru.velialcult.treasures.menu.setup;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.velialcult.library.bukkit.inventory.PlayerInputHandler;
import ru.velialcult.library.bukkit.inventory.menu.ChangeListStringMenu;
import ru.velialcult.library.bukkit.utils.InventoryUtil;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.library.java.text.ReplaceData;
import ru.velialcult.treasures.treasure.Treasure;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.Arrays;

public class TreasureSetupMenu {

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
            TreasureList.generateInventory(player);
            return true;
        });

        SuppliedItem listItems = new SuppliedItem(() ->
                                                          s -> VersionAdapter.getSkullBuilder()
                                                                  .setDisplayName("§6Список предметов")
                                                                  .setLore(VersionAdapter.TextUtil().setReplaces(new ArrayList<>(Arrays.asList(
                                                                          "",
                                                                          "§7В данном меню вы можете посмотреть предметы,",
                                                                          "§7Которые будут выпадать в сокровищнице",
                                                                          "",
                                                                          "§8▹ §eНажмите, чтобы посмотреть"
                                                                  ))))
                                                                  .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZmMDQxOTc2YTA5ZGQwNTNlM2QxZDRlNjExYWFjMDk1OTRkNzRmYzcxYTBlYzRkYTAxMTA0MTZkMzE3ZGJhOCJ9fX0=")
                                                                  .build(), click -> {
           TreasureListItemsMenu.generateInventory(player, treasure);
            return true;
        });

        SuppliedItem lore = new AutoUpdateItem(20, () ->
                s -> VersionAdapter.getSkullBuilder()
                        .setDisplayName("§6Установить текст описания")
                        .setLore(VersionAdapter.TextUtil().setReplaces(new ArrayList<>(Arrays.asList(
                                "",
                                "§7С помощью данной настройки вы можете установить",
                                "§7Текст описания, которое отображается в меню",
                                "",
                                " §8▪ §fТекущий текст: ",
                                "§a{text}",
                                "",
                                "§8▹ §eНажмите, чтобы установить"
                        )), new ReplaceData("{text}", String.join("\n", InventoryUtil.getColoredLore(treasure.getLore(), "  §8◦", "")))))
                        .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdlZTRlNDg5NWE5YzZlMjQzMzZlMjQ2NjVjNTYzODI3MmEzZmVlYWM5NTU4ODJmZjkyYWUzMjE1YWU3ZiJ9fX0=")
                        .build()) {
            @Override
            public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                ChangeListStringMenu.generateInventory(player, treasure, () -> {
                    generateInventory(player, treasure);
                });
            }
        };

        SuppliedItem displayName = new AutoUpdateItem(20, () ->
                s -> VersionAdapter.getSkullBuilder()
                        .setDisplayName("§6Изменить отображаемое название")
                        .setLore(new ArrayList<>(Arrays.asList(
                                "",
                                "§7Данная функция позволит вам изменить",
                                "§7Отображаемое название для данной сокровищницы",
                                "§7В инвентаре и чате",
                                "",
                                "§8▪ §fТекущее название: " + (treasure.getDisplayName().isEmpty() ? "§cНет" : treasure.getDisplayName()),
                                "",
                                "§8▹ §eНажмите, чтобы изменить"
                        )))
                        .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkOGEzYTNiMzZhZGQ1ZDk1NDFhOGVjOTcwOTk2ZmJkY2RlYTk0MTRjZDc1NGM1MGU0OGU1ZDM0ZjFiZjYwYSJ9fX0=")
                        .build()) {
            @Override
            public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                VersionAdapter.MessageUtils().sendMessage(player, "§6➤ §fВведите, пожалуйста, значение нового отображаемого названия");
                player.closeInventory();
                PlayerInputHandler.addPlayer(player, (str) -> {
                    treasure.setDisplayName(str);
                    generateInventory(player, treasure);
                });
            }
        };

        SuppliedItem key = new AutoUpdateItem(20, () ->
                s -> VersionAdapter.getItemBuilder()
                        .setDisplayName("§6Изменить предмет ключа")
                        .setLore(new ArrayList<>(Arrays.asList(
                                "",
                                "§7Данная функция позволит вам изменить",
                                "§7Предмет, которым будет открываться данная",
                                "§7сокровищница",
                                "",
                                "§8▹ §eНажмите, чтобы изменить"
                        )))
                        .setType(XMaterial.TRIPWIRE_HOOK.parseMaterial())
                        .build()) {
            @Override
            public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                TreasureKeyItemSetting.generateInventory(player, treasure, treasure.getKeyItem());
            }
        };

        Gui gui = Gui.normal()
                .setStructure(
                        "b b b b b b b b b",
                        "b q . w . e . r b",
                        "b . . . . . . . b",
                        "b . . . . . . . b",
                        "b b b b b b b b b",
                        ". . . . c . . . . "
                )
                .addIngredient('c', closeButton)
                .addIngredient('q', listItems)
                .addIngredient('w', displayName)
                .addIngredient('e', lore)
                .addIngredient('r', key)
                .addIngredient('b', XMaterial.GRAY_STAINED_GLASS_PANE.parseItem())
                .build();
        Window window = Window.single()
                .setViewer(player)
                .setTitle("§8CultTreasures By VelialCult ")
                .setGui(gui)
                .build();
        window.open();
    }
}
