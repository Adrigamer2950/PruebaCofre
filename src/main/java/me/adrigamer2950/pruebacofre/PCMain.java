package me.adrigamer2950.pruebacofre;

import me.adrigamer2950.pruebacofre.config.LootTableModelDeserializer;
import me.adrigamer2950.pruebacofre.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PCMain implements ModInitializer {

    public static final String MOD_ID = "pruebacofre";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private ModConfig config;

    @Override
    public void onInitialize() {
        config = new ModConfig();

        this.reloadResourceLoader();
        this.reloadCommand();

        LOGGER.info("Enabled!");
    }

    public void reloadCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                CommandManager.literal("chestloot")
                        .executes(ctx -> {
                            ctx.getSource().sendMessage(
                                    Text.literal("§cDebes usar §6/chestloot reload §cpara recargar la configuración")
                            );

                            return 0;
                        })
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(
                                CommandManager.literal("reload")
                                        .executes(ctx -> {
                                            ctx.getSource().sendMessage(
                                                    Text.literal("§aRecargando loot tables...")
                                            );

                                            this.config.reloadData();

                                            ctx.getSource().sendMessage(
                                                    Text.literal("§aLoot tables recargadas")
                                            );

                                            return 1;
                                        })
                        )
        ));
    }

    public void reloadResourceLoader() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier(MOD_ID, "loot_table_per_biome");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        manager.findResources(
                                "loot_table_per_biome",
                                identifier -> identifier.getPath().endsWith(ModConfig.PER_BIOME_CHEST_FILE)
                        ).forEach((identifier, resource) -> {
                            try (InputStream stream = resource.getInputStream()) {
                                String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

                                LootTableModelDeserializer.fromJson(json);
                            } catch (IOException e) {
                                LOGGER.error("Error while trying to read : " + identifier);
                            }
                        });
                    }
                }
        );
    }
}
