package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.*
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.vigilance.gui.VigilancePalette
import java.awt.Color

class DropDown(
    initialSelection: Int,
    private val options: List<String>,
    backgroundColor: Color = VigilancePalette.HIGHLIGHT,
    outlineEffect: OutlineEffect? = OutlineEffect(VigilancePalette.DARK_DIVIDER, 0.5f),
    optionPadding: Float = 5f
) : UIBlock(backgroundColor) {
    private var selected = initialSelection
    private var onValueChange: (Int) -> Unit = { }
    private var active = false

    private val currentSelectionText = UIText(options[selected]).constrain {
        x = 5.pixels()
        y = 6.pixels()
        color = VigilancePalette.MID_TEXT.asConstraint()
    } childOf this

    private val downArrow = UIImage.ofResource(SettingComponent.DOWN_ARROW_PNG).constrain {
        x = 5.pixels(true)
        y = 7.5.pixels()
        width = 9.pixels()
        height = 5.pixels()
    } childOf this

    private val upArrow = UIImage.ofResource(SettingComponent.UP_ARROW_PNG).constrain {
        x = 5.pixels(true)
        y = 7.5.pixels()
        width = 9.pixels()
        height = 5.pixels()
    }

    private val optionsHolder = UIContainer().constrain {
        x = 5.pixels()
        y = 22.pixels()
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint(optionPadding) + optionPadding.pixels()
    } childOf this

    private val mappedOptions = options.mapIndexed { index, option ->
        // TODO: Wrap this somehow
        UIText(option).constrain {
            y = SiblingConstraint(optionPadding)
            color = VigilancePalette.MID_TEXT.asConstraint()
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
            width = 22.pixels() + ChildBasedMaxSizeConstraint().to(optionsHolder)
            height = 20.pixels()
        }

        readdOptionComponents()

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

        onMouseClick { event ->
            event.stopPropagation()

            if (active) {
                collapse()
            } else {
                expand()
            }
        }
    }

    fun select(index: Int) {
        selected = index
        onValueChange(index)
        currentSelectionText.setText(options[index])
        collapse()
        readdOptionComponents()
    }

    fun onValueChange(listener: (Int) -> Unit) {
        onValueChange = listener
    }

    fun getValue() = selected

    private fun expand() {
        active = true

        animate {
            setHeightAnimation(Animations.IN_SIN, 0.35f, 20.pixels() + RelativeConstraint(1f).to(optionsHolder))
        }

        replaceChild(upArrow, downArrow)
    }

    fun collapse(unHover: Boolean = false) {
        active = false

        animate {
            setHeightAnimation(Animations.OUT_SIN, 0.35f, 20.pixels())
        }

        if (unHover) {
            unHoverText(currentSelectionText)
        }

        replaceChild(downArrow, upArrow)
    }

    private fun hoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, VigilancePalette.BRIGHT_TEXT.asConstraint())
        }
    }

    private fun unHoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, VigilancePalette.MID_TEXT.asConstraint())
        }
    }

    private fun readdOptionComponents() {
        optionsHolder.clearChildren()
        mappedOptions.forEachIndexed { index, component ->
            if (index != selected)
                component childOf optionsHolder
        }
    }
}
