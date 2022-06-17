package gg.essential.vigilance.gui

import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.PropertyItem

class SettingsCategory(category: Category) : UIContainer() {
    init {
        constrain {
            width = 100.percent
            height = ChildBasedSizeConstraint()
        }

        if (category.description != null) {
            UIWrappedText(category.description, centered = true).constrain {
                x = CenterConstraint()
                y = SiblingConstraint(DataBackedSetting.INNER_PADDING)
                width = 100.percent - (DataBackedSetting.INNER_PADDING * 2f).pixels
                color = VigilancePalette.text.toConstraint()
            } childOf this
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
                settingsObject childOf this
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
    }
}
