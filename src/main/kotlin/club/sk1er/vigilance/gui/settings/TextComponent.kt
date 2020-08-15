package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.input.AbstractTextInput
import club.sk1er.elementa.components.input.UIMultilineTextInput
import club.sk1er.elementa.components.input.UITextInput
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import java.awt.Color

class TextComponent(private val initial: String, placeholder: String, wrap: Boolean) : SettingComponent() {
    private val textHolder = UIBlock(Color(33, 34, 38)).constrain {
        width = ChildBasedSizeConstraint() + 6.pixels()
        height = ChildBasedSizeConstraint() + 6.pixels()
    } childOf this effect OutlineEffect(Color(80, 80, 80), 0.5f)

    private val textInput: AbstractTextInput = if (wrap) {
        UIMultilineTextInput(placeholder).constrain {
            x = 3.pixels()
            y = 3.pixels()
            width = basicWidthConstraint { this@TextComponent.parent.getWidth() * 0.4f }
        }.setMaxLines(10)
    } else {
        UITextInput(placeholder).constrain {
            x = 3.pixels()
            y = 3.pixels()
        }.setMinWidth(50.pixels()).setMaxWidth(
            basicWidthConstraint { this@TextComponent.parent.getWidth() * 0.5f }
        )
    }

    private var hasSetInitialText = false

    init {
        textInput childOf textHolder
        textInput.onUpdate { newText ->
            changeValue(newText)
        }.onMouseClick { event ->
            event.stopPropagation()

            textInput.grabWindowFocus()
        }.onFocus {
            textInput.setActive(true)
        }.onFocusLost {
            textInput.setActive(false)
        }

        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }
    }

    override fun animationFrame() {
        super.animationFrame()

        if (!hasSetInitialText) {
            textInput.setText(initial)
            hasSetInitialText = true
        }
    }

    override fun closePopups() {
        textInput.setActive(false)
    }
}