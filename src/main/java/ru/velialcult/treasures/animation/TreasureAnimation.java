package ru.velialcult.treasures.animation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.library.java.text.ReplaceData;
import ru.velialcult.treasures.CultTreasures;
import ru.velialcult.treasures.file.MessagesFile;
import ru.velialcult.treasures.file.TranslationsFile;
import ru.velialcult.treasures.treasure.Treasure;

public class TreasureAnimation {
    private static final int HEIGHT = 10;
    private static final double SPEED = 0.5;

    public static void playOpenAnimation(Location location, Treasure treasure, ItemStack reward, Player player) {
        Location startLocation = location.clone();
        TreasureOpener treasureOpener = CultTreasures.getInstance().getTreasureOpener();
        MessagesFile messagesFile = CultTreasures.getInstance().getMessagesFile();
        Item item = location.getWorld().dropItem(location.add(0.5, 1, 0.5), reward);
        item.setPickupDelay(32767);
        new BukkitRunnable() {
            boolean isFalling = false;

            @Override
            public void run() {
                if (item.getLocation().getY() >= location.getY() + HEIGHT && !isFalling) {
                    isFalling = true;
                    item.setVelocity(new Vector(0, -SPEED, 0));
                } else if (item.getLocation().getY() <= location.getY() && isFalling) {
                    this.cancel();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            item.remove();
                            player.getInventory().addItem(reward);
                            treasureOpener.finishOpeningTreasure(startLocation);
                            cancel();
                        }
                    }.runTaskLater(CultTreasures.getInstance(), 40L);

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        VersionAdapter.MessageUtils().sendMessage(onlinePlayer, messagesFile.getFileOperations().getList("messages.treasure.reward",
                                                                                                                         new ReplaceData("{player}", player.getName()),
                                                                                                                         new ReplaceData("{treasure}", treasure.getDisplayName()),
                                                                                                                         new ReplaceData("{item}", item.getItemStack().getItemMeta().hasDisplayName() ? item.getItemStack().getItemMeta().getDisplayName() : TranslationsFile.getTranslationItem(item.getItemStack().getType()))
                        ));
                    }
                } else if (!isFalling) {
                    item.setVelocity(new Vector(0, SPEED, 0));
                    location.getWorld().playSound(item.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1F, 1F);
                    location.getWorld().spawnParticle(Particle.FLAME, item.getLocation(), 10, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer(CultTreasures.getInstance(), 0L, 1L);
    }
}
