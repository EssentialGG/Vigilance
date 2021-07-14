package gg.essential.vigilance.gui.settings

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color

class DropDown(
    initialSelection: Int,
    private val options: List<String>,
    //backgroundColor: Color = VigilancePalette.DARK_HIGHLIGHT,
    outlineEffect: OutlineEffect? = OutlineEffect(VigilancePalette.getDivider(), 1f).bindColor(VigilancePalette.dividerState),
    optionPadding: Float = 6f
) : UIBlock() {
    private var selected = initialSelection
    private var onValueChange: (Int) -> Unit = { }
    private var active = false

    private val currentSelectionText by UIText(options[selected]).constrain {
        x = 5.pixels()
        y = 6.pixels()
        color = VigilancePalette.midTextState.toConstraint()
        fontProvider = getFontProvider()
    } childOf this

    private val downArrow by UIImage.ofResourceCached(SettingComponent.DOWN_ARROW_PNG).constrain {
        x = 5.pixels(true)
        y = 7.5.pixels()
        width = 9.pixels()
        height = 5.pixels()
    } childOf this

    private val upArrow by UIImage.ofResourceCached(SettingComponent.UP_ARROW_PNG).constrain {
        x = 5.pixels(true)
        y = 7.5.pixels()
        width = 9.pixels()
        height = 5.pixels()
    }

    private val optionsHolder by UIContainer().constrain {
        x = 5.pixels()
        y = SiblingConstraint() boundTo currentSelectionText
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint(optionPadding) + optionPadding.pixels()
    } childOf this

    private val mappedOptions = options.mapIndexed { index, option ->
        // TODO: Wrap this somehow
        UIText(option).constrain {
            y = SiblingConstraint() + optionPadding.pixels()
            color = Color(0, 0, 0, 0).toConstraint()
            fontProvider = getFontProvider()
        }.onMouseEnter {
            hoverText(this)
        }.onMouseLeave {
            unHoverText(this)
        }.onMouseClick { event ->
            event.stopPropagation()
            select(index)
        }
    }

    init {
        constrain {
            width = 22.pixels() + ChildBasedMaxSizeConstraint().boundTo(optionsHolder)
            height = 20.pixels()
            color = VigilancePalette.darkHighlightState.toConstraint()
        }

        readOptionComponents()

        outlineEffect?.let(::enableEffect)

        val outlineContainer = UIContainer().constrain {
            x = (-1).pixels()
            y = (-1).pixels()
            width = RelativeConstraint(1f) + 2.pixels()
            height = RelativeConstraint(1f) + 3f.pixels()
        }
        outlineContainer.parent = this
        children.add(0, outlineContainer)
        enableEffect(ScissorEffect(outlineContainer))

        onMouseEnter {
            hoverText(currentSelectionText)
        }

        onMouseLeave {
            if (active) return@onMouseLeave

            unHoverText(currentSelectionText)
        }

        onLeftClick { event ->
            event.stopPropagation()

            if (active) {
                collapse()
            } else {
                expand()
            }
        }
    }

    fun select(index: Int) {
        if (options.indices.contains(index)) {
            selected = index
            onValueChange(index)
            currentSelectionText.setText(options[index])
            collapse()
            readOptionComponents()
        }
    }

    fun onValueChange(listener: (Int) -> Unit) {
        onValueChange = listener
    }

    fun getValue() = selected

    private fun expand() {
        active = true
        mappedOptions.forEach {
            it.setColor(VigilancePalette.midTextState.toConstraint())
        }

        animate {
            setHeightAnimation(Animations.IN_SIN, 0.35f, 20.pixels() + RelativeConstraint(1f).boundTo(optionsHolder))
        }

        replaceChild(upArrow, downArrow)
    }

    fun collapse(unHover: Boolean = false) {
        if (active)
            replaceChild(downArrow, upArrow)
        active = false

        animate {
            setHeightAnimation(Animations.OUT_SIN, 0.35f, 20.pixels())

            onComplete {
                mappedOptions.forEach {
                    it.setColor(Color(0, 0, 0, 0).toConstraint())
                }
            }
        }

        if (unHover)
            unHoverText(currentSelectionText)
    }

    private fun hoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, VigilancePalette.brightTextState.toConstraint())
        }
    }

    private fun unHoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, VigilancePalette.midTextState.toConstraint())
        }
    }

    private fun readOptionComponents() {
        optionsHolder.clearChildren()
        mappedOptions.forEachIndexed { index, component ->
            if (index != selected)
                component childOf optionsHolder
        }
    }
}
