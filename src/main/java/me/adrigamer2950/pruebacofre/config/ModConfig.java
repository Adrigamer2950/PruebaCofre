package me.adrigamer2950.pruebacofre.config;

import lombok.Getter;
import lombok.SneakyThrows;
import me.adrigamer2950.pruebacofre.PCMain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

@Getter
public class ModConfig {

    public static final String PER_BIOME_CHEST_FILE = "per_biome_chest.json";
    private static final String PATH = "./config/%s/%s".formatted(
            PCMain.MOD_ID,
            PER_BIOME_CHEST_FILE
    );

    private final File file;

    @SneakyThrows
    public ModConfig() {
        File configFolder = new File("./config", PCMain.MOD_ID);

        if (!configFolder.exists())
            configFolder.mkdirs();

        this.file = new File(PATH);

        if (this.file.exists()) return;
        this.file.createNewFile();

        FileOutputStream output = new FileOutputStream(PATH);
        output.write(
                PCMain.class.getClassLoader().getResourceAsStream("data/pruebacofre/loot_table_per_biome/per_biome_chest.json").readAllBytes()
        );
        output.close();
    }

    @SneakyThrows
    public void reloadData() {
        //noinspection resource
        FileInputStream inputStream = new FileInputStream(this.file);
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        LootTableModelDeserializer.fromJson(json);
    }
}
