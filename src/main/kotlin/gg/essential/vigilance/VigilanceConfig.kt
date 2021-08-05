package gg.essential.vigilance

import gg.essential.elementa.utils.withAlpha
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.gui.VigilancePalette
import java.awt.Color
import java.io.File

/**
 * This is the general config for Vigilance, accessible with the
 * /vigilance command. The values here are for the user to change
 * (i.e. NOT your mod)! The color scheme can be obtained from either
 * VigilanceConfig#getPalette OR VigilancePalette#INSTANCE. They both
 * return the same thing so it's just a preference.
 *
 * <p>
 * If you need an example config, see: [gg.essential.vigilance.example.ExampleConfig].
 * </p>
 */
object VigilanceConfig {
    // colours are still obtained through VigilancePalette.
    fun getPalette(): VigilancePalette = VigilancePalette

    private var brightDivider = Color(151, 151, 151)
    private var divider = Color(80, 80, 80)
    private var darkDivider = Color(50, 50, 50)
    private var outline = Color(48, 48, 49)
    private var scrollBar = Color(45, 45, 45)
    private var brightHighlight = Color(50, 50, 50)
    private var highlight = Color(33, 34, 38)
    private var darkHighlight = Color(27, 28, 33)
    private var lightBackground = Color(32, 32, 33)
    private var background = Color(22, 22, 24)
    private var darkBackground = Color(10, 10, 11)
    private var searchBarBackground = Color(27, 28, 33)
    private var brightText = Color(255, 255, 255)
    private var midText = Color(187, 187, 187)
    private var darkText = Color(119, 119, 121)
    private var modalBackground = Color(0, 0, 0, 100)
    private var warning = Color(239, 83, 80)
    private var accent = Color(1, 165, 82)
    private var success = Color(1, 165, 82)
    private var transparent = Color(0, 0, 0, 0)
    private var disabled = Color(80, 80, 80)

    internal fun setAllInPalette() {
        VigilancePalette.brightDividerState.set(brightDivider)
        VigilancePalette.dividerState.set(divider)
        VigilancePalette.darkDividerState.set(darkDivider)
        VigilancePalette.outlineState.set(outline)
        VigilancePalette.scrollBarState.set(scrollBar)
        VigilancePalette.brightHighlightState.set(brightHighlight)
        VigilancePalette.highlightState.set(highlight)
        VigilancePalette.darkHighlightState.set(darkHighlight)
        VigilancePalette.lightBackgroundState.set(lightBackground)
        VigilancePalette.backgroundState.set(background)
        VigilancePalette.darkBackgroundState.set(darkBackground)
        VigilancePalette.searchBarBackgroundState.set(searchBarBackground)
        VigilancePalette.brightTextState.set(brightText)
        VigilancePalette.midTextState.set(midText)
        VigilancePalette.darkTextState.set(darkText)
        VigilancePalette.modalBackgroundState.set(modalBackground)
        VigilancePalette.warningState.set(warning)
        VigilancePalette.accentState.set(accent)
        VigilancePalette.successState.set(success)
        VigilancePalette.transparentState.set(transparent)
        VigilancePalette.disabledState.set(disabled)
        VigilancePalette.bgNoAlpha.set(background.withAlpha(0))
    }
}