package me.adrigamer2950.pruebacofre.config.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BiomeLootTablesModel {

    public List<String> defaultTables;
    public List<BiomeModel> biomes;
}
