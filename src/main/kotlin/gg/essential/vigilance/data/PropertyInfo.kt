package gg.essential.vigilance.data

import gg.essential.vigilance.gui.settings.SettingComponent

/**
 * Defines a custom property type. Registered with [PropertyType.CUSTOM] and [Property.customPropertyInfo].
 */
abstract class PropertyInfo {
    abstract fun createSettingComponent(initialValue: Any?): SettingComponent
}