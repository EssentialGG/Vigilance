plugins {
    kotlin("jvm") version "1.5.10" apply false
    id("fabric-loom") version "0.4-SNAPSHOT" apply false
    id("com.replaymod.preprocess") version "e4476a6"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.7.1"
}

version = determineVersion()

// Loom tries to find the active mixin version by recursing up to the root project and checking each project's
// compileClasspath and build script classpath (in that order). Since we've loom in our root project's classpath,
// loom will only find it after checking the root project's compileClasspath (which doesn't exist by default).
configurations.register("compileClasspath")

preprocess {
//    "1.16.2-fabric"(11602, "yarn") {
    "1.16.2"(11602, "srg") {
        "1.15.2"(11502, "srg") {
            "1.12.2"(11202, "srg", file("versions/1.15.2-1.12.2.txt")) {
                "1.8.9"(10809, "srg", file("versions/1.12.2-1.8.9.txt"))
            }
        }
    }
//    }
}

apiValidation {
    ignoredPackages.add("gg.essential.vigilance.example")
}

subprojects {
    val rootApiDir = rootProject.file("api")
    val projectApiDir = project.file("api").also { it.mkdirs() }
    val copyApiDefinition by tasks.registering(Sync::class) {
        from(rootApiDir)
        into(projectApiDir)
        rename { "${project.name}.api" }
    }
    tasks.withType<kotlinx.validation.ApiCompareCompareTask> {
        dependsOn(copyApiDefinition)
        this.projectApiDir = projectApiDir
    }
    afterEvaluate {
        tasks.named<Sync>("apiDump") {
            into(rootApiDir)
            rename { "Vigilance.api" }
        }
    }
}

fun determineVersion(): String {
    val branch = branch()
    var version = buildId() ?: return "$branch-SNAPSHOT"
    if (branch != "master") {
        version += "+$branch"
    }
    return version
}
fun buildId(): String? = project.properties["BUILD_ID"]?.toString()
fun branch(): String = project.properties["branch"]?.toString() ?: try {
    val stdout = java.io.ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim()
} catch (e: Throwable) {
    "unknown"
}
