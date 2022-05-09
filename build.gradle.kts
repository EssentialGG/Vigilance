import gg.essential.gradle.multiversion.StripReferencesTransform.Companion.registerStripReferencesAttribute
import gg.essential.gradle.util.*

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.8.0"
    id("org.jetbrains.dokka") version "1.6.10" apply false
    id("gg.essential.defaults")
}

kotlin.jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(8))
}
tasks.compileKotlin.setJvmDefault("all-compatibility")

val internal = makeConfigurationForInternalDependencies {
    relocate("com.electronwill.nightconfig", "gg.essential.vigilance.impl.nightconfig")
    remapStringsIn("com.electronwill.nightconfig.core.file.FormatDetector")
}

val common = registerStripReferencesAttribute("common") {
    excludes.add("net.minecraft")
}

dependencies {
    internal(libs.nightconfig.toml)

    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.reflect)
    compileOnly(libs.jetbrains.annotations)

    // Depending on LWJGL3 instead of 2 so we can choose opengl bindings only
    compileOnly("org.lwjgl:lwjgl-opengl:3.3.1")
    // Depending on 1.8.9 for all of these because that's the oldest version we support
    compileOnly(libs.versions.elementa.map { "gg.essential:elementa-1.8.9-forge:$it" }) {
        attributes { attribute(common, true) }
    }
    compileOnly("gg.essential:universalcraft-1.8.9-forge") {
        attributes { attribute(common, true) }
    }

    compileOnly("com.google.code.gson:gson:2.2.4")

    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testImplementation("io.strikt:strikt-core:0.22.1")
}

apiValidation {
    ignoredProjects.addAll(project("platform").allprojects.map { it.name })
    ignoredPackages.add("gg.essential.vigilance.example")
    nonPublicMarkers.add("org.jetbrains.annotations.ApiStatus\$Internal")
}
