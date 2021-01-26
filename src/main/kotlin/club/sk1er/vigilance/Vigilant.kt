package club.sk1er.vigilance

import club.sk1er.mods.core.universal.UChat
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
    /*
    TODO: Fix this in production
    private val miscData = (this::class as KClass<Vigilant>).memberProperties
        .filter { it.findAnnotation<Data>() != null }
        .map { it.apply { isAccessible = true } as KMutableProperty1<Vigilant, Any?> to it.findAnnotation<Data>()!! }
    */

    private val fileConfig = FileConfig.of(file)
    private val categoryDescription = mutableMapOf<String, CategoryDescription>()
    private var dirty = false
    private var hasError = false

    init {
        try {
            propertyCollector.initialize(this)
        } catch (e: Throwable) {
            e.printStackTrace()
            hasError = true
        }
    }

    fun initialize() {
        Vigilance.initialize()

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

    fun gui(): SettingsGui? {
        return if (hasError) {
            UChat.chat("&c[Vigilance] Error while creating config screen; check your logs for more information")
            null
        } else SettingsGui(this)
    }

    fun registerProperty(prop: PropertyData) {
        val fullPath = prop.attributes.fullPropertyPath()

        val oldValue: Any? = fileConfig.get(fullPath) ?: prop.getAsAny()

        prop.setValue(oldValue)

        propertyCollector.addProperty(prop)
    }

    fun <T> registerListener(property: KProperty<T>, listener: (T) -> Unit) {
        registerListener<T>(property.javaField!!, Consumer { listener(it) })
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> registerListener(field: Field, listener: Consumer<T>) {
        propertyCollector
            .getProperties()
            .firstOrNull { it.value is FieldBackedPropertyValue && it.value.field == field }!!
            .action = { obj -> listener.accept(obj as T) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> registerListener(propertyName: String, listener: Consumer<T>) {
        propertyCollector.getProperties()
            .firstOrNull { it.value is FieldBackedPropertyValue && it.value.field.name == propertyName }!!
            .action = { obj -> listener.accept(obj as T) }
    }

    fun setCategoryDescription(category: String, description: String) {
        val current = categoryDescription[category]
        if (current != null) {
            current.description = description
        } else {
            categoryDescription[category] = CategoryDescription(description)
        }
    }

    fun setSubcategoryDescription(category: String, subcategory: String, description: String) {
        val current = categoryDescription[category]
        if (current != null) {
            current.subcategoryDescriptions[subcategory] = description
        } else {
            categoryDescription[category] = CategoryDescription(null, mutableMapOf(subcategory to description))
        }
    }

    fun getCategories(): List<Category> {
        return propertyCollector.getProperties()
            .filter { !it.attributes.hidden }
            .groupBy { it.attributes.category }
            .map { Category(it.key, it.value.splitBySubcategory(), categoryDescription[it.key]?.description) }
    }

    fun getCategoryFromSearch(term: String): Category {
        val sorted = propertyCollector
            .getProperties()
            .sortedBy { it.attributes.subcategory }
            .filter {
                !it.attributes.hidden && (it.attributes.name.contains(term, ignoreCase = true) || it.attributes.description
                    .contains(term, ignoreCase = true))
            }

        return Category("", sorted.splitBySubcategory(), null)
    }

    fun markDirty() {
        dirty = true
    }

    fun preload() {}

    private fun readData() {
        fileConfig.load()

        propertyCollector.getProperties().filter { it.value.writeDataToFile }.forEach {
            val fullPath = it.attributes.fullPropertyPath()

            var oldValue: Any? = fileConfig.get(fullPath)

            if (it.attributes.type == PropertyType.COLOR) {
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

        propertyCollector.getProperties().filter { it.value.writeDataToFile }.forEach {
            val fullPath = it.attributes.fullPropertyPath()

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
        val sorted = this.sortedBy { it.attributes.subcategory }.map { PropertyItem(it) }
        val withSubcategory = mutableListOf<CategoryItem>()

        var currentSubcategory = ""
        for (item in sorted) {
            if (item.data.attributes.subcategory != currentSubcategory) {
                currentSubcategory = item.data.attributes.subcategory
                val subcategoryInfo = categoryDescription[item.data.attributes.category]?.subcategoryDescriptions?.get(currentSubcategory)
                withSubcategory.add(DividerItem(currentSubcategory, subcategoryInfo))
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

    data class CategoryDescription(
        var description: String?,
        val subcategoryDescriptions: MutableMap<String, String> = mutableMapOf()
    )

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
            value: PropertyValue,
            type: PropertyType,
            name: String,
            description: String = "",
            min: Int = 0,
            max: Int = 0,
            minF: Float = 0f,
            maxF: Float = 0f,
            decimalPlaces: Int = 1,
            options: List<String> = listOf(),
            allowAlpha: Boolean = true,
            placeholder: String = "",
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((T) -> Unit)? = null
        ) {
            val data = PropertyData(
                PropertyAttributes(
                    type,
                    name,
                    category,
                    subcategory,
                    description,
                    min,
                    max,
                    minF,
                    maxF,
                    decimalPlaces,
                    options,
                    allowAlpha,
                    placeholder,
                    triggerActionOnInitialization,
                    hidden
                ),
                value,
                instance
            )

            if (action != null) {
                data.action = { action(it as T) }
            }

            properties.add(data)
        }

        fun <T> property(
            field: KMutableProperty0<T>,
            type: PropertyType,
            name: String = field.name,
            description: String = "",
            min: Int = 0,
            max: Int = 0,
            minF: Float = 0f,
            maxF: Float = 0f,
            decimalPlaces: Int = 1,
            options: List<String> = listOf(),
            allowAlpha: Boolean = true,
            placeholder: String = "",
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((T) -> Unit)? = null
        ) {
            property(
                KPropertyBackedPropertyValue(field),
                type,
                name,
                description,
                min,
                max,
                minF,
                maxF,
                decimalPlaces,
                options,
                allowAlpha,
                placeholder,
                triggerActionOnInitialization,
                hidden,
                action
            )
        }

        fun checkbox(
            field: KMutableProperty0<Boolean>,
            name: String = field.name,
            description: String = "",
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((Boolean) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.CHECKBOX,
                name,
                description,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun switch(
            field: KMutableProperty0<Boolean>,
            name: String = field.name,
            description: String = "",
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((Boolean) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.SWITCH,
                name,
                description,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun text(
            field: KMutableProperty0<String>,
            name: String = field.name,
            description: String = "",
            placeholder: String = "",
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((String) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.TEXT,
                name,
                description,
                placeholder = placeholder,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun paragraph(
            field: KMutableProperty0<String>,
            name: String = field.name,
            description: String = "",
            placeholder: String = "",
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((String) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.PARAGRAPH,
                name,
                description,
                placeholder = placeholder,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun percentSlider(
            field: KMutableProperty0<Float>,
            name: String = field.name,
            description: String = "",
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((Float) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.PERCENT_SLIDER,
                name,
                description,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun slider(
            field: KMutableProperty0<Int>,
            name: String = field.name,
            description: String = "",
            min: Int = 0,
            max: Int = 0,
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((Int) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.SLIDER,
                name,
                description,
                min,
                max,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun decimalSlider(
            field: KMutableProperty0<Float>,
            name: String = field.name,
            description: String = "",
            min: Float = 0f,
            max: Float = 0f,
            decimalPlaces: Int = 1,
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((Float) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.DECIMAL_SLIDER,
                name,
                description,
                minF = min,
                maxF = max,
                decimalPlaces = decimalPlaces,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun number(
            field: KMutableProperty0<Int>,
            name: String = field.name,
            description: String = "",
            min: Int = 0,
            max: Int = 0,
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((Int) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.NUMBER,
                name,
                description,
                min,
                max,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun color(
            field: KMutableProperty0<Color>,
            name: String = field.name,
            description: String = "",
            allowAlpha: Boolean = true,
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((Color) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.COLOR,
                name,
                description,
                allowAlpha = allowAlpha,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun selector(
            field: KMutableProperty0<Int>,
            name: String = field.name,
            description: String = "",
            options: List<String> = listOf(),
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((Int) -> Unit)? = null
        ) {
            property(
                field,
                PropertyType.SELECTOR,
                name,
                description,
                options = options,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
            )
        }

        fun button(
            name: String,
            description: String = "",
            buttonText: String = "",
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: (() -> Unit)
        ) {
            property<Nothing>(
                KFunctionBackedPropertyValue(action),
                PropertyType.BUTTON,
                name,
                description,
                placeholder = buttonText,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = null
            )
        }
    }
}
