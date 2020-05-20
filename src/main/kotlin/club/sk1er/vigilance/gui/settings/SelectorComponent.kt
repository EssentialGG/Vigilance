package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.dsl.childOf
import club.sk1er.elementa.dsl.constrain

class SelectorComponent(initialSelection: Int, options: List<String>) : SettingComponent() {
    private val dropDown = DropDown(initialSelection, options) childOf this

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
        dropDown.collapse()
    }
}