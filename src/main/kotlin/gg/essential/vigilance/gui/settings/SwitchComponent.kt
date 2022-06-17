package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.USound
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color

class SwitchComponent(initialState: Boolean) : SettingComponent() {

    internal var enabled = initialState

    private val switchBox = UIBlock(getSwitchColor()).constrain {
        x = getSwitchPosition()
        width = 50.percent
        height = 100.percent
    } childOf this

    init {
        constrain {
            width = 18.pixels
            height = 9.pixels
        }

        effect(getOutlineEffect())

        onLeftClick {
            USound.playButtonPress()
            enabled = !enabled
            changeValue(enabled)

            removeEffect<OutlineEffect>()
            enableEffect(getOutlineEffect())

            switchBox.setColor((if (isHovered()) getSwitchColor().map { it.brighter() } else getSwitchColor()).toConstraint())
            switchBox.animate {
                setXAnimation(Animations.OUT_EXP, 0.5f, getSwitchPosition())
            }
        }

        onMouseEnter {
            switchBox.animate {
                setColorAnimation(Animations.OUT_EXP, .25f, getSwitchColor().map { it.brighter() }.toConstraint())
            }
        }

        onMouseLeave {
            switchBox.animate {
                setColorAnimation(Animations.OUT_EXP, .25f, getSwitchColor().toConstraint())
            }
        }
    }

    private fun getOutlineEffect(): OutlineEffect = OutlineEffect(getSwitchColor().get(), 1f).bindColor(getSwitchColor())

    private fun getSwitchColor(): BasicState<Color> = if (enabled) VigilancePalette.green else VigilancePalette.text

    private fun getSwitchPosition(): PixelConstraint = if (enabled) 0.pixels else 0.pixels(alignOpposite = true)
}
