package gg.essential.vigilance.data

import gg.essential.vigilance.Vigilant
import java.io.File

object ExampleConfig : Vigilant(File("config.toml")) {
    @Property(type = PropertyType.NUMBER, name = "Num Property", category = "Cat2")
    var numProperty = 0

    @Property(type = PropertyType.SLIDER, name = "Slider Property", category = "Cat1", subcategory = "Subcat1")
    var sliderProperty = 0

    @Property(type = PropertyType.TEXT, name = "Text Property", category = "Cat2", subcategory = "Subcat1")
    var textProperty = "Thing!"

    @Property(type = PropertyType.SWITCH, name = "Switch Property", category = "Cat1", subcategory = "Subcat2")
    var switchProperty = false
}