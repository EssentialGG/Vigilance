package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIRoundedRectangle
import club.sk1er.elementa.components.UIWrappedText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.ClickEffect
import club.sk1er.vigilance.gui.VigilancePalette
import club.sk1er.vigilance.gui.withAlpha

class ButtonComponent(private val data: PropertyData) : SettingComponent() {
    private val buttonText = data.property.placeholder.let {
        if (it.isEmpty()) "Activate" else it
    }

    private val container = UIRoundedRectangle(2f).constrain {
        width = ChildBasedSizeConstraint() + 2.pixels()
        height = ChildBasedSizeConstraint() + 2.pixels()
        color = VigilancePalette.OUTLINE.asConstraint()
    } childOf this

    private val contentContainer = UIRoundedRectangle(2f).constrain {
        x = 1.pixel()
        y = 1.pixel()
        width = ChildBasedSizeConstraint()
        height = ChildBasedSizeConstraint() + 10.pixels()
        color = VigilancePalette.LIGHT_BACKGROUND.asConstraint()
    } childOf container

    private val text = UIWrappedText(buttonText, trimText = true).constrain {
        x = CenterConstraint() + 10.pixels()
        y = CenterConstraint()
        width = basicWidthConstraint { (buttonText.width(getTextScale()) + 20f).coerceAtMost(300f) }
        height = 9.pixels()
        color = VigilancePalette.MID_TEXT.asConstraint()
    } childOf contentContainer

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        enableEffect(ClickEffect(VigilancePalette.ACCENT.withAlpha(0.5f)))

        container.onMouseEnter {
            container.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.ACCENT.asConstraint())
            }
        }.onMouseLeave {
            container.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.OUTLINE.asConstraint())
            }
        }.onMouseClick {
            data.action.let {
                if (it == null)
                    throw IllegalStateException("Expected button property \"${data.property.name}\" to have an action")
                it(0)
            }
        }
    }
}
