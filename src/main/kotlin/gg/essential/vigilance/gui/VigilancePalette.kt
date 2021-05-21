package gg.essential.vigilance.gui

import gg.essential.elementa.state.BasicState
import gg.essential.vigilance.VigilanceConfig
import java.awt.Color

object VigilancePalette {
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

    // these are marked as internal because ideally the user is only changing the colours in the settings gui
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

    init {
        VigilanceConfig.setAllInPalette()
    }
}
