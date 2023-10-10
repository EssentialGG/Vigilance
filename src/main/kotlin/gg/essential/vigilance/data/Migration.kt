package gg.essential.vigilance.data

import gg.essential.vigilance.Vigilant

/** See [Vigilant.migrations]. */
fun interface Migration {
    fun apply(config: MutableMap<String, Any?>)
}
