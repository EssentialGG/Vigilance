package gg.essential.vigilance.impl

import org.jetbrains.annotations.ApiStatus
import java.util.*

@ApiStatus.Internal
interface Platform {

    fun i18n(key: String): String

    fun isAllowedInChat(char: Char): Boolean

    @ApiStatus.Internal
    companion object {
        internal val platform: Platform =
            ServiceLoader.load(Platform::class.java, Platform::class.java.classLoader).iterator().next()
    }
}