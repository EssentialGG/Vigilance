package club.sk1er.vigilance.data

import club.sk1er.vigilance.Vigilant

object ExampleConfig : Vigilant() {
    @Property(type = PropertyType.NUMBER, name = "Num Property", category = "Cat2")
    var numProperty = 0

    @Property(type = PropertyType.SLIDER, name = "Slider Property", category = "Cat1", subcategory = "Subcat1")
    var sliderProperty = 0

    @Property(type = PropertyType.TEXT, name = "Text Property", category = "Cat2", subcategory = "Subcat1")
    var textProperty = "Thing!"

    @Property(type = PropertyType.SWITCH, name = "Switch Property", category = "Cat1", subcategory = "Subcat2")
    var switchProperty = false
}