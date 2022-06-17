package gg.essential.vigilance.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.FillConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.GuiScale
import gg.essential.universal.UKeyboard
import gg.essential.universal.USound
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.gui.common.IconButton
import gg.essential.vigilance.gui.settings.*
import gg.essential.vigilance.utils.onLeftClick
import gg.essential.vigilance.utils.scrollGradient

class SettingsGui(
    config: Vigilant
) : WindowScreen(
    version = ElementaVersion.V2,
    newGuiScale = GuiScale.scaleForScreenSize().ordinal,
    restoreCurrentGuiOnClose = true,
) {
    val dividerWidth = 3f

    private val background by UIBlock(VigilancePalette.mainBackground).constrain {
        width = 100.percent
        height = 100.percent
    } childOf window

    private val container by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 85.percent
        height = 75.percent
    } childOf window

    private val titleBar by SettingsTitleBar(this, config) childOf container

    private val bottomContainer by UIContainer().constrain {
        y = SiblingConstraint()
        width = 100.percent
        height = FillConstraint()
    } childOf container

    private val leftDivider by UIBlock(VigilancePalette.darkGray).constrain {
        width = dividerWidth.pixels
        height = 100.percent
    } childOf bottomContainer

    private val sidebar by UIContainer().constrain {
        x = SiblingConstraint()
        width = 25.percent - (dividerWidth * 2f).pixels
        height = 100.percent
    } effect ScissorEffect() childOf bottomContainer

    private val middleDivider by UIBlock(VigilancePalette.darkGray).constrain {
        x = 0.pixels(alignOpposite = true)
        width = dividerWidth.pixels
        height = 100.percent
    } childOf sidebar

    private val sidebarScroller by ScrollComponent(pixelsPerScroll = 25f).constrain {
        width = 100.percent - dividerWidth.pixels
        height = 100.percent
    } childOf sidebar scrollGradient 20.pixels

    private val categoryScrollBar by UIBlock().constrain {
        x = 0.pixels(true)
        width = dividerWidth.pixels
        color = VigilancePalette.scrollbar.toConstraint()
    } childOf sidebar

    private val content by UIContainer().constrain {
        x = SiblingConstraint()
        width = FillConstraint()
        height = 100.percent
    } effect(ScissorEffect()) childOf bottomContainer

    private val rightDivider by UIBlock(VigilancePalette.darkGray).constrain {
        x = 0.pixels(alignOpposite = true)
        width = dividerWidth.pixels
        height = 100.percent
    } childOf content

    private val contentScroller by ScrollComponent(
        "No matching settings found :(",
        innerPadding = 10f,
        pixelsPerScroll = 25f,
    ).constrain {
        width = 100.percent - 10.pixels - dividerWidth.pixels
        height = 100.percent
    } childOf content scrollGradient 20.pixels

    private val contentScrollbar by UIBlock().constrain {
        x = 0.pixels(alignOpposite = true)
        width = dividerWidth.pixels()
        color = VigilancePalette.scrollbar.toConstraint()
    } childOf content

    private val bottomDivider by UIBlock(VigilancePalette.darkGray).constrain {
        y = 0.pixels(alignOpposite = true)
        width = 100.percent
        height = dividerWidth.pixels
    } childOf container

    private val backButton by IconButton(VigilancePalette.ARROW_LEFT_4X7).constrain {
        x = SiblingConstraint(18f, alignOpposite = true) boundTo titleBar
        y = CenterConstraint() boundTo titleBar
        width = 17.pixels
        height = 17.pixels
    } childOf window

    private val categories = config.getCategories()

    init {

//        backContainer.onMouseEnter {
//            animate {
//                setColorAnimation(Animations.OUT_EXP, animTime, VigilancePalette.brightDividerState.toConstraint())
//            }
//            backIcon.animate {
//                setColorAnimation(Animations.OUT_EXP, animTime, VigilancePalette.brightTextState.toConstraint())
//            }
//        }.onMouseLeave {
//            backIcon.animate {
//                setColorAnimation(Animations.OUT_EXP, animTime, VigilancePalette.text.toConstraint())
//            }
//            animate {
//                setColorAnimation(Animations.OUT_EXP, animTime, VigilancePalette.brightDividerState.toConstraint())
//            }
//        }.onLeftClick {
//            USound.playButtonPress()
//            restorePreviousScreen()
//        }

        backButton.onActiveClick { restorePreviousScreen() }

        window.onLeftClick {
            contentScroller.allChildren.filterIsInstance<Setting>().forEach { it.closePopups() }
        }

        sidebarScroller.setVerticalScrollBarComponent(categoryScrollBar, true)
        contentScroller.setVerticalScrollBarComponent(contentScrollbar, true)

        repeat(8) {
            categories.forEach { category ->
                val label = CategoryLabel(this, category)
                label childOf sidebarScroller
            }
        }
    }

    private var currentCategory = SettingsCategory(categories.first()) childOf contentScroller

    init {
        sidebarScroller.childrenOfType<CategoryLabel>().firstOrNull()?.select()

        fun UIComponent.click(): Unit =
            mouseClick(getLeft() + (getRight() - getLeft()) / 2.0, getTop() + (getBottom() - getTop()) / 2.0, 0)

        window.onKeyType { _, keyCode ->
            if (UKeyboard.isKeyDown(UKeyboard.KEY_MINUS)) {
                Inspector(window) childOf window
                return@onKeyType
            }

            currentCategory.childrenOfType<DataBackedSetting>().filter { it.isHovered() }.forEach { child ->
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
                        child.component.dropDown.select(child.component.dropDown.selectedIndex.get() - 1)
                    } else if (keyCode == UKeyboard.KEY_DOWN) {
                        child.component.dropDown.select(child.component.dropDown.selectedIndex.get() + 1)
                    }
                }
                return@forEach
            }
        }
    }

    fun selectCategory(category: Category) {
        val newCategory = SettingsCategory(category) childOf contentScroller

        currentCategory.childrenOfType<Setting>().forEach { it.closePopups(instantly = true) }
        currentCategory.hide()
        newCategory.unhide()
        contentScroller.scrollToTop()
        currentCategory = newCategory

        sidebarScroller.childrenOfType<CategoryLabel>().firstOrNull { it.isSelected }?.deselect()
    }

    override fun updateGuiScale() {
        newGuiScale = GuiScale.scaleForScreenSize().ordinal
        super.updateGuiScale()
    }

    companion object {
        const val animTime = .5f
    }
}
