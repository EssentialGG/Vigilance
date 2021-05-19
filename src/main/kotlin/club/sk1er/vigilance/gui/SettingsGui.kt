package club.sk1er.vigilance.gui

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.WindowScreen
import club.sk1er.elementa.components.*
import club.sk1er.elementa.components.input.UITextInput
import club.sk1er.elementa.components.inspector.Inspector
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.elementa.state.toConstraint
import club.sk1er.mods.core.universal.UKeyboard
import club.sk1er.vigilance.VigilanceConfig
import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Category
import club.sk1er.vigilance.gui.settings.*
import java.awt.Color

class SettingsGui(private val config: Vigilant) : WindowScreen() {
    private val background = UIBlock().constrain {
        width = RelativeConstraint(1f)
        height = RelativeConstraint(1f)
        color = VigilancePalette.backgroundState.toConstraint()
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

    private val titleLabel = UIWrappedText(config.guiTitle, shadow = false).constrain {
        textScale = 2f.pixels()
        width = 90.percent()
        color = VigilancePalette.brightTextState.toConstraint()
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

    private val categoryScrollBar = UIBlock().constrain {
        x = 7.5f.pixels(true)
        width = 3.pixels()
        color = VigilancePalette.scrollBarState.toConstraint()
    } childOf scrollContainer

    private val searchBox = UIBlock().constrain {
        x = 5.pixels(true)
        y = 5.pixels()
        width = 20.pixels()
        height = 20.pixels()
        color = VigilancePalette.searchBarBackgroundState.toConstraint()
    } childOf window effect ScissorEffect()

    private val searchIcon = UIImage.ofResource("/vigilance/search.png").constrain {
        x = 2.pixels()
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
        color = VigilancePalette.brightTextState.toConstraint()
    } childOf searchBox

    init {
        // this doesn't seem to work?
        //window.onKeyType { typedChar, keyCode ->
        //    if (typedChar.toInt() == 6)
        //        searchInput.grabWindowFocus()
        //    else
        //        defaultKeyBehavior(typedChar, keyCode)
        //}

        searchBox.onMouseClick { event ->
            if (event.mouseButton == 1 && searchInput.isActive()) {
                hideSearch()
            } else {
                searchInput.grabWindowFocus()
                searchBox.animate {
                    setWidthAnimation(Animations.OUT_EXP, 1f, ChildBasedSizeConstraint(4f) + 8.pixels())
                }
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
        }.onMouseEnter {
            animate {
                setColorAnimation(Animations.OUT_EXP, .3f, VigilancePalette.warningState.toConstraint())
            }
        }.onMouseLeave {
            animate {
                setColorAnimation(Animations.OUT_EXP, .3f, VigilancePalette.brightTextState.toConstraint())
            }
        }

        window.onMouseClick {
            currentCategory.closePopups()
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

    private val splitter = UIBlock().constrain {
        x = SiblingConstraint()
        width = 1.pixels()
        height = RelativeConstraint(1f)
        color = VigilancePalette.darkDividerState.toConstraint()
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

        fun UIComponent.click(): Unit =
            mouseClick(getLeft() + (getRight() - getLeft()) / 2.0, getTop() + (getBottom() - getTop()) / 2.0, 0)


        window.onKeyType { typedChar, keyCode ->
            if (UKeyboard.isKeyDown(UKeyboard.KEY_MINUS)) {
                Inspector(window) childOf window
                return@onKeyType
            }
            if (!searchInput.isActive() && typedChar.isLetterOrDigit()) {
                searchBox.click()
                searchInput.setText("${searchInput.getText()}$typedChar")
                return@onKeyType
            }

            try {
                for (child in currentCategory.scroller.allChildren) {
                    if (child.isHovered() && child is DataBackedSetting) {
                        when (child.component) {
                            is AbstractSliderComponent -> if (keyCode == UKeyboard.KEY_LEFT) {
                                child.component.incrementBy(-.05f)
                            } else if (keyCode == UKeyboard.KEY_RIGHT) {
                                child.component.incrementBy(.05f)
                            }
                            is NumberComponent -> if (keyCode == UKeyboard.KEY_UP) {
                                child.component.increment()
                            } else if (keyCode == UKeyboard.KEY_DOWN) {
                                child.component.decrement()
                            }
                            is SwitchComponent -> when (keyCode) {
                                UKeyboard.KEY_LEFT -> if (child.component.enabled) child.component.click()
                                UKeyboard.KEY_RIGHT -> if (!child.component.enabled) child.component.click()
                                UKeyboard.KEY_ENTER -> child.component.click()
                            }
                            is CheckboxComponent -> if (keyCode == UKeyboard.KEY_ENTER) child.component.click()
                            is ButtonComponent -> if (keyCode == UKeyboard.KEY_ENTER) child.component.container.click()
                            is SelectorComponent -> if (keyCode == UKeyboard.KEY_UP) {
                                child.component.dropDown.select(child.component.dropDown.getValue() - 1)
                            } else if (keyCode == UKeyboard.KEY_DOWN) {
                                child.component.dropDown.select(child.component.dropDown.getValue() + 1)
                            }
                        }
                        break
                    }
                }
            } catch (e: Exception) {
                // idk this crashed once lol
                e.printStackTrace()
            }
        }
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

    // this is temporary and will *probably* not be in a proper modcore release
    override fun onTick() {
        if (VigilanceConfig.awaitShowColourWindow && config is VigilanceConfig) {
            VigilanceConfig.awaitShowColourWindow = false
            var d = false
            var o = 0f to 0f
            val block = UIBlock(VigilancePalette.backgroundState.toConstraint()).constrain {
                x = CenterConstraint()
                y = CenterConstraint()
                height = 300.pixels()
                width = 200.pixels()
            }.onMouseClick { e ->
                if (e.mouseButton == 0) {
                    o = e.absoluteX to e.absoluteY
                    d = true
                }
            }.onMouseDrag { mouseX, mouseY, _ ->
                if (d) {
                    val x = mouseX + getLeft()
                    val y = mouseY + getTop()
                    setX((getLeft() + (x - o.first)).pixels())
                    setY((getTop() + (y - o.second)).pixels())
                    o = x to y
                }
            }.onMouseRelease {
                d = false
            } effect OutlineEffect(Color.BLACK, 2f) childOf window
            val scroller = ScrollComponent().constrain {
                y = 3.pixels()
                height = FillConstraint(false) - 3.pixels()
                width = 100.percent()
            } childOf block

            fun getColorString(color: Color): String =
                "#%06x".format(color.rgb and 0xffffff) + "%02x".format(color.alpha)

            listOf(
                VigilancePalette.brightDividerState to "Bright Divider",
                VigilancePalette.dividerState to "Divider",
                VigilancePalette.darkDividerState to "Dark Divider",
                VigilancePalette.outlineState to "Outline",
                VigilancePalette.scrollBarState to "Scroll Bar",
                VigilancePalette.brightHighlightState to "Bright Highlight",
                VigilancePalette.highlightState to "Highlight",
                VigilancePalette.darkHighlightState to "Dark Highlight",
                VigilancePalette.lightBackgroundState to "Light Background",
                VigilancePalette.backgroundState to "Background",
                VigilancePalette.darkBackgroundState to "Dark Background",
                VigilancePalette.searchBarBackgroundState to "Search Bar Background",
                VigilancePalette.brightTextState to "Bright Text",
                VigilancePalette.midTextState to "Mid Text",
                VigilancePalette.darkTextState to "Dark Text",
                VigilancePalette.modalBackgroundState to "Modal Background",
                VigilancePalette.warningState to "Warning",
                VigilancePalette.accentState to "Accent",
                VigilancePalette.successState to "Success",
                VigilancePalette.transparentState to "Transparent",
                VigilancePalette.disabledState to "Disabled"
            ).forEach {
                val c = UIContainer().constrain {
                    x = 5.percent()
                    y = SiblingConstraint(5f)
                    width = 80.percent()
                    height = 20.pixels()
                } effect OutlineEffect(Color.GRAY, 1f) childOf scroller
                UIText(it.second).constrain {
                    y = CenterConstraint()
                    x = 1.pixels()
                    textScale = .8f.pixels()
                } childOf c
                val preview = UIBlock(VigilancePalette.lightBackgroundState.toConstraint()).constrain {
                    x = 90.percent()
                    y = CenterConstraint()
                    width = ('F'.width() * 10).pixels()
                    height = 10.pixels()
                } childOf c
                val t = UIText(getColorString(it.first.get()), shadow = false).constrain {
                    textScale = .8f.pixels()
                    y = CenterConstraint()
                    x = CenterConstraint()
                    color = VigilancePalette.brightTextState.toConstraint()
                } childOf preview
                preview.hide(true)
                UIBlock(it.first.toConstraint()).constrain {
                    height = 16.pixels()
                    width = 16.pixels()
                    x = 10.pixels(alignOpposite = true)
                    y = CenterConstraint()
                }.onMouseEnter {
                    preview.unhide()
                    t.setText(getColorString(it.first.get()))
                }.onMouseLeave {
                    preview.hide(true)
                }.onMouseClick { e ->
                    // uncertain if this method exists in later versions so im just being safe
                    //#if MC<=11202
                    if (e.mouseButton == 0) setClipboardString(getColorString(it.first.get()))
                    //#endif
                } effect OutlineEffect(Color.LIGHT_GRAY, 1f) childOf c
            }
        }
        super.onTick()
    }
}
