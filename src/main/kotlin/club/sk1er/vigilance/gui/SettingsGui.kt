package club.sk1er.vigilance.gui

import club.sk1er.elementa.WindowScreen
import club.sk1er.elementa.components.*
import club.sk1er.elementa.components.input.UITextInput
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Category
import java.awt.Color

class SettingsGui(config: Vigilant) : WindowScreen() {
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

    private val searchBox = UIBlock(Color(41, 42, 46)).constrain {
        x = 0.pixels(true)
        y = 5.pixels()
        width = 20.pixels()
        height = 20.pixels()
    } childOf window effect ScissorEffect()

    private val searchIcon = SVGComponent.ofResource("/vigilance/search.svg").constrain {
        x = 4.pixels()
        y = CenterConstraint()
        width = 12.pixels()
        height = 12.pixels()
    } childOf searchBox

    private val searchContainer = UIContainer().constrain {
        x = SiblingConstraint(4f)
        y = 5.5f.pixels()
        width = 100.pixels()
        height = 12.pixels()
    } childOf searchBox

    private val searchInput = UITextInput("Search...", shadow = false).constrain {
        width = 75.pixels()
        height = RelativeConstraint(1f)
    } childOf searchContainer

    private val searchCloseIcon = SVGComponent.ofResource("/vigilance/x.svg").constrain {
        x = SiblingConstraint(4f)
        y = CenterConstraint()
        width = 12.pixels()
        height = 12.pixels()
    } childOf searchBox

    init {
        window.onKeyType { typedChar, keyCode ->
            if (typedChar.toInt() == 6)
                searchInput.grabWindowFocus()
            else
                defaultKeyBehavior(typedChar, keyCode)
        }

        searchBox.onMouseClick { event ->
            searchInput.grabWindowFocus()
            searchBox.animate {
                setWidthAnimation(Animations.OUT_EXP, 1f, ChildBasedSizeConstraint(4f) + 8.pixels())
            }
            event.stopPropagation()
        }.onMouseEnter {
            if (searchInput.isActive()) return@onMouseEnter
            searchBox.animate {
                setWidthAnimation(Animations.OUT_EXP, 1f, 65.pixels())
            }
        }.onMouseLeave {
            if (searchInput.isActive()) return@onMouseLeave
            hideSearch()
        }

        searchInput.onUpdate { searchTerm ->
            val searchCategory = config.getCategoryFromSearch(searchTerm)
            selectCategory(searchCategory)
        }.onFocus {
            searchInput.setActive(true)
            searchBox.animate {
                setWidthAnimation(Animations.OUT_EXP, 1f, ChildBasedSizeConstraint(4f) + 8.pixels())
            }
        }.onFocusLost {
            searchInput.setActive(false)
            hideSearch()
        }

        searchCloseIcon.onMouseClick { event ->
            searchInput.setText("")
            searchInput.releaseWindowFocus()
            event.stopPropagation()
        }

        window.onMouseClick {
            currentCategory.closePopups()
        }

        categoryScroller.setScrollBarComponent(categoryScrollBar, true)
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
        val newCategory = categories[category] ?: SettingsCategory(category) childOf categoryHolder
        if (newCategory == currentCategory) return

        currentCategory.hide()
        newCategory.unhide()
        currentCategory = newCategory

        categoryScroller.allChildren.filterIsInstance<CategoryLabel>().firstOrNull { it.isSelected }?.deselect()
    }

    private fun hideSearch() {
        searchBox.animate {
            setWidthAnimation(Animations.OUT_EXP, 1f, 20.pixels())
        }
    }

    companion object {
        val ACCENT_COLOR = Color(1, 165, 82)
        val GRAY_COLOR = Color(145, 145, 147)
        val DISABLED_COLOR = Color(73, 73, 73)
    }
}