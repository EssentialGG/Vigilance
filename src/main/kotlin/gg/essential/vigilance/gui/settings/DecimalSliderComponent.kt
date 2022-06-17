package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.gui.VigilancePalette
import java.util.*

class DecimalSliderComponent(
    initialValue: Float,
    min: Float,
    max: Float, decimalPlaces: Int = 1
) : AbstractSliderComponent() {

    init {
        UIText(min.toString()).constrain {
            y = CenterConstraint()
            color = VigilancePalette.text.toConstraint()
        } childOf this
    }

    override val slider by Slider((initialValue - min) / (max - min)).constrain {
        x = SiblingConstraint()
        width = 60.pixels
        height = 12.pixels
    } childOf this

    init {
        UIText(max.toString()).constrain {
            x = SiblingConstraint()
            y = CenterConstraint()
            color = VigilancePalette.text.toConstraint()
        } childOf this
    }

    private val currentValueText by UIText(initialValue.toString()).constrain {
        x = CenterConstraint() boundTo slider.grabBox
        y = 150.percent
        color = VigilancePalette.text.toConstraint()
    } childOf slider

    init {
        slider.onValueChange { newPercentage ->
            val newValue = "%.${decimalPlaces}f".format(Locale.US,min + (newPercentage * (max - min)))
            changeValue(newValue.toFloat())
            currentValueText.setText(newValue)
        }

        sliderInit()
    }
}
