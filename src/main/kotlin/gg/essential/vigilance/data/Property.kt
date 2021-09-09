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
     * Extra search tags to help lost users
     */
    val searchTags: Array<String> = [],
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
     * Extra search tags to help lost users
     */
    val searchTags: List<String> = listOf(),
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
                I18n.format(property.name),
                I18n.format(property.category),
                I18n.format(property.subcategory),
                I18n.format(property.description),
                property.searchTags.map { I18n.format(it) },
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

        @JvmOverloads
        fun fromParams(
            type: PropertyType,
            name: String,
            category: String = "",
            subcategory: String = "",
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
            protected: Boolean = false,
            triggerActionOnInitialization: Boolean = false,
            hidden: Boolean = false
        ) : PropertyAttributes {
            return PropertyAttributes(
                type,
                I18n.format(name),
                I18n.format(category),
                I18n.format(subcategory),
                I18n.format(description),
                searchTags.map { I18n.format(it) },
                min, max,
                minF, maxF,
                decimalPlaces,
                increment,
                options,
                allowAlpha,
                placeholder,
                protected,
                triggerActionOnInitialization,
                hidden
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
