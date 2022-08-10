package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.BasicState
import gg.essential.universal.UKeyboard
import gg.essential.vigilance.gui.common.IconButton
import gg.essential.vigilance.gui.common.input.UITextInput
import gg.essential.vigilance.gui.common.shadow.ShadowIcon
import gg.essential.vigilance.utils.bindParent
import gg.essential.vigilance.utils.not
import gg.essential.vigilance.utils.onLeftClick
import gg.essential.vigilance.utils.state

internal class Searchbar(
    placeholder: String = "Search...",
    initialValue: String = "",
    private val activateOnSearchHokey: Boolean = true,
    private val activateOnType: Boolean = true,
    private val expandedWidth: Int = 104,
) : UIContainer() {

    private val collapsed = BasicState(true)
    val textContent = BasicState(initialValue)

    private val toggleIcon = collapsed.map {
        if (it) {
            VigilancePalette.SEARCH_7X
        } else {
            VigilancePalette.CANCEL_5X
        }
    }

    private val toggleButton by IconButton(
        toggleIcon,
        tooltipText = "".state(),
        enabled = true.state(),
        buttonText = "".state(),
        iconShadow = true.state(),
        textShadow = true.state()
    ).constrain {
        x = 0.pixels(alignOpposite = true)
        width = AspectConstraint()
        height = 100.percent
    }.onLeftClick {
        collapsed.set { !it }
        if (collapsed.get()) {
            textContent.set("")
        } else {
            activateSearch()
        }
    } childOf this

    private val searchContainer by UIBlock(VigilancePalette.button).constrain {
        width = expandedWidth.pixels
        height = 100.percent
    }.bindParent(this, !collapsed)

    private val searchIcon by ShadowIcon(VigilancePalette.SEARCH_7X, true).constrain {
        x = 5.pixels
        y = CenterConstraint()
    }.rebindPrimaryColor(VigilancePalette.textHighlight) childOf searchContainer

    private val searchInput: UITextInput by UITextInput(placeholder = placeholder).constrain {
        x = SiblingConstraint(5f)
        y = CenterConstraint()
        width = FillConstraint(useSiblings = false)
        height = 9.pixels
    } childOf searchContainer

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = 17.pixels
        }

        searchContainer.onLeftClick {
            activateSearch()
        }

        searchInput.onUpdate {
            textContent.set(it)
        }
        textContent.onSetValue {
            if (it != searchInput.getText()) {
                searchInput.setText(it)
            }
        }
    }

    override fun afterInitialization() {
        super.afterInitialization()

        Window.of(this).onKeyType { typedChar, keyCode ->
            when {
                activateOnSearchHokey && keyCode == UKeyboard.KEY_F && UKeyboard.isCtrlKeyDown()
                        && !UKeyboard.isShiftKeyDown() && !UKeyboard.isAltKeyDown() -> {
                    collapsed.set(false)
                    activateSearch()
                }
                activateOnType && !typedChar.isISOControl() -> {
                    collapsed.set(false)
                    activateSearch()
                    searchInput.keyType(typedChar, keyCode)
                }
            }
        }
    }

    fun setText(text: String) {
        searchInput.setText(text)
        textContent.set(text)
    }

    fun getText(): String {
        return textContent.get()
    }

    fun activateSearch() {
        searchInput.grabWindowFocus()
        searchInput.focus()
    }

    fun deactivateSearch() {
        searchInput.releaseWindowFocus()
    }
}
