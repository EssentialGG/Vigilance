package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.gui.SettingsGui
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick

class SwitchComponent(initialState: Boolean) : SettingComponent() {
    internal var enabled = initialState

    private val switchBox = UIBlock(getSwitchColor()).constrain {
        x = getSwitchPosition()
        width = RelativeConstraint(0.5f)
        height = RelativeConstraint(1f)
    } childOf this

    init {
        constrain {
            width = 20.pixels()
            height = 10.pixels()
        }

        effect(getOutlineEffect())

        onLeftClick {
            enabled = !enabled
            changeValue(enabled)

            removeEffect<OutlineEffect>()
            enableEffect(getOutlineEffect())

            switchBox.setColor(getSwitchColor().toConstraint())
            switchBox.animate {
                setXAnimation(Animations.OUT_EXP, 0.5f, getSwitchPosition())
            }
        }

        onMouseEnter {
            switchBox.animate {
                setColorAnimation(Animations.OUT_EXP, .25f, getSwitchColor().map { it.darker() }.toConstraint())
            }
        }

        onMouseLeave {
            switchBox.animate {
                setColorAnimation(Animations.OUT_EXP, .25f, getSwitchColor().toConstraint())
            }
        }
    }

    private fun getOutlineEffect() = OutlineEffect(getSwitchColor().get(), 0.5f).bindColor(getSwitchColor())

    private fun getSwitchColor() = if (enabled) VigilancePalette.accentState else VigilancePalette.brightDividerState

    private fun getSwitchPosition() = if (enabled) 0.pixels(true) else 0.pixels()
}
