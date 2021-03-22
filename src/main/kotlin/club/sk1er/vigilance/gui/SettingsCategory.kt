package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.ScrollComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIWrappedText
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.state.toConstraint
import club.sk1er.elementa.utils.withAlpha
import club.sk1er.vigilance.data.Category

class SettingsCategory(category: Category) : UIContainer() {
    private val scrollerBoundingBox = UIContainer().constrain {
        width = RelativeConstraint(1f) - 5.pixels()
        height = RelativeConstraint(1f)
    } childOf this

    internal val scroller = ScrollComponent(
        "No matching settings found :(",
        innerPadding = 4f,
        pixelsPerScroll = 25f,
        customScissorBoundingBox = scrollerBoundingBox
    ).constrain {
        y = 50.pixels(alignOpposite = true)
        width = RelativeConstraint(1f) - 5.pixels()
        height = RelativeConstraint(1f) - 50.pixels()
    } childOf this

    private val scrollBar = UIBlock().constrain {
        x = SiblingConstraint() + 2.pixels()
        width = 3.pixels()
        color = VigilancePalette.scrollBlockState.toConstraint()
    } childOf this

    init {
        constrain {
            width = RelativeConstraint(1f)
            height = RelativeConstraint(1f)
        }

        if (category.description != null) {
            val textContainer = UIContainer().constrain {
                x = DataBackedSetting.INNER_PADDING.pixels()
                y = SiblingConstraint(DataBackedSetting.INNER_PADDING)
                width = 100.percent() - (DataBackedSetting.INNER_PADDING * 2f).pixels()
                height = ChildBasedMaxSizeConstraint() + DataBackedSetting.INNER_PADDING.pixels()
            } childOf scroller

            UIWrappedText(category.description, centered = true).constrain {
                x = CenterConstraint()
                y = SiblingConstraint() + 3.pixels()
                width = 70.percent()
                color = VigilancePalette.midTextState.toConstraint()
            } childOf textContainer
        }

        val categoryItemsSettingsObjects: ArrayList<DataBackedSetting> = ArrayList()

        category.items.forEach {
            //it.toSettingsObject()?.childOf(scroller)
            val settingsObject = it.toSettingsObject()
            if (settingsObject != null) {
                settingsObject childOf scroller
                if (settingsObject is DataBackedSetting) {
                    categoryItemsSettingsObjects.add(settingsObject)
                    if (settingsObject.data.isHidden()) {
                        settingsObject.hide(true)
                    }

                    if (settingsObject.data.hasDependants) {
                        settingsObject.component.onValueChange { v ->
                            settingsObject.data.setValue(v)
                            categoryItemsSettingsObjects.forEach { setting ->
                                if (setting.data.dependsOn != null) {
                                    setting.hideMaybe()
                                }
                            }
                            //.forEach { setting ->
                            //    if (setting.data.dependsOn != null) {
                            //        setting.hideMaybe()
                            //    }
                            //}
                        }
                    }
                }
            }
        }

        scroller.setVerticalScrollBarComponent(scrollBar, true)

        GradientBlock(VigilancePalette.BACKGROUND.withAlpha(0), VigilancePalette.BACKGROUND).constrain {
            y = 0.pixels(alignOpposite = true)
            width = 100.percent() - 10.pixels()
            height = 50.pixels()
        }.onMouseClick {
            it.stopPropagation()
            scroller.mouseClick(it.absoluteX.toDouble(), it.absoluteY.toDouble(), it.mouseButton)
        }.onMouseScroll {
            it.stopPropagation()
            scroller.mouseScroll(it.delta)
        } childOf this
    }

    fun closePopups() {
        scroller.allChildren.filterIsInstance<Setting>().forEach {
            it.closePopups()
        }
    }

    fun scrollToTop() {
        scroller.scrollToTop(smoothScroll = false)
    }
}
