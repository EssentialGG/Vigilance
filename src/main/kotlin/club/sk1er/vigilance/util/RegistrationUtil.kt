package club.sk1er.vigilance.util

import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Property
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.data.PropertyData.Companion.withValue
import club.sk1er.vigilance.data.PropertyType
import java.util.function.Consumer

class RegistrationUtil {
    companion object {
        var instance: Vigilant? = null
    }

    fun createAndRegisterConfig(type: PropertyType, category: String, subCategory: String, name: String, description: String, defaultValue: Any?, onUpdate: Consumer<Any?>?): PropertyData? {
        val config = createConfig(type, category, subCategory, name, description, defaultValue, onUpdate)
        register(config)
        return config
    }

    fun createConfig(type: PropertyType, category: String, subCategory: String, name: String, description: String, defaultValue: Any?, onUpdate: Consumer<Any?>?): PropertyData {
        if(instance == null) throw IllegalStateException("Config property cannot be created until a Vigilant instance is supplied!")
        val property = createProperty(type, category, subCategory, name, description)
        val pro = withValue(property, defaultValue, instance!!)
        if (onUpdate != null) pro.setCallbackConsumer(onUpdate)
        return pro
    }

    fun register(data: PropertyData) {
        if(instance == null) throw IllegalStateException("Config property cannot be created until a Vigilant instance is supplied!")
        instance!!.registerProperty(data)
    }


    fun createProperty(type: PropertyType?, category: String?, subCategory: String?, name: String?, description: String?): Property? {
        return object : Property() {
            fun annotationType(): Class<out Annotation?> {
                return Property::class.java
            }

            fun type(): PropertyType? {
                return type
            }

            fun subcategory(): String? {
                return subCategory
            }

            fun options(): Array<String?>? {
                return arrayOf()
            }

            fun name(): String? {
                return name
            }

            fun min(): Int {
                return 0
            }

            fun max(): Int {
                return 0
            }

            fun description(): String? {
                return description
            }

            fun category(): String? {
                return category
            }
        }
    }
}