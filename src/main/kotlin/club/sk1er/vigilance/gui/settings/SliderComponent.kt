package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.gui.SettingsGui
import kotlin.math.roundToInt

class SliderComponent(initialValue: Int, min: Int, max: Int) : SettingComponent() {
    private val minText = UIText(min.toString()).constrain {
        y = CenterConstraint()
        color = SettingsGui.GRAY_COLOR.asConstraint()
    } childOf this

    private val slider = Slider(initialValue.toFloat() / (max - min)).constrain {
        x = SiblingConstraint(PADDING)
        width = 60.pixels()
        height = 10.pixels()
    } childOf this

    private val maxText = UIText(max.toString()).constrain {
        x = SiblingConstraint(PADDING)
        y = CenterConstraint()
        color = SettingsGui.GRAY_COLOR.asConstraint()
    } childOf this

    private val currentValueText = UIText(initialValue.toString()).constrain {
        x = CenterConstraint().to(slider.grabBox) as XConstraint
        y = RelativeConstraint(1.5f)
        color = SettingsGui.GRAY_COLOR.asConstraint()
    } childOf slider

    init {
        constrain {
            width = ChildBasedSizeConstraint(PADDING)
            height = ChildBasedMaxSizeConstraint()
        }

        slider.onValueChange { newPercentage ->
            val newValue = (min + (newPercentage * (max - min))).roundToInt()
            changeValue(newValue)
            currentValueText.setText(newValue.toString())
        }
    }

    companion object {
        private const val PADDING = 7f
    }
}