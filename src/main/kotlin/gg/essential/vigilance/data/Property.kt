package gg.essential.vigilance.data

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

    val hidden: Boolean = false
)

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

fun PropertyAttributes.fullPropertyPath(): String {
    val sb = StringBuilder()

    sb.append(category.toPropertyPath()).append(".")

    if (subcategory != "") {
        sb.append(subcategory.toPropertyPath()).append(".")
    }

    sb.append(name.toPropertyPath())

    return sb.toString()
}

private fun String.toPropertyPath() = this.lowercase(Locale.ENGLISH).replace(" ", "_")
