package club.sk1er.vigilance.data

class Category(val name: String, val items: List<CategoryItem>) {
    override fun toString(): String {
        return "Category \"$name\"\n ${items.joinToString(separator = "\n") { "\t$it" }}"
    }
}

sealed class CategoryItem

class DividerItem(val name: String) : CategoryItem() {
    override fun toString(): String {
        return "Divider \"$name\""
    }
}

class PropertyItem(val data: PropertyData) : CategoryItem() {
    override fun toString(): String {
        return "${data.property.type} \"${data.property.name}\""
    }
}