package gg.essential.vigilance.data

import gg.essential.elementa.state.State
import gg.essential.vigilance.Vigilant
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KMutableProperty0

@Deprecated("Use Elementa's State instead", ReplaceWith("State", "gg.essential.elementa.state.State"))
abstract class PropertyValue {
    @Deprecated("Use PropertyData#initialized instead.")
    var initialized: Boolean = false

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

class StateBackedPropertyValue(private val state: State<*>) : PropertyValue() {
    override fun getValue(instance: Vigilant): Any? {
        return state.get()
    }

    override fun setValue(value: Any?, instance: Vigilant) {
        (state as? State<Any?>)?.set(value)
    }
}

@Deprecated("Callable property values have been deprecated. Please use PropertyData#action instead.")
abstract class CallablePropertyValue : PropertyValue() {
    override val writeDataToFile = false

    override fun getValue(instance: Vigilant): Nothing = throw IllegalStateException()

    override fun setValue(value: Any?, instance: Vigilant): Nothing = throw IllegalStateException()

    abstract operator fun invoke(instance: Vigilant)
}

class MethodBackedPropertyValue(internal val method: Method) : CallablePropertyValue() {
    override fun invoke(instance: Vigilant) {
        method.invoke(instance)
    }
}

class KFunctionBackedPropertyValue(private val kFunction: () -> Unit) : CallablePropertyValue() {
    override fun invoke(instance: Vigilant) {
        kFunction()
    }
}

class PropertyValueBackedState<T>(private val propertyValue: PropertyValue, private val instance: Vigilant) : State<T>() {
    override fun get(): T =
        propertyValue.getValue(instance) as T

    override fun set(value: T) {
        propertyValue.setValue(propertyValue, instance)
        super.set(value)
    }
}
