package ru.velialcult.treasures.data;

import ru.velialcult.library.java.database.table.ColumnType;
import ru.velialcult.library.java.database.table.TableColumn;
import ru.velialcult.library.java.database.table.TableConstructor;
import ru.velialcult.treasures.CultTreasures;

public class DataBaseManager {

    private final CultTreasures plugin;

    public DataBaseManager(CultTreasures plugin) {
        this.plugin = plugin;
        createTables();
    }

    private void createTables() {
        new TableConstructor("opened_treasures",
                             new TableColumn("player_id", ColumnType.VARCHAR_32),
                             new TableColumn("treasure_id", ColumnType.VARCHAR_32),
                             new TableColumn("count", ColumnType.INT)
        ).create(plugin.getDataBase());
        new TableConstructor("treasures_keys",
                             new TableColumn("player_id", ColumnType.VARCHAR_32),
                             new TableColumn("treasure_id", ColumnType.VARCHAR_32),
                             new TableColumn("count", ColumnType.INT)
        ).create(plugin.getDataBase());
    }
}
