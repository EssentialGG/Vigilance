package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.elementa.state.toConstraint
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.USound
import gg.essential.vigilance.data.CallablePropertyValue
import gg.essential.vigilance.data.PropertyData
import gg.essential.vigilance.gui.ExpandingClickEffect
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.impl.I18n
import gg.essential.vigilance.utils.onLeftClick

class ButtonComponent(placeholder: String? = null, private val callback: () -> Unit) : SettingComponent() {

    private var textState: State<String> = BasicState(placeholder.orEmpty().ifEmpty { "Activate" }).map { I18n.format(it) }
    private var listener: () -> Unit = textState.onSetValue {
        text.setText(textState.get())
    }

    val container by UIBlock(VigilancePalette.button).constrain {
        x = 1.pixel
        y = 1.pixel
        width = ChildBasedSizeConstraint() + 14.pixels
        height = ChildBasedSizeConstraint() + 8.pixels
    } childOf this

    private val text by UIWrappedText(textState.get(), trimText = true).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = width.coerceAtMost(300.pixels)
        height = 10.pixels
        color = VigilancePalette.text.toConstraint()
    } childOf container

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        enableEffect(ExpandingClickEffect(VigilancePalette.getGreen().withAlpha(0.5f), scissorBoundingBox = container))

        container.onMouseEnter {
            container.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.buttonHighlight.toConstraint())
            }
        }.onMouseLeave {
            container.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.button.toConstraint())
            }
        }.onLeftClick {
            USound.playButtonPress()
            callback()
        }
    }

    fun bindText(newTextState: State<String>) = apply {
        listener()
        textState = newTextState
        text.bindText(textState.map { I18n.format(it) })

        listener =  textState.onSetValue {
            text.setText(textState.get())
        }
    }

    fun getText() = textState.get()
    fun setText(text: String) = apply { textState.set(text) }

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
