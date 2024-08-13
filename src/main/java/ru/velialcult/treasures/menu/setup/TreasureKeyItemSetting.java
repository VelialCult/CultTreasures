package ru.velialcult.treasures.menu.setup;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.velialcult.library.bukkit.inventory.PlayerInputHandler;
import ru.velialcult.library.bukkit.inventory.menu.ChangeListStringMenu;
import ru.velialcult.library.bukkit.utils.InventoryUtil;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.library.java.text.ReplaceData;
import ru.velialcult.treasures.treasure.KeyItem;
import ru.velialcult.treasures.treasure.Treasure;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.Arrays;

public class TreasureKeyItemSetting {

    protected static void generateInventory(Player player, Treasure treasure, KeyItem keyItem) {

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

        SuppliedItem head = new AutoUpdateItem(20, () ->
                s -> VersionAdapter.getSkullBuilder()
                        .setDisplayName("§6Установить текстуру головы")
                        .setLore(VersionAdapter.TextUtil().setReplaces(new ArrayList<>(Arrays.asList(
                                "",
                                "§7С помощью данной настройки вы можете установить",
                                "§7Тип предмета, который будет выдаваться игрокам",
                                "",
                                " §8▪ §fТекущий url: §e" + (keyItem.getUrl().isEmpty() ? "§cНет" : keyItem.getUrl()),
                                "",
                                "§8▹ §eНажмите, чтобы установить"
                        ))))
                        .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdlZTRlNDg5NWE5YzZlMjQzMzZlMjQ2NjVjNTYzODI3MmEzZmVlYWM5NTU4ODJmZjkyYWUzMjE1YWU3ZiJ9fX0=")
                        .build()) {
            @Override
            public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                VersionAdapter.MessageUtils().sendMessage(player, "§6➤ §fВведите, пожалуйста, напишите в чат URL головы, которую хотите установить");
                player.closeInventory();
                PlayerInputHandler.addPlayer(player, (str) -> {
                    keyItem.setUrl(str);
                    generateInventory(player, treasure, keyItem);
                });
            }
        };


        SuppliedItem material = new AutoUpdateItem(20, () ->
                s -> VersionAdapter.getSkullBuilder()
                        .setDisplayName("§6Установить предмет")
                        .setLore(VersionAdapter.TextUtil().setReplaces(new ArrayList<>(Arrays.asList(
                                "",
                                "§7С помощью данной настройки вы можете установить",
                                "§7Тип предмета, который будет выдаваться игрокам",
                                "",
                                "§cВнимание! Чтобы установить голову, используйте",
                                "§cСлово head",
                                "",
                                " §8▪ §fТекущий предмет: " + keyItem.getMaterial(),
                                "",
                                "§8▹ §eНажмите, чтобы установить"
                        ))))
                        .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdlZTRlNDg5NWE5YzZlMjQzMzZlMjQ2NjVjNTYzODI3MmEzZmVlYWM5NTU4ODJmZjkyYWUzMjE1YWU3ZiJ9fX0=")
                        .build()) {
            @Override
            public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                VersionAdapter.MessageUtils().sendMessage(player, "§6➤ §fВведите, пожалуйста, возьмите предмет в руку и напишите §6§lустановить");
                player.closeInventory();
                PlayerInputHandler.addPlayer(player, (str) -> {
                    if (str.equalsIgnoreCase("установить") || str.equalsIgnoreCase("head")) {
                        if (str.equalsIgnoreCase("head")) {
                            keyItem.setMaterial("head");
                        } else {
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            if (itemStack.getType() != Material.AIR) {
                                keyItem.setMaterial(itemStack.getType().toString());
                            }
                        }
                        generateInventory(player, treasure, keyItem);
                    }
                });
            }
        };

        SuppliedItem lore = new AutoUpdateItem(20, () ->
                s -> VersionAdapter.getSkullBuilder()
                        .setDisplayName("§6Установить текст описания")
                        .setLore(VersionAdapter.TextUtil().setReplaces(new ArrayList<>(Arrays.asList(
                                "",
                                "§7С помощью данной настройки вы можете установить",
                                "§7Текст описания, которое отображается у предмета",
                                "",
                                " §8▪ §fТекущий текст: ",
                                "§a{text}",
                                "",
                                "§8▹ §eНажмите, чтобы установить"
                        )), new ReplaceData("{text}", String.join("\n", InventoryUtil.getColoredLore(keyItem.getLore(), "  §8◦", "")))))
                        .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdlZTRlNDg5NWE5YzZlMjQzMzZlMjQ2NjVjNTYzODI3MmEzZmVlYWM5NTU4ODJmZjkyYWUzMjE1YWU3ZiJ9fX0=")
                        .build()) {
            @Override
            public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                ChangeListStringMenu.generateInventory(player, keyItem, () -> {
                    generateInventory(player, treasure, keyItem);
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
                                "§8▪ §fТекущее название: " + (keyItem.getDisplayName().isEmpty() ? "§cНет" : keyItem.getDisplayName()),
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
                    keyItem.setDisplayName(str);
                    generateInventory(player, treasure, keyItem);
                });
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
                .addIngredient('q', material)
                .addIngredient('w', displayName)
                .addIngredient('e', lore)
                .addIngredient('r', head)
                .addIngredient('b', XMaterial.GRAY_STAINED_GLASS_PANE.parseItem())
                .build();
        Window window = Window.single()
                .setViewer(player)
                .setTitle("§8Настройка предмета ключа")
                .setGui(gui)
                .build();
        window.open();
    }
}
