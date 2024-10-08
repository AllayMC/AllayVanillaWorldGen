plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.allaymc.allayvanillaworldgen"
description = "AllayVanillaWorldGen"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven("https://www.jitpack.io/")
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
}

dependencies {
    compileOnly(group = "com.github.AllayMC.Allay", name = "api", version = "master-SNAPSHOT")
    compileOnly(group = "com.github.AllayMC.Allay", name = "server", version = "master-SNAPSHOT")
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.34")
    implementation(group = "org.allaymc", name = "JEGeneratorBinary", version = "1.20.6-R0.2")

    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.34")
}