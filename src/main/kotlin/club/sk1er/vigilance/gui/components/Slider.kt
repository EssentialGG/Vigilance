package club.sk1er.vigilance.gui.components

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIRoundedRectangle
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.StencilEffect
import club.sk1er.vigilance.data.PropertyData
import java.awt.Color

class Slider(private var value: Float = 0.5f) : UIComponent() {
    private var grabbed = false

    private val slide = UIRoundedRectangle(1f).constrain {
        y = CenterConstraint()
        width = RelativeConstraint()
        height = 2.pixels()
        color = Color(0, 170, 165, 0).asConstraint()
    }.enableEffect(StencilEffect()) childOf this

    private val slideBackground = UIBlock().constrain {
        width = FillConstraint()
        height = RelativeConstraint()
        color = Color(120, 120, 120, 0).asConstraint()
    } childOf slide

    internal val knob = Knob(10)

    private var onUpdate: (current: Float) -> Unit = { }

    init {
        constrain {
            x = CenterConstraint()
            y = SiblingConstraint()
            width = RelativeConstraint(0.75f)
            height = 20.pixels()
        }

        knob.constrain {
            x = 0.pixels().to(slideBackground) as XConstraint
        }.onMouseEnter {
            if (!grabbed) knob.hover()
        }.onMouseLeave {
            if (!grabbed) knob.unHover()
        }.onMouseClick { _, _, _ ->
            knob.grab()
            grabbed = true
        }.onMouseRelease {
            if (!grabbed) return@onMouseRelease
            knob.release()
            grabbed = false
        } childOf this

        onMouseDrag { mouseX, _, _ ->
            if (!grabbed) return@onMouseDrag
            slideBackground.animate {
                setXAnimation(
                    Animations.OUT_EXP,
                    0.5f,
                    mouseX.pixels().minMax(0.pixels(), 0.pixels(true).alignOutside(true))
                )
            }

            setValue((slideBackground.getLeft() - slide.getRight()) / (slide.getLeft() - slide.getRight()) * -1 + 1)
        }
    }

    fun setValue(value: Float) {
        this.value = value
        onUpdate(value)
    }

    fun onUpdate(action: (current: Float) -> Unit) = apply {
        onUpdate = action
    }

    fun fadeIn() {
        knob.fadeIn()
        slideBackground.setX((value * (slide.getRight() - slide.getLeft())).pixels())

        slide.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 170, 165, 255).asConstraint()) }
        slideBackground.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(120, 120, 120, 255).asConstraint()) }
    }

    fun fadeOut() {
        knob.fadeOut()
        slide.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 170, 165, 0).asConstraint()) }
        slideBackground.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(120, 120, 120, 0).asConstraint()) }
    }
}