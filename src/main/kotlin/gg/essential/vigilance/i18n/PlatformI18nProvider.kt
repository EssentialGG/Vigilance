package gg.essential.vigilance.i18n

import gg.essential.vigilance.impl.Platform.Companion.platform

object PlatformI18nProvider: I18nProvider {
    override fun translate(key: String): String = platform.i18n(key)
}