package club.sk1er.vigilance.gui.components

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.animate
import club.sk1er.elementa.dsl.constrain
import club.sk1er.elementa.dsl.pixels
import club.sk1er.elementa.effects.StencilEffect
import java.awt.Color

class DropDown @JvmOverloads constructor(vararg elements: UIComponent, var floating: Boolean = true) : UIBlock(Color.RED) {
    var active = false

    init {
        elements.forEach { addChild(it.constrain { y = SiblingConstraint() }) }
        setWidth(ChildBasedMaxSizeConstraint())
        setHeight(0.pixels())
        enableEffect(StencilEffect())
    }

    override fun getHeight() = if (floating) 0f else super.getHeight()

    fun addElement(element: UIComponent) = apply {
        addChild(element.constrain { y = SiblingConstraint() })
    }

    fun open() {
        active = true
        animate {
            setHeightAnimation(Animations.OUT_EXP, 0.5f, ChildBasedSizeConstraint())
        }
    }

    fun close() {
        active = false
        animate {
            setHeightAnimation(Animations.OUT_EXP, 0.5f, 0.pixels())
        }
    }
}