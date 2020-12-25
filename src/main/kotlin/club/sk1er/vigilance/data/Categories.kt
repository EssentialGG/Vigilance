package club.sk1er.vigilance.data

import club.sk1er.vigilance.gui.*
import club.sk1er.vigilance.gui.settings.*

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

class PropertyItem(val data: PropertyData) : CategoryItem() {
    override fun toSettingsObject(): Setting {
        val component = when (data.getDataType()) {
            PropertyType.SWITCH -> SwitchComponent(data)
            PropertyType.PERCENT_SLIDER -> PercentSliderComponent(data)
            PropertyType.SLIDER -> SliderComponent(data)
            PropertyType.NUMBER -> NumberComponent(data)
            PropertyType.SELECTOR -> SelectorComponent(data)
            PropertyType.COLOR -> ColorComponent(data)
            PropertyType.TEXT -> TextComponent(data, false)
            PropertyType.PARAGRAPH -> TextComponent(data, true)
            PropertyType.BUTTON -> ButtonComponent(data)
        }

        return DataBackedSetting(data, component)
    }

    override fun toString(): String {
        return "${data.attributes.type} \"${data.attributes.name}\""
    }
}
