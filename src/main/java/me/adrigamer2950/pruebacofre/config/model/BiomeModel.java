package me.adrigamer2950.pruebacofre.config.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BiomeModel {

    private String biome;
    private List<String> lootTables;
}
