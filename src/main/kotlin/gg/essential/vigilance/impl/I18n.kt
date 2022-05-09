package gg.essential.vigilance.impl

import gg.essential.vigilance.impl.Platform.Companion.platform
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object I18n {
    fun format(key: String): String = platform.i18n(key)
}