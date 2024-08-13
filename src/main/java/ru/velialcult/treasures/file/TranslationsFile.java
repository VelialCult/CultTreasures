package ru.velialcult.treasures.file;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;
import ru.velialcult.library.bukkit.file.FileRepository;
import ru.velialcult.treasures.CultTreasures;

public class TranslationsFile {

    private static final FileConfiguration config = FileRepository.getByName(CultTreasures.getInstance(), "translation.yml").getConfiguration();

    private static String getTranslation(String path) {
        if (!config.contains(path)) {
            return "&cПеревод не найден";
        } else {
            return config.getString(path);
        }
    }

    public static String getTranslationItem(Material material) {
        return getTranslation("items." + material.toString());
    }

    public static String getTranslationEffect(PotionEffectType effectType) {
        return getTranslation("effects." + effectType.getName());
    }
}
