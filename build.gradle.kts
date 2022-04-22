import gg.essential.gradle.util.*

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
    id("gg.essential.defaults.maven-publish")
}

val kotlin_version = "1.5.10"

group = "gg.essential"

java.withSourcesJar()
tasks.compileKotlin.setJvmDefault(if (platform.mcVersion >= 11400) "all" else "all-compatibility")
loom.noServerRunConfigs()

val internal = makeConfigurationForInternalDependencies {
    relocate("com.electronwill.nightconfig", "gg.essential.vigilance.impl.nightconfig")
    remapStringsIn("com.electronwill.nightconfig.core.file.FormatDetector")
}

dependencies {
    internal("com.electronwill.night-config:toml:3.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    compileOnly("org.jetbrains:annotations:23.0.0")

    modApi("gg.essential:elementa-$platform:476") {
        exclude(module = "kotlin-reflect")
        exclude(module = "kotlin-stdlib-jdk8")
    }

    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testImplementation("io.strikt:strikt-core:0.22.1")

    if (platform.isFabric) {
        val fabricApiVersion = when(platform.mcVersion) {
            11404 -> "0.4.3+build.247-1.14"
            11502 -> "0.5.1+build.294-1.15"
            11601 -> "0.14.0+build.371-1.16"
            11602 -> "0.17.1+build.394-1.16"
            11701 -> "0.38.1+1.17"
            11801 -> "0.46.4+1.18"
            else -> throw GradleException("Unsupported platform $platform")
        }
        val fabricApiModules = mutableListOf(
                "api-base",
                "networking-v0",
                "keybindings-v0",
                "resource-loader-v0",
                "command-api-v1",
                "lifecycle-events-v1",
        )
        if (platform.mcVersion >= 11600) {
            fabricApiModules.add("key-binding-api-v1")
        }
        fabricApiModules.forEach { module ->
            // Using this combo to add it to our deps but not to our maven publication cause it's only for the example
            modRuntime(modCompileOnly(fabricApi.module("fabric-$module", fabricApiVersion))!!)
        }
    }
}

tasks.jar {
    exclude("gg/essential/vigilance/example/**")
    exclude("META-INF/mods.toml")
    exclude("mcmod.info")
    exclude("kotlin/**")
    manifest {
        attributes(mapOf("FMLModType" to "LIBRARY"))
    }
}
