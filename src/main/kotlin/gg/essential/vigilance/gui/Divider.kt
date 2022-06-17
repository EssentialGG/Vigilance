package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.toConstraint

class Divider(val name: String, description: String?) : Setting() {
    private val dividerContainer by UIContainer().constrain {
        width = 100.percent
        height = ChildBasedMaxSizeConstraint()
    } childOf this

    private val textContainer by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ChildBasedSizeConstraint() + 16.pixels
        height = ChildBasedMaxSizeConstraint()
    } childOf dividerContainer

    init {
        UIText(name).constrain {
            x = CenterConstraint()
            color = VigilancePalette.text.toConstraint()
        } childOf textContainer

        // Divider line left
        UIBlock(VigilancePalette.textDisabled).constrain {
            y = CenterConstraint()
            width = 50.percent - (100.percent boundTo textContainer) / 2
            height = 1.pixel
        } childOf dividerContainer

        // Divider line middle
        if (name.isEmpty()) {
            UIBlock(VigilancePalette.textDisabled).constrain {
                x = 0.pixels boundTo textContainer
                y = CenterConstraint()
                width = 100.percent boundTo textContainer
                height = 1.pixel
            } childOf dividerContainer
        }

        // Divider line right
        UIBlock(VigilancePalette.textDisabled).constrain {
            x = SiblingConstraint() boundTo textContainer
            y = CenterConstraint()
            width = FillConstraint()
            height = 1.pixel
        } childOf dividerContainer

        if (description != null) {
            UIWrappedText(description, centered = true).constrain {
                x = CenterConstraint()
                y = SiblingConstraint(DataBackedSetting.INNER_PADDING) boundTo textContainer
                width = 100.percent - (DataBackedSetting.INNER_PADDING * 2f).pixels
                height += (DataBackedSetting.INNER_PADDING - 8f).pixels
                color = VigilancePalette.text.toConstraint()
            } childOf this
        }

        constrain {
            y = SiblingConstraint(16f)
            height = ChildBasedSizeConstraint()
        }
    }

    internal var hidden: Boolean = false

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
