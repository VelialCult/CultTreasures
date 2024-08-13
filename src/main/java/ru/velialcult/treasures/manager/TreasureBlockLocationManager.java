package ru.velialcult.treasures.manager;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.treasures.file.MessagesFile;

import java.util.ArrayList;
import java.util.List;

public class TreasureBlockLocationManager {

    private final List<Location> treasureBlockLocations;
    private final MessagesFile messagesFile;

    public TreasureBlockLocationManager(MessagesFile messagesFile) {
        this.treasureBlockLocations = new ArrayList<>();
        this.messagesFile = messagesFile;
    }

    public void setTreasureBlockLocation(Player player, Block block) {
        if (treasureBlockLocations.contains(block.getLocation())) {
            VersionAdapter.MessageUtils().sendMessage(player, messagesFile.getFileOperations().getString("messages.commands.set-block.already-exists"));
        } else {
            treasureBlockLocations.add(block.getLocation());
            VersionAdapter.MessageUtils().sendMessage(player, messagesFile.getFileOperations().getString("messages.commands.set-block.set"));
        }
    }

    public List<Location> getTreasureBlockLocations() {
        return treasureBlockLocations;
    }
}
