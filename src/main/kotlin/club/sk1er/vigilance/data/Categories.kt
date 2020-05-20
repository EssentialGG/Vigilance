package club.sk1er.vigilance.data

import club.sk1er.vigilance.gui.*
import club.sk1er.vigilance.gui.settings.SwitchSetting

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
        return when (data.getDataType()) {
            PropertyType.SWITCH -> SwitchSetting(data)
//            PropertyType.TEXT -> TODO()
//            PropertyType.PARAGRAPH -> TODO()
//            PropertyType.SLIDER -> TODO()
//            PropertyType.NUMBER -> TODO()
//            PropertyType.COLOR -> TODO()
//            PropertyType.SELECTOR -> TODO()
            else -> null
        }
    }

    override fun toString(): String {
        return "${data.property.type} \"${data.property.name}\""
    }
}