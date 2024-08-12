# Vigilance

A full Java inter-op library for Minecraft mod settings. Built using [Elementa](https://github.com/Sk1erLLC/Elementa), 
this library provides a simple and clean configuration menu and an easy-to-use setting declaration system.

## Dependency

```kotlin
repository {
    // All versions of Vigilance and UniversalCraft are published to Essential's public maven repository.
    // (if you're still using Groovy build scripts, replace `()` with `{}`)
    maven(url = "https://repo.essential.gg/repository/maven-public")
}
dependencies {
    // Add Vigilance dependency. For the latest $vigilanceVersion, see the badge below this code snippet.
    implementation("gg.essential:vigilance:$vigilanceVersion")

    // Vigilance itself is independent of Minecraft versions and mod loaders, instead it depends on UniversalCraft which
    // provides bindings to specific Minecraft versions.
    // As such, you must include the UniversalCraft version for the Minecraft version + mod loader you're targeting.
    // For a list of all available platforms, see https://github.com/EssentialGG/UniversalCraft
    // For your convenience, the latest $ucVersion is also included in a badge below this code snippet.
    // (Note: if you are not using Loom, replace `modImplementation` with `implementation` or your equivalent)
    modImplementation("gg.essential:universalcraft-1.8.9-forge:$ucVersion")

    // If you're using Fabric, you may use its jar-in-jar mechanism to bundle Vigilance and UniversalCraft with your
    // mod by additionally adding them to the `include` configuration like this (in place of the above):
    implementation(include("gg.essential:vigilance:$vigilanceVersion")!!)
    modImplementation(include("gg.essential:universalcraft-1.8.9-forge:$ucVersion"))
    // If you're using Forge, you must instead include them directly into your jar file and relocate them to your
    // own package (this is important! otherwise you will be incompatible with other mods!)
    // using e.g. https://gradleup.com/shadow/configuration/relocation/
    // For an example, read the IMPORTANT section below.
}
```

<img alt="gg.essential:vigilance" src="https://img.shields.io/badge/dynamic/xml?color=A97BFF&label=Latest%20Vigilance&query=/metadata/versioning/versions/version[not(contains(text(),'%2B'))][last()]&url=https://repo.essential.gg/repository/maven-releases/gg/essential/vigilance/maven-metadata.xml">
<img alt="gg.essential:universalcraft-1.8.9-forge" src="https://img.shields.io/badge/dynamic/xml?color=A97BFF&label=Latest%20UniversalCraft&query=/metadata/versioning/versions/version[not(contains(text(),'%2B'))][last()]&url=https://repo.essential.gg/repository/maven-releases/gg/essential/universalcraft-1.8.9-forge/maven-metadata.xml">

<h2><span style="font-size:3em; color:red;">IMPORTANT!</span></h2>

If you are using Forge, you must also relocate Vigilance to avoid incompatibility with other mods.
To do this, you may use the Shadow Gradle plugin.

<details><summary>Groovy Version</summary>

You can do this by either putting it in your plugins block:
```groovy
plugins {
    id 'com.github.johnrengelman.shadow' version "$version"
}
```
or by including it in your buildscript's classpath and applying it:
```groovy
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath "gradle.plugin.com.github.jengelman.gradle.plugins:shadow:$version"
    }
}

apply plugin: 'com.github.johnrengelman.shadow'
```
You'll then want to relocate Vigilance to your own package to avoid breaking other mods
```groovy
shadowJar {
    archiveClassifier.set(null)
    relocate("gg.essential.vigilance", "your.package.vigilance")
    // vigilance dependencies
    relocate("gg.essential.elementa", "your.package.elementa")
    // elementa dependencies
    relocate("gg.essential.universalcraft", "your.package.universalcraft")
}
tasks.named("reobfJar").configure { dependsOn(tasks.named("shadowJar")) }
```

</details>

<details><summary>Kotlin Script Version</summary>

You can do this by either putting it in your plugins block:
```kotlin
plugins {
    id("com.github.johnrengelman.shadow") version "$version"
}
```
or by including it in your buildscript's classpath and applying it:
```kotlin
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:$version")
    }
}

apply(plugin = "com.github.johnrengelman.shadow")
```
You'll then want to relocate Vigilance to your own package to avoid breaking other mods
```kotlin
tasks.shadowJar {
    archiveClassifier.set(null)
    relocate("gg.essential.vigilance", "your.package.vigilance")
    // vigilance dependencies
    relocate("gg.essential.elementa", "your.package.elementa")
    // elementa dependencies
    relocate("gg.essential.universalcraft", "your.package.universalcraft")
}
tasks.reobfJar { dependsOn(tasks.shadowJar) }
```

</details>

## Examples

For examples of how to use Vigilance, refer to the `example` sub-project within Vigilance for
[annotation style](https://github.com/Sk1erLLC/Vigilance/blob/master/example/src/main/kotlin/gg/essential/vigilance/example/ExampleConfig.kt) 
and [DSL style](https://github.com/Sk1erLLC/Vigilance/blob/master/example/src/main/kotlin/gg/essential/vigilance/example/ExampleConfigDSL.kt)

You can run the examples via `./gradlew :example:run`.
