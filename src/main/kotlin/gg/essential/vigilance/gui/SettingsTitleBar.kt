package gg.essential.vigilance.gui

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.transitions.SlideToTransition
import gg.essential.universal.UKeyboard
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.utils.onLeftClick

class SettingsTitleBar(private val gui: SettingsGui, private val config: Vigilant) : UIContainer() {
    private val standardBar by StandardTitleBar()
    private val searchFriendsBar by InputTitleBar(UIImage.Companion.ofResourceCached("/vigilance/search.png"), 16, 16)

    private var displayedBar: Bar = standardBar childOf this

    init {
        enableEffect(ScissorEffect())

        standardBar.onClickSearch {
            showSearchFriendsBar()
        }

        searchFriendsBar.onUpdate { str ->
            gui.selectCategory(config.getCategoryFromSearch(str))
        }.onCancel {
            gui.selectCategory(config.getCategoryFromSearch(""))
        }
    }

    private fun showStandardBar(): Unit = showBar(standardBar, true)
    private fun showSearchFriendsBar(): Unit = showBar(searchFriendsBar, false)

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
            UIText(config.guiTitle).constrain {
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
                onClickSearch()
            } childOf iconContainer
        }

        fun onClickSearch(action: () -> Unit) = apply {
            onClickSearch = action
        }
    }

    private inner class InputTitleBar(icon: UIImage, iconWidth: Int, iconHeight: Int) : Bar() {
        private var updateAction: ((String) -> Unit)? = null
        private var cancelAction: (() -> Unit)? = null

        private val leftIcon: UIComponent by makeIcon(icon, iconWidth, iconHeight).constrain {
            x = 5.pixels()
        } childOf this

        private val input: UITextInput by UITextInput("Search...", shadow = false).constrain {
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
            showStandardBar()
            cancelAction?.invoke()
            input.releaseWindowFocus()
        } childOf this

        init {
            onLeftClick {
                input.grabWindowFocus()
            }

            input.onUpdate {
                updateAction?.invoke(input.getText())
            }.onKeyType { _, keyCode ->
                if (keyCode == UKeyboard.KEY_ESCAPE)
                    showStandardBar()
            }
        }

        fun onUpdate(action: (String) -> Unit) = apply {
            updateAction = action
        }

        fun onCancel(action: () -> Unit) = apply {
            cancelAction = action
        }

        override fun onShow() {
            input.grabWindowFocus()
        }

        override fun onHide() {
            input.releaseWindowFocus()
            delay(500) {
                input.setText("")
            }
        }
    }
}
