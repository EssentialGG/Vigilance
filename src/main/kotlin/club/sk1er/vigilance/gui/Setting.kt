package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.constrain

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
