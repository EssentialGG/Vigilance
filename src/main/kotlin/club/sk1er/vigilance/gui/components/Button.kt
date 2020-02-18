package club.sk1er.vigilance.gui.components

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UICircle
import club.sk1er.elementa.components.UIRoundedRectangle
import club.sk1er.elementa.components.UIWrappedText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.elementa.effects.StencilEffect
import java.awt.Color

class Button @JvmOverloads constructor(buttonText: String, private var style: Int = 0) : UIComponent() {
    private var active = false
    private var onClickAction: () -> Unit = {}

    private val background = when (style) {
        ROUNDED_GRAY -> UIRoundedRectangle(4f).constrain {
            color = Color(120, 120, 120, 0).asConstraint()
        }.enableEffect(StencilEffect())
        RECTANGLE_COLOR -> UIBlock().constrain {
            color = Color(0, 205, 200, 0).asConstraint()
        }.enableEffect(ScissorEffect())
        RECTANGLE_GRAY -> UIBlock().constrain {
            color = Color(120, 120, 120, 0).asConstraint()
        }.enableEffect(ScissorEffect())
        ROUNDED_TRANSPARENT -> UIRoundedRectangle(4f).enableEffect(StencilEffect())
        RECTANGLE_TRANSPARENT -> UIBlock().enableEffect(ScissorEffect())
        else -> UIRoundedRectangle(4f).constrain {
            color = Color(0, 205, 200, 0).asConstraint()
        }.enableEffect(StencilEffect())
    }

    private val click = UICircle() childOf background

    private val text = UIWrappedText(buttonText, shadow = false, centered = true)

    init {
        background.constrain {
            width = RelativeConstraint()
            height = RelativeConstraint()
        } childOf this

        text.constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = RelativeConstraint(1f)
            color = if (style == ROUNDED_TRANSPARENT || style == RECTANGLE_TRANSPARENT) {
                Color.WHITE.asConstraint()
            } else {
                Color.BLACK.asConstraint()
            }
        } childOf background

        onMouseClick { mouseX, mouseY, _ ->
            if (!active) return@onMouseClick
            click.constrain {
                x = mouseX.pixels()
                y = mouseY.pixels()
                radius = 0.pixels()
                color = Color(0, 0, 0, 75).asConstraint()
            }

            click.animate {
                setRadiusAnimation(Animations.OUT_CUBIC, 0.5f, RelativeConstraint(2f))
            }

            onClickAction()
        }

        onMouseRelease {
            if (!active) return@onMouseRelease
            click.animate {
                setColorAnimation(Animations.OUT_EXP, 1f, Color(0, 0, 0, 0).asConstraint())
            }
        }

        onMouseEnter {
            if (!active) return@onMouseEnter
            when (style) {
                ROUNDED_GRAY, RECTANGLE_GRAY -> background.animate {
                    setColorAnimation(Animations.OUT_EXP, 1f, Color(100, 100, 100, 255).asConstraint())
                }
                ROUNDED_COLOR, RECTANGLE_COLOR -> background.animate {
                    setColorAnimation(Animations.OUT_EXP, 1f, Color(0, 170, 165, 255).asConstraint())
                }
            }

        }

        onMouseLeave {
            if (!active) return@onMouseLeave
            when (style) {
                ROUNDED_GRAY, RECTANGLE_GRAY -> background.animate {
                    setColorAnimation(Animations.OUT_EXP, 1f, Color(120, 120, 120, 255).asConstraint())
                }
                ROUNDED_COLOR, RECTANGLE_COLOR -> background.animate {
                    setColorAnimation(Animations.OUT_EXP, 1f, Color(0, 205, 200, 255).asConstraint())
                }
            }
        }
    }

    fun onClick(method: () -> Unit) = apply {
        onClickAction = method
    }

    fun fadeIn() {
        active = true
        var color = text.getColor()
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(color.red, color.green, color.blue, 255).asConstraint())
        }

        if (style == ROUNDED_TRANSPARENT || style == RECTANGLE_TRANSPARENT) return
        color = background.getColor()
        background.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(color.red, color.green, color.blue, 255).asConstraint())
        }
    }

    fun fadeOut() {
        active = false
        var color = text.getColor()
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(color.red, color.green, color.blue, 0).asConstraint())
        }

        if (style == ROUNDED_TRANSPARENT || style == RECTANGLE_TRANSPARENT) return
        color = background.getColor()
        background.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(color.red, color.green, color.blue, 0).asConstraint())
        }
    }

    fun setText(newText: String) {
        text.setText(newText)
    }

    companion object {
        val ROUNDED_COLOR = 0
        val RECTANGLE_COLOR = 1
        val ROUNDED_GRAY = 2
        val RECTANGLE_GRAY = 3
        val ROUNDED_TRANSPARENT = 4
        val RECTANGLE_TRANSPARENT = 5
    }
}