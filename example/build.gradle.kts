plugins {
    kotlin("jvm")
    application
    id("gg.essential.defaults.repo")
}

dependencies {
    implementation(libs.universalcraft.standalone)
    implementation(project(":"))
}

kotlin.jvmToolchain(8)

application {
    mainClass.set("gg.essential.vigilance.example.MainKt")
}
