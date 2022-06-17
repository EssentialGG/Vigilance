package gg.essential.vigilance.gui.settings

import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.vigilance.impl.I18n

class SelectorComponent(initialSelection: Int, options: List<String>) : SettingComponent() {
    internal val dropDown by DropDown(initialSelection, options.map { I18n.format(it) }) childOf this

    init {
        constrain {
            y = 18.pixels()
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
