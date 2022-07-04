package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.USound
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick

class CheckboxComponent(initialValue: Boolean) : SettingComponent() {

    var checked: Boolean = initialValue
        set(value) {
            changeValue(value)
            field = value
        }

    private val checkmark = UIImage.ofResourceCached("/vigilance/check.png").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 16.pixels
        height = 12.pixels
        color = VigilancePalette.primary.toConstraint()
    } childOf this

    init {
        constrain {
            width = 20.pixels
            height = AspectConstraint()
        }

        effect(getOutlineEffect())

        if (!checked)
            checkmark.hide(instantly = true)

        onLeftClick {
            USound.playButtonPress()
            checked = !checked

            removeEffect<OutlineEffect>()
            effect(getOutlineEffect())

            if (checked) {
                checkmark.unhide()
            } else {
                checkmark.hide()
            }
        }
    }

    private fun getOutlineEffect() = OutlineEffect(getSettingColor().get(), 1f).bindColor(getSettingColor())

    private fun getSettingColor() = if (checked) VigilancePalette.primary else VigilancePalette.componentBorder
}
