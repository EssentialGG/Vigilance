package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.dsl.childOf
import club.sk1er.elementa.dsl.constrain
import club.sk1er.vigilance.data.PropertyData

class SelectorComponent(propertyData: PropertyData) : SettingComponent(propertyData) {
    private val dropDown = DropDown(propertyData.getValue(), propertyData.attributes.options) childOf this

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        dropDown.onValueChange { newValue ->
            changeValue(newValue)
        }
    }

    override fun externalSetValue(newValue: Any?) {
        if (newValue !is Int)
            throw IllegalArgumentException("SelectorComponent externalSetValue expected an Int type, found ${newValue?.javaClass?.simpleName}")
        dropDown.select(newValue)
    }

    override fun closePopups() {
        dropDown.collapse(true)
    }
}
