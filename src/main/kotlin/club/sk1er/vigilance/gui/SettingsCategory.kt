package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.ScrollComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.data.Category

class SettingsCategory(category: Category) : UIContainer() {
    private val scrollerBoundingBox = UIContainer().constrain {
        width = RelativeConstraint(1f) - 5.pixels()
        height = RelativeConstraint(1f)
    } childOf this

    private val scroller = ScrollComponent(
        "No matching settings found :(",
        innerPadding = 4f,
        pixelsPerScroll = 25f,
        customScissorBoundingBox = scrollerBoundingBox
    ).constrain {
        y = 50.pixels(alignOpposite = true)
        width = RelativeConstraint(1f) - 5.pixels()
        height = RelativeConstraint(1f) - 50.pixels()
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
