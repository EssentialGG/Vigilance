package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.dsl.constrain
import club.sk1er.elementa.dsl.pixels
import club.sk1er.vigilance.gui.DataBackedSetting

abstract class SettingComponent : UIContainer() {
    private var onValueChange: (Any?) -> Unit = {}

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
        this.onValueChange(newValue)
    }
}