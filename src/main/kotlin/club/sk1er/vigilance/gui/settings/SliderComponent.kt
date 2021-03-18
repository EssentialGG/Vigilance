package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.boundTo
import club.sk1er.elementa.dsl.childOf
import club.sk1er.elementa.dsl.constrain
import club.sk1er.elementa.dsl.pixels
import club.sk1er.elementa.state.toConstraint
import club.sk1er.vigilance.gui.VigilancePalette
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
