import gg.essential.gradle.multiversion.mergePlatformSpecifics
import gg.essential.gradle.util.*

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
    id("gg.essential.defaults.maven-publish")
}

group = "gg.essential"

java.withSourcesJar()
tasks.compileKotlin.setJvmDefault(if (platform.mcVersion >= 11400) "all" else "all-compatibility")
loom.noServerRunConfigs()

val common by configurations.creating
configurations.compileClasspath { extendsFrom(common) }
configurations.runtimeClasspath { extendsFrom(common) }

dependencies {
    common(project(":"))

    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.reflect)
    compileOnly(libs.jetbrains.annotations)

    modApi(libs.versions.elementa.map { "gg.essential:elementa-$platform:$it" }) {
        exclude(module = "kotlin-reflect")
        exclude(module = "kotlin-stdlib-jdk8")
    }

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
            modLocalRuntime(modCompileOnly(fabricApi.module("fabric-$module", fabricApiVersion))!!)
        }
    }
}

tasks.jar {
    dependsOn(common)
    from({ common.map { zipTree(it) } })
    mergePlatformSpecifics()

    // We build the common module with legacy default impl for backwards compatibility, but we only need those for
    // 1.12.2 and older. Newer versions have never shipped with legacy default impl.
    if (platform.mcVersion >= 11400) {
        exclude("**/*\$DefaultImpls.class")
    }

    exclude("gg/essential/vigilance/example/**")
    exclude("META-INF/mods.toml")
    exclude("mcmod.info")
    exclude("kotlin/**")
}

tasks.named<Jar>("sourcesJar") {
    from(project(":").sourceSets.main.map { it.allSource })
}
