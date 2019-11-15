package club.sk1er.vigilance.gui.components

import club.sk1er.elementa.components.UICircle
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import java.awt.Color

class Knob(private val size: Int, toggled: Boolean = true) : UIContainer() {
    private val hover = UICircle().constrain {
        x = (size / 2).pixels()
        y = CenterConstraint()
        width = size.pixels()
    } childOf this

    private val click = UICircle().constrain {
        x = (size / 2).pixels()
        y = CenterConstraint()
        width = size.pixels()
    } childOf this

    private val knob = UICircle().constrain {
        x = (size / 2).pixels()
        y = CenterConstraint()
        width = size.pixels()
    } childOf this

    init {
        constrain {
            y = CenterConstraint()
            width = size.pixels()
            height = size.pixels()
        }

        if (toggled) {
            knob.setColor(Color(0, 210, 205, 0).asConstraint())
            hover.setColor(Color(0, 255, 250, 0).asConstraint())
        } else {
            knob.setColor(Color(164, 164, 164, 0).asConstraint())
            hover.setColor(Color(255, 255, 255, 0).asConstraint())
        }
    }

    fun fadeIn() {
        var color = Color(hover.getColor().red, hover.getColor().green, hover.getColor().blue, 50)
        hover.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, color.asConstraint()) }
        color = Color(knob.getColor().red, knob.getColor().green, knob.getColor().blue, 255)
        knob.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, color.asConstraint()) }
    }

    fun fadeOut() {
        var color = Color(hover.getColor().red, hover.getColor().green, hover.getColor().blue, 0)
        hover.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, color.asConstraint()) }
        color = Color(knob.getColor().red, knob.getColor().green, knob.getColor().blue, 0)
        knob.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, color.asConstraint()) }
    }

    fun hover() = hover.animate { setWidthAnimation(Animations.OUT_EXP, 0.5f, (size * 1.75f).pixels()) }
    fun unHover() = hover.animate { setWidthAnimation(Animations.OUT_EXP, 0.5f, size.pixels()) }

    fun grab() = hover.animate { setWidthAnimation(Animations.OUT_EXP, 0.5f, (size * 2.25f).pixels()) }
    fun release() {
        if (isHovered()) hover.animate { setWidthAnimation(Animations.OUT_EXP, 0.5f, (size * 1.75f).pixels()) }
        else unHover()
    }

    fun click(toggled: Boolean) {
        click.constrain {
            width = size.pixels()
            color = Color(255, 255, 255, 75).asConstraint()
        }
        click.animate {
            setWidthAnimation(Animations.OUT_EXP, 0.5f, (size * 1.75f).pixels())
            setColorAnimation(Animations.OUT_EXP, 1f, Color(255, 255, 255, 0).asConstraint())
        }

        if (toggled) {
            animate { setXAnimation(Animations.OUT_EXP, 0.5f, 0.pixels()) }
            hover.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(255, 255, 255, 50).asConstraint()) }
            knob.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(164, 164, 164, 255).asConstraint()) }
        } else {
            animate { setXAnimation(Animations.OUT_EXP, 0.5f, 0.pixels(true)) }
            hover.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 255, 250, 50).asConstraint()) }
            knob.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 210, 205, 255).asConstraint()) }
        }
    }
}