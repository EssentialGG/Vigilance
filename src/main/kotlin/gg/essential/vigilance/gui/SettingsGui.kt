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
        color = VigilancePalette.getDivider().toConstraint()
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
                setColorAnimation(Animations.OUT_EXP, animTime, VigilancePalette.getDivider().toConstraint())
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

    private val categories = config.getCategories()

    init {
        categories.forEach { cat ->
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

    private var currentCategory = SettingsCategory(categories.first()) childOf categoryHolder

    init {
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
        val newCategory = SettingsCategory(category) childOf categoryHolder

        currentCategory.scroller.childrenOfType<Setting>().forEach { it.closePopups(instantly = true) }
        currentCategory.hide()
        newCategory.unhide()
        newCategory.scrollToTop()
        currentCategory = newCategory

        categoryScroller.allChildren.filterIsInstance<CategoryLabel>().firstOrNull { it.isSelected }?.deselect()
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
