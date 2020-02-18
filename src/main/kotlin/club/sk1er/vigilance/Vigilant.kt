package club.sk1er.vigilance

import club.sk1er.vigilance.data.*
import club.sk1er.vigilance.gui.SettingsGui
import com.electronwill.nightconfig.core.file.FileConfig
import java.io.File
import kotlin.concurrent.fixedRateTimer
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

abstract class Vigilant(file: File) {
    private val properties: MutableList<PropertyData> =
            this::class.java.declaredFields
                    .filter { it.isAnnotationPresent(Property::class.java) }
                    .map {
                        PropertyData.fromField(
                            it.getAnnotation(Property::class.java),
                            it.apply { it.isAccessible = true },
                            this
                        )
                    }.toMutableList()

    private val fileConfig = FileConfig.of(file)
    private var dirty = false

    fun initialize() {
        readData()

        fixedRateTimer(period = 10 * 1000) { writeData() }

        Runtime.getRuntime().addShutdownHook(Thread { writeData() })
    }

    fun gui() = SettingsGui(this)

    fun registerProperty(prop: PropertyData) {
        val fullPath = prop.property.fullPropertyPath()

        val oldValue: Any? = fileConfig.get(fullPath) ?: prop.getAsAny()

        prop.setValue(oldValue)

        properties.add(prop);
    }

    fun registerListener(field: KProperty<*>, listener: (Any?) -> Unit) {
        properties
                .firstOrNull { it.value is FieldBackedPropertyValue && it.value.field == field.javaField }
                ?.action = listener
    }

    fun getCategories(): List<Category> {
        val groupedByCategory = properties.groupBy { it.property.category }
        return groupedByCategory.map { Category(it.key, it.value.splitBySubcategory()) }
    }

    fun getCategoryFromSearch(term: String): Category {
        val sorted = properties
                .sortedBy { it.property.subcategory }
                .filter { !it.property.hidden && (it.property.name.contains(term, ignoreCase = true) || it.property.description.contains(term, ignoreCase = true)) }

        return Category("", sorted.splitBySubcategory())
    }

    fun markDirty() {
        dirty = true
    }

    fun preload() {}

    private fun readData() {
        fileConfig.load()

        properties.forEach {
            val fullPath = it.property.fullPropertyPath()

            val oldValue: Any? = fileConfig.get(fullPath) ?: it.getAsAny()

            it.setValue(oldValue)
        }
    }

    fun writeData() {
        if (!dirty) return

        properties.forEach {
            val fullPath = it.property.fullPropertyPath()

            fileConfig.set(fullPath, it.getValue())
        }

        fileConfig.save()

        dirty = false
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