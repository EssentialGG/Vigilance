package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.SettingsGui
import club.sk1er.vigilance.gui.VigilancePalette

class SwitchComponent(propertyData: PropertyData) : SettingComponent(propertyData) {
    private var enabled = propertyData.getValue<Boolean>()

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

        onMouseClick {
            changeValue(!enabled)
            setEnabled(!enabled)
        }
    }

    override fun externalSetValue(newValue: Any?) {
        if (newValue !is Boolean)
            throw IllegalArgumentException("SwitchComponent externalSetValue expected a Boolean type, found ${newValue?.javaClass?.simpleName}")
        setEnabled(newValue)
    }

    private fun setEnabled(enabled: Boolean) {
        this.enabled = enabled

        removeEffect<OutlineEffect>()
        enableEffect(getOutlineEffect())

        switchBox.setColor(getSwitchColor().asConstraint())
        switchBox.animate {
            setXAnimation(Animations.OUT_EXP, 0.5f, getSwitchPosition())
        }
    }

    private fun getOutlineEffect() = OutlineEffect(getSwitchColor(), 0.5f)

    private fun getSwitchColor() = if (enabled) VigilancePalette.ACCENT else VigilancePalette.BRIGHT_DIVIDER

    private fun getSwitchPosition() = if (enabled) 0.pixels(true) else 0.pixels()
}
