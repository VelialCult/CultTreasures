package ru.velialcult.treasures.menu.setup;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.treasures.treasure.Treasure;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.VirtualInventoryManager;
import xyz.xenondevs.invui.inventory.event.UpdateReason;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

import java.util.*;
import java.util.stream.Collectors;

public class TreasureAddDropItemsMenu {

    public static void generateInventory(Player player, Treasure treasure) {

        VirtualInventory virtualInventory = VirtualInventoryManager.getInstance().getOrCreate(player.getUniqueId(), 54);

        SuppliedItem closeButton = new SuppliedItem(() ->
                                                            s -> VersionAdapter.getSkullBuilder()
                                                                    .setDisplayName("§6Закрыть меню")
                                                                    .setLore(new ArrayList<>(Arrays.asList(
                                                                            "§7Нажмите, чтобы закрыть данное",
                                                                            "§7меню."
                                                                    )))
                                                                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M1ZmQ0MTJiY2VjNDhmMjk4MDIyOWY0NGFkNzg1ZTlkMjQ3ZjY2YjZhOTFlZWY3YTk4ZDc2NmJkMTFkNGExOSJ9fX0=")
                                                                    .build(), click -> {
            virtualInventory.removeIf(UpdateReason.SUPPRESSED, (itemStack) -> itemStack != null);
            TreasureSetupMenu.generateInventory(player, treasure);
            return true;
        });

        SuppliedItem saveButton = new SuppliedItem(() ->
                                                           s -> VersionAdapter.getItemBuilder()
                                                                   .setDisplayName("§6Сохранить предметы")
                                                                   .setLore(new ArrayList<>(Arrays.asList(
                                                                           "",
                                                                           "§7Нажмите, чтобы сохранить все добавленные",
                                                                           "§7предметы.",
                                                                           "",
                                                                           " §8▹ §eНажмите, чтобы сохранить предметы"
                                                                   )))
                                                                   .setType(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial())
                                                                   .build(), click -> {
            List<ItemStack> nonNullItems = Arrays.stream(virtualInventory.getItems())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            for (ItemStack itemStack : nonNullItems) {
                if (treasure.getItems().keySet().stream().noneMatch(item -> item.isSimilar(itemStack))) {
                    treasure.getItems().put(itemStack, 0.0D);
                }
            }

            virtualInventory.removeIf(UpdateReason.SUPPRESSED, (itemStack) -> itemStack != null);
            TreasureSetupMenu.generateInventory(player, treasure);
            return true;
        });

        Gui gui = Gui.normal()
                .setStructure(
                        "b b b b b b b b b",
                        "b v v v v v v v b",
                        "b v v v v v v v b",
                        "b v v v v v v v b",
                        "b b b b b b b b b",
                        ". . s . c . . . . "
                )
                .addIngredient('b', XMaterial.GRAY_STAINED_GLASS_PANE.parseItem())
                .addIngredient('v', virtualInventory)
                .addIngredient('s', saveButton)
                .addIngredient('c', closeButton)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("§8Добавление предметов в сокровищницу")
                .setGui(gui)
                .build();
        window.open();
    }
}
