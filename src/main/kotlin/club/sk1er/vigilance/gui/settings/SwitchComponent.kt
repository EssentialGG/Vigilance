package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.WidthConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.DataBackedSetting
import club.sk1er.vigilance.gui.DataBackedSetting.Companion.INNER_PADDING
import club.sk1er.vigilance.gui.SettingsGui
import java.awt.Color

class SwitchComponent(initialState: Boolean) : SettingComponent() {
    private var enabled = initialState

    init {
        constrain {
            width = 20.pixels()
            height = 10.pixels()
        }

        effect(getOutlineEffect())
    }

    private val switchBox = UIBlock(getSwitchColor()).constrain {
        x = getSwitchPosition()
        width = RelativeConstraint(0.5f)
        height = RelativeConstraint(1f)
    } childOf this

    init {
        onMouseClick {
            enabled = !enabled
            changeValue(enabled)

            removeEffect<OutlineEffect>()
            enableEffect(getOutlineEffect())

            switchBox.setColor(getSwitchColor().asConstraint())
            switchBox.animate {
                setXAnimation(Animations.OUT_EXP, 0.5f, getSwitchPosition())
            }
        }
    }

    private fun getOutlineEffect() = OutlineEffect(getSwitchColor(), 0.5f)

    private fun getSwitchColor() = if (enabled) SettingsGui.ACCENT_COLOR else SettingsGui.DISABLED_COLOR

    private fun getSwitchPosition() = if (enabled) 0.pixels(true) else 0.pixels()
}