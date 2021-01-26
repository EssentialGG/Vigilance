package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.SVGComponent
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIImage
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.elementa.state.toConstraint
import club.sk1er.vigilance.gui.VigilancePalette
import java.awt.Color

class NumberComponent(initialValue: Int, private val min: Int, private val  max: Int) : SettingComponent() {
    private var value = initialValue

    private val valueText = UIText(value.toString()).constrain {
        y = CenterConstraint()
        textScale = 1.5f.pixels()
        color = VigilancePalette.accentState.toConstraint()
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

    private val incrementImage = UIImage.ofResource(UP_ARROW_PNG).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 9.pixels()
        height = 5.pixels()
    } childOf incrementControl

    private val decrementControl = UIContainer().constrain {
        y = SiblingConstraint(CONTROL_PADDING)
        width = CONTROL_WIDTH.pixels()
        height = CONTROL_HEIGHT.pixels()
    } childOf controlContainer

    private val decrementIcon = UIImage.ofResource(DOWN_ARROW_PNG).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 9.pixels()
        height = 5.pixels()
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
        changeOutlineColor(control, VigilancePalette.ACCENT)

        val change = if (control == incrementControl) 1 else -1
        value = (value + change).coerceIn(min..max)
        valueText.setText(value.toString())
        changeValue(value)

        if (isControlDisabled(control)) {
            changeOutlineColor(control, VigilancePalette.DISABLED)
        }

        // TODO: Repeat these increases until the mouse is released!
    }

    private fun releaseControl(control: UIComponent) {
        changeOutlineColor(
            control,
            if (isControlDisabled(control)) VigilancePalette.DISABLED else VigilancePalette.MID_TEXT
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
