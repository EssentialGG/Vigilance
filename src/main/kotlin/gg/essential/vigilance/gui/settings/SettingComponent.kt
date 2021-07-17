package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.vigilance.gui.DataBackedSetting

abstract class SettingComponent : UIContainer() {
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

    fun changeValue(newValue: Any?, callListener: Boolean = true) {
        if (newValue != lastValue) {
            lastValue = newValue
            this.onValueChange(newValue)
        }
    }

    open fun closePopups(instantly: Boolean = false) { }

    companion object {
        const val DOWN_ARROW_PNG = "/vigilance/arrow-down.png"
        const val UP_ARROW_PNG = "/vigilance/arrow-up.png"
    }
}
