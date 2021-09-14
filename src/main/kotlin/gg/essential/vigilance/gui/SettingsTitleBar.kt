package gg.essential.vigilance.gui

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.Window
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.transitions.SlideToTransition
import gg.essential.universal.UKeyboard
import gg.essential.universal.USound
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.utils.onLeftClick
import net.minecraft.client.resources.I18n

class SettingsTitleBar(private val gui: SettingsGui, private val config: Vigilant, window: Window) : UIContainer() {
    private val standardBar by StandardTitleBar()
    private val searchBar by InputTitleBar(UIImage.Companion.ofResourceCached("/vigilance/search.png"), 16, 16)

    private var displayedBar: Bar = standardBar childOf this

    init {
        enableEffect(ScissorEffect())

        standardBar.onClickSearch {
            showSearchBar()
        }

        searchBar.onUpdate { str ->
            gui.selectCategory(config.getCategoryFromSearch(str))
        }
        
        window.onKeyType { typedChar, keyCode ->
            if (searchBar.isHidden) {
                when {
                    UKeyboard.run { keyCode == KEY_F && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown() } -> showSearchBar()
                    typedChar != '\u0000' -> {
                        showSearchBar()
                        searchBar.input.keyType(typedChar, keyCode)
                    }
                }
            } else if (typedChar != '\u0000') {
                searchBar.input.run {
                    grabWindowFocus()
                    focus()
                    keyType(typedChar, keyCode)
                }
            }
        }
    }

    private fun showStandardBar(): Unit = showBar(standardBar, true)
    private fun showSearchBar(): Unit = showBar(searchBar, false)

    private fun showBar(bar: Bar, fromTop: Boolean) {
        if (displayedBar == bar)
            return

        bar.constrain {
            y = SiblingConstraint(alignOpposite = fromTop)
        } childOf this

        displayedBar.onHide()
        bar.onShow()

        val transition = if (fromTop) {
            SlideToTransition.Bottom(0.5f)
        } else SlideToTransition.Top(0.5f)

        transition.transition(displayedBar) {
            removeChild(displayedBar)
            bar.constrain {
                y = 0.pixels()
            }
            displayedBar = bar
        }
    }

    private inner class StandardTitleBar : Bar() {
        private lateinit var onClickSearch: () -> Unit

        init {
            UIText(I18n.format(config.guiTitle)).constrain {
                x = 15.pixels()
                y = CenterConstraint()
            } childOf this
        }

        private val iconContainer by UIContainer().constrain {
            x = 0.pixels(alignOpposite = true)
            width = ChildBasedSizeConstraint()
            height = 100.percent()
        } childOf this

        init {
            makeIcon(UIImage.ofResourceCached("/vigilance/search.png"), 16, 16).onLeftClick {
                USound.playButtonPress()
                onClickSearch()
            } childOf iconContainer
        }

        fun onClickSearch(action: () -> Unit) = apply {
            onClickSearch = action
        }
    }

    private inner class InputTitleBar(icon: UIImage, iconWidth: Int, iconHeight: Int) : Bar() {
        private var updateAction: ((String) -> Unit)? = null
        var isHidden = true

        private val leftIcon: UIComponent by makeIcon(icon, iconWidth, iconHeight).constrain {
            x = 5.pixels()
        } childOf this

        val input: UITextInput by UITextInput("Search...", shadow = false).constrain {
            x = SiblingConstraint(15f)
            y = CenterConstraint()
            width = 100.percent() - 10.pixels() - basicWidthConstraint {
                leftIcon.getWidth() + cancelIcon.getWidth()
            }
            height = 10.pixels()
        } childOf this

        init {
            input.lineHeight = 10f
        }

        private val cancelIcon by makeIcon(UIImage.ofResourceCached("/vigilance/cancel.png"), 16, 16).constrain {
            x = 0.pixels(alignOpposite = true)
        }.onLeftClick {
            USound.playButtonPress()
            showStandardBar()
            input.releaseWindowFocus()
        } childOf this

        init {
            onLeftClick {
                if (!cancelIcon.isHovered()) {
                    input.grabWindowFocus()
                }
            }

            input.onUpdate {
                if (!isHidden) updateAction?.invoke(it)
            }.onKeyType { _, keyCode ->
                if (keyCode == UKeyboard.KEY_ESCAPE) {
                    showStandardBar()
                    input.releaseWindowFocus()
                }
            }
        }

        fun onUpdate(action: (String) -> Unit) = apply {
            updateAction = action
        }

        override fun onShow() {
            input.grabWindowFocus()
            input.focus()
            input.delay(2) {
                input.setActive(true)
            }
            input.setText("")
            isHidden = false
        }

        override fun onHide() {
            input.releaseWindowFocus()
            input.delay(2) {
                input.setActive(false)
            }
            isHidden = true
        }
    }
}
