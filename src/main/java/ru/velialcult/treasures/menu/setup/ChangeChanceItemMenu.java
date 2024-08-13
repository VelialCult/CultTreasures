package ru.velialcult.treasures.menu.setup;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.treasures.treasure.Treasure;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class ChangeChanceItemMenu {

    public static void generateInventory(Player player, Treasure treasure, ItemStack itemStack) {
        AtomicReference<Double> percent = new AtomicReference<>(treasure.getItems().getOrDefault(itemStack, 0.0D));

        SuppliedItem info = new AutoUpdateItem(20, () ->
                s -> VersionAdapter.getItemBuilder()
                        .setItem(itemStack.clone())
                        .setDisplayName("§6Информация")
                        .setLore(new ArrayList<>(Arrays.asList(
                                "",
                                "§7Изменяйте вероятность появления",
                                "§7Блока, чтобы уменьшить их количество",
                                "§7В шахте.",
                                "",
                                " §8▪ §fТекущая вероятность: §e" + percent.get(),
                                "",
                                "§8▹ §eНажмите, чтобы сохарнить"
                        )))
                        .build()) {
            @Override
            public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                treasure.getItems().put(itemStack, percent.get());
                TreasureListItemsMenu.generateInventory(player, treasure);
            }
        };

        SuppliedItem delete = new SuppliedItem(() ->
                                                       s -> VersionAdapter.getSkullBuilder()
                                                               .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDczZmQ4YTA2ZTZlYTgyMDc5NGNkYTIxNGZjNDZiNGMzMjlmYTlhZjMyNGU0NGVhYjQ0OTZjMmQ5ZjViYTZmZCJ9fX0=")
                                                               .setDisplayName("§6Удалить")
                                                               .setLore(new ArrayList<>(Arrays.asList(
                                                                       "",
                                                                       "§7Нажмите, чтобы добавить удалить",
                                                                       "§7данный блок из списка",
                                                                       "",
                                                                       "§8▹ §eНажмите, чтобы удалить"
                                                               )))
                                                               .build(), click -> {
            treasure.getItems().remove(itemStack);
            TreasureListItemsMenu.generateInventory(player, treasure);
            return true;
        });

        SuppliedItem add1 = new SuppliedItem(() ->
                                                     s -> VersionAdapter.getItemBuilder()
                                                             .setType(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial())
                                                             .setDisplayName("§aДобавить 1%")
                                                             .setLore(new ArrayList<>(Arrays.asList(
                                                                     "",
                                                                     "§7Нажмите, чтобы добавить §f1% §7к",
                                                                     "§7вероятности выпадения",
                                                                     "",
                                                                     "§8▹ §eНажмите, чтобы добавить"
                                                             )))
                                                             .build(), click -> {
            percent.updateAndGet(v -> v + 1.0);
            return true;
        });
        SuppliedItem add10 = new SuppliedItem(() ->
                                                      s -> VersionAdapter.getItemBuilder()
                                                              .setType(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial())
                                                              .setDisplayName("§aДобавить 10%")
                                                              .setLore(new ArrayList<>(Arrays.asList(
                                                                      "",
                                                                      "§7Нажмите, чтобы добавить §f10% §7к",
                                                                      "§7вероятности выпадения",
                                                                      "",
                                                                      "§8▹ §eНажмите, чтобы добавить"
                                                              )))
                                                              .build(), click -> {
            percent.updateAndGet(v -> v + 10.0);
            return true;
        });
        SuppliedItem add50 = new SuppliedItem(() ->
                                                      s -> VersionAdapter.getItemBuilder()
                                                              .setType(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial())
                                                              .setDisplayName("§aДобавить 50%")
                                                              .setLore(new ArrayList<>(Arrays.asList(
                                                                      "",
                                                                      "§7Нажмите, чтобы добавить §f50% §7к",
                                                                      "§7вероятности выпадения",
                                                                      "",
                                                                      "§8▹ §eНажмите, чтобы добавить"
                                                              )))
                                                              .build(), click -> {
            percent.updateAndGet(v -> v + 50.0);
            return true;
        });
        SuppliedItem remove1 = new SuppliedItem(() ->
                                                        s -> VersionAdapter.getItemBuilder()
                                                                .setType(XMaterial.RED_STAINED_GLASS_PANE.parseMaterial())
                                                                .setDisplayName("§cУбавить на 1%")
                                                                .setLore(new ArrayList<>(Arrays.asList(
                                                                        "",
                                                                        "§7Нажмите, чтобы убавить на §f1%",
                                                                        "§7вероятности выпадения",
                                                                        "",
                                                                        "§8▹ §eНажмите, чтобы убавить"
                                                                )))
                                                                .build(), click -> {
            percent.updateAndGet(v -> {
                v = v - 1.0;
                if (v < 0) v = 0.1;
                return v;
            });
            return true;
        });
        SuppliedItem remove10 = new SuppliedItem(() ->
                                                         s -> VersionAdapter.getItemBuilder()
                                                                 .setType(XMaterial.RED_STAINED_GLASS_PANE.parseMaterial())
                                                                 .setDisplayName("§cУбавить на 10%")
                                                                 .setLore(new ArrayList<>(Arrays.asList(
                                                                         "",
                                                                         "§7Нажмите, чтобы убавить на §f10%",
                                                                         "§7вероятности выпадения",
                                                                         "",
                                                                         "§8▹ §eНажмите, чтобы убавить"
                                                                 )))
                                                                 .build(), click -> {
            percent.updateAndGet(v -> {
                v = v - 10.0;
                if (v < 0) v = 0.1;
                return v;
            });
            return true;
        });
        SuppliedItem remove50 = new SuppliedItem(() ->
                                                         s -> VersionAdapter.getItemBuilder()
                                                                 .setType(XMaterial.RED_STAINED_GLASS_PANE.parseMaterial())
                                                                 .setDisplayName("§cУбавить на 50%")
                                                                 .setLore(new ArrayList<>(Arrays.asList(
                                                                         "",
                                                                         "§7Нажмите, чтобы убавить на §f50%",
                                                                         "§7вероятности выпадения",
                                                                         "",
                                                                         "§8▹ §eНажмите, чтобы убавить"
                                                                 )))
                                                                 .build(), click -> {
            percent.updateAndGet(v -> {
                v = v - 50.0;
                if (v < 0) v = 0.1;
                return v;
            });
            return true;
        });
        Gui gui = Gui.normal()
                .setStructure(
                        ". . . . . . . . .",
                        ". q w e i r t y .",
                        ". . . . . . . . .",
                        ". . . . o . . . ."
                )
                .addIngredient('q', add1)
                .addIngredient('w', add10)
                .addIngredient('e', add50)
                .addIngredient('r', remove50)
                .addIngredient('t', remove10)
                .addIngredient('y', remove1)
                .addIngredient('i', info)
                .addIngredient('o', delete)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("§8Настройка вероятности предмета")
                .setGui(gui)
                .build();
        window.open();
    }
}
