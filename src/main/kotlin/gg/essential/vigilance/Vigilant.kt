package gg.essential.vigilance

import com.electronwill.nightconfig.core.file.FileConfig
import gg.essential.universal.UChat
import gg.essential.vigilance.data.*
import gg.essential.vigilance.gui.SettingsGui
import net.minecraft.client.resources.I18n
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
    val guiTitle: String = "Settings",
    private val propertyCollector: PropertyCollector = JVMAnnotationPropertyCollector(),
    val sortingBehavior: SortingBehavior = SortingBehavior()
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

    /**
     * Initialise your config.
     */
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

    /**
     * Open your config's gui.
     *
     * @return config gui instance
     */
    fun gui(): SettingsGui? {
        return if (hasError) {
            UChat.chat("&c[Vigilance] Error while creating config screen; check your logs for more information")
            null
        } else SettingsGui(this)
    }

    fun registerProperty(prop: PropertyData) {
        val fullPath = prop.attributesExt.fullPropertyPath()

        val oldValue: Any? = fileConfig.get(fullPath) ?: prop.getAsAny()

        prop.setValue(oldValue)

        propertyCollector.addProperty(prop)
    }

    @Deprecated(
        message = "Due to startup performance penalties due to KReflect, we advise against using this.",
        replaceWith = ReplaceWith(
            "registerListener<T>(property.javaField!!, Consumer { listener(it) })",
            "kotlin.reflect.jvm.javaField",
            "java.util.function.Consumer"
        )
    )
    fun <T> registerListener(property: KProperty<T>, listener: (T) -> Unit) {
        registerListener<T>(property.javaField!!) { listener(it) }
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

    @Deprecated(
        message = "Due to startup performance penalties due to KReflect, we advise against using this.",
        replaceWith = ReplaceWith(
            "addDependency<T>(javaField!!, dependency.javaField!!)",
            "kotlin.reflect.jvm.javaField",
            "kotlin.reflect.jvm.javaField"
        ),
    )
    infix fun <T> KProperty<T>.dependsOn(dependency: KProperty<T>): Unit =
        addDependency(javaField!!, dependency.javaField!!)

    @Deprecated(
        message = "Due to startup performance penalties due to KReflect, we advise against using this.",
        replaceWith = ReplaceWith(
            "addDependency<T>(property.javaField!!, dependency.javaField!!)",
            "kotlin.reflect.jvm.javaField",
            "kotlin.reflect.jvm.javaField"
        ),
    )
    fun <T> addDependency(property: KProperty<T>, dependency: KProperty<T>): Unit =
        addDependency(property.javaField!!, dependency.javaField!!)

    fun addDependency(propertyName: String, dependencyName: String): Unit =
        addDependency(propertyCollector.getProperty(propertyName)!!, propertyCollector.getProperty(dependencyName)!!)

    fun addDependency(field: Field, dependency: Field): Unit =
        addDependency(propertyCollector.getProperty(field)!!, propertyCollector.getProperty(dependency)!!)

    private fun addDependency(property: PropertyData, dependency: PropertyData) {
        if (dependency.getDataType() != PropertyType.SWITCH && dependency.getDataType() != PropertyType.CHECKBOX) {
            error("Dependency must be a boolean PropertyType!")
        }

        property.dependsOn = dependency
        dependency.hasDependants = true
    }

    @Deprecated(
        message = "Due to startup performance penalties due to KReflect, we advise against using this.",
        replaceWith = ReplaceWith("hidePropertyIf(javaField!!, condition())", "kotlin.reflect.jvm.javaField"),
    )
    fun <T> KProperty<T>.hiddenIf(condition: () -> Boolean): Unit = hidePropertyIf(propertyCollector.getProperty(javaField!!)!!, condition())

    @Deprecated(
        message = "Due to startup performance penalties due to KReflect, we advise against using this.",
        replaceWith = ReplaceWith("hidePropertyIf(property.javaField!!, condition())", "kotlin.reflect.jvm.javaField"),
    )
    fun <T> hidePropertyIf(property: KProperty<T>, condition: () -> Boolean): Unit = hidePropertyIf(propertyCollector.getProperty(property.javaField!!)!!, condition())

    fun hidePropertyIf(propertyName: String, condition: () -> Boolean): Unit = hidePropertyIf(propertyCollector.getProperty(propertyName)!!, condition())

    fun hidePropertyIf(field: Field, condition: () -> Boolean): Unit = hidePropertyIf(propertyCollector.getProperty(field)!!, condition())

    // i hate java
    fun hidePropertyIf(field: Field, condition: Boolean): Unit = hidePropertyIf(propertyCollector.getProperty(field)!!, condition)

    private fun hidePropertyIf(property: PropertyData, condition: Boolean) {
        if (condition) {
            property.attributesExt.hidden = true
        }
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
            .filter { !it.attributesExt.hidden }
            .groupBy { it.attributesExt.localizedCategory to it.attributesExt.category }
            .map { Category(it.key.first, it.value.splitBySubcategory(), categoryDescription[it.key.second]?.description?.let { desc -> I18n.format(desc) }) }
            .sortedWith(sortingBehavior.getCategoryComparator())
    }

    fun getCategoryFromSearch(term: String): Category {
        val sorted = propertyCollector.getProperties()
            .filter {
                !it.attributesExt.hidden && (it.attributesExt.localizedName.contains(term, ignoreCase = true) || it.attributesExt.localizedDescription
                    .contains(term, ignoreCase = true) || it.attributesExt.localizedSearchTags.any { str -> str.contains(term, ignoreCase = true) })
            }
            .sortedWith(sortingBehavior.getPropertyComparator())

        return Category("", sorted.splitBySubcategory(), null)
    }

    fun markDirty() {
        dirty = true
    }

    fun preload() {}

    private fun readData() {
        fileConfig.load()

        propertyCollector.getProperties().filter { it.value.writeDataToFile }.forEach {
            val fullPath = it.attributesExt.fullPropertyPath()

            var oldValue: Any? = fileConfig.get(fullPath)

            if (it.attributesExt.type == PropertyType.COLOR) {
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
            val fullPath = it.attributesExt.fullPropertyPath()

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
        val items = this.groupBy { it.attributesExt.subcategory }.entries.sortedWith(sortingBehavior.getSubcategoryComparator())
        val withDividers = mutableListOf<CategoryItem>()

        items.forEachIndexed { index, (subcategoryName, listOfProperties) ->
            val subcategoryInfo = categoryDescription[listOfProperties[0].attributesExt.category]?.subcategoryDescriptions?.get(subcategoryName)?.let { I18n.format(it) }
            if (index > 0 || subcategoryName.isNotBlank() || !subcategoryInfo.isNullOrBlank()) {
                withDividers.add(DividerItem(I18n.format(subcategoryName), subcategoryInfo))
            }
            withDividers.addAll(listOfProperties.sortedWith(sortingBehavior.getPropertyComparator()).map { PropertyItem(it, it.attributesExt.localizedSubcategory) })
        }

        return withDividers
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
            searchTags: List<String> = listOf(),
            min: Int = 0,
            max: Int = 0,
            minF: Float = 0f,
            maxF: Float = 0f,
            decimalPlaces: Int = 1,
            increment: Int = 1,
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
                    increment,
                    options,
                    allowAlpha,
                    placeholder,
                    triggerActionOnInitialization,
                    hidden
                ),
                value,
                instance
            ).also { it.attributesExt.searchTags.toMutableList().addAll(searchTags) }

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
            increment: Int = 1,
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
                min = min,
                max = max,
                minF = minF,
                maxF = maxF,
                decimalPlaces = decimalPlaces,
                increment = increment,
                options = options,
                allowAlpha = allowAlpha,
                placeholder = placeholder,
                triggerActionOnInitialization = triggerActionOnInitialization,
                hidden = hidden,
                action = action
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

        @Deprecated("", level = DeprecationLevel.HIDDEN)
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
            increment: Int = 1,
            options: List<String> = listOf(),
            allowAlpha: Boolean = true,
            placeholder: String = "",
            triggerActionOnInitialization: Boolean = true,
            hidden: Boolean = false,
            action: ((T) -> Unit)? = null
        ) = property(value, type, name, description, emptyList(), min, max, minF, maxF, decimalPlaces, increment, options, allowAlpha, placeholder, triggerActionOnInitialization, hidden, action)
    }
}
