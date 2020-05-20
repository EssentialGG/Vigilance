package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.gui.SettingsGui

class PercentSliderComponent(initialValue: Float) : SettingComponent() {
    init {
        constrain {
            width = ChildBasedSizeConstraint(5f)
            height = ChildBasedMaxSizeConstraint()
        }
    }

    private val percentageText = UIText(getFormattedPercent(initialValue)).constrain {
        y = CenterConstraint()
        color = SettingsGui.DISABLED_COLOR.asConstraint()
    } childOf this

    private val slider = Slider(initialValue).constrain {
        x = SiblingConstraint() + 5.pixels()
        width = 60.pixels()
        height = 10.pixels()
    } childOf this

    init {
        slider.onValueChange { newPercentage ->
            changeValue(newPercentage)
            percentageText.setText(getFormattedPercent())
        }
    }

    private fun getFormattedPercent(value: Float? = null): String {
        return "%.2f".format(value ?: slider.getCurrentPercentage())
    }
}