package club.sk1er.vigilance

import club.sk1er.elementa.utils.withAlpha
import club.sk1er.vigilance.data.Property
import club.sk1er.vigilance.data.PropertyType
import club.sk1er.vigilance.gui.VigilancePalette
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
 * If you need an example config, see: [club.sk1er.vigilance.example.ExampleConfig].
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

        registerListener(::brightDivider) { VigilancePalette.brightDividerState.set(it) }
        registerListener(::divider) { VigilancePalette.dividerState.set(it) }
        registerListener(::darkDivider) { VigilancePalette.darkDividerState.set(it) }
        registerListener(::outline) { VigilancePalette.outlineState.set(it) }
        registerListener(::scrollBar) { VigilancePalette.scrollBarState.set(it) }
        registerListener(::brightHighlight) { VigilancePalette.brightHighlightState.set(it) }
        registerListener(::highlight) { VigilancePalette.highlightState.set(it) }
        registerListener(::darkHighlight) { VigilancePalette.darkHighlightState.set(it) }
        registerListener(::lightBackground) { VigilancePalette.lightBackgroundState.set(it) }
        registerListener(::background) {
            VigilancePalette.backgroundState.set(it)
            VigilancePalette.bgNoAlpha.set(it.withAlpha(0))
        }
        registerListener(::darkBackground) { VigilancePalette.darkBackgroundState.set(it) }
        registerListener(::searchBarBackground) { VigilancePalette.searchBarBackgroundState.set(it) }
        registerListener(::brightText) { VigilancePalette.brightTextState.set(it) }
        registerListener(::midText) { VigilancePalette.midTextState.set(it) }
        registerListener(::darkText) { VigilancePalette.darkTextState.set(it) }
        registerListener(::modalBackground) { VigilancePalette.modalBackgroundState.set(it) }
        registerListener(::warning) { VigilancePalette.warningState.set(it) }
        registerListener(::accent) { VigilancePalette.accentState.set(it) }
        registerListener(::success) { VigilancePalette.successState.set(it) }
        registerListener(::transparent) { VigilancePalette.transparentState.set(it) }
        registerListener(::disabled) { VigilancePalette.disabledState.set(it) }
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