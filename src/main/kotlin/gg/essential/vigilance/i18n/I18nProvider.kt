package gg.essential.vigilance.i18n

import gg.essential.vigilance.Vigilant

/**
 * An interface that can be implemented to allow for the use of custom internationalization
 * systems. To use a custom provider, pass it to the `i18nProvider` argument of the
 * [Vigilant] constructor. The default provider, [PlatformI18nProvider], uses Minecraft's
 * internationalization system.
 */
fun interface I18nProvider {

    /**
     * Localizes a key
     * @param key the localization key
     * @return the localized string
     */
    fun translate(key: String): String

}