package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.USound
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.gui.common.shadow.ShadowIcon
import gg.essential.vigilance.utils.*
import gg.essential.vigilance.utils.ReadyOnlyState
import gg.essential.vigilance.utils.and
import gg.essential.vigilance.utils.hoveredState

internal class DropDown(
    initialSelection: Int,
    private val options: List<String>,
) : UIBlock() {

    private val writableExpandedState: State<Boolean> = BasicState(false)


    /** Public States **/
    val selectedIndex: State<Int> = BasicState(initialSelection)
    val selectedText: State<String> = selectedIndex.map {
        options[it]
    }
    internal val expandedState = ReadyOnlyState(writableExpandedState)

    private val selectedArea by UIContainer().constrain {
        width = 100.percent
        height = 17.pixels
    } childOf this

    private val selectAreaHovered = selectedArea.hoveredState()

    private val currentSelectionText by UIText().bindText(selectedText).constrain {
        x = 5.pixels
        y = CenterConstraint()
        color = VigilancePalette.getTextColor((selectAreaHovered and writableExpandedState)).toConstraint()
    } childOf selectedArea

    private val iconState = writableExpandedState.map {
        if (it) {
            VigilancePalette.ARROW_UP_7X4
        } else {
            VigilancePalette.ARROW_DOWN_7X4
        }
    }

    private val downArrow by ShadowIcon(iconState, BasicState(true)).constrain {
        x = 5.pixels(alignOpposite = true)
        y = CenterConstraint()
    }.rebindPrimaryColor(VigilancePalette.getTextColor(selectAreaHovered)) childOf selectedArea


    private val expandedBlock by UIBlock(VigilancePalette.buttonHighlight).constrain {
        y = SiblingConstraint()
        width = 100.percent
        height = 0.pixels // Start collapsed
    } childOf this effect ScissorEffect()

    private val expandedContentArea by UIBlock(VigilancePalette.componentBackground).centered().constrain {
        width = 100.percent - 4.pixels
        height = ChildBasedSizeConstraint() + 6.pixels
    } childOf expandedBlock

    private val expandedContent by UIContainer().centered().constrain {
        width = 100.percent
        height = ChildBasedSizeConstraint()
    } childOf expandedContentArea

    private fun getMaxItemWidth(): Float {
        return options.maxOf {
            it.width()
        }
    }

    init {
        constrain {
            width = (getMaxItemWidth() + 25).pixels
            height = ChildBasedSizeConstraint()
        }
        setColor((selectAreaHovered or expandedState).map {
            if (it) {
                VigilancePalette.getButtonHighlight()
            } else {
                VigilancePalette.getComponentBackgroundHighlight()
            }
        }.toConstraint())

        options.withIndex().forEach { (index, value) ->
            val optionContainer by UIBlock().constrain {
                y = SiblingConstraint()
                width = 100.percent
                height = ChildBasedSizeConstraint() + 8.pixels
            }.onLeftClick {
                USound.playButtonPress()
                it.stopPropagation()
                select(index)
            } childOf expandedContent
            val hovered = optionContainer.hoveredState()

            optionContainer.setColor(hovered.map {
                if (it) {
                    VigilancePalette.getButton()
                } else {
                    VigilancePalette.getComponentBackground()
                }
            }.toConstraint())
            val text by UIText(value).constrain {
                x = 5.pixels
                y = CenterConstraint()
                color = VigilancePalette.getTextColor(hovered).toConstraint()
            } childOf optionContainer
        }

        selectedArea.onLeftClick { event ->
            USound.playButtonPress()
            event.stopPropagation()

            if (writableExpandedState.get()) {
                collapse()
            } else {
                expand()
            }
        }
    }

    fun select(index: Int) {
        if (index in options.indices) {
            selectedIndex.set(index)
            collapse()
        }
    }

    fun expand(instantly: Boolean = false) {
        writableExpandedState.set(true)
        applyExpandedBlockHeight(instantly, ChildBasedSizeConstraint() + 4.pixels)
    }

    fun collapse(instantly: Boolean = false) {
        writableExpandedState.set(false)
        applyExpandedBlockHeight(instantly, 0.pixels)
    }

    private fun applyExpandedBlockHeight(instantly: Boolean, heightConstraint: HeightConstraint) {
        if (instantly) {
            expandedBlock.setHeight(heightConstraint)
        } else {
            expandedBlock.animate {
                setHeightAnimation(Animations.OUT_EXP, 0.25f, heightConstraint)
            }
        }
    }
}