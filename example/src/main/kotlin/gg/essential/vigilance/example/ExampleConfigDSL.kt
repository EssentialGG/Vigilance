@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package gg.essential.vigilance.example

import gg.essential.universal.UChat
import gg.essential.vigilance.Vigilant
import java.awt.Color
import java.io.File

/**
 * The same configuration as the ExampleConfig, but using the property DSL.
 */
object ExampleConfigDSL : Vigilant(File("./config/example.toml")) {
    var demoCheckbox = false
    var demoSwitch = false
    var demoText = ""
    var demoParagraph = ""
    var demoPercentSlider = 0f
    var demoSlider = 0
    var demoDecimalSlider = 0f
    var demoNumber = 0
    var demoColor: Color = Color.WHITE
    var demoSelector = 0

    var offSwitch = false
    var onSwitch = true

    var emptyText = ""
    var textWithInitialValue = "I am a text property!"
    var textWithPlaceholder = ""
    var textWithPlaceholderAndInitialValue = "I am a text property!"

    var emptyParagraph = ""
    var paragraphWithInitialValue = "I am a paragraph! I can have text that is much, much longer than the regular text property. I also use Elementa's multiline text input component, which supports features like coyp paste, cursor navigation, click selection, and more :)"
    var paragraphWithPlaceholder = ""
    var paragraphWithPlaceholderAndInitialValue = "I am a paragraph! I can have text that is much, much longer than the regular text property. I also use Elementa's multiline text input component, which supports features like coyp paste, cursor navigation, click selection, and more :)"

    var percentSliderAtZero = 0f
    var percentSliderAtHalf = 0.5f

    var sliderMiddle = 5
    var negativeSlider = 0
    var hugeSlider = 0

    var numberMiddle = 5
    var hugeNumber = 0

    var nonWhiteColor = Color(20, 190, 240)
    var colorWithAlpha = Color(20, 190, 240, 255 / 2)

    var selectorInitialValue = 2
    var largeSelector = 0

    var switchWithLongName = false
    var switchWithLongDescription = false

    var hiddenProperty = false
    
    init {
        category("Property Overview") {
            checkbox(::demoCheckbox, "Checkbox", "This is a checkbox property. It stores a boolean value.")
            switch(::demoSwitch, "Switch", "This is a switch property. It stores a boolean value.")
            text(::demoText, "Text", "This is a text property. It stores a single line of continuous text.")
            paragraph(::demoParagraph, "Paragraph")
            percentSlider(
                ::demoPercentSlider,
                "Percent Slider",
                "This is a percent slider property. It stores a floating-point number between 0.0 and 1.0."
            )
            slider(
                ::demoSlider,
                "Slider",
                "This is a slider property. It stores an integer between a defined minimum and maximum integer.",
                min = 0,
                max = 10
            )
            decimalSlider(
                ::demoDecimalSlider,
                "Decimal Slider",
                "This is a decimal slider property. It stores a floating point number between a defined minimum and maximum.",
                0f,
                12f,
                2
            )
            number(
                ::demoNumber,
                "Number",
                "This is a number property. It stores an integer between a defined minimum and maximum integer.",
                min = 0,
                max = 10
            )
            color(::demoColor, "Color", "This is a color property. It stores a color.")
            selector(
                ::demoSelector,
                "Selector",
                "This is a selector property. It stores a specific item in a list of strings. The property will store the index of the list, not the string.",
                options = listOf("Option 1", "Option 2", "Option 3")
            )
            button("Button", "This is a button property. It runs an action when clicked.") {
                UChat.chat("demoButton clicked!")
            }
        }

        category("Property Deep-Dive") {
            subcategory("Switches") {
                switch(::offSwitch, "Initially off switch", "Switch that starts in the off position")
                switch(::onSwitch, "Initially on switch", "Switch that starts in the on position")
            }

            subcategory("Texts") {
                text(::emptyText, "Empty text", "A text property with no initial text or placeholder")
                text(::textWithInitialValue, "Text with initial value", "A text property with some initial value but no placeholder")
                text(
                    ::textWithPlaceholder,
                    "Text with placeholder",
                    "A text property with a placeholder bu not initial value",
                    placeholder = "Type some text!"
                )
                text(
                    ::textWithPlaceholderAndInitialValue,
                    "Text with placeholder and initial value",
                    "A text property with a placeholder and initial value. The placeholder does not appear unless the text is deleted by the user.",
                    placeholder = "Type some text!"
                )
            }

            subcategory("Paragraphs") {
                paragraph(::emptyParagraph, "Empty paragraph", "A paragraph property with no initial text or placeholder")
                paragraph(
                    ::paragraphWithInitialValue,
                    "Paragraph with initial value",
                    "A paragraph property with some initial value but no placeholder"
                )
                paragraph(
                    ::paragraphWithPlaceholder,
                    "Paragraph with placeholder",
                    "A paragraph property with a placeholder bu not initial value",
                    placeholder = "Type some text!"
                )
                paragraph(
                    ::paragraphWithPlaceholderAndInitialValue,
                    "Paragraph with placeholder and initial value",
                    "A paragraph property with a placeholder and initial value. The placeholder does not appear unless the text is deleted by the user.",
                    placeholder = "Type some text!"
                )
            }

            subcategory("Percent Sliders") {
                percentSlider(::percentSliderAtZero, "Percent slider", "A percent slider property with a starting value of 0.0 (0%).")
                percentSlider(::percentSliderAtHalf, "Percent slider at half", "A percent slider property with a starting value of 0.5 (50%).")
            }

            subcategory("Sliders") {
                slider(
                    ::sliderMiddle,
                    "Slider starting in middle",
                    "A slider property initially in the middle",
                    min = 0,
                    max = 10
                )
                slider(
                    ::negativeSlider,
                    "Slider with negative values",
                    "A slider property with negative numbers in its range",
                    min = -10,
                    max = 10
                )
                slider(
                    ::hugeSlider,
                    "Slider with huge range",
                    "A slider property with a huge range (0 to 1,000,000)",
                    min = 0,
                    max = 1_000_000
                )
            }

            subcategory("Numbers") {
                number(
                    ::numberMiddle,
                    "Number with initial value",
                    "A number property with an initial value starting in the middle of its range.",
                    min = 0,
                    max = 10
                )
                number(
                    ::hugeNumber,
                    "Number with huge range",
                    "A number property with a huge range (0 to 1,000,000).",
                    min = 0,
                    max = 1_000_000
                )
            }

            subcategory("Colors") {
                color(
                    ::nonWhiteColor,
                    "Color with an initial, non-white value",
                    "A color property with an initial value of a non-white color.",
                    allowAlpha = false
                )
                color(::colorWithAlpha, "Color with alpha", "A color property which allows the selection of alpha.") {
                    println("colorWithAlpha listener activated! New color: $it")
                }
            }

            subcategory("Selectors") {
                selector(
                    ::selectorInitialValue,
                    "Selector with initial non-zero value",
                    "A selector property whose initially-selected value is not the first item.",
                    options = listOf("Option 1", "Option 2", "Option 3", "Option 4")
                )

                selector(
                    ::largeSelector,
                    "Selector with many options",
                    "A selector property which has a large number of options",
                    options = listOf(
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
                    )
                )
            }

            subcategory("Buttons") {
                button("Normal button", "A normal button. Buttons use the placeholder property for their text. If no placeholder is provided (or it is empty), it will be \"Activate\". Button field values do not matter, but they cannot be null.") {
                    UChat.chat("normalButton clicked!")
                }
                button("Button with text", "A button that has a custom placeholder, giving it different text") {
                    UChat.chat("customButton clicked!")
                }
            }
        }

        category("Meta") {
            switch(
                ::switchWithLongName,
                "This is a switch property with a very long name. It is recommended to use the description for lengthy property text, however this is still supported"
            )
            switch(
                ::switchWithLongDescription,
                "Property with long description",
                "This is a property with a very long description. As the above property says, the description is the preferred place for lengthy instruction text within a property. However, long text here is still not recommended -- try to keep descriptions as concise as possible!"
            )
        }

        category("Hidden") {
            switch(
                ::hiddenProperty,
                "Hidden switch",
                "This is a hidden property. It will not appear in the in-game GUI, but will still be managed by Vigilance (i.e. saved to a file, and changeable via code).",
                hidden = true
            )
        }

        initialize()
    }
}
