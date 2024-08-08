package net.plastoid501.collect.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.plastoid501.collect.CollectItems;
import net.plastoid501.collect.config.Configs;
import net.plastoid501.collect.config.HotkeyConfig;
import net.plastoid501.collect.config.ModConfig;
import net.plastoid501.collect.config.CollectItemConfig;
import net.plastoid501.collect.config.json.JHotkeyConfig;
import net.plastoid501.collect.config.json.JCollectItemConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static void generateClientModConfig() {
        Path path = FileUtil.getConfigPath().resolve(CollectItems.MOD_ID + ".json");
        if (Files.exists(path)){
            return;
        }

        Map<String, JCollectItemConfig> toggleHotkeys = Configs.getJToggleHotkeys();
        Map<String, JHotkeyConfig> hotkeys = Configs.getJHotkeys();

        ModConfig config = new ModConfig(toggleHotkeys, hotkeys);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(config);

        try (FileWriter writer = new FileWriter(path.toString())) {
            writer.write(json);
        } catch (IOException e) {
            CollectItems.LOGGER.error(e.getMessage());
        }
    }

    public static void generateClientModConfig(ModConfig config) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(config);

        Path path = FileUtil.getConfigPath().resolve(CollectItems.MOD_ID + ".json");

        if (Files.notExists(path)){
            try (FileWriter writer = new FileWriter(path.toString())) {
                writer.write(json);
            } catch (IOException e) {
                CollectItems.LOGGER.error(e.getMessage());
            }
        }
    }

    public static ModConfig readConfig() {
        Path path = FileUtil.getConfigPath().resolve(CollectItems.MOD_ID + ".json");
        Gson gson = new Gson();
        ModConfig config;

        try {
            String jsonContent = Files.readString(path);
            config = gson.fromJson(jsonContent, ModConfig.class);
        } catch (IOException | JsonSyntaxException e) {
            CollectItems.LOGGER.error(e.getMessage());
            return Configs.config;
        }

        if (config == null) {
            return Configs.config;
        }

        return config;
    }

    public static void saveConfig(ModConfig config) {
        Path path = FileUtil.getConfigPath().resolve(CollectItems.MOD_ID + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = Files.newBufferedWriter(path)) {
            gson.toJson(config, writer);
        } catch (IOException | JsonIOException e) {
            CollectItems.LOGGER.error(e.getMessage());
        }

        updateConfigs();
    }

    public static void updateThrowItemConfig(String target, JCollectItemConfig toggle) {
        ModConfig config = readConfig();
        Map<String, JCollectItemConfig> toggleConfigMap = config.getToggleHotkeys();
        toggleConfigMap.replace(target, toggle);
        saveConfig(config);
    }

    public static void updateHotkeyConfig(String target, List<String> Keys) {
        ModConfig config = readConfig();
        Map<String, JHotkeyConfig> hotkeyConfigMap = config.getHotkeys();
        hotkeyConfigMap.get(target).setKeys(Keys);
        saveConfig(config);
    }

    public static void updateConfigs() {
        ModConfig config = readConfig();
        if (config != null) {
            if (config.getToggleHotkeys() == null || config.getHotkeys() == null) {
                saveConfig(Configs.config);
                return;
            }

            boolean flag = false;

            if (config.getHotkeys().get(Configs.openGUI.getId()) == null) {
                flag = true;
                config.getHotkeys().put(Configs.openGUI.getId(), Configs.openGUI);
            }
            Configs.openGUI = new HotkeyConfig("openGUI", config.getHotkeys().get(Configs.openGUI.getId()).getKeys(), Configs.openGUI.getNarrator());

            if (config.getHotkeys().get(Configs.storeItem.getId()) == null) {
                flag = true;
                config.getHotkeys().put(Configs.storeItem.getId(), Configs.storeItem);
            }
            Configs.storeItem = new HotkeyConfig("storeItem", config.getHotkeys().get(Configs.storeItem.getId()).getKeys(), Configs.storeItem.getNarrator());

            if (config.getToggleHotkeys().get(Configs.collectItems.getId()) == null) {
                flag = true;
                config.getToggleHotkeys().put(Configs.collectItems.getId(), Configs.collectItems);
            }
            Configs.collectItems = new CollectItemConfig("collectItems", config.getToggleHotkeys().get(Configs.collectItems.getId()).isEnable(), config.getToggleHotkeys().get(Configs.collectItems.getId()).getKeys(), config.getToggleHotkeys().get(Configs.collectItems.getId()).getSelected(), Configs.collectItems.getStacks(), Configs.collectItems.getNarrator());

            if (flag) {
                saveConfig(Configs.config);
                return;
            }
            Configs.config = config;

        }
    }
}
