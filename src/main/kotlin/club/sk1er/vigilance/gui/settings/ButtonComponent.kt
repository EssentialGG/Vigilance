package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIRoundedRectangle
import club.sk1er.elementa.components.UIWrappedText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.state.toConstraint
import club.sk1er.elementa.utils.withAlpha
import club.sk1er.vigilance.data.CallablePropertyValue
import club.sk1er.vigilance.data.KFunctionBackedPropertyValue
import club.sk1er.vigilance.data.MethodBackedPropertyValue
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.ExpandingClickEffect
import club.sk1er.vigilance.gui.VigilancePalette

class ButtonComponent(placeholder: String? = null, private val callback: () -> Unit) : SettingComponent() {
    private val buttonText = placeholder ?: "Activate"

    internal val container = UIRoundedRectangle(2f).constrain {
        width = ChildBasedSizeConstraint() + 2.pixels()
        height = ChildBasedSizeConstraint() + 2.pixels()
        color = VigilancePalette.outlineState.toConstraint()
    } childOf this

    private val contentContainer = UIRoundedRectangle(2f).constrain {
        x = 1.pixel()
        y = 1.pixel()
        width = ChildBasedSizeConstraint()
        height = ChildBasedSizeConstraint() + 10.pixels()
        color = VigilancePalette.lightBackgroundState.toConstraint()
    } childOf container

    private val text = UIWrappedText(buttonText, trimText = true).constrain {
        x = CenterConstraint() + 10.pixels()
        y = CenterConstraint()
        width = basicWidthConstraint { (buttonText.width(getTextScale()) + 20f).coerceAtMost(300f) }
        height = 9.pixels()
        color = VigilancePalette.midTextState.toConstraint()
    } childOf contentContainer

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
        }.onMouseClick { callback() }
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
