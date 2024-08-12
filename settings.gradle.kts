pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
    }
    plugins {
        val egtVersion = "0.6.2"
        id("gg.essential.defaults") version egtVersion
        id("gg.essential.defaults.maven-publish") version egtVersion
    }
}

rootProject.name = "Vigilance"

include(":example")
