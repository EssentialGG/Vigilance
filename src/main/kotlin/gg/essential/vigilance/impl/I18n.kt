package gg.essential.vigilance.impl

import gg.essential.universal.UI18n
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object I18n {
    fun format(key: String): String = UI18n.i18n(key)
}