package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.VigilancePalette
import kotlin.math.roundToInt

class SliderComponent(propertyData: PropertyData) : SettingComponent(propertyData) {
    private val initialValue = propertyData.getValue<Int>()
    private val min = propertyData.attributes.min
    private val max = propertyData.attributes.max

    private val minText = UIText(min.toString()).constrain {
        y = CenterConstraint()
        color = VigilancePalette.MID_TEXT.asConstraint()
    } childOf this

    private val slider = Slider((initialValue.toFloat() - min) / (max - min)).constrain {
        x = SiblingConstraint()
        width = 60.pixels()
        height = 12.pixels()
    } childOf this

    private val maxText = UIText(max.toString()).constrain {
        x = SiblingConstraint()
        y = CenterConstraint()
        color = VigilancePalette.MID_TEXT.asConstraint()
    } childOf this

    private val currentValueText = UIText(initialValue.toString()).constrain {
        x = CenterConstraint().to(slider.grabBox) as XConstraint
        y = RelativeConstraint(1.5f)
        color = VigilancePalette.MID_TEXT.asConstraint()
    } childOf slider

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedMaxSizeConstraint()
        }

        slider.onValueChange { newPercentage ->
            val newValue = (min + (newPercentage * (max - min))).roundToInt()
            changeValue(newValue)
            currentValueText.setText(newValue.toString())
        }
    }

    override fun externalSetValue(newValue: Any?) {
        if (newValue !is Int)
            throw IllegalArgumentException("SliderComponent externalSetValue expected an Int type, found ${newValue?.javaClass?.simpleName}")
        val newPercentage = (newValue.toFloat() - min) / (max - min)
        slider.setCurrentPercentage(newPercentage)
        currentValueText.setText(newValue.toString())
    }
}
