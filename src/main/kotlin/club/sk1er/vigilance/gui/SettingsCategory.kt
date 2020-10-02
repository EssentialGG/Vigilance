package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.ScrollComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.data.Category

class SettingsCategory(private val category: Category) : UIContainer() {
    private val scroller = ScrollComponent(innerPadding = 4f, emptyString = "No matching settings found :(").constrain {
        width = RelativeConstraint(1f) - 5.pixels()
        height = RelativeConstraint(1f)
    } childOf this

    private val scrollBar = UIBlock(VigilancePalette.SCROLL_BAR).constrain {
        x = SiblingConstraint() + 2.pixels()
        width = 3.pixels()
    } childOf this

    init {
        constrain {
            width = RelativeConstraint(1f)
            height = RelativeConstraint(1f)
        }

        category.items.forEach {
            it.toSettingsObject()?.childOf(scroller)
        }

        scroller.setVerticalScrollBarComponent(scrollBar, true)
    }

    fun closePopups() {
        scroller.allChildren.filterIsInstance<Setting>().forEach {
            it.closePopups()
        }
    }
}
