package ru.velialcult.treasures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.velialcult.library.bukkit.file.FileRepository;
import ru.velialcult.library.bukkit.utils.ConfigurationUtil;
import ru.velialcult.library.java.database.DataBase;
import ru.velialcult.library.java.database.DataBaseType;
import ru.velialcult.library.update.UpdateChecker;
import ru.velialcult.treasures.animation.TreasureOpener;
import ru.velialcult.treasures.command.CultTreasureCommand;
import ru.velialcult.treasures.data.DataBaseManager;
import ru.velialcult.treasures.data.PlayerData;
import ru.velialcult.treasures.file.ConfigFile;
import ru.velialcult.treasures.file.InventoriesFile;
import ru.velialcult.treasures.file.MessagesFile;
import ru.velialcult.treasures.handler.EventListener;
import ru.velialcult.treasures.loader.TreasureBlockLocationsLoader;
import ru.velialcult.treasures.loader.TreasureLoader;
import ru.velialcult.treasures.manager.KeyManager;
import ru.velialcult.treasures.manager.PlayerManager;
import ru.velialcult.treasures.manager.TreasureBlockLocationManager;
import ru.velialcult.treasures.manager.TreasureManager;
import ru.velialcult.treasures.saver.TreasureBlockLocationSaver;
import ru.velialcult.treasures.saver.TreasureSaver;

public class CultTreasures extends JavaPlugin {

    private static CultTreasures instance;

    private TreasureManager treasureManager;
    private KeyManager keyManager;
    private PlayerManager playerManager;
    private TreasureBlockLocationManager treasureBlockLocationManager;

    private TreasureOpener treasureOpener;

    private TreasureSaver treasureSaver;
    private TreasureBlockLocationSaver treasureBlockLocationSaver;

    private DataBase dataBase;

    private ConfigFile configFile;
    private InventoriesFile inventoriesFile;
    private MessagesFile messagesFile;

    @Override
    public void onEnable() {
        instance = this;
        long mills = System.currentTimeMillis();
        try {

            this.saveDefaultConfig();
            loadConfigs();

            UpdateChecker updateChecker = new UpdateChecker(this, "CultTreasures");
            updateChecker.check();

            String dataBaseType = getConfig().getString("settings.database.type");
            if (dataBaseType.equalsIgnoreCase("mysql")) {
                this.dataBase = new DataBase(this, DataBaseType.MySQL);
                dataBase.connect(getConfig().getString("settings.database.mysql.user"), getConfig().getString("settings.database.mysql.password"), getConfig().getString("settings.database.mysql.url"));
            } else {
                this.dataBase  = new DataBase(this, DataBaseType.SQLite);
                dataBase.connect();
            }

            new DataBaseManager(this);

            playerManager = new PlayerManager();

            keyManager = new KeyManager(playerManager);

            treasureBlockLocationManager = new TreasureBlockLocationManager(messagesFile);

            treasureOpener = new TreasureOpener();

            treasureManager = new TreasureManager(keyManager, treasureBlockLocationManager, treasureOpener, messagesFile);

            treasureSaver = new TreasureSaver(this);
            treasureBlockLocationSaver = new TreasureBlockLocationSaver(this);
            TreasureLoader treasureLoader = new TreasureLoader(this);
            treasureManager.getTreasures().addAll(treasureLoader.loadTreasure());
            TreasureBlockLocationsLoader treasureBlockLocationsLoader = new TreasureBlockLocationsLoader(this);
            treasureBlockLocationManager.getTreasureBlockLocations().addAll(treasureBlockLocationsLoader.loadLocations());

            Bukkit.getPluginManager().registerEvents(new EventListener(treasureBlockLocationManager, treasureManager), this);

            CultTreasureCommand cultTreasureCommand = new CultTreasureCommand(messagesFile, treasureManager, treasureBlockLocationManager, keyManager);

            Bukkit.getPluginCommand("culttreasures").setExecutor(cultTreasureCommand);
            Bukkit.getPluginCommand("culttreasures").setTabCompleter(cultTreasureCommand);

            getLogger().info("Плагин был загружен за " + ChatColor.YELLOW + (System.currentTimeMillis() - mills)  + "ms");
        } catch (Exception e) {
            getLogger().severe("Произошла ошибка при инициализации плагина: " + e.getMessage());
        }
    }

    private void loadConfigs() {
        ConfigurationUtil.loadConfigurations(this, "treasures.yml", "inventories.yml", "messages.yml", "translation.yml");
        FileRepository.load(this);
        configFile = new ConfigFile(this);
        configFile.load();
        inventoriesFile = new InventoriesFile(this);
        inventoriesFile.load();
        messagesFile = new MessagesFile(this);
        messagesFile.load();
    }

    @Override
    public void onDisable() {

        treasureSaver.save(treasureManager.getTreasures());
        treasureBlockLocationSaver.save(treasureBlockLocationManager.getTreasureBlockLocations());

        for (PlayerData playerData : playerManager.getPlayerData().values())
            playerData.savePlayerData(dataBase);

        if (dataBase.getConnector().isConnected()) {
            dataBase.getConnector().close();
        }
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public static CultTreasures getInstance() {
        return instance;
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public InventoriesFile getInventoriesFile() {
        return inventoriesFile;
    }

    public TreasureManager getTreasureManager() {
        return treasureManager;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public TreasureOpener getTreasureOpener() {
        return treasureOpener;
    }

    public MessagesFile getMessagesFile() {
        return messagesFile;
    }
}
