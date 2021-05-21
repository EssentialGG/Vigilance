package club.sk1er.vigilance.gui.settings

import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.toConstraint
import club.sk1er.vigilance.gui.SettingsGui
import club.sk1er.vigilance.gui.VigilancePalette
import java.util.*

class PercentSliderComponent(initialValue: Float) : AbstractSliderComponent() {
    private val percentageText = UIText(getFormattedPercent(initialValue)).constrain {
        y = CenterConstraint()
        color = VigilancePalette.midTextState.toConstraint()
    } childOf this

    override val slider = Slider(initialValue).constrain {
        x = SiblingConstraint()
        width = 60.pixels()
        height = 12.pixels()
    } childOf this

    init {
        slider.onValueChange { newPercentage ->
            changeValue(newPercentage)
            percentageText.setText(getFormattedPercent())
        }

        sliderInit()
    }

    private fun getFormattedPercent(value: Float? = null): String {
        return "%.2f".format(Locale.US,value ?: slider.getCurrentPercentage())
    }
}
