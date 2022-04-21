package gg.essential.vigilance.data

import java.awt.Color
import java.lang.reflect.Field

enum class PropertyType(val type: Class<*>) {
    SWITCH(Boolean::class.java),
    CHECKBOX(Boolean::class.java),
    TEXT(String::class.java),
    PARAGRAPH(String::class.java),
    PERCENT_SLIDER(Float::class.java),
    SLIDER(Int::class.java),
    DECIMAL_SLIDER(Float::class.java),
    NUMBER(Int::class.java),
    COLOR(Color::class.java),
    SELECTOR(Int::class.java),
    BUTTON(Nothing::class.java),
    CUSTOM(Any::class.java) {
        override fun isFieldValid(field: Field): Boolean {
            val propertyAnnotation = field.getAnnotation(Property::class.java)
            return propertyAnnotation.customPropertyInfo.java != Nothing::class.java
        }
    };

    open fun isFieldValid(field: Field): Boolean = field.type == type
}
