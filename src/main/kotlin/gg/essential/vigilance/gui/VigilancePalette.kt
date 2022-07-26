package gg.essential.vigilance.gui

import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.vigilance.VigilanceConfig
import gg.essential.vigilance.utils.ImageFactory
import gg.essential.vigilance.utils.ResourceImageFactory
import java.awt.Color

object VigilancePalette {

    // Old
    fun getBrightDivider(): Color = brightDividerState.get()
    fun getDivider(): Color = dividerState.get()
    fun getDarkDivider(): Color = darkDividerState.get()
    fun getOutline(): Color = outlineState.get()
    fun getScrollBar(): Color = scrollBarState.get()
    fun getBrightHighlight(): Color = brightHighlightState.get()
    fun getHighlight(): Color = highlightState.get()
    fun getDarkHighlight(): Color = darkHighlightState.get()
    fun getLightBackground(): Color = lightBackgroundState.get()
    fun getBackground(): Color = backgroundState.get()
    fun getDarkBackground(): Color = darkBackgroundState.get()
    fun getSearchBarBackground(): Color = searchBarBackgroundState.get()
    fun getBrightText(): Color = brightTextState.get()
    fun getMidText(): Color = midTextState.get()
    fun getDarkText(): Color = darkTextState.get()
    fun getModalBackground(): Color = modalBackgroundState.get()
    fun getWarning(): Color = warningState.get()
    fun getAccent(): Color = accentState.get()
    fun getSuccess(): Color = successState.get()
    fun getTransparent(): Color = transparentState.get()
    fun getDisabled(): Color = disabledState.get()

    // New
    fun getPrimary(): Color = primary.get()
    fun getComponentBorderDark(): Color = componentBorderDark.get()
    fun getMidGray(): Color = midGray.get()
    fun getButton(): Color = button.get()
    fun getButtonHighlight(): Color = buttonHighlight.get()
    fun getText(): Color = text.get()
    fun getTextHighlight(): Color = textHighlight.get()
    fun getTextDisabled(): Color = textDisabled.get()
    fun getTextWarning(): Color = textWarning.get()
    fun getComponentBackground(): Color = componentBackground.get()
    fun getComponentBackgroundHighlight(): Color = componentBackgroundHighlight.get()
    fun getComponentBorder(): Color = componentBorder.get()
    fun getComponentHighlight(): Color = componentHighlight.get()
    fun getDividerDark(): Color = dividerDark.get()
    fun getScrollbar(): Color = scrollbar.get()
    fun getTextShadow(): Color = textShadow.get()
    fun getMainBackground(): Color = mainBackground.get()
    fun getTextActive(): Color = textActive.get()

    // These are marked as internal because ideally the user is only changing the colours in the settings gui
    // Old
    internal val brightDividerState = BasicState(Color.BLACK)
    internal val dividerState = BasicState(Color.BLACK)
    internal val darkDividerState = BasicState(Color.BLACK)
    internal val outlineState = BasicState(Color.BLACK)
    internal val scrollBarState = BasicState(Color.BLACK)
    internal val brightHighlightState = BasicState(Color.BLACK)
    internal val highlightState = BasicState(Color.BLACK)
    internal val darkHighlightState = BasicState(Color.BLACK)
    internal val lightBackgroundState = BasicState(Color.BLACK)
    internal val backgroundState = BasicState(Color.BLACK)
    internal val darkBackgroundState = BasicState(Color.BLACK)
    internal val searchBarBackgroundState = BasicState(Color.BLACK)
    internal val brightTextState = BasicState(Color.BLACK)
    internal val midTextState = BasicState(Color.BLACK)
    internal val darkTextState = BasicState(Color.BLACK)
    internal val modalBackgroundState = BasicState(Color.BLACK)
    internal val warningState = BasicState(Color.BLACK)
    internal val accentState = BasicState(Color.BLACK)
    internal val successState = BasicState(Color.BLACK)
    internal val transparentState = BasicState(Color.BLACK)
    internal val disabledState = BasicState(Color.BLACK)
    internal val bgNoAlpha = BasicState(Color.BLACK)

    // New
    internal val primary = BasicState(Color.BLACK)
    internal val componentBorderDark = BasicState(Color.BLACK)
    internal val midGray = BasicState(Color.GRAY)
    internal val button = BasicState(Color.BLACK)
    internal val buttonHighlight = BasicState(Color.BLACK)
    internal val text = BasicState(Color.BLACK)
    internal val textHighlight = BasicState(Color.WHITE)
    internal val textDisabled = BasicState(Color.BLACK)
    internal val textWarning = BasicState(Color.BLACK)
    internal val componentBackground = BasicState(Color.BLACK)
    internal val componentBackgroundHighlight = BasicState(Color.BLACK)
    internal val componentBorder = BasicState(Color.BLACK)
    internal val componentHighlight = BasicState(Color.BLACK)
    internal val dividerDark = BasicState(Color.BLACK)
    internal val scrollbar = BasicState(Color.BLACK)
    internal val textShadow = BasicState(Color.BLACK)
    internal val mainBackground = BasicState(Color.BLACK)
    internal val textActive = BasicState(Color.BLACK)

    internal val SEARCH_7X: ImageFactory = ResourceImageFactory("/vigilance/search_7x7.png")
    internal val CANCEL_5X: ImageFactory = ResourceImageFactory("/vigilance/cancel_5x5.png")
    internal val ARROW_LEFT_4X7: ImageFactory = ResourceImageFactory("/vigilance/arrow-left.png")
    internal val ARROW_UP_7X4: ImageFactory = ResourceImageFactory("/vigilance/arrow-up.png")
    internal val ARROW_DOWN_7X4: ImageFactory = ResourceImageFactory("/vigilance/arrow-down.png")

    init {
        VigilanceConfig.setAllInPalette()
    }

    /* Utilities for colors */
    internal fun getTextColor(hovered: State<Boolean>, enabled: State<Boolean>): State<Color> {
        return hovered.zip(enabled).map { (hovered, enabled) ->
            if (enabled) {
                if (hovered) {
                    getTextHighlight()
                } else {
                    getText()
                }
            } else {
                getTextDisabled()
            }
        }
    }

    internal fun getTextColor(hovered: State<Boolean>): State<Color> = getTextColor(hovered, BasicState(true))

    internal fun getButtonColor(hovered: State<Boolean>, enabled: State<Boolean>): State<Color> {
        return hovered.zip(enabled).map { (hovered, enabled) ->
            if (enabled) {
                if (hovered) {
                    getButtonHighlight()
                } else {
                    getButton()
                }
            } else {
                getComponentBackground()
            }
        }
    }
}
