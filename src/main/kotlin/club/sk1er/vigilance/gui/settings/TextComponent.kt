package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.input.AbstractTextInput
import club.sk1er.elementa.components.input.UIMultilineTextInput
import club.sk1er.elementa.components.input.UITextInput
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.VigilancePalette
import java.awt.Color

class TextComponent(propertyData: PropertyData, wrap: Boolean) : SettingComponent(propertyData) {
    private val placeholder = propertyData.attributes.placeholder

    private val textHolder = UIBlock(VigilancePalette.DARK_HIGHLIGHT).constrain {
        width = ChildBasedSizeConstraint() + 6.pixels()
        height = ChildBasedSizeConstraint() + 6.pixels()
    } childOf this effect OutlineEffect(VigilancePalette.DIVIDER, 0.5f)

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

    override fun externalSetValue(newValue: Any?) {
        if (newValue !is String)
            throw IllegalArgumentException("TextComponent externalSetValue expected a String type, found ${newValue?.javaClass?.simpleName}")
        textInput.setText(newValue)
    }

    override fun animationFrame() {
        super.animationFrame()

        if (!hasSetInitialText) {
            textInput.setText(propertyData.getValue())
            hasSetInitialText = true
        }
    }

    override fun closePopups() {
        textInput.setActive(false)
    }
}
