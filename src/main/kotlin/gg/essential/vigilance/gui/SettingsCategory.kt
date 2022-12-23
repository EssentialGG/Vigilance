package gg.essential.vigilance.gui

import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.DividerItem
import gg.essential.vigilance.data.PropertyItem
import gg.essential.vigilance.utils.scrollGradient

class SettingsCategory(category: Category) : UIContainer() {

    internal val scroller by ScrollComponent(
        "No matching settings found :(",
        innerPadding = 10f,
        pixelsPerScroll = 25f,
    ).constrain {
        width = 100.percent - (10 + SettingsGui.dividerWidth).pixels
        height = 100.percent
    } childOf this scrollGradient 20.pixels

    private val scrollBar by UIBlock(VigilancePalette.scrollbar).constrain {
        x = 0.pixels(alignOpposite = true)
        width = SettingsGui.dividerWidth.pixels
    } childOf this

    init {
        constrain {
            width = 100.percent
            height = 100.percent
        }

        if (category.description != null) {
            UIWrappedText(category.description, shadowColor = VigilancePalette.getTextShadowLight(), centered = true).constrain {
                x = CenterConstraint()
                y = SiblingConstraint(DataBackedSetting.INNER_PADDING)
                width = 100.percent - (DataBackedSetting.INNER_PADDING * 2f).pixels
                color = VigilancePalette.text.toConstraint()
            } childOf scroller
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
                            scroller.sortChildren { child ->
                                category.items.indexOfFirst { categoryItem ->
                                    (categoryItem is DividerItem && child is Divider && categoryItem.name == child.name)
                                            || (categoryItem is PropertyItem && child is DataBackedSetting && categoryItem.data == child.data)
                                }
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

        scroller.onMouseScroll {
            closePopups()
        }
    }

    @JvmOverloads
    fun closePopups(instantly: Boolean = false) {
        scroller.childrenOfType<Setting>().forEach {
            it.closePopups(instantly)
        }
    }

    fun scrollToTop() {
        scroller.scrollToTop(smoothScroll = false)
    }
}
