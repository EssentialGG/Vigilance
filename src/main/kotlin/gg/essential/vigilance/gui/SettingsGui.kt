package gg.essential.vigilance.gui

import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.GuiScale
import gg.essential.universal.UKeyboard
import gg.essential.universal.USound
import gg.essential.vigilance.VigilanceConfig
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.gui.settings.*
import gg.essential.vigilance.utils.onLeftClick
import net.minecraft.client.Minecraft
import java.awt.Color

class SettingsGui(
    private val config: Vigilant
) : WindowScreen(newGuiScale = GuiScale.scaleForScreenSize().ordinal, restoreCurrentGuiOnClose = true) {
    init {
        UIBlock(VigilancePalette.backgroundState).constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf window
    }

    private val content by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 85.percent()
        height = 75.percent()
    } childOf window

    private val backContainer by UIContainer().constrain {
        x = (SiblingConstraint(20f, alignOpposite = true) boundTo content) + 6.5.pixel()
        y = 0.5.pixels() boundTo content
        width = ChildBasedSizeConstraint() + 20.pixels()
        height = ChildBasedSizeConstraint() + 20.pixels()
    } childOf window

    private val backIcon by UIImage.ofResource("/vigilance/arrow-left.png").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 4.pixels()
        height = 7.pixels()
    } childOf backContainer

    init {
        SettingsTitleBar(this, config, window).constrain {
            width = 100.percent()
            height = 30.pixels()
        } childOf content
    }

    private val mainContent by UIContainer().constrain {
        y = SiblingConstraint() //30.px
        width = FillConstraint(false)
        height = FillConstraint(false)
    } childOf content

    init {
        backContainer.onMouseEnter {
            backIcon.animate {
                setColorAnimation(Animations.OUT_EXP, animTime, VigilancePalette.getAccent().toConstraint())
            }
        }.onMouseLeave {
            backIcon.animate {
                setColorAnimation(Animations.OUT_EXP, animTime, VigilancePalette.getBrightText().toConstraint())
            }
        }.onLeftClick {
            USound.playButtonPress()
            restorePreviousScreen()
        }

        UIBlock(VigilancePalette.dividerState).constrain {
            width = 1.pixel()
            height = 100.percent()
        } childOf content
    }

    private val sidebar by UIContainer().constrain {
        width = 25.percent()
        height = 100.percent()
    } effect ScissorEffect() childOf mainContent

    private val theGuy by UIContainer().constrain {
        x = 10.pixels()
        width = 100.percent()
        height = 100.percent()
    } childOf sidebar

    private val scrollContainer by UIContainer().constrain {
        x = 5.pixels()
        y = SiblingConstraint() + 7.pixels()
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
        UIBlock(VigilancePalette.dividerState).constrain {
            y = (-.5f).pixels()
            x = SiblingConstraint()
            width = 1.pixels()
            height = RelativeConstraint(1f) + .5f.pixels()
        } childOf mainContent
    }


    private val categoryHolder by UIContainer().constrain {
        x = SiblingConstraint() + 5.pixels()
        width = FillConstraint(false)
        height = RelativeConstraint(1f)
    } childOf mainContent

    init {
        UIBlock(VigilancePalette.dividerState).constrain {
            x = 0.pixel(alignOpposite = true)
            width = 1.pixel()
            height = 100.percent()
        } childOf content
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

        window.onKeyType { _, keyCode ->
            if (UKeyboard.isKeyDown(UKeyboard.KEY_MINUS)) {
                Inspector(window) childOf window
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

        currentCategory.scroller.childrenOfType<DataBackedSetting>().map { it.component }.filterIsInstance<SelectorComponent>().forEach { it.dropDown.setFloating(false) }
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
    companion object {
        const val animTime = .5f
    }
}
