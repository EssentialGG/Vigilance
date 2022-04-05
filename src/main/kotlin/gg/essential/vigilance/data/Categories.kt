package gg.essential.vigilance.data

import gg.essential.vigilance.gui.*
import gg.essential.vigilance.gui.settings.*

class Category(val name: String, val items: List<CategoryItem>, val description: String?) {
    override fun toString(): String {
        return "Category \"$name\"\n ${items.joinToString(separator = "\n") { "\t$it" }}"
    }
}

sealed class CategoryItem {
    abstract fun toSettingsObject(): Setting?
}

class DividerItem(val name: String, private val description: String?) : CategoryItem() {
    override fun toSettingsObject(): Setting {
        return Divider(name, description)
    }

    override fun toString(): String {
        return "Divider \"$name\""
    }
}

class PropertyItem(val data: PropertyData, val subcategory: String) : CategoryItem() {
    override fun toSettingsObject(): Setting {
        val component = when (data.getDataType()) {
            PropertyType.SWITCH -> SwitchComponent(data.getAsBoolean())
            PropertyType.CHECKBOX -> CheckboxComponent(data.getAsBoolean())
            PropertyType.PERCENT_SLIDER -> PercentSliderComponent(data.getValue())
            PropertyType.SLIDER -> SliderComponent(data.getValue(), data.attributesExt.min, data.attributesExt.max)
            PropertyType.DECIMAL_SLIDER -> DecimalSliderComponent(
                data.getValue(),
                data.attributesExt.minF,
                data.attributesExt.maxF,
                data.attributesExt.decimalPlaces
            )
            PropertyType.NUMBER -> NumberComponent(
                data.getValue(),
                data.attributesExt.min,
                data.attributesExt.max,
                data.attributesExt.increment
            )
            PropertyType.SELECTOR -> SelectorComponent(data.getValue(), data.attributesExt.options.toList())
            PropertyType.COLOR -> ColorComponent(data.getValue(), data.attributesExt.allowAlpha)
            PropertyType.TEXT -> TextComponent(
                data.getValue(),
                data.attributesExt.placeholder,
                false,
                data.attributesExt.protected
            )
            PropertyType.PARAGRAPH -> TextComponent(
                data.getValue(),
                data.attributesExt.placeholder,
                wrap = true,
                protected = false
            )
            PropertyType.BUTTON -> ButtonComponent(data.attributesExt.placeholder, data)
            PropertyType.CUSTOM -> {
                val propertyInfoClass = data.attributesExt.customPropertyInfo
                propertyInfoClass
                    .constructors
                    .find { it.parameters.isEmpty() }
                    ?.call()
                    ?.createSettingComponent(data.getValue())
                    ?: error("PropertyInfo class $propertyInfoClass has no zero-arg constructor!")
            }
        }

        return DataBackedSetting(data, component)
    }

    override fun toString(): String {
        return "${data.attributesExt.type} \"${data.attributesExt.name}\""
    }
}
