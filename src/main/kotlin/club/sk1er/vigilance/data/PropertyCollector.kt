package club.sk1er.vigilance.data

import gg.essential.universal.UChat
import club.sk1er.vigilance.Vigilant
import java.lang.reflect.Field

abstract class PropertyCollector {
    private val collectedProperties = mutableListOf<PropertyData>()

    fun initialize(instance: Vigilant) {
        collectedProperties.addAll(collectProperties(instance))
    }

    protected abstract fun collectProperties(instance: Vigilant): List<PropertyData>

    fun getProperties() = collectedProperties

    fun addProperty(propertyData: PropertyData) {
        collectedProperties.add(propertyData)
    }
}

class JVMAnnotationPropertyCollector : PropertyCollector() {
    override fun collectProperties(instance: Vigilant): List<PropertyData> {
        val fieldPropertyData = instance::class.java.declaredFields
            .filter { it.isAnnotationPresent(Property::class.java) }
            .map { field ->
                field.isAccessible = true

                PropertyData.fromField(field.getAnnotation(Property::class.java), field, instance).also { data ->
                    if (!data.attributes.type.isFieldValid(field)) {
                        throw IllegalStateException("[Vigilance] Error while creating GUI ${instance::class.simpleName}: " +
                            "field ${field.name} of PropertyType ${data.attributes.type.name} has invalid JVM type " +
                            field.type.simpleName)
                    }
                }
            }

        val methodPropertyData = instance::class.java.declaredMethods
            .filter { it.isAnnotationPresent(Property::class.java) }
            .map { method ->
                method.isAccessible = true

                PropertyData.fromMethod(method.getAnnotation(Property::class.java), method, instance).also { data ->
                    if (data.attributes.type != PropertyType.BUTTON) {
                        throw IllegalStateException("[Vigilance] Error while creating GUI ${instance::class.simpleName}: " +
                            "expected method ${method.name} to have PropertyType BUTTON, but found PropertyType " +
                            data.attributes.type.name)
                    }
                }
            }

        return fieldPropertyData + methodPropertyData
    }
}
