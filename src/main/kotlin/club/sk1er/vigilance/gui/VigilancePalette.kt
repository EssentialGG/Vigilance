package club.sk1er.vigilance.gui

import club.sk1er.elementa.state.BasicState
import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Property
import club.sk1er.vigilance.data.PropertyType
import java.awt.Color
import java.io.File

object VigilancePalette : Vigilant(File("./config/vigilance.toml")) {
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
    var SUCCESS = ACCENT

    @Property(PropertyType.COLOR, "Transparent", "Color Scheme")
    var TRANSPARENT = Color(0, 0, 0, 0)

    @Property(PropertyType.COLOR, "Disabled", "Color Scheme")
    var DISABLED = Color(80, 80, 80)

    init {
        initialize()

        registerListener(::BRIGHT_DIVIDER) { brightDividerState.set(it) }
        registerListener(::DIVIDER) { dividerState.set(it) }
        registerListener(::OUTLINE) { accentState.set(it) }
        registerListener(::SCROLL_BAR) { scrollBlockState.set(it) }
        registerListener(::DARK_HIGHLIGHT) { darkHighlightState.set(it) }
        registerListener(::LIGHT_BACKGROUND) { lightBackgroundState.set(it) }
        registerListener(::ACCENT) { accentState.set(it) }
        registerListener(::BRIGHT_TEXT) { accentState.set(it) }
        registerListener(::MID_TEXT) { accentState.set(it) }
        registerListener(::BACKGROUND) { backgroundState.set(it) }
    }

    // these are marked as internal because ideally the user is only changing the colours in the settings gui
    internal val brightDividerState = BasicState(BRIGHT_DIVIDER)
    internal val dividerState = BasicState(DIVIDER)
    internal val outlineState = BasicState(OUTLINE)
    internal val scrollBlockState = BasicState(SCROLL_BAR)
    internal val darkHighlightState = BasicState(DARK_HIGHLIGHT)
    internal val lightBackgroundState = BasicState(LIGHT_BACKGROUND)
    internal val backgroundState = BasicState(BACKGROUND)
    internal val brightTextState = BasicState(BRIGHT_TEXT)
    internal val midTextState = BasicState(MID_TEXT)
    internal val accentState = BasicState(ACCENT)
}
