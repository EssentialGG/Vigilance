package gg.essential.vigilance.gui.settings

import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*

class SelectorComponent(initialSelection: Int, options: List<String>) : SettingComponent() {

    internal val dropDown by DropDownComponent(initialSelection, options) childOf this

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        dropDown.selectedIndex.onSetValue { newValue ->
            changeValue(newValue)
        }
    }

    override fun closePopups(instantly: Boolean) {
        dropDown.collapse(instantly)
    }
}
