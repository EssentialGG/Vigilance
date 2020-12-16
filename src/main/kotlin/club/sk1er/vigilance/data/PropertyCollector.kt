package club.sk1er.vigilance.data

import club.sk1er.vigilance.Vigilant

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
            .map {
                PropertyData.fromField(
                    it.getAnnotation(Property::class.java),
                    it.apply { it.isAccessible = true },
                    instance
                )
            }

        val methodPropertyData = instance::class.java.declaredMethods
            .filter { it.isAnnotationPresent(Property::class.java) }
            .map {
                PropertyData.fromMethod(
                    it.getAnnotation(Property::class.java),
                    it.apply { it.isAccessible = true },
                    instance
                )
            }

        return fieldPropertyData + methodPropertyData
    }
}
