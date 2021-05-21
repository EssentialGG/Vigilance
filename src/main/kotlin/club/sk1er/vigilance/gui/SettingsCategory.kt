package club.sk1er.vigilance.gui

import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.toConstraint
import gg.essential.elementa.utils.withAlpha
import club.sk1er.vigilance.data.Category
import club.sk1er.vigilance.data.DividerItem
import club.sk1er.vigilance.data.PropertyItem

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
        color = VigilancePalette.scrollBarState.toConstraint()
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
        val dividerItemsSettingsObjects: ArrayList<Divider> = ArrayList()

        fun hideDividerMaybe(divider: Divider): Boolean {
            var f = false
            category.items.forEach {
                if (it is PropertyItem && it.subcategory == divider.name) {
                    if (it.data.dependsOn != null) {
                        f = true
                    }
                    if (!it.data.isHidden()) {
                        return false
                    }
                }
            }
            return f
        }

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
                            dividerItemsSettingsObjects.forEach { divider ->
                                divider.hideMaybe(hideDividerMaybe(divider))
                            }
                        }
                    }
                } else if (settingsObject is Divider) {
                    var flag = false
                    var flag2 = false
                    for (item in category.items) {
                        if (item is PropertyItem && item.subcategory == settingsObject.name) {
                            if (item.data.dependsOn != null) {
                                flag = true
                            }
                            if (!item.data.isHidden()) {
                                flag2 = true
                                break
                            }
                        }
                    }
                    if (flag) {
                        dividerItemsSettingsObjects.add(settingsObject)
                        if (!flag2) {
                            settingsObject.hide(true)
                            settingsObject.hidden = true
                        }
                    }
                }
            }
        }

        scroller.setVerticalScrollBarComponent(scrollBar, true)

        GradientComponent().bindStartColor(VigilancePalette.bgNoAlpha).bindEndColor(VigilancePalette.backgroundState).constrain {
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
