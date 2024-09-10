package me.adrigamer2950.pruebacofre.config;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import me.adrigamer2950.pruebacofre.chest.LootableChestProcessor;
import me.adrigamer2950.pruebacofre.config.model.BiomeLootTablesModel;
import me.adrigamer2950.pruebacofre.config.model.BiomeModel;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;

public class LootTableModelDeserializer {

    @SneakyThrows
    public static void fromJson(String json) {
        BiomeLootTablesModel model = new Gson().fromJson(json, BiomeLootTablesModel.class);

        HashMap<String, List<Identifier>> lootTables = new HashMap<>();

        if (model.getBiomes() != null)
            for (BiomeModel ltModel : model.getBiomes())
                lootTables.put(
                        ltModel.getBiome(),
                        ltModel.getLootTables()
                                .stream().map(Identifier::new)
                                .toList()
                );

        List<Identifier> defaultTables = (
                model.getDefaultTables() == null ? List.of("minecraft:undefined") : model.getDefaultTables()
        )
                .stream()
                .map(Identifier::new)
                .toList();

        LootableChestProcessor.setInstance(
                new LootableChestProcessor(
                        lootTables,
                        defaultTables
                )
        );
    }
}
