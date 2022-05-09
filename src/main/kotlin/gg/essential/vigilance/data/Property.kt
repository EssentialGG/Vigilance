package gg.essential.vigilance.data

import gg.essential.vigilance.impl.I18n
import java.util.*
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
annotation class Property(
    val type: PropertyType,
    val name: String,
    val i18nName: String = "",
    val category: String,
    val i18nCategory: String = "",
    val subcategory: String = "",
    val i18nSubcategory: String = "",
    val description: String = "",
    /**
     * Reserved for [PropertyType.SLIDER] and [PropertyType.NUMBER]
     */
    val min: Int = 0,
    /**
     * Reserved for [PropertyType.SLIDER] and [PropertyType.NUMBER]
     */
    val max: Int = 0,

    /**
     * Reserved for [PropertyType.DECIMAL_SLIDER]
     */
    val minF: Float = 0f,
    /**
     * Reserved for [PropertyType.DECIMAL_SLIDER]
     */
    val maxF: Float = 0f,

    /**
     * Reserved for [PropertyType.DECIMAL_SLIDER]
     */
    val decimalPlaces: Int = 1,

    /**
     * Reserved for [PropertyType.NUMBER]
     */
    val increment: Int = 1,

    /**
     * Reserved for [PropertyType.SELECTOR]
     */
    val options: Array<String> = [],
    /**
     * Reserved for [PropertyType.COLOR]
     */
    val allowAlpha: Boolean = true,
    /**
     * Reserved for [PropertyType.TEXT] and [PropertyType.PARAGRAPH]
     */
    val placeholder: String = "",
    /**
     * Reserved for [PropertyType.TEXT]
     */
    val protectedText: Boolean = false,
    /**
     * Whether or not this property's action should be triggered with it's initial
     * value when the configuration is initialized. Useful when you only want to
     * react to changes by the user.
     */
    val triggerActionOnInitialization: Boolean = true,

    val hidden: Boolean = false,

    /**
     * Extra search tags to help lost users
     */
    val searchTags: Array<String> = [],

    /**
     * Reserved for [PropertyType.CUSTOM]. Denotes a class which defines the custom property's behaviour and UI.
     */
    val customPropertyInfo: KClass<out PropertyInfo> = Nothing::class,
)

@Deprecated("Use PropertyAttributesExt Instead", ReplaceWith("PropertyAttributesExt", "gg.essential.vigilance.data.Property.PropertyAttributesExt"))
data class PropertyAttributes(
    val type: PropertyType,
    val name: String,
    val category: String,
    val subcategory: String = "",
    val description: String = "",
    /**
     * Reserved for [PropertyType.SLIDER] and [PropertyType.NUMBER]
     */
    val min: Int = 0,
    /**
     * Reserved for [PropertyType.SLIDER] and [PropertyType.NUMBER]
     */
    val max: Int = 0,
    /**
     * Reserved for [PropertyType.DECIMAL_SLIDER]
     */
    val minF: Float = 0f,
    /**
     * Reserved for [PropertyType.DECIMAL_SLIDER]
     */
    val maxF: Float = 0f,
    /**
     * Reserved for [PropertyType.DECIMAL_SLIDER]
     */
    val decimalPlaces: Int = 1,
    /**
     * Reserved for [PropertyType.NUMBER]
     */
    val increment: Int = 1,
    /**
     * Reserved for [PropertyType.SELECTOR]
     */
    val options: List<String> = listOf(),
    /**
     * Reserved for [PropertyType.COLOR]
     */
    val allowAlpha: Boolean = true,
    /**
     * Reserved for [PropertyType.TEXT], [PropertyType.PARAGRAPH], and [PropertyType.BUTTON]
     */
    val placeholder: String = "",
    /**
     * Reserved for [PropertyType.TEXT]
     */
    val protected: Boolean = false,
    /**
     * Whether or not this property's action should be triggered with it's initial
     * value when the configuration is initialized. Useful when you only want to
     * react to changes by the user.
     */
    val triggerActionOnInitialization: Boolean = true,

    var hidden: Boolean = false
) {
    companion object {
        @Deprecated("Use PropertyAttributesExt instead", ReplaceWith("PropertyAttributesExt.fromPropertyAnnotation", "gg.essential.vigilance.data.Property.PropertyAttributesExt.fromPropertyAnnotation"))
        fun fromPropertyAnnotation(property: Property): PropertyAttributes {
            return PropertyAttributes(
                property.type,
                property.name,
                property.category,
                property.subcategory,
                property.description,
                property.min,
                property.max,
                property.minF,
                property.maxF,
                property.decimalPlaces,
                property.increment,
                property.options.toList(),
                property.allowAlpha,
                property.placeholder,
                property.protectedText,
                property.triggerActionOnInitialization,
                property.hidden
            )
        }
    }
}

class PropertyAttributesExt(
    val type: PropertyType,
    val name: String,
    val category: String,
    val subcategory: String = "",
    val description: String = "",
    /**
     * Reserved for [PropertyType.SLIDER] and [PropertyType.NUMBER]
     */
    val min: Int = 0,
    /**
     * Reserved for [PropertyType.SLIDER] and [PropertyType.NUMBER]
     */
    val max: Int = 0,
    /**
     * Reserved for [PropertyType.DECIMAL_SLIDER]
     */
    val minF: Float = 0f,
    /**
     * Reserved for [PropertyType.DECIMAL_SLIDER]
     */
    val maxF: Float = 0f,
    /**
     * Reserved for [PropertyType.DECIMAL_SLIDER]
     */
    val decimalPlaces: Int = 1,
    /**
     * Reserved for [PropertyType.NUMBER]
     */
    val increment: Int = 1,
    /**
     * Reserved for [PropertyType.SELECTOR]
     */
    val options: List<String> = listOf(),
    /**
     * Reserved for [PropertyType.COLOR]
     */
    val allowAlpha: Boolean = true,
    /**
     * Reserved for [PropertyType.TEXT], [PropertyType.PARAGRAPH], and [PropertyType.BUTTON]
     */
    val placeholder: String = "",
    /**
     * Reserved for [PropertyType.TEXT]
     */
    val protected: Boolean = false,
    /**
     * Whether or not this property's action should be triggered with it's initial
     * value when the configuration is initialized. Useful when you only want to
     * react to changes by the user.
     */
    val triggerActionOnInitialization: Boolean = true,

    var hidden: Boolean = false,

    /**
     * Search tags to help lost users
     */
    val searchTags: List<String> = listOf(),

    private val i18nName: String = name,
    private val i18nCategory: String = category,
    private val i18nSubcategory: String = subcategory,

    /**
     * Reserved for [PropertyType.CUSTOM]. Denotes a class which defines the custom property's behaviour and UI.
     */
    val customPropertyInfo: Class<out PropertyInfo> = Nothing::class.java,
) {
    constructor(propertyAttributes: PropertyAttributes) : this(
        propertyAttributes.type,
        propertyAttributes.name,
        propertyAttributes.category,
        propertyAttributes.subcategory,
        propertyAttributes.description,
        propertyAttributes.min,
        propertyAttributes.max,
        propertyAttributes.minF,
        propertyAttributes.maxF,
        propertyAttributes.decimalPlaces,
        propertyAttributes.increment,
        propertyAttributes.options.toList(),
        propertyAttributes.allowAlpha,
        propertyAttributes.placeholder,
        propertyAttributes.protected,
        propertyAttributes.triggerActionOnInitialization,
        propertyAttributes.hidden,
        listOf()
    )

    @Deprecated("", level = DeprecationLevel.HIDDEN)
    @JvmOverloads
    constructor(
        type: PropertyType,
        name: String,
        category: String,
        subcategory: String = "",
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
        protected: Boolean = false,
        triggerActionOnInitialization: Boolean = true,
        hidden: Boolean = false,
        searchTags: List<String> = listOf(),
    ) : this(type, name, category, subcategory, description, min, max, minF, maxF, decimalPlaces, increment, options, allowAlpha, placeholder, protected, triggerActionOnInitialization, hidden, searchTags, name)

    @Deprecated("", level = DeprecationLevel.HIDDEN)
    constructor(
        type: PropertyType,
        name: String,
        category: String,
        subcategory: String = "",
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
        protected: Boolean = false,
        triggerActionOnInitialization: Boolean = true,
        hidden: Boolean = false,
        searchTags: List<String> = listOf(),
        i18nName: String = name,
        i18nCategory: String = category,
        i18nSubcategory: String = subcategory,
    ) : this(type, name, category, subcategory, description, min, max, minF, maxF, decimalPlaces, increment, options, allowAlpha, placeholder, protected, triggerActionOnInitialization, hidden, searchTags, i18nName, i18nCategory, i18nSubcategory)


    internal val localizedName get() = I18n.format(i18nName)

    internal val localizedCategory get() = I18n.format(i18nCategory)

    internal val localizedSubcategory get() = I18n.format(i18nSubcategory)

    internal val localizedDescription get() = I18n.format(description)

    internal val localizedSearchTags get() = searchTags.map { I18n.format(it) }

    companion object {
        fun fromPropertyAnnotation(property: Property): PropertyAttributesExt {
            return PropertyAttributesExt(
                property.type,
                property.name,
                property.category,
                property.subcategory,
                property.description,
                property.min,
                property.max,
                property.minF,
                property.maxF,
                property.decimalPlaces,
                property.increment,
                property.options.toList(),
                property.allowAlpha,
                property.placeholder,
                property.protectedText,
                property.triggerActionOnInitialization,
                property.hidden,
                property.safeGet(emptyArray()) { searchTags }.toList(),
                property.safeGet("") { i18nName }.ifEmpty { property.name },
                property.safeGet("") { i18nCategory }.ifEmpty { property.category },
                property.safeGet("") { i18nSubcategory }.ifEmpty { property.subcategory },
                property.safeGet(Nothing::class.java) { customPropertyInfo.java },
            )
        }
    }
}

private inline fun <T> Property.safeGet(default: T, getter: Property.() -> T) =
    try {
        getter()
    } catch (e: AbstractMethodError) {
        default
    }

fun PropertyAttributesExt.toPropertyAttributes(): PropertyAttributes =
    PropertyAttributes(
        this.type,
        this.name,
        this.category,
        this.subcategory,
        this.description,
        this.min,
        this.max,
        this.minF,
        this.maxF,
        this.decimalPlaces,
        this.increment,
        this.options.toList(),
        this.allowAlpha,
        this.placeholder,
        this.protected,
        this.triggerActionOnInitialization,
        this.hidden
    )

fun PropertyAttributesExt.fullPropertyPath(): String {
    val sb = StringBuilder()

    sb.append(category.toPropertyPath()).append(".")

    if (subcategory != "") {
        sb.append(subcategory.toPropertyPath()).append(".")
    }

    sb.append(name.toPropertyPath())

    return sb.toString()
}

@Deprecated("", ReplaceWith("PropertyAttributesExt(this).fullPropertyPath()", "gg.essential.vigilance.data.PropertyAttributesExt"))
fun PropertyAttributes.fullPropertyPath(): String = PropertyAttributesExt(this).fullPropertyPath()

private fun String.toPropertyPath() = this.lowercase(Locale.ENGLISH).replace(" ", "_")
