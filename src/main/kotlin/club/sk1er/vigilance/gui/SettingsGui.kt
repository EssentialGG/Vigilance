package club.sk1er.vigilance.gui

import club.sk1er.elementa.WindowScreen
import club.sk1er.elementa.components.ScrollComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.FillConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Category
import java.awt.Color

class SettingsGui(private val config: Vigilant) : WindowScreen() {
    private val background = UIBlock(Color(22, 22, 24)).constrain {
        width = RelativeConstraint(1f)
        height = RelativeConstraint(1f)
    } childOf window

    private val outerContainer = UIContainer().constrain {
        x = RelativeConstraint(0.1f)
        y = RelativeConstraint(0.1f)
        width = RelativeConstraint(0.8f)
        height = RelativeConstraint(0.8f)
    } childOf window

    private val sidebar = UIContainer().constrain {
        width = RelativeConstraint(0.3f)
        height = RelativeConstraint(1f)
    } childOf outerContainer

    private val titleLabel = UIText("Settings", shadow = false).constrain {
        x = CenterConstraint() - 10.pixels()
        textScale = 2f.pixels()
    } childOf sidebar

    private val scrollContainer = UIContainer().constrain {
        y = SiblingConstraint() + 20.pixels()
        width = RelativeConstraint(1f) - 10.pixels()
        height = FillConstraint()
    } childOf sidebar

    private val categoryScroller = ScrollComponent().constrain {
        width = RelativeConstraint(1f)
        height = RelativeConstraint(1f)
    } childOf scrollContainer

    private val categoryScrollBar = UIBlock(Color(45, 45, 45)).constrain {
        x = 7.5f.pixels(true)
        width = 3.pixels()
    } childOf scrollContainer

    init {
        categoryScroller.setScrollBarComponent(categoryScrollBar)
    }

    private val categories = config.getCategories().associateWith(::SettingsCategory)

    init {
        categories.keys.forEach { cat ->
            val label = CategoryLabel(this, cat)
            label childOf categoryScroller
        }
    }

    private val splitter = UIBlock(Color(151, 151, 151)).constrain {
        x = SiblingConstraint()
        width = 1.pixels()
        height = RelativeConstraint(1f)
    } childOf outerContainer

    private val categoryHolder = UIContainer().constrain {
        x = SiblingConstraint() + 5.pixels()
        width = FillConstraint()
        height = RelativeConstraint(1f)
    } childOf outerContainer

    private var currentCategory = categories.values.first()

    init {
        categories.values.forEach {
            it childOf categoryHolder

            if (it != currentCategory) {
                it.hide(true)
            }
        }

        categoryScroller.allChildren.filterIsInstance<CategoryLabel>().first().select()
    }

    fun selectCategory(category: Category) {
        val newCategory = categories[category] ?: SettingsCategory(category)
        if (newCategory == currentCategory) return

        currentCategory.hide()
        newCategory.unhide()
        currentCategory = newCategory

        categoryScroller.allChildren.filterIsInstance<CategoryLabel>().firstOrNull { it.isSelected }?.deselect()
    }
}