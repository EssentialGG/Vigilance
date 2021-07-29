package gg.essential.vigilance

import gg.essential.elementa.effects.StencilEffect

object Vigilance {
    private var initialized = false

    @JvmStatic
    fun initialize() {
        if (initialized)
            return

        initialized = true
        StencilEffect.enableStencil()
    }
}
