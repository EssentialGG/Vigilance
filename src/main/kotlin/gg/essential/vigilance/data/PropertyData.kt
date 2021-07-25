package gg.essential.vigilance.data

import gg.essential.vigilance.Vigilant
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.function.Consumer
import kotlin.reflect.KMutableProperty0

data class PropertyData(val attributes: PropertyAttributes, val value: PropertyValue, val instance: Vigilant) {
    var action: ((Any?) -> Unit)? = null
    var dependsOn: (() -> Boolean)? = null
    var hasDependants: Boolean = false

    fun getDataType() = attributes.type

    inline fun <reified T> getValue(): T {
        return value.getValue(instance) as T
    }

    fun getAsAny(): Any? = value.getValue(instance)

    fun getAsBoolean(): Boolean = getValue()

    fun <T> getAs(clazz: Class<T>) = clazz.cast(getAsAny())

    fun isHidden(): Boolean = if (dependsOn == null) false else !dependsOn!!()

    fun setValue(value: Any?) {
        if (value == null) {
            println("null value assigned to property ${attributes.name}, but Vigilance does not support null values")
            return
        }

        if (attributes.triggerActionOnInitialization || this.value.initialized)
            action?.invoke(value)

        this.value.initialized = true
        this.value.setValue(value, instance)

        instance.markDirty()
    }

    fun setCallbackConsumer(callback: Consumer<Any?>) {
        this.action = callback::accept
    }

    companion object {
        fun fromField(property: Property, field: Field, instance: Vigilant): PropertyData {
            return PropertyData(
                PropertyAttributes.fromPropertyAnnotation(property),
                FieldBackedPropertyValue(field),
                instance
            )
        }

        fun fromField(propertyAttributes: PropertyAttributes, field: Field, instance: Vigilant): PropertyData {
            return PropertyData(
                propertyAttributes,
                FieldBackedPropertyValue(field),
                instance
            )
        }

        fun fromMethod(propertyAttributes: Property, method: Method, instance: Vigilant): PropertyData {
            return PropertyData(
                PropertyAttributes.fromPropertyAnnotation(propertyAttributes),
                MethodBackedPropertyValue(method),
                instance
            )
        }

        fun withValue(property: Property, obj: Any?, instance: Vigilant): PropertyData {
            return PropertyData(
                PropertyAttributes.fromPropertyAnnotation(property),
                ValueBackedPropertyValue(obj),
                instance
            )
        }
    }
}

abstract class PropertyValue {
    var initialized = false

    open val writeDataToFile = true

    abstract fun getValue(instance: Vigilant): Any?
    abstract fun setValue(value: Any?, instance: Vigilant)
}

class FieldBackedPropertyValue(internal val field: Field) : PropertyValue() {
    override fun getValue(instance: Vigilant): Any? {
        return field.get(instance)
    }

    override fun setValue(value: Any?, instance: Vigilant) {
        if (value is Double && field.type == Float::class.java) {
            field.set(instance, value.toFloat())
        } else if (value is Float && field.type == Double::class.java) {
            field.set(instance, value.toDouble())
        } else {
            field.set(instance, value)
        }
    }
}

class ValueBackedPropertyValue(private var obj: Any?) : PropertyValue() {
    override fun getValue(instance: Vigilant): Any? {
        return obj
    }

    override fun setValue(value: Any?, instance: Vigilant) {
        obj = value
    }
}

class KPropertyBackedPropertyValue<T>(internal val property: KMutableProperty0<T>) : PropertyValue() {
    override fun getValue(instance: Vigilant) = property.get()

    override fun setValue(value: Any?, instance: Vigilant) {
        property.set(value as T)
    }
}

abstract class CallablePropertyValue : PropertyValue() {
    override val writeDataToFile = false

    override fun getValue(instance: Vigilant): Nothing = throw IllegalStateException()

    override fun setValue(value: Any?, instance: Vigilant): Nothing = throw IllegalStateException()

    abstract operator fun invoke(instance: Vigilant)
}

class MethodBackedPropertyValue(private val method: Method) : CallablePropertyValue() {
    override fun invoke(instance: Vigilant) {
        method.invoke(instance)
    }
}

class KFunctionBackedPropertyValue(private val kFunction: () -> Unit) : CallablePropertyValue() {
    override fun invoke(instance: Vigilant) {
        kFunction()
    }
}
