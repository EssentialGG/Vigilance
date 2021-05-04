package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.components.UIWrappedText
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.elementa.state.toConstraint
import club.sk1er.elementa.transitions.RecursiveFadeInTransition
import club.sk1er.elementa.transitions.RecursiveFadeOutTransition
import club.sk1er.vigilance.Vigilant
import java.awt.Color

class Divider(val name: String, description: String?) : Setting() {
    private val label = UIText(name).constrain {
        x = CenterConstraint()
        y = 5.pixels()
        color = VigilancePalette.brightTextState.toConstraint()
    } childOf this

    private val leftLine = UIBlock().constrain {
        x = 0.pixels()
        y = basicYConstraint { label.getTop() + label.getHeight() / 2f }
        width = basicWidthConstraint { label.getLeft() - getLeft() - 10f }
        height = 1.pixels()
        color = VigilancePalette.darkDividerState.toConstraint()
    } childOf this

    private val rightLine = UIBlock().constrain {
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
            } childOf textContainer
        } else {
            constrain {
                height = ChildBasedMaxSizeConstraint() + 10.pixels()
            }
        }

        animateAfterUnhide {
            RecursiveFadeInTransition(.5f).transition(this@Divider)
        }
    }

    fun hideMaybe(h: Boolean) {
        if (h != hidden) {
            if (h) {
                hidden = true
                RecursiveFadeOutTransition(.5f).transition(this) {
                    if (hidden) {
                        hide(true)
                    }
                }
            } else {
                hidden = false
                unhide()
            }
        }
    }
}
