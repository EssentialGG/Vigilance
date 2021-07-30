package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.state.toConstraint
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.USound
import gg.essential.vigilance.data.CallablePropertyValue
import gg.essential.vigilance.data.PropertyData
import gg.essential.vigilance.gui.ExpandingClickEffect
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick

class ButtonComponent(placeholder: String? = null, private val callback: () -> Unit) : SettingComponent() {
    private val buttonText = placeholder ?: "Activate"

    internal val container by UIRoundedRectangle(2f).constrain {
        width = ChildBasedSizeConstraint() + 2.pixels()
        height = ChildBasedSizeConstraint() + 2.pixels()
        color = VigilancePalette.outlineState.toConstraint()
    } childOf this

    private val contentContainer by UIRoundedRectangle(2f).constrain {
        x = 1.pixel()
        y = 1.pixel()
        width = ChildBasedSizeConstraint() + 20.pixels()
        height = ChildBasedSizeConstraint() + 10.pixels()
        color = VigilancePalette.lightBackgroundState.toConstraint()
    } childOf container

    init {
        UIWrappedText(buttonText, trimText = true).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = width.coerceAtMost(300.pixels())
            height = 10.pixels()
            color = VigilancePalette.midTextState.toConstraint()
            fontProvider = DefaultFonts.VANILLA_FONT_RENDERER
        } childOf contentContainer
    }

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        // For some reason the width and height for the scissor need to be an additional pixel
        val bbox = UIContainer().constrain {
            x = contentContainer.constraints.x
            y = contentContainer.constraints.y
            width = contentContainer.constraints.width + 1.pixel()
            height = contentContainer.constraints.height + 1.pixels()
        }

        bbox.parent = container

        enableEffect(ExpandingClickEffect(VigilancePalette.getAccent().withAlpha(0.5f), scissorBoundingBox = bbox))

        container.onMouseEnter {
            container.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.accentState.toConstraint())
            }
        }.onMouseLeave {
            container.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.outlineState.toConstraint())
            }
        }.onLeftClick {
            USound.playButtonPress()
            callback()
        }
    }

    constructor(placeholder: String? = null, data: PropertyData) : this(placeholder, callbackFromPropertyData(data))

    companion object {
        private fun callbackFromPropertyData(data: PropertyData): () -> Unit {
            val value = data.value
            if (value !is CallablePropertyValue)
                throw IllegalStateException()

            return { value.invoke(data.instance) }
        }
    }
}
