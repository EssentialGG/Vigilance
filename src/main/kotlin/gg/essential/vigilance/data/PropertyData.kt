package gg.essential.vigilance.data

import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.vigilance.Vigilant
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.function.Consumer

data class PropertyData(@Deprecated("Replace with attributesExt", ReplaceWith("attributesExt")) val attributes: PropertyAttributes, @Deprecated("Replace with strictValue", ReplaceWith("strictValue")) val value: PropertyValue, val instance: Vigilant) {
    constructor(attributesExt: PropertyAttributesExt, value: PropertyValue, instance: Vigilant) :
        this(attributesExt.toPropertyAttributes(), value, instance) {
            this.attributesExt = attributesExt
        }

    constructor(attributesExt: PropertyAttributesExt, state: State<*>, instance: Vigilant) :
        this(attributesExt, StateBackedPropertyValue(state), instance) {
            this.state = state
        }

    constructor(attributesExt: PropertyAttributes, state: State<*>, instance: Vigilant) :
        this(attributesExt, StateBackedPropertyValue(state), instance) {
            this.state = state
        }

    var attributesExt: PropertyAttributesExt = PropertyAttributesExt(attributes)
        private set
    var state: State<*> = PropertyValueBackedState<Any?>(value, instance)
        private set

    @Deprecated("Please use the state's listeners instead.")
    var action: ((Any?) -> Unit)?
        set(value) {
            // If the action is not null, it's added as a listener to the state.
            if (value != null) this.state.onSetValue(value)
        }
        get() {
            throw IllegalAccessError("Action property is deprecated. Please use the state's listeners instead.")
        }

    var dependsOn: PropertyData? = null
    var hasDependants: Boolean = false
    var writeDataToFile: Boolean = true
        internal set

    private var initialized: Boolean
        get() = value.initialized
        set(isInitialized) {
            value.initialized = isInitialized
        }

    fun getDataType() = attributesExt.type

    inline fun <reified T> getValue(): T {
        return state.get() as T
    }

    fun getAsAny(): Any? = state.get()

    fun getAsBoolean(): Boolean = getValue()

    fun <T> getAs(clazz: Class<T>) = clazz.cast(getAsAny())

    fun isHidden(): Boolean = if (dependsOn == null) false else !dependsOn!!.getAsBoolean()

    fun setValue(value: Any?) {
        if (value == null) {
            println("null value assigned to property ${attributesExt.name}, but Vigilance does not support null values")
            return
        }

        (this.state as State<Any?>).set(value)

        this.initialized = true

        instance.markDirty()
    }

    fun setCallbackConsumer(callback: Consumer<Any?>) {
        (this.state as State<Any?>).onSetValue {
            if (attributesExt.triggerActionOnInitialization || this.initialized) {
                callback.accept(it)
            }
        }
    }

    companion object {
        fun fromField(property: Property, field: Field, instance: Vigilant): PropertyData {
            return PropertyData(
                PropertyAttributesExt.fromPropertyAnnotation(property),
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

        fun fromMethod(property: Property, method: Method, instance: Vigilant): PropertyData {
            return PropertyData(
                PropertyAttributesExt.fromPropertyAnnotation(property),
                BasicState(Unit),
                instance
            ).apply {
                setCallbackConsumer { method.invoke(this.instance) }
            }
        }

        fun withValue(property: Property, obj: Any?, instance: Vigilant): PropertyData {
            return PropertyData(
                PropertyAttributesExt.fromPropertyAnnotation(property),
                ValueBackedPropertyValue(obj),
                instance
            )
        }
    }
}
