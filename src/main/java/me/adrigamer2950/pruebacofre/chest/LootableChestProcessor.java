package me.adrigamer2950.pruebacofre.chest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import me.adrigamer2950.pruebacofre.config.LootTableModelDeserializer;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class LootableChestProcessor {

    private final HashMap<String, List<Identifier>> lootTables;
    private final List<Identifier> defaultTables;

    @Getter
    @Setter
    public static LootableChestProcessor instance;

    public static void processChest(LootableChest chest, BlockPos pos, World world) {
        if (chest.isUsed()) return;

        String biome = world.getBiome(pos).getKey().orElseThrow().getValue().toString();

        Random r = world.getRandom();

        Identifier lootTable = getInstance().getLootTableForBiome(biome, r);

        LootableContainerBlockEntity.setLootTable(world, r, pos, lootTable);
        chest.setUsed(true);
    }

    private Identifier getLootTableForBiome(String biome, Random r) {
        if (!lootTables.containsKey(biome)) return this.getRandom(this.defaultTables, r);

        List<Identifier> lootTables = this.lootTables.get(biome);

        return this.getRandom(lootTables, r);
    }

    private Identifier getRandom(List<Identifier> tables, Random r) {
        if (!tables.isEmpty())
            return tables.get(r.nextBetween(0, tables.size() - 1));
        else
            return new Identifier("minecraft", "empty");
    }
}
