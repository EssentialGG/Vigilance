package gg.essential.vigilance.gui.settings

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.USound
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color

class ColorComponent(initial: Color, private val allowAlpha: Boolean) : SettingComponent() {
    private var active = false

    private val currentColorHex by UITextInput().constrain {
        x = 5.pixels()
        y = 6.pixels()
        width = RelativeConstraint(1f) - 30.pixels()
        color = VigilancePalette.text.toConstraint()
        fontProvider = DefaultFonts.VANILLA_FONT_RENDERER
    } childOf this

    private val downArrow by UIImage.ofResourceCached(DOWN_ARROW_PNG).constrain {
        x = 5.pixels(true)
        y = 7.5.pixels()
        width = 9.pixels()
        height = 5.pixels()
    } childOf this

    private val upArrow by UIImage.ofResourceCached(UP_ARROW_PNG).constrain {
        x = 5.pixels(true)
        y = 7.5.pixels()
        width = 9.pixels()
        height = 5.pixels()
    }

    private val colorPicker by ColorPicker(initial, allowAlpha).constrain {
        x = CenterConstraint()
        y = 22.pixels()
        width = RelativeConstraint(0.9f)
        height = (if (allowAlpha) 78 else 62).pixels()
        fontProvider = DefaultFonts.VANILLA_FONT_RENDERER
    } childOf this

    init {
        constrain {
            width = 85.pixels()
            height = 20.pixels()
        }

        colorPicker.hide(instantly = true)

        enableEffect(OutlineEffect(VigilancePalette.getDivider(), 1f).bindColor(VigilancePalette.dividerState))
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

        onLeftClick { event ->
            USound.playButtonPress()
            event.stopPropagation()

            if (active) {
                collapse()
            } else {
                expand()
            }
        }

        currentColorHex.setText(getColorString(initial))

        currentColorHex.onLeftClick { event ->
            if (!active) return@onLeftClick

            USound.playButtonPress()
            currentColorHex.grabWindowFocus()
            event.stopPropagation()
        }.onFocus {
            currentColorHex.setActive(true)
        }.onFocusLost {
            currentColorHex.setActive(false)
        }

        currentColorHex.onActivate { color ->
            // remove the leading # and validate that all chars are valid hex chars
            val noHash = color.replace("#", "").let { it.toIntOrNull(16)?.run { it } ?: "make this fail length checks" }
            // make sure the hex string is formatted how we want it
            val formattedColor = when (noHash.length) {
                3 -> noHash.asIterable().joinToString("") { "$it$it" } + "ff"
                4 -> noHash.asIterable().joinToString("") { "$it$it" }
                6 -> "${noHash}ff"
                8 -> noHash
                else -> "%06x".format(colorPicker.getCurrentColor().rgb and 0xffffff) + "%02x".format(colorPicker.getCurrentColor().alpha)
            }
            // set alpha if we need to
            if (allowAlpha) {
                colorPicker.setAlpha(formattedColor.takeLast(2).toInt(16) / 255f)
            }
            // parse hex into hsb values, removing alpha values if they're there
            val hex = formattedColor.dropLast(2).toInt(16)
            val hsb = Color.RGBtoHSB((hex shr 16) and 0xff, (hex shr 8) and 0xff, hex and 0xff, null)
            colorPicker.setHSB(hsb[0], hsb[1], hsb[2])
            // update the text if it's currently a bit wonky
            currentColorHex.setText(getColorString(colorPicker.getCurrentColor()))
        }

        colorPicker.onValueChange { color ->
            changeValue(color)
            currentColorHex.setText(getColorString(color))
        }

        colorPicker.onLeftClick { event ->
            USound.playButtonPress()
            event.stopPropagation()
        }
    }

    override fun closePopups(instantly: Boolean) {
        collapse(true, instantly)
    }

    private fun expand() {
        active = true

        animate {
            setHeightAnimation(Animations.IN_SIN, 0.35f, 25.pixels() + RelativeConstraint(1f).boundTo(colorPicker))
        }

        replaceChild(upArrow, downArrow)
        colorPicker.unhide(useLastPosition = true)
    }

    private fun collapse(unHover: Boolean = false, instantly: Boolean = false) {
        if (active)
            replaceChild(downArrow, upArrow)
        active = false

        if (instantly) {
            setHeight(20.pixels())
            colorPicker.hide(instantly = true)
        } else {
            animate {
                setHeightAnimation(Animations.OUT_SIN, 0.35f, 20.pixels())
                onComplete {
                    colorPicker.hide(instantly = true)
                }
            }
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
            setColorAnimation(Animations.OUT_EXP, 0.25f, VigilancePalette.text.toConstraint())
        }
    }

    private fun getColorString(color: Color): String {
        return "#%06x".format(color.rgb and 0xffffff) + if (allowAlpha) {
            "%02x".format(color.alpha)
        } else ""
    }
}
