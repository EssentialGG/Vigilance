package club.sk1er.vigilance.gui

import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Property
import club.sk1er.vigilance.data.PropertyType
import java.awt.Color
import java.io.File

object VigilancePalette : Vigilant(File("./config/vigilance.toml")) {
    @Property(PropertyType.COLOR, "Dark Divider", "Color Scheme", allowAlpha = false)
    var DARK_DIVIDER = Color(80, 80, 80)
    @Property(PropertyType.COLOR, "Divider", "Color Scheme", allowAlpha = false)
    var DIVIDER = Color(151, 151, 151)
    @Property(PropertyType.COLOR, "Scroll Bar", "Color Scheme", allowAlpha = false)
    var SCROLL_BAR = Color(70, 70, 70)
    @Property(PropertyType.COLOR, "Bright Highlight", "Color Scheme", allowAlpha = false)
    var BRIGHT_HIGHLIGHT = Color(50, 50, 50)
    @Property(PropertyType.COLOR, "Highlight", "Color Scheme", allowAlpha = false)
    var HIGHLIGHT = Color(33, 34, 38)
    @Property(PropertyType.COLOR, "Dark Highlight", "Color Scheme", allowAlpha = false)
    var DARK_HIGHLIGHT = Color(29, 29, 32)
    @Property(PropertyType.COLOR, "Background", "Color Scheme", allowAlpha = false)
    var BACKGROUND = Color(22, 22, 24)
    @Property(PropertyType.COLOR, "Dark Background", "Color Scheme", allowAlpha = false)
    var DARK_BACKGROUND = Color(10, 10, 11)
    @Property(PropertyType.COLOR, "Bright Text", "Color Scheme", allowAlpha = false)
    var BRIGHT_TEXT = Color(255, 255, 255)
    @Property(PropertyType.COLOR, "Mid Text", "Color Scheme", allowAlpha = false)
    var MID_TEXT = Color(187, 187, 187)
    @Property(PropertyType.COLOR, "Dark Text", "Color Scheme", allowAlpha = false)
    var DARK_TEXT = Color(119, 119, 121)
    @Property(PropertyType.COLOR, "Modal Background", "Color Scheme", allowAlpha = false)
    var MODAL_BACKGROUND = Color(0, 0, 0, 100)
    @Property(PropertyType.COLOR, "Warning", "Color Scheme", allowAlpha = false)
    var WARNING = Color(239, 83, 80)
    @Property(PropertyType.COLOR, "Success", "Color Scheme", allowAlpha = false)
    var SUCCESS = Color(0, 167, 81)
    @Property(PropertyType.COLOR, "Accent", "Color Scheme", allowAlpha = false)
    var ACCENT = Color(1, 165, 82)
    @Property(PropertyType.COLOR, "Transparent", "Color Scheme", allowAlpha = false)
    var TRANSPARENT = Color(0, 0, 0, 0)
    @Property(PropertyType.COLOR, "Disabled", "Color Scheme", allowAlpha = false)
    var DISABLED = Color(80, 80, 80)

    init {
        initialize()
    }
}

fun Color.withAlpha(alpha: Int) = Color(this.red, this.green, this.blue, alpha)
fun Color.invisible() = withAlpha(0)