package gg.essential.vigilance.example

import gg.essential.universal.UScreen
import gg.essential.universal.standalone.runUniversalCraft

fun main() = runUniversalCraft("Vigilance Example", 854, 480) { window ->
    UScreen.displayScreen(ExampleConfig.gui())
    window.renderScreenUntilClosed()
}
