package ru.velialcult.treasures.manager;

import ru.velialcult.treasures.data.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, PlayerData> playerData;

    public PlayerManager() {
        this.playerData = new HashMap<>();
    }

    public PlayerData getPlayerData(UUID uuid) {
        if (!this.playerData.containsKey(uuid)) {
            PlayerData playerData = new PlayerData(uuid);
            this.playerData.put(uuid, playerData);
        }
        return playerData.get(uuid);
    }

    public Map<UUID, PlayerData> getPlayerData() {
        return playerData;
    }
}
