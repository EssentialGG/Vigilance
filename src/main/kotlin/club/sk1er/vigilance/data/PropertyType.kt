package club.sk1er.vigilance.data

import java.awt.Color
import java.lang.reflect.Field

enum class PropertyType {
    SWITCH() {
        override fun isFieldValid(field: Field) = field.type == Boolean::class.java
    },
    TEXT() {
        override fun isFieldValid(field: Field) = field.type == String::class.java
    },
    PARAGRAPH() {
        override fun isFieldValid(field: Field) = field.type == String::class.java
    },
    SLIDER() {
        override fun isFieldValid(field: Field) = field.type == Int::class.java
    },
    NUMBER() {
        override fun isFieldValid(field: Field) = field.type == Int::class.java
    },
    COLOR() {
        override fun isFieldValid(field: Field) = field.type == Color::class.java
    },
    DROP_DOWN() {
        override fun isFieldValid(field: Field) = field.type == String::class.java
    };

    abstract fun isFieldValid(field: Field): Boolean
}