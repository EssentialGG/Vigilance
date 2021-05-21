package gg.essential.vigilance.gui.settings

import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain

class SelectorComponent(initialSelection: Int, options: List<String>) : SettingComponent() {
    internal val dropDown = DropDown(initialSelection, options) childOf this

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        dropDown.onValueChange { newValue ->
            changeValue(newValue)
        }
    }

    override fun closePopups() {
        dropDown.collapse(true)
    }
}
