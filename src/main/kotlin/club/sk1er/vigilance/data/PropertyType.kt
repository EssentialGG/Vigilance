package club.sk1er.vigilance.data

import java.awt.Color
import java.lang.reflect.Field

enum class PropertyType {
    SWITCH {
        override fun isFieldValid(field: Field) = field.type == Boolean::class.java
    },
    TEXT {
        override fun isFieldValid(field: Field) = field.type == String::class.java
    },
    PARAGRAPH {
        override fun isFieldValid(field: Field) = field.type == String::class.java
    },
    PERCENT_SLIDER {
        override fun isFieldValid(field: Field) = field.type == Float::class.java
    },
    SLIDER {
        override fun isFieldValid(field: Field) = field.type == Int::class.java
    },
    NUMBER {
        override fun isFieldValid(field: Field) = field.type == Int::class.java
    },
    COLOR {
        override fun isFieldValid(field: Field) = field.type == Color::class.java
    },
    SELECTOR {
        override fun isFieldValid(field: Field) = field.type == Int::class.java
    },
    BUTTON {
        override fun isFieldValid(field: Field) = false
    };

    abstract fun isFieldValid(field: Field): Boolean
}
