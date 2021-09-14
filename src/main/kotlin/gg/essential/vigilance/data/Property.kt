package gg.essential.vigilance.data

import net.minecraft.client.resources.I18n
import java.util.*

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
annotation class Property(
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
    val searchTags: Array<String> = []
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

class PropertyAttributesExt @JvmOverloads constructor(
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
    val searchTags: List<String> = listOf()
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
                try {
                    property.searchTags
                } catch (e: AbstractMethodError) {
                    emptyArray()
                }.toList()
            )
        }
    }
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

private fun String.toPropertyPath() = this.lowercase(Locale.ENGLISH).replace(" ", "_")
