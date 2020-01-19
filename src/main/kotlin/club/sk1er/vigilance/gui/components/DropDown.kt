package club.sk1er.vigilance.gui.components

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UICircle
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIRoundedRectangle
import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.StencilEffect
import java.awt.Color

class DropDown @JvmOverloads constructor(var floating: Boolean = true) : UIRoundedRectangle(5f) {
    var active = false
    var selected = 0

    val selectionBox = UIContainer().constrain {
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint(padding = 2f)
    }
    val clickBox = UIContainer()
    val click = UICircle()

    var onSelect: (selected: UIComponent) -> Unit = { }

    init {
        setColor(Color(120, 120, 120, 0).asConstraint())
        setWidth(ChildBasedMaxSizeConstraint() + 8.pixels())
        setHeight(12.pixels())
        enableEffect(StencilEffect())

        onMouseClick { mouseX, mouseY, mouseButton ->
            if (!active) {
                open()
            }
        }

        addChildren(selectionBox, clickBox.addChild(click))
    }

    override fun getHeight() = if (floating) 0f else super.getHeight()

    fun onSelect(action: (selected: UIComponent) -> Unit) = apply {
        onSelect = action
    }

    fun addElement(element: UIComponent) = apply {
        selectionBox.addChild(element.constrain {
            y = SiblingConstraint() + 2.pixels()
            x = 4.pixels()
            color = Color(255, 255, 255, 0).asConstraint()
        }.onMouseClick { mouseX, mouseY, _ ->
            if (active && getHeight() > 12) {
                selected = selectionBox.children.indexOf(element)
                onSelect(element)
                close(mouseX, mouseY)
            }
        })
    }

    fun open() {
        active = true
        animate {
            setHeightAnimation(Animations.OUT_EXP, 0.5f, ChildBasedSizeConstraint() + (children.size * 5).pixels())
        }

        selectionBox.animate{
            setYAnimation(Animations.OUT_EXP, 0.5f, 2.pixels())
        }
    }

    fun close(mouseX: Float, mouseY: Float) {
        active = false
        animate {
            setHeightAnimation(Animations.OUT_EXP, 0.5f, 12.pixels())
        }

        selectionBox.animate{
            setYAnimation(Animations.OUT_EXP, 0.5f, (selected * -12 + selected).pixels())
        }

        click.constrain {
            x = (mouseX - getLeft() + selectionBox.getLeft()).pixels()
            y = (mouseY - getTop() + selectionBox.getTop()).pixels()
            radius = 0.pixels()
            color = Color(0, 0, 0, 75).asConstraint()
        }

        click.animate {
            setRadiusAnimation(Animations.OUT_CUBIC, 0.5f, 25.pixels())
            setColorAnimation(Animations.OUT_CUBIC, 0.5f, Color(0, 0, 0, 0).asConstraint(), 0.25f)
        }
    }

    fun fadeIn() {
        animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(120, 120, 120, 255).asConstraint())
        }

        selectionBox.children.forEach { child ->
            child.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.asConstraint())
            }
        }
    }

    fun fadeOut() {
        animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(120, 120, 120, 0).asConstraint())
        }

        selectionBox.children.forEach { child ->
            child.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, Color(255, 255, 255, 0).asConstraint())
            }
        }
    }
}