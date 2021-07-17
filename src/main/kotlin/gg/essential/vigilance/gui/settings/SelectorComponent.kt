package gg.essential.vigilance.gui.settings

import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*

class SelectorComponent(initialSelection: Int, options: List<String>) : SettingComponent() {
    internal val dropDown by DropDown(initialSelection, options) childOf this

    init {
        constrain {
            y = 18.pixels()
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        dropDown.onValueChange { newValue ->
            changeValue(newValue)
        }
    }

    override fun closePopups(instantly: Boolean) {
        dropDown.collapse(true, instantly)
    }
}
