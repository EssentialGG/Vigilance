import gg.essential.gradle.multiversion.StripReferencesTransform.Companion.registerStripReferencesAttribute
import gg.essential.gradle.util.*
import gg.essential.gradle.util.RelocationTransform.Companion.registerRelocationAttribute

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.8.0"
    id("org.jetbrains.dokka") version "1.9.20"
    id("gg.essential.defaults")
    id("gg.essential.defaults.maven-publish")
}

group = "gg.essential"
version = versionFromBuildIdAndBranch()

kotlin.jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(8))
}
tasks.compileKotlin.setJvmDefault("all-compatibility")

val internal by configurations.creating {
    val relocated = registerRelocationAttribute("internal-relocated") {
        relocate("com.electronwill.nightconfig", "gg.essential.vigilance.impl.nightconfig")
        remapStringsIn("com.electronwill.nightconfig.core.file.FormatDetector")
    }
    attributes { attribute(relocated, true) }
}

val common = registerStripReferencesAttribute("common") {
    excludes.add("net.minecraft")
}

dependencies {
    internal(libs.nightconfig.toml)
    implementation(prebundle(internal))

    compileOnly(libs.kotlin.stdlib.jdk8)
    compileOnly(libs.kotlin.reflect)
    compileOnly(libs.jetbrains.annotations)

    api(libs.elementa)

    // Depending on 1.8.9 for all of these because that's the oldest version we support
    compileOnly(libs.universalcraft.forge10809) {
        attributes { attribute(common, true) }
    }

    compileOnly("com.google.code.gson:gson:2.2.4")

    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testImplementation("io.strikt:strikt-core:0.22.1")
}

tasks.processResources {
    inputs.property("project.version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.jar {
    dependsOn(internal)
    from({ internal.map { zipTree(it) } })
}

apiValidation {
    ignoredProjects.addAll(subprojects.map { it.name })
    nonPublicMarkers.add("org.jetbrains.annotations.ApiStatus\$Internal")
}

tasks.test {
    useJUnitPlatform()
}

java.withSourcesJar()

publishing.publications.named<MavenPublication>("maven") {
    artifactId = "vigilance"
}
