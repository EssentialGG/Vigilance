package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.constrain

abstract class Setting : UIContainer() {
    init {
        constrain {
            y = SiblingConstraint(10f)
            width = RelativeConstraint(1f)
            height = ChildBasedMaxSizeConstraint()
        }
    }

    open fun closePopups() { }
}
