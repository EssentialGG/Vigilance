package club.sk1er.vigilance.oldgui

import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.pixels
import club.sk1er.elementa.dsl.plus
import club.sk1er.vigilance.data.PropertyData

abstract class SettingObject(private val prop: PropertyData? = null) : UIContainer() {
    var active = false

    init {
        setX(CenterConstraint())
        setY(SiblingConstraint())
        setWidth(RelativeConstraint(0.75f))
        setHeight(ChildBasedSizeConstraint() + 5.pixels())
    }

    open fun animateIn() { active = true }
    open fun animateOut() { active = false }

    override fun draw() {
        if (prop?.property?.hidden == true) return
        super.draw()
    }

    override fun getHeight(): Float {
        if (prop?.property?.hidden == true) return 0f
        return super.getHeight()
    }
}