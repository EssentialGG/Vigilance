package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.impl.I18n

class SettingsTitleBar(private val gui: SettingsGui, private val config: Vigilant, window: Window) :
    UIContainer() {

    // Notches in titlebar
    private val leftDivider by UIBlock(VigilancePalette.componentHighlight).constrain {
        width = SettingsGui.dividerWidth.pixels
        height = 100.percent
    } childOf this

    private val contentContainer by UIBlock(VigilancePalette.getComponentBackground()).constrain {
        x = SiblingConstraint()
        width = 100.percent - (SettingsGui.dividerWidth * 2f).pixels
        height = 100.percent
    } childOf this

    private val rightDivider by UIBlock(VigilancePalette.componentHighlight).constrain {
        x = 0.pixels(alignOpposite = true)
        width = SettingsGui.dividerWidth.pixels
        height = 100.percent
    } childOf this

    private val titleText by UIText(I18n.format(config.guiTitle)).constrain {
        x = 10.pixels
        y = CenterConstraint()
    } childOf contentContainer

    private val middleDivider by UIBlock(VigilancePalette.componentHighlight).constrain {
        x = 25.percent + SettingsGui.dividerWidth.pixels
        width = SettingsGui.dividerWidth.pixels
        height = 100.percent
    } childOf this

    private val searchBar by Searchbar().constrain {
        x = 10.pixels(alignOpposite = true)
        y = CenterConstraint()
        height = 17.pixels
    } childOf contentContainer

    init {
        constrain {
            width = 100.percent
            height = 30.pixels
        }

        searchBar.textContent.onSetValue {
            gui.selectCategory(config.getCategoryFromSearch(it))
        }
    }
}
