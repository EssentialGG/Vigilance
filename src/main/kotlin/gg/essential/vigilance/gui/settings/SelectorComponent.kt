package gg.essential.vigilance.gui.settings

import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.vigilance.impl.I18n

class SelectorComponent(initialSelection: Int, options: List<String>) : SettingComponent() {

    internal val dropDown by DropDownComponent(initialSelection, options.map { I18n.format(it) }) childOf this

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            // Height of the drop-down in the collapsed state.
            // The height is locked at this value so that when the dropdown
            // opens, the size of the component doesn't change.
            height = 17.pixels
        }

        dropDown.selectedIndex.onSetValue { newValue ->
            changeValue(newValue)
        }
    }

    override fun closePopups(instantly: Boolean) {
        dropDown.collapse(instantly)
    }
}
