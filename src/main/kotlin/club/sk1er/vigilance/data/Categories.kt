package club.sk1er.vigilance.data

import club.sk1er.vigilance.gui.*
import club.sk1er.vigilance.gui.settings.*

class Category(val name: String, val items: List<CategoryItem>) {
    override fun toString(): String {
        return "Category \"$name\"\n ${items.joinToString(separator = "\n") { "\t$it" }}"
    }
}

sealed class CategoryItem {
    abstract fun toSettingsObject(): Setting?
}

class DividerItem(val name: String) : CategoryItem() {
    override fun toSettingsObject(): Setting? {
        return Divider(name)
    }

    override fun toString(): String {
        return "Divider \"$name\""
    }
}

class PropertyItem(val data: PropertyData) : CategoryItem() {
    override fun toSettingsObject(): Setting? {
        val component = when (data.getDataType()) {
            PropertyType.SWITCH -> SwitchComponent(data.getAsBoolean())
            PropertyType.PERCENT_SLIDER -> PercentSliderComponent(data.getValue())
            PropertyType.SLIDER -> SliderComponent(data.getValue(), data.property.min, data.property.max)
            PropertyType.NUMBER -> NumberComponent(data.getValue(), data.property.min, data.property.max)
            PropertyType.SELECTOR -> SelectorComponent(data.getValue(), data.property.options.toList())
            PropertyType.COLOR -> ColorComponent(data.getValue(), data.property.allowAlpha)
            PropertyType.TEXT -> TextComponent(data.getValue(), data.property.placeholder, false)
            PropertyType.PARAGRAPH -> TextComponent(data.getValue(), data.property.placeholder, true)
        } ?: return null

        return DataBackedSetting(data, component)
    }

    override fun toString(): String {
        return "${data.property.type} \"${data.property.name}\""
    }
}