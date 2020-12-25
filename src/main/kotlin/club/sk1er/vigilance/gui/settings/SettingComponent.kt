package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.dsl.constrain
import club.sk1er.elementa.dsl.pixels
import club.sk1er.elementa.svg.SVGParser
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.DataBackedSetting

abstract class SettingComponent(protected val propertyData: PropertyData) : UIContainer() {
    private var onValueChange: (Any?) -> Unit = {}
    private var lastValue: Any? = null

    init {
        constrain {
            x = DataBackedSetting.INNER_PADDING.pixels(true)
            y = CenterConstraint()
        }
    }

    fun onValueChange(listener: (Any?) -> Unit) {
        this.onValueChange = listener
    }

    fun changeValue(newValue: Any?) {
        if (newValue != lastValue) {
            lastValue = newValue
            this.onValueChange(newValue)
        }
    }

    abstract fun externalSetValue(newValue: Any?)

    open fun closePopups() { }

    override fun draw() {
        if (propertyData.dirty) {
            propertyData.dirty = false
            externalSetValue(propertyData.getAsAny())
        }

        super.draw()
    }

    companion object {
        const val DOWN_ARROW_PNG = "/vigilance/arrow-down.png"
        const val UP_ARROW_PNG = "/vigilance/arrow-up.png"
    }
}
