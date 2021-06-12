package gg.essential.vigilance.gui

import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.GuiScale
import gg.essential.universal.UKeyboard
import gg.essential.universal.UResolution
import gg.essential.vigilance.VigilanceConfig
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.gui.settings.*
import net.minecraft.client.Minecraft
import java.awt.Color

class SettingsGui(private val config: Vigilant) : WindowScreen(newGuiScale = GuiScale.scaleForScreenSize().ordinal) {
    init {
        DefaultFonts.ELEMENTA_MINECRAFT_FONT_RENDERER.getStringWidth("Hello World", 10f)
    }

    init {
        UIBlock().constrain {
            width = RelativeConstraint(1f)
            height = RelativeConstraint(1f)
            color = VigilancePalette.backgroundState.toConstraint()
        } childOf window
    }

    private val outerContainer by UIContainer().constrain {
        x = RelativeConstraint(0.12f)
        y = RelativeConstraint(0.1f)
        width = RelativeConstraint(0.78f)
        height = RelativeConstraint(0.8f)
    } childOf window

    private val sidebar by UIContainer().constrain {
        width = RelativeConstraint(0.25f)
        height = RelativeConstraint(1f)
    } childOf outerContainer

    init {
        UIWrappedText(config.guiTitle, shadow = false).constrain {
            textScale = 2f.pixels()
            width = 90.percent()
            color = VigilancePalette.brightTextState.toConstraint()
            // issues with height/scaling when msdf. not a priority atm but will fix eventually
            //fontProvider = DefaultFonts.ELEMENTA_MINECRAFT_FONT_RENDERER
        } childOf sidebar
    }

    private val scrollContainer by UIContainer().constrain {
        y = SiblingConstraint() + 40.pixels()
        width = RelativeConstraint(1f) - 10.pixels()
        height = FillConstraint()
    } childOf sidebar

    private val categoryScroller by ScrollComponent(pixelsPerScroll = 25f).constrain {
        width = RelativeConstraint(1f)
        height = RelativeConstraint(1f)
    } childOf scrollContainer

    private val categoryScrollBar by UIBlock().constrain {
        x = 7.5f.pixels(true)
        width = 3.pixels()
        color = VigilancePalette.scrollBarState.toConstraint()
    } childOf scrollContainer

    init {
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

    init {
        UIBlock().constrain {
            x = SiblingConstraint()
            width = 1.pixels()
            height = RelativeConstraint(1f)
            color = VigilancePalette.darkDividerState.toConstraint()
        } childOf outerContainer
    }

    private val categoryHolder by UIContainer().constrain {
        x = SiblingConstraint() + 5.pixels()
        width = FillConstraint()
        height = RelativeConstraint(1f)
    } childOf outerContainer

    private val searchContainer by UIContainer().constrain {
        // this is so scuffed lol
        x = basicXConstraint { categoryHolder.getLeft() + 4f }
        y = basicYConstraint { categoryHolder.getTop() - 22 }
        width = basicWidthConstraint { categoryHolder.getWidth() - 11f }
        height = 20.pixels()
    } childOf window

    init {
        UIImage.ofResourceCached("/vigilance/search.png").constrain {
            x = 2.pixels()
            y = CenterConstraint() - 2.pixels()
            width = 16.pixels()
            height = 16.pixels()
        } childOf searchContainer
    }

    private val searchInputContainer by UIContainer().constrain {
        x = SiblingConstraint(6f)
        y = 3.5f.pixels()
        height = 12.pixels()
        width = 100.percent()
    } childOf searchContainer

    private val searchInput by UITextInput("Search...", shadow = false).constrain {
        width = 90.percent()
        height = 100.percent()
        fontProvider = DefaultFonts.ELEMENTA_MINECRAFT_FONT_RENDERER
    } childOf searchInputContainer

    private val searchIndicator by UIBlock(VigilancePalette.darkDividerState).constrain {
        y = 1.pixel(alignOpposite = true)
        height = 1.pixels()
        width = 100.percent()
    } childOf searchContainer

    private val searchIndicatorAccent by UIBlock(VigilancePalette.accentState).constrain {
        height = 100.percent()
        width = 0.pixels()
        x = CenterConstraint()
    } childOf searchIndicator

    init {
        searchContainer.onMouseClick { event ->
            searchInput.grabWindowFocus()
            searchIndicatorAccent.animate {
                setWidthAnimation(Animations.OUT_EXP, .5f, 100.percent())
            }
            event.stopPropagation()
        }.onMouseEnter {
            searchIndicator.animate {
                setColorAnimation(Animations.OUT_EXP, .25f, VigilancePalette.dividerState.toConstraint())
            }
        }.onMouseLeave {
            searchIndicator.animate {
                setColorAnimation(Animations.OUT_EXP, .25f, VigilancePalette.darkDividerState.toConstraint())
            }
        }

        searchInput.onUpdate {
            selectCategory(config.getCategoryFromSearch(it))
        }.onFocusLost {
            searchIndicatorAccent.setWidth(0.pixels())
            searchInput.setActive(false)
        }
    }

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
                searchContainer.click()
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

    // this is temporary and will *probably* not be in a proper essential release
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

    // TODO: 5/30/2021 Port for 1.15+
    //#if MC<11500
    override fun setWorldAndResolution(mc: Minecraft, width: Int, height: Int) {
        window.onWindowResize()
        newGuiScale = scaleForScreenSize().ordinal
        super.setWorldAndResolution(mc, width, height)
    }
    //#endif
}
