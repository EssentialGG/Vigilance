package gg.essential.vigilance.example

import gg.essential.universal.UChat
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.awt.Color
import java.io.File
import kotlin.math.PI

/**
 * An example configuration which gives an overview of all property types,
 * as well as a visual demonstration of each option. Also demos some
 * aspects such as fields with different initial values.
 */
object ExampleConfig : Vigilant(File("./config/example.toml")) {
    @Property(
        type = PropertyType.CHECKBOX,
        name = "Checkbox",
        description = "This is a checkbox property. It stores a boolean value.",
        category = "Property Overview"
    )
    var demoCheckbox = true

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
        type = PropertyType.DECIMAL_SLIDER,
        name = "Decimal Slider",
        description = "This is a decimal slider property. It stores a floating point number between a defined minimum and maximum.",
        category = "Property Overview",
        minF = 0f,
        maxF = 12f,
        decimalPlaces = 2
    )
    var decimalSlider = 1f

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
        UChat.chat("demoButton clicked!")
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Tom",
        description = "",
        category = "Property Overview"
    )
    var toggleTom = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Property Pete",
        description = "",
        category = "Property Overview",
        subcategory = "Subcategory Steve"
    )
    var propertyPete = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Checkbox Chuck",
        description = "",
        category = "Property Overview",
        subcategory = "Subcategory Steve"
    )
    var checkboxChuck = false

    @Property(
        type = PropertyType.TEXT,
        name = "Password",
        description = "Anything you type here won't be visible unless the eye is clicked!",
        category = "Property Overview",
        placeholder = "HELLO",
        protectedText = true
    )
    var password: String = ""

    @Property(
        type = PropertyType.SWITCH,
        name = "Switch with dependants",
        description = "When ticked, this switch will make another setting appear",
        category = "Property Deep-Dive",
        subcategory = "Dependencies"
    )
    var dependency = true

    @Property(
        type = PropertyType.TEXT,
        name = "Dependant",
        description = "This setting depends on the above switch!",
        category = "Property Deep-Dive",
        subcategory = "Dependencies"
    )
    var dependant: String = "hey"

    @Property(
        type = PropertyType.TEXT,
        name = "Inverted",
        description = "This setting only shows when Switch with dependants toggle is false",
        category = "Property Deep-Dive",
        subcategory = "Dependencies"
    )
    var inverted = "hi"

    @Property(
        type = PropertyType.SELECTOR,
        name = "Selector with dependants",
        description = "When Show is selected, this selector will make the Value Dependant setting appear",
        category = "Property Deep-Dive",
        subcategory = "Dependencies",
        options = ["Show", "Hide", "Also Hide"]
    )
    var selectorDependency = 0

    @Property(
        type = PropertyType.TEXT,
        name = "Value Dependant",
        description = "This setting only shows when Selector with dependants is set to Show",
        category = "Property Deep-Dive",
        subcategory = "Dependencies",
    )
    var valueDependant = "Fancy"

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
    var paragraphWithInitialValue = "I am a paragraph! I can have text that is much, much longer than the regular text property. I also use Elementa's multiline text input component, which supports features like copy paste, cursor navigation, click selection, and more :)"

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
    var paragraphWithPlaceholderAndInitialValue = "I am a paragraph! I can have text that is much, much longer than the regular text property. I also use Elementa's multiline text input component, which supports features like copy paste, cursor navigation, click selection, and more :)"

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
        type = PropertyType.DECIMAL_SLIDER,
        name = "Decimal Slider with small range",
        description = "A decimal slider property with a small range (0 to 1)",
        category = "Property Deep-Dive",
        subcategory = "Decimal Sliders",
        minF = 0f,
        maxF = 1f,
        decimalPlaces = 3
    )
    var smallRangeDecimalSlider = .5f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Decimal Slider with many decimal places",
        description = "A decimal slider property with ten decimal places. Note that the value is a floating point number so it will get trimmed.",
        category = "Property Deep-Dive",
        subcategory = "Decimal Sliders",
        minF = 1f,
        maxF = 5f,
        decimalPlaces = 10
    )
    var tenDecimalPlacesSlider = PI.toFloat()

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
        name = "Number with increment",
        description = "Number with an increment that is not 1. Hint: Try holding shift!",
        category = "Property Deep-Dive",
        subcategory = "Numbers",
        min = -500,
        max = 500,
        increment = 25
    )
    var numberIncrement = 250

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
        UChat.chat("normalButton clicked!")
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
        UChat.chat("customButton clicked!")
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "Conditional Property (W)",
        description = "This property will only be visible if a condition is met! In this case, it will be visible if you are on Windows.",
        category = "Property Deep-Dive",
        subcategory = "Hidden (Conditional)"
    )
    var windowsOnlyProperty = false


    @Property(
        type = PropertyType.SWITCH,
        name = "Conditional Property (M)",
        description = "This property will only be visible if a condition is met! In this case, it will be visible if you are on macOS.",
        category = "Property Deep-Dive",
        subcategory = "Hidden (Conditional)"
    )
    var macOnlyProperty = false


    @Property(
        type = PropertyType.SWITCH,
        name = "Conditional Property (L)",
        description = "This property will only be visible if a condition is met! In this case, it will be visible if you are on Linux.",
        category = "Property Deep-Dive",
        subcategory = "Hidden (Conditional)"
    )
    var linuxOnlyProperty = false

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

        val clazz = javaClass
        registerListener(clazz.getDeclaredField("colorWithAlpha")) { color: Color ->
            UChat.chat("colorWithAlpha listener activated! New color: $color")
        }

        addDependency(clazz.getDeclaredField("dependant"), clazz.getDeclaredField("dependency"))
        addDependency(clazz.getDeclaredField("propertyPete"), clazz.getDeclaredField("toggleTom"))
        addDependency(clazz.getDeclaredField("checkboxChuck"), clazz.getDeclaredField("toggleTom"))
        addDependency("valueDependant", "selectorDependency") { value: Int -> value == 0 }
        addDependency("inverted", "dependency") { value: Boolean -> !value }

        val os = System.getProperty("os.name", "windows").lowercase()
        hidePropertyIf("windowsOnlyProperty") { !os.contains("windows") }
        hidePropertyIf("macOnlyProperty") { !os.contains("mac") }
        hidePropertyIf("linuxOnlyProperty") { !os.contains("linux") }

        setCategoryDescription(
            "Property Overview",
            "This category is a quick overview of all of the components. For a deep-dive into the component, check their specific subcategories."
        )

        setCategoryDescription(
            "Property Deep-Dive",
            "This category will go in depth into every component, and show off some of the customization options available in Vigilance. It contains a subcategory for every single property type available."
        )

        setSubcategoryDescription(
            "Property Deep-Dive",
            "Buttons",
            "Buttons are a great way for the user to run an action. Buttons don't have any associated state, and as such their annotation target has to be a method."
        )
    }
}
