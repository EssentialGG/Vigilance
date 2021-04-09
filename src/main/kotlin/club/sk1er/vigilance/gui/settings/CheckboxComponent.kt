package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIImage
import club.sk1er.elementa.constraints.AspectConstraint
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.elementa.state.toConstraint
import club.sk1er.vigilance.gui.VigilancePalette

class CheckboxComponent(initialValue: Boolean) : SettingComponent() {
    var checked: Boolean = initialValue
        set(value) {
            changeValue(value)
            field = value
        }

    private val checkmark = UIImage.ofResource("/vigilance/check.png").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 16.pixels()
        height = 12.pixels()
        color = getSettingColor().toConstraint()
    } childOf this

    init {
        constrain {
            width = 20.pixels()
            height = AspectConstraint()
        }

        effect(getOutlineEffect())

        if (!checked)
            checkmark.hide(instantly = true)

        onMouseClick {
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

    private fun getOutlineEffect() = OutlineEffect(getSettingColor().get(), 0.5f).bindColor(getSettingColor())

    private fun getSettingColor() = if (checked) VigilancePalette.accentState else VigilancePalette.brightDividerState
}
