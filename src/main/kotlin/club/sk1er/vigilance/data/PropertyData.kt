package club.sk1er.vigilance.data

import club.sk1er.vigilance.Vigilant
import java.lang.reflect.Field
import java.util.function.Consumer

data class PropertyData(val property: Property, val instance: Vigilant) {
    fun getDataType() = property.type
    var obj: Any? = null
    var field: Field? = null
    var action: Consumer<Any>? = null

    constructor(property: Property, obj: Any, instance: Vigilant) : this(property, instance) {
        this.obj = obj;
    }

    constructor(property: Property, field: Field, instance: Vigilant) : this(property, instance) {
        this.field = field
    }


    inline fun <reified T> getValue(): T {
        if (obj != null) return obj as T

        return field?.get(instance) as T
    }

    fun setValue(value: Any?) {
        if (value != null) action?.accept(value)

        if (obj != null) obj = value
        else field?.set(instance, value)

        instance.markDirty()
    }
}