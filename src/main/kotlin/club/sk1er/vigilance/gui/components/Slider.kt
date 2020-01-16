package club.sk1er.vigilance.gui.components

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIRoundedRectangle
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.StencilEffect
import club.sk1er.vigilance.data.PropertyData
import java.awt.Color

class Slider(private val prop: PropertyData) : UIComponent() {
    private var grabbed = false
    var value = 0.5f

    private val slide = UIRoundedRectangle(1f).constrain {
        y = CenterConstraint()
        width = RelativeConstraint()
        height = 2.pixels()
        color = Color(120, 120, 120, 0).asConstraint()
    }.enableEffect(StencilEffect()) childOf this

    private val slideBackground = UIBlock().constrain {
        width = RelativeConstraint()
        height = RelativeConstraint()
        color = Color(0, 170, 165, 0).asConstraint()
    } childOf slide

    internal val knob = Knob(10)

    init {

        knob.constrain {
            x = 100.pixels(true)
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
            knob.animate {
                setXAnimation(Animations.OUT_EXP, 0.5f, (mouseX - knob.getRadius() / 2).pixels().minMax(0.pixels(), 0.pixels(true)))
            }
            slideBackground.animate {
                setWidthAnimation(Animations.OUT_EXP, 0.5f, mouseX.pixels().minMax(0.pixels(), RelativeConstraint()))
            }

            value = slideBackground.getWidth() / (slide.getRight() - slide.getLeft())
            val tmp = (prop.property.min + ((prop.property.max - prop.property.min) * value)).toInt()
            prop.setValue(tmp)
        }

        constrain {
            x = CenterConstraint()
            y = SiblingConstraint()
            width = RelativeConstraint(0.75f)
            height = 20.pixels()
        }
    }

    fun fadeIn() {
        knob.fadeIn()
        slide.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(120, 120, 120, 255).asConstraint()) }
        slideBackground.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 170, 165, 255).asConstraint()) }
    }

    fun fadeOut() {
        knob.fadeOut()
        slide.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(120, 120, 120, 0).asConstraint()) }
        slideBackground.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 170, 165, 0).asConstraint()) }
    }
}