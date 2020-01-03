package club.sk1er.vigilance

import club.sk1er.vigilance.data.*
import com.electronwill.nightconfig.core.file.FileConfig
import java.io.File
import kotlin.concurrent.fixedRateTimer

abstract class Vigilant(file: File) {
    private val properties: MutableList<PropertyData> =
            this::class.java.declaredFields
                    .filter { it.isAnnotationPresent(Property::class.java) }
                    .map {
                        PropertyData(
                                it.getAnnotation(Property::class.java),
                                it.apply { it.isAccessible = true },
                                this
                        )
                    }.toMutableList()

    private val fileConfig = FileConfig.of(file)
    private var dirty = false

    init {
        readData()

        fixedRateTimer(period = 10 * 1000) {
            if (dirty) {
                writeData()
                dirty = false
            }
        }
    }

    fun registerProperty(prop: PropertyData) {
        properties.add(prop);
    }

    fun getCategories(): List<Category> {
        val groupedByCategory = properties.groupBy { it.property.category }
        return groupedByCategory.map { Category(it.key, it.value.splitBySubcategory()) }
    }

    fun markDirty() {
        dirty = true
    }

    fun preload() {}


    private fun readData() {
        fileConfig.load()

        properties.forEach {
            val fullPath = it.property.fullPropertyPath()

            val oldValue: Any = fileConfig.get(fullPath) ?: run {
                markDirty()
                return@forEach
            }

            it.setValue(oldValue)
        }
    }

    private fun writeData() {
        properties.forEach {
            val fullPath = it.property.fullPropertyPath()

            fileConfig.set(fullPath, it.getValue())
        }

        fileConfig.save()
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