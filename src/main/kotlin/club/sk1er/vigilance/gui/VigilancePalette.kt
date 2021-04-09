package club.sk1er.vigilance.gui

import club.sk1er.elementa.state.BasicState
import club.sk1er.elementa.utils.withAlpha
import club.sk1er.mods.core.universal.UChat
import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Property
import club.sk1er.vigilance.data.PropertyType
import java.awt.Color
import java.io.File

object VigilancePalette : Vigilant(File("./config/vigilance.toml"), "Vigilance Palette") {
    @Property(PropertyType.COLOR, "Bright Divider", "Color Scheme")
    var BRIGHT_DIVIDER = Color(151, 151, 151)

    @Property(PropertyType.COLOR, "Divider", "Color Scheme")
    var DIVIDER = Color(80, 80, 80)

    @Property(PropertyType.COLOR, "Dark Divider", "Color Scheme")
    var DARK_DIVIDER = Color(50, 50, 50)

    @Property(PropertyType.COLOR, "Outline", "Color Scheme")
    var OUTLINE = Color(48, 48, 49)

    @Property(PropertyType.COLOR, "Scroll Bar", "Color Scheme")
    var SCROLL_BAR = Color(45, 45, 45)

    @Property(PropertyType.COLOR, "Bright Highlight", "Color Scheme")
    var BRIGHT_HIGHLIGHT = Color(50, 50, 50)

    @Property(PropertyType.COLOR, "Highlight", "Color Scheme")
    var HIGHLIGHT = Color(33, 34, 38)

    @Property(PropertyType.COLOR, "Dark Highlight", "Color Scheme")
    var DARK_HIGHLIGHT = Color(27, 28, 33)

    @Property(PropertyType.COLOR, "Light Background", "Color Scheme")
    var LIGHT_BACKGROUND = Color(32, 32, 33)

    @Property(PropertyType.COLOR, "Background", "Color Scheme")
    var BACKGROUND = Color(22, 22, 24)

    @Property(PropertyType.COLOR, "Dark Background", "Color Scheme")
    var DARK_BACKGROUND = Color(10, 10, 11)

    @Property(PropertyType.COLOR, "Search Bar Background", "Color Scheme")
    var SEARCH_BAR_BACKGROUND = Color(27, 28, 33)

    @Property(PropertyType.COLOR, "Bright Text", "Color Scheme")
    var BRIGHT_TEXT = Color(255, 255, 255)

    @Property(PropertyType.COLOR, "Mid Text", "Color Scheme")
    var MID_TEXT = Color(187, 187, 187)

    @Property(PropertyType.COLOR, "Dark Text", "Color Scheme")
    var DARK_TEXT = Color(119, 119, 121)

    @Property(PropertyType.COLOR, "Modal Background", "Color Scheme")
    var MODAL_BACKGROUND = Color(0, 0, 0, 100)

    @Property(PropertyType.COLOR, "Warning", "Color Scheme")
    var WARNING = Color(239, 83, 80)

    @Property(PropertyType.COLOR, "Accent", "Color Scheme")
    var ACCENT = Color(1, 165, 82)

    @Property(PropertyType.COLOR, "Success", "Color Scheme")
    var SUCCESS = Color(1, 165, 82)

    @Property(PropertyType.COLOR, "Transparent", "Color Scheme")
    var TRANSPARENT = Color(0, 0, 0, 0)

    @Property(PropertyType.COLOR, "Disabled", "Color Scheme")
    var DISABLED = Color(80, 80, 80)

    @Property(
        type = PropertyType.BUTTON,
        name = "Show Colour Window",
        description = "Shows the colour palette window thing idk this is probably temp anyway",
        placeholder = "Click Here!",
        category = "Color Scheme",
        subcategory = "Window"
    )
    private fun showColourWindow() {
        awaitShowColourWindow = true
    }

    internal var awaitShowColourWindow: Boolean = false

    init {
        initialize()

        registerListener(::BRIGHT_DIVIDER) { brightDividerState.set(it) }
        registerListener(::DIVIDER) { dividerState.set(it) }
        registerListener(::DARK_DIVIDER) { darkDividerState.set(it) }
        registerListener(::OUTLINE) { outlineState.set(it) }
        registerListener(::SCROLL_BAR) { scrollBlockState.set(it) }
        registerListener(::BRIGHT_HIGHLIGHT) { brightHighlightState.set(it) }
        registerListener(::HIGHLIGHT) { highlightState.set(it) }
        registerListener(::DARK_HIGHLIGHT) { darkHighlightState.set(it) }
        registerListener(::LIGHT_BACKGROUND) { lightBackgroundState.set(it) }
        registerListener(::BACKGROUND) { backgroundState.set(it); bgNoAlpha.set(it.withAlpha(0)) }
        registerListener(::DARK_BACKGROUND) { darkBackgroundState.set(it) }
        registerListener(::SEARCH_BAR_BACKGROUND) { searchBarBackgroundState.set(it) }
        registerListener(::BRIGHT_TEXT) { brightTextState.set(it) }
        registerListener(::MID_TEXT) { midTextState.set(it) }
        registerListener(::DARK_TEXT) { darkTextState.set(it) }
        registerListener(::MODAL_BACKGROUND) { modalBackgroundState.set(it) }
        registerListener(::WARNING) { warningState.set(it) }
        registerListener(::ACCENT) { accentState.set(it) }
        registerListener(::SUCCESS) { successState.set(it) }
        registerListener(::TRANSPARENT) { transparentState.set(it) }
        registerListener(::DISABLED) { disabledState.set(it) }
    }

    // these are marked as internal because ideally the user is only changing the colours in the settings gui
    internal val brightDividerState = BasicState(BRIGHT_DIVIDER)
    internal val dividerState = BasicState(DIVIDER)
    internal val darkDividerState = BasicState(DARK_DIVIDER)
    internal val outlineState = BasicState(OUTLINE)
    internal val scrollBlockState = BasicState(SCROLL_BAR)
    internal val brightHighlightState = BasicState(BRIGHT_HIGHLIGHT)
    internal val highlightState = BasicState(HIGHLIGHT)
    internal val darkHighlightState = BasicState(DARK_HIGHLIGHT)
    internal val lightBackgroundState = BasicState(LIGHT_BACKGROUND)
    internal val backgroundState = BasicState(BACKGROUND)
    internal val darkBackgroundState = BasicState(DARK_BACKGROUND)
    internal val searchBarBackgroundState = BasicState(SEARCH_BAR_BACKGROUND)
    internal val brightTextState = BasicState(BRIGHT_TEXT)
    internal val midTextState = BasicState(MID_TEXT)
    internal val darkTextState = BasicState(DARK_TEXT)
    internal val modalBackgroundState = BasicState(MODAL_BACKGROUND)
    internal val warningState = BasicState(WARNING)
    internal val accentState = BasicState(ACCENT)
    internal val successState = BasicState(SUCCESS)
    internal val transparentState = BasicState(TRANSPARENT)
    internal val disabledState = BasicState(DISABLED)

    internal val bgNoAlpha = BasicState(BACKGROUND.withAlpha(0))
}
