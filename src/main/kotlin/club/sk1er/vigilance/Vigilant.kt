package club.sk1er.vigilance

import club.sk1er.vigilance.data.*
import club.sk1er.vigilance.gui.SettingsGui
import com.electronwill.nightconfig.core.file.FileConfig
import java.awt.Color
import java.io.File
import java.lang.reflect.Field
import java.util.function.Consumer
import kotlin.concurrent.fixedRateTimer
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

abstract class Vigilant @JvmOverloads constructor(
    file: File,
    private val propertyCollector: PropertyCollector = JVMAnnotationPropertyCollector()
) {
    init {
        propertyCollector.initialize(this)
    }

    /*
    TODO: Fix this in production
    private val miscData = (this::class as KClass<Vigilant>).memberProperties
        .filter { it.findAnnotation<Data>() != null }
        .map { it.apply { isAccessible = true } as KMutableProperty1<Vigilant, Any?> to it.findAnnotation<Data>()!! }
    */

    private val fileConfig = FileConfig.of(file)
    private var dirty = false

    fun initialize() {
        try {
            readData()
        } catch (e: Throwable) {
            writeData()
            println("Failed to read Vigilant config data from ${fileConfig.file.name}")
            e.printStackTrace()
        }

        fixedRateTimer(period = 30 * 1000) { writeData() }

        Runtime.getRuntime().addShutdownHook(Thread { writeData() })
    }

    fun gui() = SettingsGui(this)

    fun registerProperty(prop: PropertyData) {
        val fullPath = prop.property.fullPropertyPath()

        val oldValue: Any? = fileConfig.get(fullPath) ?: prop.getAsAny()

        prop.setValue(oldValue)

        propertyCollector.addProperty(prop)
    }

    fun <T> registerListener(property: KProperty<T>, listener: (T) -> Unit) {
        registerListener<T>(property.javaField!!, Consumer { listener(it) })
    }

    fun <T> registerListener(field: Field, listener: Consumer<T>) {
        propertyCollector
            .getProperties()
            .firstOrNull { it.value is FieldBackedPropertyValue && it.value.field == field }!!
            .action = { obj -> listener.accept(obj as T) }
    }

    fun <T> registerListener(propertyName: String, listener: Consumer<T>) {
        propertyCollector.getProperties()
            .firstOrNull { it.value is FieldBackedPropertyValue && it.value.field.name == propertyName }!!
            .action = { obj -> listener.accept(obj as T) }
    }

    fun getCategories(): List<Category> {
        return propertyCollector.getProperties()
            .filter { !it.property.hidden }
            .groupBy { it.property.category }
            .map { Category(it.key, it.value.splitBySubcategory()) }
    }

    fun getCategoryFromSearch(term: String): Category {
        val sorted = propertyCollector
            .getProperties()
            .sortedBy { it.property.subcategory }
            .filter {
                !it.property.hidden && (it.property.name.contains(term, ignoreCase = true) || it.property.description
                    .contains(term, ignoreCase = true))
            }

        return Category("", sorted.splitBySubcategory())
    }

    fun markDirty() {
        dirty = true
    }

    fun preload() {}

    private fun readData() {
        fileConfig.load()

        propertyCollector.getProperties().forEach {
            val fullPath = it.property.fullPropertyPath()

            var oldValue: Any? = fileConfig.get(fullPath)

            if (it.property.type == PropertyType.COLOR) {
                oldValue = if (oldValue is String) {
                    val split = oldValue.split(",").map(String::toInt)
                    if (split.size == 4) Color(split[1], split[2], split[3], split[0]) else null
                } else {
                    null
                }
            }

            it.setValue(oldValue ?: it.getAsAny())
        }

        // Leave until misc data is supported
        // miscData.forEach { (property, ann) ->
        //     val path = ann.prefix + "." + property.name
        //
        //     val oldValue: Any? = fileConfig.get(path) ?: property.get(this)
        //     property.set(this, oldValue)
        // }
    }

    fun writeData() {
        if (!dirty) return

        propertyCollector.getProperties().forEach {
            val fullPath = it.property.fullPropertyPath()

            var toSet = it.getAsAny()

            if (toSet is Color) {
                toSet = "${toSet.alpha},${toSet.red},${toSet.green},${toSet.blue}"
            }

            fileConfig.set(fullPath, toSet)
        }

        // Leave until misc data is supported
        // miscData.forEach { (property, ann) ->
        //     val path = ann.prefix + "." + property.name
        //
        //     fileConfig.set(path, property.get(this))
        // }

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

    // Leave until misc data is supported
    // protected fun <T> watched(initialValue: T): ReadWriteProperty<Any?, T> =
    //     object : ObservableProperty<T>(initialValue) {
    //         override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
    //             dirty = true
    //         }
    //     }

    /**
     * Property DSL
     */

    fun category(name: String, builder: CategoryPropertyBuilder.() -> Unit) {
        val categoryBuilder = CategoryPropertyBuilder(name, "", this)
        categoryBuilder.apply(builder)
        categoryBuilder.properties.forEach(propertyCollector::addProperty)
    }

    class CategoryPropertyBuilder(
        private val category: String,
        private val subcategory: String,
        private val instance: Vigilant
    ) {
        internal val properties = mutableListOf<PropertyData>()

        fun subcategory(subcategory: String, builder: CategoryPropertyBuilder.() -> Unit) {
            val categoryBuilder = CategoryPropertyBuilder(category, subcategory, instance)
            categoryBuilder.apply(builder)
            properties.addAll(categoryBuilder.properties)
        }

        fun <T> property(
            field: KMutableProperty0<T>,
            type: PropertyType,
            name: String = field.name,
            description: String = "",
            min: Int = 0,
            max: Int = 0,
            options: List<String> = listOf(),
            allowAlpha: Boolean = true,
            placeholder: String = "",
            hidden: Boolean = false
        ) {
            properties.add(PropertyData(
                PropertyAttributes(
                    type,
                    name,
                    category,
                    subcategory,
                    description,
                    min,
                    max,
                    options,
                    allowAlpha,
                    placeholder,
                    hidden
                ),
                KPropertyBackedPropertyValue(field),
                instance
            ))
        }

        fun switch(
            field: KMutableProperty0<Boolean>,
            name: String = field.name,
            description: String = "",
            hidden: Boolean = false
        ) {
            property(field, PropertyType.SWITCH, name, description, hidden = hidden)
        }

        fun text(
            field: KMutableProperty0<String>,
            name: String = field.name,
            description: String = "",
            placeholder: String = "",
            hidden: Boolean = false
        ) {
            property(field, PropertyType.TEXT, name, description, placeholder = placeholder, hidden = hidden)
        }

        fun paragraph(
            field: KMutableProperty0<String>,
            name: String = field.name,
            description: String = "",
            placeholder: String = "",
            hidden: Boolean = false
        ) {
            property(field, PropertyType.PARAGRAPH, name, description, placeholder = placeholder, hidden = hidden)
        }

        fun percentSlider(
            field: KMutableProperty0<Float>,
            name: String = field.name,
            description: String = "",
            hidden: Boolean = false
        ) {
            property(field, PropertyType.PERCENT_SLIDER, name, description, hidden = hidden)
        }

        fun slider(
            field: KMutableProperty0<Int>,
            name: String = field.name,
            description: String = "",
            min: Int = 0,
            max: Int = 0,
            hidden: Boolean = false
        ) {
            property(field, PropertyType.SLIDER, name, description, min, max, hidden = hidden)
        }

        fun number(
            field: KMutableProperty0<Int>,
            name: String = field.name,
            description: String = "",
            min: Int = 0,
            max: Int = 0,
            hidden: Boolean = false
        ) {
            property(field, PropertyType.NUMBER, name, description, min, max, hidden = hidden)
        }

        fun color(
            field: KMutableProperty0<Color>,
            name: String = field.name,
            description: String = "",
            allowAlpha: Boolean = true,
            hidden: Boolean = false
        ) {
            property(field, PropertyType.COLOR, name, description, allowAlpha = allowAlpha, hidden = hidden)
        }

        fun selector(
            field: KMutableProperty0<Int>,
            name: String = field.name,
            description: String = "",
            options: List<String> = listOf(),
            hidden: Boolean = false
        ) {
            property(field, PropertyType.SELECTOR, name, description, options = options, hidden = hidden)
        }
    }
}
