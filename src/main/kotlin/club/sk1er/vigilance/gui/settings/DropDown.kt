package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.*
import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.vigilance.gui.SettingsGui
import net.minecraft.client.Minecraft
import java.awt.Color

class DropDown(initialSelection: Int, private val options: List<String>) : UIBlock(Color(33, 34, 38)) {
    private var selected = initialSelection
    private var onValueChange: (Int) -> Unit = { }
    private var active = false

    private val currentSelectionText = UIText(options[selected]).constrain {
        x = 5.pixels()
        y = 6.pixels()
        color = SettingsGui.GRAY_COLOR.asConstraint()
    } childOf this

    private val dropDownArrow = SVGComponent(SettingComponent.DOWN_ARROW_SVG).constrain {
        x = 5.pixels(true)
        y = 5.pixels()
        width = 10.pixels()
        height = 10.pixels()
    } childOf this

    private val optionsHolder = UIContainer().constrain {
        x = 5.pixels()
        y = 21.pixels()
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint(OPTION_PADDING) + OPTION_PADDING.pixels()
    } childOf this

    init {
        constrain {
            width = 22.pixels() + ChildBasedMaxSizeConstraint().to(optionsHolder)
            height = 20.pixels()
        }

        options.forEachIndexed { index, opt ->
            // TODO: Wrap this somehow
            UIText(opt).constrain {
                y = SiblingConstraint(OPTION_PADDING)
                color = SettingsGui.GRAY_COLOR.asConstraint()
            }.onMouseEnter {
                hoverText(this)
            }.onMouseLeave {
                unHoverText(this)
            }.onMouseClick { event ->
                event.stopPropagation()
                select(index)
            } childOf optionsHolder
        }

        enableEffect(OutlineEffect(Color(80, 80, 80), 0.5f))
        val outlineContainer = UIContainer().constrain {
            x = (-1).pixels()
            y = (-1).pixels()
            width = RelativeConstraint(1f) + 2.pixels()
            height = RelativeConstraint(1f) + 2.5f.pixels()
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
            (Minecraft.getMinecraft().currentScreen as? SettingsGui)?.hideSearch()

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
    }

    private fun expand() {
        active = true

        animate {
            setHeightAnimation(Animations.IN_SIN, 0.35f, 20.pixels() + RelativeConstraint(1f).to(optionsHolder))
        }

        dropDownArrow.setSVG(SettingComponent.UP_ARROW_SVG)
    }

    fun collapse(unHover: Boolean = false) {
        active = false

        animate {
            setHeightAnimation(Animations.OUT_SIN, 0.35f, 20.pixels())
        }

        if (unHover) {
            unHoverText(currentSelectionText)
        }

        dropDownArrow.setSVG(SettingComponent.DOWN_ARROW_SVG)
    }

    private fun hoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, Color.WHITE.asConstraint())
        }
    }

    private fun unHoverText(text: UIComponent) {
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.25f, SettingsGui.GRAY_COLOR.asConstraint())
        }
    }

    fun onValueChange(listener: (Int) -> Unit) {
        onValueChange = listener
    }

    companion object {
        private const val OPTION_PADDING = 5f
    }
}