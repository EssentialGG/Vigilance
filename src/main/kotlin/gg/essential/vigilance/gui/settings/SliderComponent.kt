package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.boundTo
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.gui.VigilancePalette
import kotlin.math.roundToInt

class SliderComponent(initialValue: Int, min: Int, max: Int) : AbstractSliderComponent() {
    private val minText = UIText(min.toString()).constrain {
        y = CenterConstraint()
        color = VigilancePalette.midTextState.toConstraint()
    } childOf this

    override val slider = Slider((initialValue.toFloat() - min) / (max - min)).constrain {
        x = SiblingConstraint()
        width = 60.pixels()
        height = 12.pixels()
    } childOf this

    private val maxText = UIText(max.toString()).constrain {
        x = SiblingConstraint()
        y = CenterConstraint()
        color = VigilancePalette.midTextState.toConstraint()
    } childOf this

    private val currentValueText = UIText(initialValue.toString()).constrain {
        x = CenterConstraint() boundTo slider.grabBox
        y = RelativeConstraint(1.5f)
        color = VigilancePalette.midTextState.toConstraint()
    } childOf slider

    init {
        slider.onValueChange { newPercentage ->
            val newValue = (min + (newPercentage * (max - min))).roundToInt()
            changeValue(newValue)
            currentValueText.setText(newValue.toString())
        }

        sliderInit()
    }
}
