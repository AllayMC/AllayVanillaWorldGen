package org.allaymc.allayvanillaworldgen;

import io.papermc.paperclip.Paperclip;
import org.allaymc.api.world.DimensionInfo;
import org.allaymc.api.world.generator.WorldGenerator;
import org.allaymc.server.Allay;
import org.allaymc.server.world.generator.AllayWorldGenerator;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class JeGeneratorLoader {
    public static final String WORK_PATH = "jegenerator";
    private static final AtomicBoolean LOADED = new AtomicBoolean(false);
    private static MethodHandle OVERWORLD;
    private static MethodHandle NETHER;
    private static MethodHandle THE_END;

    public static void setup() {
        if (LOADED.compareAndSet(false, true)) {
            setup0();
            waitStart();
        }
    }

    public static WorldGenerator getJeGenerator(DimensionInfo info) {
        if (LOADED.compareAndSet(false, true)) {
            JeGeneratorLoader.setup();
            JeGeneratorLoader.waitStart();
        }
        try {
            if (info == DimensionInfo.NETHER) {
                return (AllayWorldGenerator) NETHER.invokeExact();
            } else if (info == DimensionInfo.THE_END) {
                return (AllayWorldGenerator) THE_END.invokeExact();
            } else {
                return (AllayWorldGenerator) OVERWORLD.invokeExact();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void setup0() {
        File file = Path.of(WORK_PATH).toFile();
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + WORK_PATH);
            }
        }
        Paperclip.setup(Allay.EXTRA_RESOURCE_CLASS_LOADER, new String[]{WORK_PATH, "allay", "--noconsole", "--nogui", "--universe=jegenerator"});
        try {
            final Class<?> main = Class.forName("org.allaymc.jegenerator.AllayVanillaGeneratorExtension", true, Allay.EXTRA_RESOURCE_CLASS_LOADER);
            final MethodType methodType = MethodType.methodType(AllayWorldGenerator.class);
            OVERWORLD = MethodHandles.lookup()
                    .findStatic(main, "overworld", methodType)
                    .asFixedArity();
            NETHER = MethodHandles.lookup()
                    .findStatic(main, "nether", methodType)
                    .asFixedArity();
            THE_END = MethodHandles.lookup()
                    .findStatic(main, "end", methodType)
                    .asFixedArity();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void waitStart() {
        spinUntil(() -> !System.getProperties().getOrDefault("complete_start", false).equals("true"), Duration.of(20, ChronoUnit.MILLIS));
    }

    private static void spinUntil(Supplier<Boolean> end, Duration interval) {
        while (end.get()) {
            try {
                long times = MILLISECONDS.convert(interval);
                //noinspection BusyWait
                Thread.sleep(times);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
