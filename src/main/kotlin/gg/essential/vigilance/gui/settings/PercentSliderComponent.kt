package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.provideDelegate
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.gui.VigilancePalette
import kotlin.math.roundToInt

class PercentSliderComponent(initialValue: Float) : AbstractSliderComponent() {

    private val percentageText by UIText(getFormattedPercent(initialValue)).constrain {
        y = CenterConstraint()
        color = VigilancePalette.text.toConstraint()
    } childOf this

    override val slider by Slider(initialValue).constrain {
        x = SiblingConstraint()
        width = 60.pixels
        height = 12.pixels
    } childOf this

    init {
        slider.onValueChange { newPercentage ->
            changeValue(newPercentage)
            percentageText.setText(getFormattedPercent())
        }

        sliderInit()
    }

    private fun getFormattedPercent(value: Float? = null): String =
        "${((value ?: slider.getCurrentPercentage()) * 100f).roundToInt()}%"
}
