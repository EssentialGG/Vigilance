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

internal class DropDownComponent(
    initialSelection: Int,
    private val options: List<String>,
    private val maxDisplayOptions: Int = 6,
) : UIBlock() {

    private val writableExpandedState: State<Boolean> = BasicState(false)
    private val optionContainerHeight = 18

    /** Public States **/
    val selectedIndex: State<Int> = BasicState(initialSelection)
    val selectedText: State<String> = selectedIndex.map {
        options[it]
    }
    val expandedState = ReadyOnlyState(writableExpandedState)

    private val selectedArea by UIContainer().constrain {
        width = 100.percent
        height = 17.pixels
    } childOf this

    private val selectAreaHovered = selectedArea.hoveredState()

    private val currentSelectionText by UIText(shadowColor = VigilancePalette.getTextShadow()).bindText(selectedText).constrain {
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
    }.bindFloating(writableExpandedState) childOf this effect ScissorEffect()

    private val scrollerContainer by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent - 4.pixels
        height = (100.percent - 4.pixels).coerceAtLeast(0.pixels)
    } childOf expandedBlock

    private val scroller by ScrollComponent().centered().constrain {
        width = 100.percent
        height = 100.percent
    } childOf scrollerContainer

    // Height set in init
    private val expandedContentArea by UIBlock(VigilancePalette.componentBackground).constrain {
        x = CenterConstraint()
        width = 100.percent
        height = ChildBasedSizeConstraint() + 6.pixels
    } childOf scroller

    private val expandedContent by UIContainer().centered().constrain {
        width = 100.percent
        height = ChildBasedSizeConstraint()
    } childOf expandedContentArea

    private val scrollbarContainer by UIContainer().constrain {
        x = 2.pixels(alignOpposite = true)
        y = CenterConstraint()
        width = 2.pixels
        height = 100.percent
    }.onLeftClick {
        it.stopPropagation()
    } childOf scrollerContainer

    private val scrollbar by UIBlock(VigilancePalette.midGray).constrain {
        width = 100.percent
    } childOf scrollbarContainer

    private fun getMaxItemWidth(): Float {
        return options.maxOf {
            it.width()
        }
    }

    private val scrollable = options.size > maxDisplayOptions

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

        if (scrollable) {
            scroller.setVerticalScrollBarComponent(scrollbar, hideWhenUseless = false)
        }

        options.withIndex().forEach { (index, value) ->
            val optionContainer by UIBlock().constrain {
                y = SiblingConstraint()
                width = 100.percent
                height = 20.pixels
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
        applyExpandedBlockHeight(
            instantly,
            (options.size.coerceAtMost(maxDisplayOptions) * 20).pixels + 10.pixels
        )
    }

    fun collapse(instantly: Boolean = false) {
        writableExpandedState.set(false)
        applyExpandedBlockHeight(instantly, 0.pixels)
    }

    private fun applyExpandedBlockHeight(
        instantly: Boolean,
        heightConstraint: HeightConstraint,
    ) {
        if (instantly) {
            expandedBlock.setHeight(heightConstraint)
        } else {
            expandedBlock.animate {
                setHeightAnimation(Animations.OUT_EXP, 0.25f, heightConstraint)
            }
        }
    }
}
