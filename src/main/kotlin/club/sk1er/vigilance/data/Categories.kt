package club.sk1er.vigilance.data

import club.sk1er.vigilance.gui.*

class Category(val name: String, val items: List<CategoryItem>) {
    override fun toString(): String {
        return "Category \"$name\"\n ${items.joinToString(separator = "\n") { "\t$it" }}"
    }
}

sealed class CategoryItem {
    abstract fun toSettingsObject(): SettingObject
}

class DividerItem(val name: String) : CategoryItem() {
    override fun toSettingsObject(): SettingObject {
        return SettingDivider(name)
    }

    override fun toString(): String {
        return "Divider \"$name\""
    }
}

class PropertyItem(val data: PropertyData) : CategoryItem() {
    override fun toSettingsObject(): SettingObject {
        return when (data.getDataType()) {
            PropertyType.SWITCH -> ToggleSetting(data.property.name, data.property.description, data)
            PropertyType.TEXT -> TODO()
            PropertyType.PARAGRAPH -> TODO()
            PropertyType.SLIDER -> SliderSetting(data.property.name, data.property.description, data)
            PropertyType.NUMBER -> TODO()
            PropertyType.COLOR -> TODO()
            PropertyType.SELECTOR -> SelectSetting(data.property.name, data.property.description, 0, data.property.options.toList())
        }
    }

    override fun toString(): String {
        return "${data.property.type} \"${data.property.name}\""
    }
}