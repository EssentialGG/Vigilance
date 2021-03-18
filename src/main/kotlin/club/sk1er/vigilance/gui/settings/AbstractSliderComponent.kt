package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.dsl.constrain

abstract class AbstractSliderComponent : SettingComponent() {
    protected abstract val slider: Slider

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedMaxSizeConstraint()
        }
    }

    fun incrementBy(inc: Float) {
        slider.setCurrentPercentage(slider.getCurrentPercentage() + inc)
    }
}