package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent

abstract class Setting : UIContainer() {
    init {
        constrain {
            y = SiblingConstraint(7f)
            width = 100.percent
            height = ChildBasedMaxSizeConstraint()
        }
    }

    open fun closePopups(instantly: Boolean = false) { }
}
