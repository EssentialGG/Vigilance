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
object VigilanceConfig : Vigilant(File("./config/vigilance.toml"), "Vigilance Config") {
    // colours are still obtained through VigilancePalette. While it is an
    fun getPalette(): VigilancePalette = VigilancePalette

    @Property(PropertyType.COLOR, "Bright Divider", "Color Scheme", subcategory = "Palette")
    private var brightDivider = Color(151, 151, 151)

    @Property(PropertyType.COLOR, "Divider", "Color Scheme", subcategory = "Palette")
    private var divider = Color(80, 80, 80)

    @Property(PropertyType.COLOR, "Dark Divider", "Color Scheme", subcategory = "Palette")
    private var darkDivider = Color(50, 50, 50)

    @Property(PropertyType.COLOR, "Outline", "Color Scheme", subcategory = "Palette")
    private var outline = Color(48, 48, 49)

    @Property(PropertyType.COLOR, "Scroll Bar", "Color Scheme", subcategory = "Palette")
    private var scrollBar = Color(45, 45, 45)

    @Property(PropertyType.COLOR, "Bright Highlight", "Color Scheme", subcategory = "Palette")
    private var brightHighlight = Color(50, 50, 50)

    @Property(PropertyType.COLOR, "Highlight", "Color Scheme", subcategory = "Palette")
    private var highlight = Color(33, 34, 38)

    @Property(PropertyType.COLOR, "Dark Highlight", "Color Scheme", subcategory = "Palette")
    private var darkHighlight = Color(27, 28, 33)

    @Property(PropertyType.COLOR, "Light Background", "Color Scheme", subcategory = "Palette")
    private var lightBackground = Color(32, 32, 33)

    @Property(PropertyType.COLOR, "Background", "Color Scheme", subcategory = "Palette")
    private var background = Color(22, 22, 24)

    @Property(PropertyType.COLOR, "Dark Background", "Color Scheme", subcategory = "Palette")
    private var darkBackground = Color(10, 10, 11)

    @Property(PropertyType.COLOR, "Search Bar Background", "Color Scheme", subcategory = "Palette")
    private var searchBarBackground = Color(27, 28, 33)

    @Property(PropertyType.COLOR, "Bright Text", "Color Scheme", subcategory = "Palette")
    private var brightText = Color(255, 255, 255)

    @Property(PropertyType.COLOR, "Mid Text", "Color Scheme", subcategory = "Palette")
    private var midText = Color(187, 187, 187)

    @Property(PropertyType.COLOR, "Dark Text", "Color Scheme", subcategory = "Palette")
    private var darkText = Color(119, 119, 121)

    @Property(PropertyType.COLOR, "Modal Background", "Color Scheme", subcategory = "Palette")
    private var modalBackground = Color(0, 0, 0, 100)

    @Property(PropertyType.COLOR, "Warning", "Color Scheme", subcategory = "Palette")
    private var warning = Color(239, 83, 80)

    @Property(PropertyType.COLOR, "Accent", "Color Scheme", subcategory = "Palette")
    private var accent = Color(1, 165, 82)

    @Property(PropertyType.COLOR, "Success", "Color Scheme", subcategory = "Palette")
    private var success = Color(1, 165, 82)

    @Property(PropertyType.COLOR, "Transparent", "Color Scheme", subcategory = "Palette")
    private var transparent = Color(0, 0, 0, 0)

    @Property(PropertyType.COLOR, "Disabled", "Color Scheme", subcategory = "Palette")
    private var disabled = Color(80, 80, 80)

    @Property(
        type = PropertyType.BUTTON,
        name = "Show Colour Window",
        description = "Shows the colour palette window thing idk this is probably temp anyway",
        placeholder = "Click Here!",
        category = "Color Scheme"
    )
    private fun showColourWindow() {
        awaitShowColourWindow = true
    }

    internal var awaitShowColourWindow: Boolean = false

    init {
        initialize()

        val clazz = javaClass
        registerListener("brightDivider") { color: Color -> VigilancePalette.brightDividerState.set(color) }
        registerListener("divider") { color: Color -> VigilancePalette.dividerState.set(color) }
        registerListener("darkDivider") { color: Color -> VigilancePalette.darkDividerState.set(color) }
        registerListener("outline") { color: Color -> VigilancePalette.outlineState.set(color) }
        registerListener("scrollBar") { color: Color -> VigilancePalette.scrollBarState.set(color) }
        registerListener("brightHighlight") { color: Color -> VigilancePalette.brightHighlightState.set(color) }
        registerListener("highlight") { color: Color -> VigilancePalette.highlightState.set(color) }
        registerListener("darkHighlight") { color: Color -> VigilancePalette.darkHighlightState.set(color) }
        registerListener("lightBackground") { color: Color -> VigilancePalette.lightBackgroundState.set(color) }
        registerListener("background") { color: Color ->
            VigilancePalette.backgroundState.set(color)
            VigilancePalette.bgNoAlpha.set(color.withAlpha(0))
        }
        registerListener("darkBackground") { color: Color -> VigilancePalette.darkBackgroundState.set(color) }
        registerListener("searchBarBackground") { color: Color -> VigilancePalette.searchBarBackgroundState.set(color) }
        registerListener("brightText") { color: Color -> VigilancePalette.brightTextState.set(color) }
        registerListener("midText") { color: Color -> VigilancePalette.midTextState.set(color) }
        registerListener("darkText") { color: Color -> VigilancePalette.darkTextState.set(color) }
        registerListener("modalBackground") { color: Color -> VigilancePalette.modalBackgroundState.set(color) }
        registerListener("warning") { color: Color -> VigilancePalette.warningState.set(color) }
        registerListener("accent") { color: Color -> VigilancePalette.accentState.set(color) }
        registerListener("success") { color: Color -> VigilancePalette.successState.set(color) }
        registerListener("transparent") { color: Color -> VigilancePalette.transparentState.set(color) }
        registerListener("disabled") { color: Color -> VigilancePalette.disabledState.set(color) }
    }

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