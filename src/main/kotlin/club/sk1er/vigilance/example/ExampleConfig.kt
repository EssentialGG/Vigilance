package club.sk1er.vigilance.example

import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Property
import club.sk1er.vigilance.data.PropertyType
import java.awt.Color
import java.io.File

object ExampleConfig : Vigilant(File("./config/example.toml")) {
    @Property(
        type = PropertyType.TEXT,
        name = "text",
        description = "example of text input that does not wrap the text",
        category = "General",
        subcategory = "Category",
        placeholder = "Empty... :("
    )
    var textInput = ""

    @Property(
        type = PropertyType.PARAGRAPH,
        name = "Paragraph",
        description = "example of text input that do wrap the text",
        category = "General",
        subcategory = "Category",
        placeholder = "Empty... :("
    )
    var bigParagraph = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus at imperdiet tortor. Quisque et accumsan metus, quis rutrum nulla. Vivamus metus lacus, tristique at tristique sit amet, pretium vel dui. Curabitur pharetra blandit dapibus. Donec ac nibh vel nisi laoreet mollis. Vivamus at metus quis diam consequat fermentum. Vivamus ut cursus eros. Vivamus maximus nulla nibh, vel interdum risus varius in."

    @Property(
        type = PropertyType.SELECTOR,
        name = "selector",
        description = "String Selector",
        category = "General",
        subcategory = "Category",
        options = ["test 1", "test 2 but my text is really long", "test 3 medium len"]
    )
    var selector = 1

    @Property(
        type = PropertyType.COLOR,
        name = "Color Picker",
        description = "Pick a color! (hopefully...)",
        category = "General",
        subcategory = "Category"
    )
    var myColor = Color.BLUE

    @Property(
        type = PropertyType.COLOR,
        name = "Color Picker wit no alpha",
        description = "Pick a color, but alpha headed out",
        category = "General",
        subcategory = "Category",
        allowAlpha = false
    )
    var alphaless = Color.MAGENTA

    @Property(
        type = PropertyType.SLIDER,
        name = "Slider",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd ",
        category = "General",
        subcategory = "General Settings",
        min = 1000,
        max = 1000000
    )
    var slide = 5

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Percent Slider",
        description = "A slider with a range of 0.0-1.0",
        category = "General",
        subcategory = "General Settings"
    )
    var percentSlide = 0.5f

    @Property(
        type = PropertyType.NUMBER,
        name = "A number!",
        description = "A number that can be incremented & decremented using button controls!",
        category = "General",
        subcategory = "General Settings",
        min = 5,
        max = 100
    )
    var number = 10

    @Property(
        type = PropertyType.SWITCH,
        name = "General 1",
        description = "This toggles something",
        category = "General",
        subcategory = "General Settings"
    )
    var toggle1 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "General 2",
        description = "This toggles something",
        category = "General",
        subcategory = "General Settings",
        hidden = true
    )
    var toggle2 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "General 3",
        description = "This toggles something",
        category = "General",
        subcategory = "General Settings"
    )
    var toggle3 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "General 4",
        description = "This toggles something",
        category = "General",
        subcategory = "General Settings"
    )
    var toggle4 = false

    @Property(
        type = PropertyType.SWITCH,
        name = "General 5",
        description = "This toggles something",
        category = "General",
        subcategory = "Category"
    )
    var toggle5 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "General 6",
        description = "This toggles something",
        category = "General",
        subcategory = "Category"
    )
    var toggle6 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Some kind of really long name",
        description = "This toggles something. This description is going to be really long, so hopefully it will wrap on to the next couple of lines making vigilance look cool!",
        category = "General",
        subcategory = "Category"
    )
    var toggle7 = false

    @Property(
        type = PropertyType.SWITCH,
        name = "General 8",
        description = "This toggles something",
        category = "General",
        subcategory = "Category"
    )
    var toggle8 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle10 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle11 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle12 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle13 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle14 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle15 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle16 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle17 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle18 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle20 = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle21 = true
    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle22 = true
    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle23 = true
    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle24 = true
    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle25 = true
    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle26 = true
    @Property(
        type = PropertyType.SWITCH,
        name = "Color 1",
        description = "This toggles something. I also have a pretty long description. EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE EEEEEEEEEEEEEEEE  EEEEEEE  wadawd awd awd  ",
        category = "Color",
        subcategory = "General Settings"
    )
    var toggle27 = true

//
//    @Data
//    var randomData: String by watched("Initial")

    init {
        initialize()
    }
}