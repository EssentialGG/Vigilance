package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.SVGComponent
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UITextInput
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.vigilance.gui.SettingsGui
import java.awt.Color

class ColorComponent(initial: Color, private val allowAlpha: Boolean) : SettingComponent() {
    private var active = false

    private val currentColorHex = UITextInput().constrain {
        x = 5.pixels()
        y = 6.pixels()
        width = RelativeConstraint(1f) - 30.pixels()
        color = SettingsGui.GRAY_COLOR.asConstraint()
    } childOf this

    private val dropDownArrow = SVGComponent(DOWN_ARROW_SVG).constrain {
        x = 5.pixels(true)
        y = 5.pixels()
        width = 10.pixels()
        height = 10.pixels()
    } childOf this

    private val colorPicker = ColorPicker(initial, allowAlpha).constrain {
        x = CenterConstraint()
        y = 22.pixels()
        width = RelativeConstraint(0.9f)
        height = 70.pixels()
    } childOf this

    init {
        constrain {
            width = 80.pixels()
            height = 20.pixels()
        }

        enableEffect(OutlineEffect(Color(80, 80, 80), 0.5f))
        val outlineContainer = UIContainer().constrain {
            x = (-1).pixels()
            y = (-1).pixels()
            width = RelativeConstraint(1f) + 2.pixels()
            height = RelativeConstraint(1f) + 3f.pixels()
        }
        outlineContainer.parent = this
        children.add(0, outlineContainer)
        enableEffect(ScissorEffect(outlineContainer))

        onMouseEnter {
            hoverText(currentColorHex)
        }

        onMouseLeave {
            if (active) return@onMouseLeave

            unHoverText(currentColorHex)
        }

        onMouseClick { event ->
            event.stopPropagation()

            if (active) {
                collapse()
            } else {
                expand()
            }
        }

        currentColorHex.minWidth = RelativeConstraint(0.5f)
        currentColorHex.maxWidth = RelativeConstraint(1f) - 25.pixels()
        currentColorHex.text = getColorString(initial)

        currentColorHex.onMouseClick { event ->
            if (!active) return@onMouseClick

            currentColorHex.active = true
            event.stopPropagation()
        }

        currentColorHex.onActivate {
            currentColorHex.active = false
            // TODO: Parse & set color
        }

        colorPicker.onValueChange { color ->
            changeValue(color)
            currentColorHex.text = getColorString(color)
        }

        colorPicker.onMouseClick { event ->
            event.stopPropagation()
        }
    }

    override fun closePopups() {
        collapse(true)
    }

    private fun expand() {
        active = true

        animate {
            setHeightAnimation(Animations.IN_SIN, 0.35f, 25.pixels() + RelativeConstraint(1f).to(colorPicker))
        }

        dropDownArrow.setSVG(UP_ARROW_SVG)
    }

    private fun collapse(unHover: Boolean = false) {
        active = false

        animate {
            setHeightAnimation(Animations.OUT_SIN, 0.35f, 20.pixels())
        }

        if (unHover) {
            unHoverText(currentColorHex)
        }

        currentColorHex.active = false

        dropDownArrow.setSVG(DOWN_ARROW_SVG)
    }

    private fun hoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, Color.WHITE.asConstraint())
        }
    }

    private fun unHoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, SettingsGui.GRAY_COLOR.asConstraint())
        }
    }

    private fun getColorString(color: Color): String {
        return if (allowAlpha) "#%08x".format(color.rgb) else "#%06x".format(color.rgb and 0xffffff)
    }
}