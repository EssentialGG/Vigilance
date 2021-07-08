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
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.GuiScale
import gg.essential.universal.UKeyboard
import gg.essential.universal.UMinecraft
import gg.essential.universal.utils.MCScreen
import gg.essential.vigilance.VigilanceConfig
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.gui.settings.*
import gg.essential.vigilance.utils.onLeftClick
import net.minecraft.client.Minecraft
import java.awt.Color

class SettingsGui(private val config: Vigilant, parent: MCScreen?) : WindowScreen(newGuiScale = GuiScale.scaleForScreenSize().ordinal) {
    init {
        UIBlock(VigilancePalette.backgroundState).constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf window
    }

    private val outerContainer by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 85.percent()
        height = 75.percent()
    } childOf window

    private val backContainer by UIContainer().constrain {
        x = (SiblingConstraint(13.5f, alignOpposite = true) boundTo outerContainer)
        y = .5f.pixels() boundTo outerContainer
        height = ChildBasedSizeConstraint() + 5.pixels()
        width = ChildBasedSizeConstraint() + 5.pixels()
    } childOf window

    private val backIcon by UIText("<", false).constrain {
        textScale = 2f.pixels()
        color = VigilancePalette.brightTextState.toConstraint()
    }.onMouseClick {
        UMinecraft.getMinecraft().displayGuiScreen(parent)
    } childOf backContainer

    init {
        backContainer.onMouseEnter {
            backIcon.animate {
                setColorAnimation(Animations.OUT_EXP, .3f, VigilancePalette.accentState.toConstraint())
            }
        }.onMouseLeave {
            backIcon.animate {
                setColorAnimation(Animations.OUT_EXP, .3f, VigilancePalette.brightTextState.toConstraint())
            }
        }

        UIBlock(VigilancePalette.darkDividerState).constrain {
            width = 1.pixel()
            height = 100.percent()
        } childOf outerContainer
    }

    private val sidebar by UIContainer().constrain {
        width = 25.percent()
        height = 100.percent()
    } effect ScissorEffect() childOf outerContainer

    private val theGuy by UIContainer().constrain {
        x = 7.5f.pixels()
        width = 100.percent()
        height = 100.percent()
    } childOf sidebar

    private val titleHolder by UIContainer().constrain {
        y = 1.pixel()
        height = ChildBasedSizeConstraint()
        width = 100.percent() - 18.pixels()
    } childOf theGuy

    private var searchExpanded = false
    private var cs = false
        set(value) {
            field = value
            println("cs $value")
        }

    private val searchContainerNew by UIBlock(VigilancePalette.backgroundState).constrain {
        x = (-20).pixels(alignOutside = true, alignOpposite = true)
        y = 1.pixel()
        height = 20.pixels()
        width = 100.percent()
    }.onMouseEnter {
        animate {
            setColorAnimation(Animations.OUT_EXP, .3f, VigilancePalette.searchBarBackgroundState.toConstraint())
        }
    }.onMouseLeave {
        if (!searchExpanded) {
            animate {
                setColorAnimation(Animations.OUT_EXP, .3f, VigilancePalette.backgroundState.toConstraint())
            }
        }
    }.onMouseClick {
        if (!cs) {
            cs = true
            if (searchExpanded) {
                searchExpanded = false
                titleHolder.animate {
                    setXAnimation(Animations.OUT_EXP, .75f, 0.pixels()).onComplete {
                        cs = false
                    }
                }
                animate {
                    setXAnimation(Animations.OUT_EXP, .75f, (-20).pixels(alignOutside = true, alignOpposite = true)).onComplete {
                        cs = false
                    }
                }
            } else {
                searchExpanded = true
                titleHolder.animate {
                    setXAnimation(Animations.OUT_EXP, .75f, 1.pixel(alignOutside = true) boundTo sidebar).onComplete {
                        cs = false
                    }
                }
                animate {
                    setXAnimation(Animations.OUT_EXP, .75f, 1.pixel()).onComplete {
                        cs = false
                    }
                }
            }
        }
    } childOf sidebar

    private val searchTextContainerNew by UIContainer().constrain {
        x = 22.pixels()
        y = 2.pixels()
        height = 16.pixels()
        width = FillConstraint() - 20.pixels()
    } childOf searchContainerNew

    private val searchBarNew by UIBlock(VigilancePalette.dividerState).constrain {
        y = 0.pixels(alignOpposite = true)
        height = 1.5f.pixels()
        width = 100.percent()
    } childOf searchTextContainerNew

    private val searchBarAccent by UIBlock(VigilancePalette.accentState).constrain {
        x = CenterConstraint()
        width = 0.pixels()
        height = 100.percent()
    } childOf searchBarNew

    private val searchTextField by UITextInput("Search...", shadow = false).constrain {
        x = 1.pixel()
        y = 2.pixels()
        height = 14.pixels()
        width = 100.percent()
    } childOf searchTextContainerNew

    private var searching = false

    init {
        searchTextContainerNew.onMouseClick { event ->
            event.stopPropagation()
            searchTextField.grabWindowFocus()
        }

        searchTextField.onUpdate {
            selectCategory(config.getCategoryFromSearch(it))
        }.onFocus {
            if (!searching) {
                searching = true
                searchBarAccent.animate {
                    setWidthAnimation(Animations.OUT_EXP, .5f, 100.percent())
                }
            }
        }.onFocusLost {
            searchBarAccent.setWidth(0.pixels())
            searchTextField.setActive(false)
            searching = false
        }

        UIImage.ofResourceCached("/vigilance/search.png").constrain {
            x = 2.pixels()
            y = 2.pixels()
            width = 16.pixels()
            height = 16.pixels()
        } childOf searchContainerNew

        UIWrappedText(config.guiTitle, shadow = false).constrain {
            textScale = 2f.pixels()
            width = 100.percent()
            color = VigilancePalette.brightTextState.toConstraint()
            // issues with height/scaling when msdf. not a priority atm but will fix eventually
            //fontProvider = DefaultFonts.VANILLA_FONT_RENDERER
        } childOf titleHolder
    }

    private val scrollContainer by UIContainer().constrain {
        y = SiblingConstraint() + 40.pixels()
        width = RelativeConstraint(1f) - 10.pixels()
        height = FillConstraint()
    } childOf theGuy

    private val categoryScroller by ScrollComponent(pixelsPerScroll = 25f).constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf scrollContainer

    private val categoryScrollBar by UIBlock().constrain {
        x = 7.5f.pixels(true)
        width = 3.pixels()
        color = VigilancePalette.scrollBarState.toConstraint()
    } childOf scrollContainer

    init {
        window.onLeftClick {
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
        UIBlock(VigilancePalette.darkDividerState).constrain {
            x = SiblingConstraint()
            width = 1.pixels()
            height = RelativeConstraint(1f)
        } childOf outerContainer
    }

    private val categoryHolder by UIContainer().constrain {
        x = SiblingConstraint() + 5.pixels()
        width = FillConstraint()
        height = RelativeConstraint(1f)
    } childOf outerContainer

    init {
        UIBlock(VigilancePalette.darkDividerState).constrain {
            x = 1.pixel(alignOpposite = true, alignOutside = true)
            width = 1.pixel()
            height = 100.percent()
        } childOf outerContainer
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
            if (!searchTextField.isActive() && typedChar.isLetterOrDigit() && searchExpanded) {
                searchTextContainerNew.click()
                searchTextField.setText("${searchTextField.getText()}$typedChar")
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
        newGuiScale = GuiScale.scaleForScreenSize().ordinal
        super.setWorldAndResolution(mc, width, height)
    }
    //#endif
}
