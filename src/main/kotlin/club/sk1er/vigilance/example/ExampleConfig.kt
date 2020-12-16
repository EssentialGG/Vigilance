@file:Suppress("unused")

package club.sk1er.vigilance.example

import club.sk1er.mods.core.universal.UniversalChat
import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Property
import club.sk1er.vigilance.data.PropertyType
import java.awt.Color
import java.io.File

/**
 * An example configuration which gives an overview of all property types,
 * as well as a visual demonstration of each option. Also demos some
 * aspects such as fields with different initial values.
 */
object ExampleConfig : Vigilant(File("./config/example.toml")) {
    @Property(
        type = PropertyType.SWITCH,
        name = "Switch",
        description = "This is a switch property. It stores a boolean value.",
        category = "Property Overview"
    )
    var demoSwitch = false

    @Property(
        type = PropertyType.TEXT,
        name = "Text",
        description = "This is a text property. It stores a single line of continuous text.",
        category = "Property Overview"
    )
    var demoText = ""

    @Property(
        type = PropertyType.PARAGRAPH,
        name = "Paragraph",
        description = "This is a paragraph property. It stores a multi-line piece of text, and expands as the user writes more text",
        category = "Property Overview"
    )
    var demoParagraph = ""

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Percent Slider",
        description = "This is a percent slider property. It stores a floating-point number between 0.0 and 1.0.",
        category = "Property Overview"
    )
    var demoPercentSlider = 0f

    @Property(
        type = PropertyType.SLIDER,
        name = "Slider",
        description = "This is a slider property. It stores an integer between a defined minimum and maximum integer.",
        category = "Property Overview",
        min = 0,
        max = 10
    )
    var demoSlider = 0

    @Property(
        type = PropertyType.NUMBER,
        name = "Number",
        description = "This is a number property. It stores an integer between a defined minimum and maximum integer.",
        category = "Property Overview",
        min = 0,
        max = 10
    )
    var demoNumber = 0

    @Property(
        type = PropertyType.COLOR,
        name = "Color",
        description = "This is a color property. It stores a color.",
        category = "Property Overview"
    )
    var demoColor: Color = Color.WHITE

    @Property(
        type = PropertyType.SELECTOR,
        name = "Selector",
        description = "This is a selector property. It stores a specific item in a list of strings. The property will store the index of the list, not the string.",
        category = "Property Overview",
        options = ["Option 1", "Option 2", "Option 3"]
    )
    var demoSelector = 0

    @Property(
        type = PropertyType.BUTTON,
        name = "Button",
        description = "This is a button property. It runs an action when clicked.",
        category = "Property Overview"
    )
    fun demoButton() {
        UniversalChat.chat("demoButton clicked!")
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "Initially off switch",
        description = "Switch that starts in the off position",
        category = "Property Deep-Dive",
        subcategory = "Switches"
    )
    var offSwitch = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Initially on switch",
        description = "Switch that starts in the on position",
        category = "Property Deep-Dive",
        subcategory = "Switches"
    )
    var onSwitch = true

    @Property(
        type = PropertyType.TEXT,
        name = "Empty text",
        description = "A text property with no initial text or placeholder",
        category = "Property Deep-Dive",
        subcategory = "Texts"
    )
    var emptyText = ""

    @Property(
        type = PropertyType.TEXT,
        name = "Text with initial value",
        description = "A text property with some initial value but no placeholder",
        category = "Property Deep-Dive",
        subcategory = "Texts"
    )
    var textWithInitialValue = "I am a text property!"

    @Property(
        type = PropertyType.TEXT,
        name = "Text with placeholder",
        description = "A text property with a placeholder but no initial value",
        category = "Property Deep-Dive",
        subcategory = "Texts",
        placeholder = "Type some text!"
    )
    var textWithPlaceholder = ""

    @Property(
        type = PropertyType.TEXT,
        name = "Text with placeholder and initial value",
        description = "A text property with a placeholder and initial value. The placeholder does not appear unless the text is deleted by the user.",
        category = "Property Deep-Dive",
        subcategory = "Texts",
        placeholder = "Type some text!"
    )
    var textWithPlaceholderAndInitialValue = "I am a text property!"

    @Property(
        type = PropertyType.PARAGRAPH,
        name = "Empty paragraph",
        description = "A paragraph property with no initial text or placeholder",
        category = "Property Deep-Dive",
        subcategory = "Paragraphs"
    )
    var emptyParagraph = ""

    @Property(
        type = PropertyType.PARAGRAPH,
        name = "Paragraph with initial value",
        description = "A paragraph property with some initial value but no placeholder",
        category = "Property Deep-Dive",
        subcategory = "Paragraphs"
    )
    var paragraphWithInitialValue = "I am a paragraph! I can have text that is much, much longer than the regular text property. I also use Elementa's multiline text input component, which supports features like coyp paste, cursor navigation, click selection, and more :)"

    @Property(
        type = PropertyType.PARAGRAPH,
        name = "Paragraph with placeholder",
        description = "A paragraph property with a placeholder but no initial value",
        category = "Property Deep-Dive",
        subcategory = "Paragraphs",
        placeholder = "Type some text!"
    )
    var paragraphWithPlaceholder = ""

    @Property(
        type = PropertyType.PARAGRAPH,
        name = "Paragraph with placeholder and initial value",
        description = "A paragraph property with a placeholder and initial value. The placeholder does not appear unless the text is deleted by the user.",
        category = "Property Deep-Dive",
        subcategory = "Paragraphs",
        placeholder = "Type some text!"
    )
    var paragraphWithPlaceholderAndInitialValue = "I am a paragraph! I can have text that is much, much longer than the regular text property. I also use Elementa's multiline text input component, which supports features like coyp paste, cursor navigation, click selection, and more :)"

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Percent slider",
        description = "A percent slider property with a starting value of 0.0 (0%).",
        category = "Property Deep-Dive",
        subcategory = "Percent Sliders"
    )
    var percentSliderAtZero = 0.0f

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Percent slider at half",
        description = "A percent slider property with a starting value of 0.5 (50%).",
        category = "Property Deep-Dive",
        subcategory = "Percent Sliders"
    )
    var percentSliderAtHalf = 0.5f

    @Property(
        type = PropertyType.SLIDER,
        name = "Slider starting in middle",
        description = "A slider property initially in the middle.",
        category = "Property Deep-Dive",
        subcategory = "Sliders",
        min = 0,
        max = 10
    )
    var sliderMiddle = 5

    @Property(
        type = PropertyType.SLIDER,
        name = "Slider with negative values",
        description = "A slider property with negative numbers in its range",
        category = "Property Deep-Dive",
        subcategory = "Sliders",
        min = -10,
        max = 10
    )
    var negativeSlider = 0

    @Property(
        type = PropertyType.SLIDER,
        name = "Slider with huge range",
        description = "A slider property with a huge range (0 to 1,000,000)",
        category = "Property Deep-Dive",
        subcategory = "Sliders",
        min = 0,
        max = 1_000_000
    )
    var hugeSlider = 0

    @Property(
        type = PropertyType.NUMBER,
        name = "Number with initial value",
        description = "A number property with an initial value starting in the middle of its range.",
        category = "Property Deep-Dive",
        subcategory = "Numbers",
        min = 0,
        max = 10
    )
    var numberMiddle = 5

    @Property(
        type = PropertyType.NUMBER,
        name = "Number with huge range",
        description = "A number property with a huge range (0 to 1,000,000).",
        category = "Property Deep-Dive",
        subcategory = "Numbers",
        min = 0,
        max = 1_000_000
    )
    var hugeNumber = 0

    @Property(
        type = PropertyType.COLOR,
        name = "Color with an initial, non-white value",
        description = "A color property with an initial value of a non-white color.",
        category = "Property Deep-Dive",
        subcategory = "Colors",
        allowAlpha = false
    )
    var nonWhiteColor = Color(20, 190, 240)

    @Property(
        type = PropertyType.COLOR,
        name = "Color with alpha",
        description = "A color property which allows the selection of alpha",
        category = "Property Deep-Dive",
        subcategory = "Colors",
        allowAlpha = true
    )
    var colorWithAlpha = Color(20, 190, 240, 255 / 2)

    @Property(
        type = PropertyType.SELECTOR,
        name = "Selector with initial non-zero value",
        description = "A selector property whose initially-selected value is not the first item.",
        category = "Property Deep-Dive",
        subcategory = "Selectors",
        options = ["Option 1", "Option 2", "Option 3", "Option 4"]
    )
    var selectorInitialValue = 2

    @Property(
        type = PropertyType.SELECTOR,
        name = "Selector with many options",
        description = "A selector property which has a large number of options",
        category = "Property Deep-Dive",
        subcategory = "Selectors",
        options = [
            "Option 1",
            "Option 2",
            "Option 3",
            "Option 4",
            "Option 5",
            "Option 6",
            "Option 7",
            "Option 8",
            "Option 9",
            "Option 10",
            "Option 11",
            "Option 12",
            "Option 13",
            "Option 14",
            "Option 15",
            "Option 16",
            "Option 17",
            "Option 18",
            "Option 19",
            "Option 20",
            "Option 21",
            "Option 22",
            "Option 23",
            "Option 24",
            "Option 25",
            "Option 26",
            "Option 27",
            "Option 28",
            "Option 29",
            "Option 30"
        ]
    )
    var largeSelector = 0

    @Property(
        type = PropertyType.BUTTON,
        name = "Normal button",
        description = "A normal button. Buttons use the placeholder property for their text. If no placeholder is provided (or it is empty), it will be \"Activate\".",
        category = "Property Deep-Dive",
        subcategory = "Buttons"
    )
    fun normalButtonAction() {
        UniversalChat.chat("normalButton clicked!")
    }

    @Property(
        type = PropertyType.BUTTON,
        name = "Button with text",
        description = "A button that has a custom placeholder, giving it different text",
        placeholder = "Click Me!",
        category = "Property Deep-Dive",
        subcategory = "Buttons"
    )
    fun customButtonAction() {
        UniversalChat.chat("customButton clicked!")
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "This is a switch property with a very long name. It is recommended to use the description for lengthy property text, however this is still supported",
        category = "Meta"
    )
    var switchWithLongName = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Property with long description",
        description = "This is a property with a very long description. As the above property says, the description is the preferred place for lengthy instruction text within a property. However, long text here is still not recommended -- try to keep descriptions as concise as possible!",
        category = "Meta"
    )
    var switchWithLongDescription = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Hidden switch",
        description = "This is a hidden property. It will not appear in the in-game GUI, but will still be managed by Vigilance (i.e. saved to a file, and changeable via code).",
        category = "Hidden",
        hidden = true
    )
    var hiddenProperty = false

    init {
        initialize()

        registerListener(::colorWithAlpha) {
            UniversalChat.chat("colorWithAlpha listener activated! New color: $it")
        }
    }
}
