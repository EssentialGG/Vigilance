package club.sk1er.vigilance

import club.sk1er.vigilance.data.*

abstract class Vigilant {
    private val properties: List<PropertyData> =
        this::class.java.declaredFields
        .filter { it.isAnnotationPresent(Property::class.java) }
        .map { PropertyData(
            it.getAnnotation(Property::class.java),
            it.apply { it.isAccessible = true },
            this
        ) }

    fun getCategories(): List<Category> {
        val groupedByCategory = properties.groupBy { it.property.category }
        return groupedByCategory.map { Category(it.key, it.value.splitBySubcategory()) }
    }

    private fun List<PropertyData>.splitBySubcategory(): List<CategoryItem> {
        val sorted = this.sortedBy { it.property.subcategory }.map { PropertyItem(it) }
        val withSubcategory = mutableListOf<CategoryItem>()

        var currentSubcategory = ""
        for (item in sorted) {
            if (item.data.property.subcategory != currentSubcategory) {
                currentSubcategory = item.data.property.subcategory
                withSubcategory.add(DividerItem(currentSubcategory))
            }
            withSubcategory.add(item)
        }

        return withSubcategory
    }
}