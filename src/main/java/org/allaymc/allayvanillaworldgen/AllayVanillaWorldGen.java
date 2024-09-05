package org.allaymc.allayvanillaworldgen;

import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.registry.Registries;
import org.allaymc.api.world.DimensionInfo;

@Slf4j
public class AllayVanillaWorldGen extends Plugin {
    @Override
    public void onLoad() {
        log.info("AllayVanillaWorldGen loaded!");
    }

    @Override
    public void onEnable() {
        log.info("AllayVanillaWorldGen enabled!");
        JeGeneratorLoader.setup();
        Registries.WORLD_GENERATOR_FACTORIES.register("JEGEN_OVERWORLD", $1 -> JeGeneratorLoader.getJeGenerator(DimensionInfo.OVERWORLD));
        Registries.WORLD_GENERATOR_FACTORIES.register("JEGEN_NETHER", $1 -> JeGeneratorLoader.getJeGenerator(DimensionInfo.NETHER));
        Registries.WORLD_GENERATOR_FACTORIES.register("JEGEN_END", $1 -> JeGeneratorLoader.getJeGenerator(DimensionInfo.THE_END));
    }

    @Override
    public void onDisable() {
        log.info("AllayVanillaWorldGen disabled!");
    }
}