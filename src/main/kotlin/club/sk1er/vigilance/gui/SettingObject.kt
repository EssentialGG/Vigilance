package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.pixels
import club.sk1er.elementa.dsl.plus

abstract class SettingObject : UIContainer() {
    var active = false

    init {
        setX(CenterConstraint())
        setY(SiblingConstraint())
        setWidth(RelativeConstraint(0.75f))
        setHeight(ChildBasedSizeConstraint() + 5.pixels())
    }

    open fun animateIn() { active = true }
    open fun animateOut() { active = false }
}