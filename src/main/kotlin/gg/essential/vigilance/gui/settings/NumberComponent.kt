package gg.essential.vigilance.gui.settings

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.provideDelegate
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.state.State
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.UKeyboard
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color

class NumberComponent(
    initialValue: Int,
    private val min: Int,
    private val max: Int,
    private val inc: Int
) : SettingComponent() {
    private var value = initialValue

    private val valueText by UIText(value.toString()).constrain {
        y = CenterConstraint()
        textScale = 1.5f.pixels()
        color = VigilancePalette.accentState.toConstraint()
        fontProvider = DefaultFonts.VANILLA_FONT_RENDERER
    } childOf this

    private val controlContainer by UIContainer().constrain {
        x = SiblingConstraint(5f)
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint(CONTROL_PADDING)
    } childOf this

    private val incrementControl by UIContainer().constrain {
        y = SiblingConstraint(CONTROL_PADDING)
        width = CONTROL_WIDTH.pixels()
        height = CONTROL_HEIGHT.pixels()
    } childOf controlContainer

    init {
        UIImage.ofResourceCached(UP_ARROW_PNG).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 9.pixels()
            height = 5.pixels()
        } childOf incrementControl
    }

    private val decrementControl by UIContainer().constrain {
        y = SiblingConstraint(CONTROL_PADDING)
        width = CONTROL_WIDTH.pixels()
        height = CONTROL_HEIGHT.pixels()
    } childOf controlContainer

    init {
        UIImage.ofResourceCached(DOWN_ARROW_PNG).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 9.pixels()
            height = 5.pixels()
        } childOf decrementControl
    }

    init {
        constrain {
            width = ChildBasedSizeConstraint(5f)
            height = ChildBasedMaxSizeConstraint()
        }

        releaseControl(incrementControl)
        releaseControl(decrementControl)

        incrementControl.onLeftClick {
            clickControl(this)
        }.onMouseRelease {
            releaseControl(this)
        }

        decrementControl.onLeftClick {
            clickControl(this)
        }.onMouseRelease {
            releaseControl(this)
        }
    }

    private fun clickControl(control: UIComponent) {
        changeOutlineColor(control, VigilancePalette.accentState)

        val flag = UKeyboard.isCtrlKeyDown()
        val change = if (control == incrementControl) {
            if (flag) max else if (UKeyboard.isShiftKeyDown()) 5 * inc else inc
        } else {
            if (flag) min else if (UKeyboard.isShiftKeyDown()) -5 * inc else -(inc)
        }
        value = if (flag) change else (value + change).coerceIn(min..max)
        valueText.setText(value.toString())
        changeValue(value)

        if (isControlDisabled(control)) {
            changeOutlineColor(control, VigilancePalette.disabledState)
        }

        // TODO: Repeat these increases until the mouse is released!
    }

    private fun releaseControl(control: UIComponent) {
        changeOutlineColor(
            control,
            if (isControlDisabled(control)) VigilancePalette.disabledState else VigilancePalette.midTextState
        )
    }

    private fun isControlDisabled(control: UIComponent) =
        (value == min && control == decrementControl) ||
        (value == max && control == incrementControl)

    private fun changeOutlineColor(control: UIComponent, colorState: State<Color>) {
        control.removeEffect<OutlineEffect>()
        control.enableEffect(OutlineEffect(colorState.get(), OUTLINE_WIDTH).bindColor(colorState))
    }

    private fun useControl(control: UIComponent, other: UIComponent, key: Int) {
        clickControl(control)
        releaseControl(other)
        startTimer(100L) {
            if (!UKeyboard.isKeyDown(key) || isControlDisabled(control)) {
                releaseControl(control)
            }
            stopTimer(it)
        }
    }

    fun increment(): Unit = useControl(incrementControl, decrementControl, UKeyboard.KEY_UP)

    fun decrement(): Unit = useControl(decrementControl, incrementControl, UKeyboard.KEY_DOWN)

    companion object {
        private const val CONTROL_WIDTH = 11f
        private const val CONTROL_HEIGHT = 8f
        private const val CONTROL_PADDING = 2f
        private const val OUTLINE_WIDTH = 1f
    }
}
