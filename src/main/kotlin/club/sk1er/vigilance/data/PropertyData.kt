package club.sk1er.vigilance.data

import club.sk1er.vigilance.Vigilant
import java.lang.reflect.Field

data class PropertyData(val property: Property, val field: Field, val instance: Vigilant) {
    fun getDataType() = property.type

    inline fun <reified T> getValue(): T {
        return field.get(instance) as T
    }

    fun setValue(value: Any?) {
        field.set(instance, value)
    }
}