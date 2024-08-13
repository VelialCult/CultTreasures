package ru.velialcult.treasures.command;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.velialcult.library.bukkit.utils.PlayerUtil;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.library.java.text.ReplaceData;
import ru.velialcult.treasures.treasure.Treasure;
import ru.velialcult.treasures.file.MessagesFile;
import ru.velialcult.treasures.manager.KeyManager;
import ru.velialcult.treasures.manager.TreasureBlockLocationManager;
import ru.velialcult.treasures.manager.TreasureManager;
import ru.velialcult.treasures.menu.setup.TreasureList;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CultTreasureCommand implements CommandExecutor, TabCompleter {

    private final MessagesFile messagesFile;
    private final TreasureManager treasureManager;
    private final KeyManager keyManager;
    private final TreasureBlockLocationManager treasureBlockLocationManager;

    public CultTreasureCommand(MessagesFile messagesFile,
                               TreasureManager treasureManager,
                               TreasureBlockLocationManager treasureBlockLocationManager,
                               KeyManager keyManager) {
        this.messagesFile = messagesFile;
        this.treasureManager = treasureManager;
        this.keyManager = keyManager;
        this.treasureBlockLocationManager = treasureBlockLocationManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("culttreasures.admin")) {
            if (args.length == 0) {
                VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.help"));
                return true;
            } else {

                String cmd = args[0].toUpperCase();
                switch (cmd) {

                    case "SET-BLOCK": {
                        if (PlayerUtil.senderIsPlayer(sender)) {
                            if (args.length != 1) {
                                VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.create.usage"));
                                return true;
                            }

                            Player player = (Player) sender;
                            Block block = player.getTargetBlockExact(10);
                            if (block == null) {
                                VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.create.block-is-null"));
                                return true;
                            }

                            treasureBlockLocationManager.setTreasureBlockLocation(player, block);
                            break;
                        }
                    }

                    case "GIVE": {
                        if (args.length != 4) {
                            VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.give.usage"));
                            return true;
                        }

                        Player target = Bukkit.getPlayer(args[1]);

                        if (target == null) {
                            VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.give.player-is-null"));
                            return true;
                        }

                        UUID uuid;

                        try {
                            uuid = UUID.fromString(args[2]);
                        } catch (Exception e) {
                            VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.wrong-value",
                                                                                                                       new ReplaceData("{value}", args[2])
                            ));
                            return true;
                        }

                        Treasure treasure = treasureManager.getTreasureByUUID(uuid);

                        if (treasure == null) {
                            VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.give.treasure-not-found"));
                            return true;
                        }

                        int amount;

                        try {
                            amount = Integer.parseInt(args[3]);
                        } catch (Exception e) {
                            VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.wrong-value",
                                                                                                                       new ReplaceData("{value}", args[3])
                            ));
                            return true;
                        }

                        keyManager.giveKey(target.getPlayer(), treasure, amount);
                        if (!PlayerUtil.senderIsPlayer(sender)) {
                            VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList(
                                    "messages.commands.give.give",
                                    new ReplaceData("{amount}", amount),
                                    new ReplaceData("{player}", target.getName())
                            ));
                        }
                        VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.give.info",
                                                                                                                   new ReplaceData("{amount}", amount)));
                        break;
                    }

                    case "SETTINGS": {
                        if (PlayerUtil.senderIsPlayer(sender)) {
                            if (args.length != 1) {
                                VersionAdapter.MessageUtils().sendMessage(sender, messagesFile.getFileOperations().getList("messages.commands.settings.usage"));
                                return true;
                            }

                            TreasureList.generateInventory( (Player) sender);
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("set-block", "give", "settings");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return treasureManager.getTreasures().stream().map(treasure -> treasure.getUuid().toString()).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}
