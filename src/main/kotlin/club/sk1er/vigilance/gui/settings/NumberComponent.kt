package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.SVGComponent
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.vigilance.gui.SettingsGui
import java.awt.Color

class NumberComponent(initialValue: Int, private val min: Int, private val  max: Int) : SettingComponent() {
    private var value = initialValue

    private val valueText = UIText(value.toString()).constrain {
        y = CenterConstraint()
        textScale = 1.5f.pixels()
        color = SettingsGui.ACCENT_COLOR.asConstraint()
    } childOf this

    private val controlContainer = UIContainer().constrain {
        x = SiblingConstraint(5f)
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint(CONTROL_PADDING)
    } childOf this

    private val incrementControl = UIContainer().constrain {
        y = SiblingConstraint(CONTROL_PADDING)
        width = CONTROL_WIDTH.pixels()
        height = CONTROL_HEIGHT.pixels()
    } childOf controlContainer

    private val incrementIcon = SVGComponent(UP_ARROW_SVG).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = (CONTROL_WIDTH * 0.8f).pixels()
        height = (CONTROL_HEIGHT * 0.8f).pixels()
    } childOf incrementControl

    private val decrementControl = UIContainer().constrain {
        y = SiblingConstraint(CONTROL_PADDING)
        width = CONTROL_WIDTH.pixels()
        height = CONTROL_HEIGHT.pixels()
    } childOf controlContainer

    private val decrementIcon = SVGComponent(DOWN_ARROW_SVG).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = (CONTROL_WIDTH * 0.8f).pixels()
        height = (CONTROL_HEIGHT * 0.8f).pixels()
    } childOf decrementControl

    init {
        constrain {
            width = ChildBasedSizeConstraint(5f)
            height = ChildBasedMaxSizeConstraint()
        }

        releaseControl(incrementControl)
        releaseControl(decrementControl)

        incrementControl.onMouseClick {
            // TODO: Enabled increases by 5/10 when holding shift, etc.
            clickControl(this)
        }.onMouseRelease {
            releaseControl(this)
        }

        decrementControl.onMouseClick {
            // TODO: Enabled increases by 5/10 when holding shift, etc.
            clickControl(this)
        }.onMouseRelease {
            releaseControl(this)
        }
    }

    private fun clickControl(control: UIComponent) {
        changeOutlineColor(control, SettingsGui.ACCENT_COLOR)

        val change = if (control == incrementControl) 1 else -1
        value = (value + change).coerceIn(min..max)
        valueText.setText(value.toString())
        changeValue(value)

        if (isControlDisabled(control)) {
            changeOutlineColor(control, SettingsGui.DISABLED_COLOR)
        }

        // TODO: Repeat these increases until the mouse is released!
    }

    private fun releaseControl(control: UIComponent) {
        changeOutlineColor(
            control,
            if (isControlDisabled(control)) SettingsGui.DISABLED_COLOR else SettingsGui.GRAY_COLOR
        )
    }

    private fun isControlDisabled(control: UIComponent) =
        (value == min && control == decrementControl) ||
        (value == max && control == incrementControl)

    private fun changeOutlineColor(control: UIComponent, color: Color) {
        control.removeEffect<OutlineEffect>()
        control.enableEffect(OutlineEffect(color, OUTLINE_WIDTH))
    }

    companion object {
        private const val CONTROL_WIDTH = 11f
        private const val CONTROL_HEIGHT = 8f
        private const val CONTROL_PADDING = 2f
        private const val OUTLINE_WIDTH = 1f
    }
}