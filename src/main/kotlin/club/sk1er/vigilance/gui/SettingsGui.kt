package club.sk1er.vigilance.gui

import club.sk1er.elementa.WindowScreen
import club.sk1er.elementa.components.*
import club.sk1er.elementa.components.input.UITextInput
import club.sk1er.elementa.components.inspector.Inspector
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Category
import club.sk1er.vigilance.gui.modal.ConfirmationModal

class SettingsGui(config: Vigilant) : WindowScreen() {
    private val background = UIBlock(VigilancePalette.BACKGROUND).constrain {
        width = RelativeConstraint(1f)
        height = RelativeConstraint(1f)
    } childOf window

    private val outerContainer = UIContainer().constrain {
        x = RelativeConstraint(0.12f)
        y = RelativeConstraint(0.1f)
        width = RelativeConstraint(0.78f)
        height = RelativeConstraint(0.8f)
    } childOf window

    private val sidebar = UIContainer().constrain {
        width = RelativeConstraint(0.25f)
        height = RelativeConstraint(1f)
    } childOf outerContainer

    private val titleLabel = UIText("Settings", shadow = false).constrain {
        textScale = 2f.pixels()
    } childOf sidebar

    private val scrollContainer = UIContainer().constrain {
        y = SiblingConstraint() + 40.pixels()
        width = RelativeConstraint(1f) - 10.pixels()
        height = FillConstraint()
    } childOf sidebar

    private val categoryScroller = ScrollComponent(pixelsPerScroll = 25f).constrain {
        width = RelativeConstraint(1f)
        height = RelativeConstraint(1f)
    } childOf scrollContainer

    private val categoryScrollBar = UIBlock(VigilancePalette.SCROLL_BAR).constrain {
        x = 7.5f.pixels(true)
        width = 3.pixels()
    } childOf scrollContainer

    private val resetBox = UIBlock(VigilancePalette.DARK_HIGHLIGHT).constrain {
        x = 5.pixels(true)
        y = 5.pixels()
        width = 20.pixels()
        height = 20.pixels()
    } effect ScissorEffect() childOf window

    private val resetIcon = UIImage.ofResource("/vigilance/trash.png").constrain {
        x = 4.5.pixels()
        y = CenterConstraint()
        width = 15.pixels()
        height = 15.pixels()
        color = VigilancePalette.WARNING.toConstraint()
    } childOf resetBox

    private val resetTextContainer = UIContainer().constrain {
        x = SiblingConstraint(4f)
        y = CenterConstraint()
        width = ChildBasedSizeConstraint()
        height = 100.percent()
    } childOf resetBox

    private val resetText = UIText("Reset All Settings").constrain {
        y = CenterConstraint()
        color = VigilancePalette.WARNING.toConstraint()
    } childOf resetTextContainer

    private val searchBox = UIBlock(VigilancePalette.DARK_HIGHLIGHT).constrain {
        x = 5.pixels(true)
        y = SiblingConstraint(10f)
        width = 20.pixels()
        height = 20.pixels()
    } childOf window effect ScissorEffect()

    private val searchIcon = UIImage.ofResource("/vigilance/search.png").constrain {
        x = 4.pixels()
        y = CenterConstraint()
        width = 16.pixels()
        height = 16.pixels()
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

        val resetConfirmationModal = ConfirmationModal("Are you sure you want to reset ALL settings?") {
            // TODO: Reset settings
        } childOf window

        resetBox.onMouseClick {
            resetConfirmationModal.fadeIn()
        }.onMouseEnter {
            resetBox.animate {
                setWidthAnimation(Animations.OUT_EXP, 1f, ChildBasedSizeConstraint(4f) + 8.pixels())
            }
        }.onMouseLeave {
            resetBox.animate {
                setWidthAnimation(Animations.OUT_EXP, 1f, 20.pixels())
            }
        }

        categoryScroller.setVerticalScrollBarComponent(categoryScrollBar, true)
    }

    private val categories = config.getCategories().associateWith(::SettingsCategory)

    init {
        categories.keys.forEach { cat ->
            val label = CategoryLabel(this, cat)
            label childOf categoryScroller
        }
    }

    private val splitter = UIBlock(VigilancePalette.DARK_DIVIDER).constrain {
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
        newCategory.scrollToTop()
        currentCategory = newCategory

        categoryScroller.allChildren.filterIsInstance<CategoryLabel>().firstOrNull { it.isSelected }?.deselect()
    }

    private fun hideSearch() {
        searchBox.animate {
            setWidthAnimation(Animations.OUT_EXP, 1f, 20.pixels())
        }
    }
}
