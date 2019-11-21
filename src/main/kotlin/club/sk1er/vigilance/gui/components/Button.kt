package club.sk1er.vigilance.gui.components

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UICircle
import club.sk1er.elementa.components.UIRoundedRectangle
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.elementa.effects.StencilEffect
import java.awt.Color

class Button @JvmOverloads constructor(buttonText: String, private var style: Int = 0) : UIComponent() {
    private var active = false

    private val background = when (style) {
        ROUNDED_GRAY -> UIRoundedRectangle(4f).constrain {
            width = RelativeConstraint()
            height = RelativeConstraint()
            color = Color(120, 120, 120, 0).asConstraint()
        }.enableEffect(StencilEffect()) childOf this
        RECTANGLE_COLOR -> UIBlock().constrain {
            width = RelativeConstraint()
            height = RelativeConstraint()
            color = Color(0, 205, 200, 0).asConstraint()
        }.enableEffect(ScissorEffect()) childOf this
        RECTANGLE_GRAY -> UIBlock().constrain {
            width = RelativeConstraint()
            height = RelativeConstraint()
            color = Color(120, 120, 120, 0).asConstraint()
        }.enableEffect(ScissorEffect()) childOf this
        else -> UIRoundedRectangle(4f).constrain {
            width = RelativeConstraint()
            height = RelativeConstraint()
            color = Color(0, 205, 200, 0).asConstraint()
        }.enableEffect(StencilEffect()) childOf this
    }

    private val click = UICircle() childOf background

    private val text = UIText(buttonText, false)

    init {
        text.constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            color = Color(0, 0, 0, 0).asConstraint()
        } childOf background

        onMouseClick { mouseX, mouseY, button ->
            if (!active) return@onMouseClick
            click.constrain {
                x = mouseX.pixels()
                y = mouseY.pixels()
                width = 0.pixels()
                color = Color(0, 0, 0, 75).asConstraint()
            }

            click.animate {
                setWidthAnimation(Animations.OUT_CUBIC, 0.5f, RelativeConstraint(2f))
            }
        }

        onMouseRelease {
            if (!active) return@onMouseRelease
            click.animate {
                setColorAnimation(Animations.OUT_EXP, 1f, Color(0, 0, 0, 0).asConstraint())
            }
        }

        onMouseEnter {
            if (!active) return@onMouseEnter
            background.animate { setColorAnimation(Animations.OUT_EXP, 1f, Color(0, 170, 165, 255).asConstraint()) }
        }

        onMouseLeave {
            if (!active) return@onMouseLeave
            background.animate { setColorAnimation(Animations.OUT_EXP, 1f, Color(0, 205, 200, 255).asConstraint()) }
        }
    }

    fun fadeIn() {
        active = true
        background.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 205, 200, 255).asConstraint()) }
        text.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color.BLACK.asConstraint()) }
    }

    fun fadeOut() {
        active = false
        background.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 255, 255, 0).asConstraint()) }
        text.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 0, 0, 0).asConstraint()) }
    }

    fun setText(newText: String) {
        text.setText(newText)
    }

    companion object {
        val ROUNDED_COLOR = 0
        val ROUNDED_GRAY = 1
        val RECTANGLE_COLOR = 2
        val RECTANGLE_GRAY = 3
    }
}