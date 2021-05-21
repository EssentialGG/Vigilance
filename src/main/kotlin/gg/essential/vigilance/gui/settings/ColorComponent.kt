package gg.essential.vigilance.gui.settings

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.SVGComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.gui.VigilancePalette
import java.awt.Color

class ColorComponent(initial: Color, private val allowAlpha: Boolean) : SettingComponent() {
    private var active = false

    private val currentColorHex = UITextInput().constrain {
        x = 5.pixels()
        y = 6.pixels()
        width = RelativeConstraint(1f) - 30.pixels()
        color = VigilancePalette.midTextState.toConstraint()
    } childOf this

    private val downArrow = UIImage.ofResource(DOWN_ARROW_PNG).constrain {
        x = 5.pixels(true)
        y = 7.5.pixels()
        width = 9.pixels()
        height = 5.pixels()
    } childOf this

    private val upArrow = UIImage.ofResource(UP_ARROW_PNG).constrain {
        x = 5.pixels(true)
        y = 7.5.pixels()
        width = 9.pixels()
        height = 5.pixels()
    }

    private val colorPicker = ColorPicker(initial, allowAlpha).constrain {
        x = CenterConstraint()
        y = 22.pixels()
        width = RelativeConstraint(0.9f)
        height = (if (allowAlpha) 78 else 62).pixels()
    } childOf this

    init {
        constrain {
            width = 85.pixels()
            height = 20.pixels()
        }

        enableEffect(OutlineEffect(VigilancePalette.getDivider(), 0.5f).bindColor(VigilancePalette.dividerState))
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

        currentColorHex.setText(getColorString(initial))

        currentColorHex.onMouseClick { event ->
            if (!active) return@onMouseClick

            currentColorHex.grabWindowFocus()
            event.stopPropagation()
        }.onFocus {
            currentColorHex.setActive(true)
        }.onFocusLost {
            currentColorHex.setActive(false)
        }

        currentColorHex.onActivate { color ->
            currentColorHex.setActive(false)

            if ((allowAlpha && color.length != 9) || (!allowAlpha && color.length != 7)) {
                currentColorHex.setText(getColorString(colorPicker.getCurrentColor()))
                return@onActivate
            }

            val hex = color.substring(if (allowAlpha) 3 else 1).toIntOrNull(16)

            if (hex == null) {
                currentColorHex.setText(getColorString(colorPicker.getCurrentColor()))
                return@onActivate
            }

            if (allowAlpha) {
                val alpha = color.substring(1, 3).toIntOrNull(16)

                if (alpha == null) {
                    currentColorHex.setText(getColorString(colorPicker.getCurrentColor()))
                    return@onActivate
                }

                colorPicker.setAlpha(alpha / 255f)
            }

            val hsb = Color.RGBtoHSB((hex shr 16) and 0xff, (hex shr 8) and 0xff, hex and 0xff, null)
            colorPicker.setHSB(hsb[0], hsb[1], hsb[2])
        }

        colorPicker.onValueChange { color ->
            changeValue(color)
            currentColorHex.setText(getColorString(color))
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
            setHeightAnimation(Animations.IN_SIN, 0.35f, 25.pixels() + RelativeConstraint(1f).boundTo(colorPicker))
        }

        replaceChild(upArrow, downArrow)
    }

    private fun collapse(unHover: Boolean = false) {
        if (active)
            replaceChild(downArrow, upArrow)
        active = false

        animate {
            setHeightAnimation(Animations.OUT_SIN, 0.35f, 20.pixels())
        }

        if (unHover) {
            unHoverText(currentColorHex)
        }

        currentColorHex.setActive(false)
    }

    private fun hoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, VigilancePalette.brightTextState.toConstraint())
        }
    }

    private fun unHoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, VigilancePalette.midTextState.toConstraint())
        }
    }

    private fun getColorString(color: Color): String {
        return "#%06x".format(color.rgb and 0xffffff) + if (allowAlpha) {
            "%02x".format(color.alpha)
        } else ""
    }
}
