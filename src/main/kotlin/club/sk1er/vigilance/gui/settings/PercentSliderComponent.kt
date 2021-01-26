package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.state.toConstraint
import club.sk1er.vigilance.gui.SettingsGui
import club.sk1er.vigilance.gui.VigilancePalette

class PercentSliderComponent(initialValue: Float) : SettingComponent() {
    private val percentageText = UIText(getFormattedPercent(initialValue)).constrain {
        y = CenterConstraint()
        color = VigilancePalette.midTextState.toConstraint()
    } childOf this

    private val slider = Slider(initialValue).constrain {
        x = SiblingConstraint()
        width = 60.pixels()
        height = 12.pixels()
    } childOf this

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedMaxSizeConstraint()
        }

        slider.onValueChange { newPercentage ->
            changeValue(newPercentage)
            percentageText.setText(getFormattedPercent())
        }
    }

    private fun getFormattedPercent(value: Float? = null): String {
        return "%.2f".format(value ?: slider.getCurrentPercentage())
    }
}
