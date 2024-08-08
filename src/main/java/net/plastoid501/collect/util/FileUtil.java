package net.plastoid501.collect.util;

import net.fabricmc.loader.api.FabricLoader;
import net.plastoid501.collect.CollectItems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static void generateClientModFolder() {
        Path path = getConfigPath().resolve(CollectItems.MOD_ID);

        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch(IOException e) {
                CollectItems.LOGGER.error(e.getMessage());
            }
        }

        NbtUtil.generateDefaultNbt();
    }




}
