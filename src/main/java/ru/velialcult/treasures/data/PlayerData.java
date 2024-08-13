package ru.velialcult.treasures.data;

import ru.velialcult.library.java.database.Connector;
import ru.velialcult.library.java.database.DataBase;
import ru.velialcult.library.java.database.query.QuerySymbol;
import ru.velialcult.library.java.database.query.SQLQuery;
import ru.velialcult.treasures.CultTreasures;
import ru.velialcult.treasures.treasure.Treasure;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private final UUID playerId;
    private final Map<UUID, Integer> openedTreasuresCount;
//    private final Map<UUID, Integer> keyCount;

    public PlayerData(UUID playerId) {
        this.playerId = playerId;
        this.openedTreasuresCount = new HashMap<>();
//        this.keyCount = new HashMap<>();

        load(CultTreasures.getInstance().getDataBase());
    }

    private void load(DataBase dataBase) {
        loadOpenedTreasuresCount(dataBase);
//        loadKeyCount(dataBase);
    }

    public void savePlayerData(DataBase dataBase) {
        saveOpenedTreasuresCount(dataBase);
//        saveKeyCount(dataBase);
    }

    public void incrementOpenedTreasures(Treasure treasure) {
        openedTreasuresCount.compute(treasure.getUuid(), (uuid, count) -> (count == null ? 0 : count) + 1);
    }

    private void loadOpenedTreasuresCount(DataBase dataBase) {
        Connector connector = dataBase.getConnector();

        connector.executeQuery(SQLQuery.selectFrom("opened_treasures")
                                       .where("player_id", QuerySymbol.EQUALLY, this.playerId.toString()),
                               rs -> {
                                while (rs.next()) {
                                    UUID treasureId = UUID.fromString(rs.getString("treasure_id"));
                                    int count = rs.getInt("count");
                                    openedTreasuresCount.put(treasureId, count);
                                }
                            return Void.TYPE;
                               }, false);
    }

//    private void loadKeyCount(DataBase dataBase) {
//        Connector connector = dataBase.getConnector();
//
//        connector.executeQuery(SQLQuery.selectFrom("treasures_keys")
//                                       .where("player_id", QuerySymbol.EQUALLY, this.playerId.toString()),
//                               rs -> {
//                                    while (rs.next()) {
//                                        UUID treasureId = UUID.fromString(rs.getString("treasure_id"));
//                                        int count = rs.getInt("count");
//                                        keyCount.put(treasureId, count);
//                                    }
//                                    return Void.TYPE;
//                               }, false);
//    }

    private void saveOpenedTreasuresCount(DataBase dataBase) {
        Connector connector = dataBase.getConnector();

        for (Map.Entry<UUID, Integer> entry : openedTreasuresCount.entrySet()) {
            connector.execute(SQLQuery.insertOrUpdate(dataBase, "opened_treasures")
                                      .set("count", entry.getValue())
                                      .set("player_id", this.playerId.toString())
                                      .set("treasure_id", entry.getKey().toString())
                                      .where("player_id", QuerySymbol.EQUALLY, this.playerId.toString())
                                      .where("treasure_id", QuerySymbol.EQUALLY, entry.getKey().toString()),
                              true);
        }
    }

//    private void saveKeyCount(DataBase dataBase) {
//        Connector connector = dataBase.getConnector();
//
//        for (Map.Entry<UUID, Integer> entry : keyCount.entrySet()) {
//            connector.execute(SQLQuery.insertOrUpdate(dataBase, "treasures_keys")
//                                      .set("count", entry.getValue())
//                                      .set("player_id", this.playerId.toString())
//                                      .set("treasure_id", entry.getKey().toString())
//                                      .where("player_id", QuerySymbol.EQUALLY, this.playerId.toString())
//                                      .where("treasure_id", QuerySymbol.EQUALLY, entry.getKey().toString()),
//                              true);
//        }
//    }

//    public Map<UUID, Integer> getKeyCount() {
//        return keyCount;
//    }

    public Map<UUID, Integer> getOpenedTreasuresCount() {
        return openedTreasuresCount;
    }
}
