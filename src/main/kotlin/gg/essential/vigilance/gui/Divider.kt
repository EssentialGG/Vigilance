package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.state.toConstraint

class Divider(val name: String, description: String?) : Setting() {
    private val label by UIText(name).constrain {
        x = CenterConstraint()
        y = 5.pixels()
        color = VigilancePalette.brightTextState.toConstraint()
        fontProvider = DefaultFonts.VANILLA_FONT_RENDERER
    } childOf this

    private val leftLine by UIBlock().constrain {
        x = 0.pixels()
        y = basicYConstraint { label.getTop() + label.getHeight() / 2f }
        width = basicWidthConstraint { label.getLeft() - getLeft() - 10f }
        height = 1.pixels()
        color = VigilancePalette.darkDividerState.toConstraint()
    } childOf this

    private val rightLine by UIBlock().constrain {
        x = basicXConstraint { label.getRight() + 10f }
        y = basicYConstraint { label.getTop() + label.getHeight() / 2f }
        width = FillConstraint()
        height = 1.pixels()
        color = VigilancePalette.darkDividerState.toConstraint()
    } childOf this

    internal var hidden: Boolean = false

    init {
        if (description != null) {
            constrain {
                height = ChildBasedSizeConstraint() + (DataBackedSetting.INNER_PADDING * 2f).pixels()
            }

            val textContainer = UIContainer().constrain {
                x = DataBackedSetting.INNER_PADDING.pixels()
                y = SiblingConstraint(DataBackedSetting.INNER_PADDING) boundTo label
                width = 100.percent() - (DataBackedSetting.INNER_PADDING * 2f).pixels()
                height = ChildBasedMaxSizeConstraint()
            } childOf this

            UIWrappedText(description, centered = true).constrain {
                x = CenterConstraint()
                y = SiblingConstraint() + 3.pixels()
                width = 70.percent()
                color = VigilancePalette.midTextState.toConstraint()
                fontProvider = DefaultFonts.VANILLA_FONT_RENDERER
            } childOf textContainer
        } else {
            constrain {
                height = ChildBasedMaxSizeConstraint() + 10.pixels()
            }
        }
    }

    fun hideMaybe(h: Boolean) {
        if (h != hidden) {
            if (h) {
                hidden = true
                if (hidden) {
                    hide(true)
                }
            } else {
                hidden = false
                unhide()
            }
        }
    }
}
