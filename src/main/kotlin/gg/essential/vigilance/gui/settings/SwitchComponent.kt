package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.USound
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.*
import gg.essential.vigilance.utils.and
import gg.essential.vigilance.utils.bindParent
import gg.essential.vigilance.utils.pollingState
import java.awt.Color

class SwitchComponent(initialState: Boolean) : SettingComponent() {

    internal val enabled = BasicState(initialState)

    private val background by UIBlock(getSwitchColor()).constrain {
        width = 100.percent
        height = 100.percent
    } childOf this

    private val switchBox = UIBlock(VigilancePalette.componentBackground).constrain {
        x = getSwitchPosition()
        y = CenterConstraint()
        width = AspectConstraint()
        height = 100.percent - 2.pixels
    } childOf this

    // Property set by Essential
    private val showToggleIndicators = pollingState {
        System.getProperty("essential.hideSwitchIndicators") != "true"
    }

    private val onIndicator by UIContainer().constrain {
        height = 100.percent
        width = 50.percent
    }.addChild {
        VigilancePalette.TOGGLE_ON_1X5.withColor(VigilancePalette.componentBackground.get()).create().constrain {
            x = CenterConstraint()
            y = CenterConstraint()
        }
    }.bindParent(this, showToggleIndicators and enabled, index = 1)

    private val offIndicator by UIContainer().constrain {
        x = 0.pixels(alignOpposite = true)
        height = 100.percent
        width = 50.percent
    }.addChild {
        VigilancePalette.TOGGLE_OFF_4X5.withColor(VigilancePalette.getMidGray()).create().constrain {
            x = CenterConstraint()
            y = CenterConstraint()
        }
    }.bindParent(this, showToggleIndicators and !enabled, index = 1)

    init {
        constrain {
            width = 20.pixels
            height = 11.pixels
        }

        onLeftClick {
            USound.playButtonPress()
            enabled.set { !it }
            changeValue(enabled.get())

            background.setColor(getSwitchColor().toConstraint())
            switchBox.animate {
                setXAnimation(Animations.OUT_EXP, 0.5f, getSwitchPosition())
            }
        }

        onMouseEnter {
            switchBox.animate {
                setColorAnimation(Animations.OUT_EXP, .25f, VigilancePalette.componentBackground.map { it.brighter() }.toConstraint())
            }
        }

        onMouseLeave {
            switchBox.animate {
                setColorAnimation(Animations.OUT_EXP, .25f, VigilancePalette.componentBackground.toConstraint())
            }
        }
    }

    private fun getSwitchColor(): BasicState<Color> = if (enabled.get()) VigilancePalette.primary else VigilancePalette.text

    private fun getSwitchPosition(): PixelConstraint = if (enabled.get()) 1.pixels(alignOpposite = true) else 1.pixels
}
