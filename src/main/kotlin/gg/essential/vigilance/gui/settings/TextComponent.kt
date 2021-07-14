package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.input.AbstractTextInput
import gg.essential.elementa.components.input.UIMultilineTextInput
import gg.essential.elementa.components.input.UIPasswordInput
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick

class TextComponent(
    private val initial: String,
    placeholder: String,
    wrap: Boolean,
    protected: Boolean
) : SettingComponent() {
    private val textHolder = UIBlock().constrain {
        width = ChildBasedSizeConstraint() + (if (protected) 14 else 6).pixels()
        height = ChildBasedSizeConstraint() + 6.pixels()
        color = VigilancePalette.darkHighlightState.toConstraint()
    } childOf this effect OutlineEffect(VigilancePalette.getDivider(), 1f).bindColor(VigilancePalette.dividerState)

    private val textInput: AbstractTextInput = when {
        wrap -> UIMultilineTextInput(placeholder).constrain {
            x = 3.pixels()
            y = 3.pixels()
            width = basicWidthConstraint { this@TextComponent.parent.getWidth() * 0.4f }
        }.setMaxLines(10)
        protected -> UIPasswordInput(placeholder = placeholder).constrain {
            x = 3.pixels()
            y = 3.pixels()
        }.setMinWidth(50.pixels()).setMaxWidth(basicWidthConstraint { this@TextComponent.parent.getWidth() * 0.5f })
        else -> UITextInput(placeholder).constrain {
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
        }.onLeftClick { event ->
            event.stopPropagation()

            textInput.grabWindowFocus()
        }.onFocus {
            textInput.setActive(true)
        }.onFocusLost {
            textInput.setActive(false)
        }

        if (protected) {
            var toggle = false
            UIImage.ofResourceCached("/vigilance/eye.png").constrain {
//            UIBlock().constrain {
                y = 3.pixels()
                x = 3.pixels(true)
                width = 12.pixels()
                height = basicHeightConstraint { textInput.getHeight() }
                color = VigilancePalette.darkTextState.toConstraint()
            }.onMouseEnter {
                if (!toggle) {
                    animate {
                        setColorAnimation(Animations.OUT_EXP, .2f, VigilancePalette.midTextState.toConstraint())
                    }
                }
            }.onMouseLeave {
                if (!toggle) {
                    animate {
                        setColorAnimation(Animations.OUT_EXP, .2f, VigilancePalette.darkTextState.toConstraint())
                    }
                }
            }.onLeftClick {
                toggle = !toggle
                (textInput as UIPasswordInput).setProtection(!toggle)
                animate {
                    setColorAnimation(Animations.OUT_EXP, .2f, if (toggle) {
                        VigilancePalette.warningState
                    } else {
                        VigilancePalette.midTextState
                    }.toConstraint())
                }
            } childOf textHolder

            textHolder.setHeight(basicHeightConstraint { textInput.getHeight() + 6f })
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
