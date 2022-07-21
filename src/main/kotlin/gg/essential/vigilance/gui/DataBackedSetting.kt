package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.data.PropertyData
import gg.essential.vigilance.gui.elementa.GuiScaleOffsetConstraint
import gg.essential.vigilance.gui.settings.SettingComponent

class DataBackedSetting(internal val data: PropertyData, internal val component: SettingComponent) : Setting() {

    private val boundingBox: UIBlock by UIBlock(VigilancePalette.componentBackground.toConstraint()).constrain {
        x = 1.pixels
        y = 1.pixels
        width = 100.percent - 2.pixels
        height = ChildBasedMaxSizeConstraint() + INNER_PADDING.pixels
    } childOf this effect OutlineEffect(VigilancePalette.getComponentBorder(), 1f).bindColor(VigilancePalette.componentBorder)

    private val textBoundingBox by UIContainer().constrain {
        x = INNER_PADDING.pixels
        y = INNER_PADDING.pixels
        width = basicWidthConstraint { component ->
            val endPos = ((boundingBox.children - component).minOfOrNull { it.getLeft() } ?: boundingBox.getRight())
            (endPos - component.getLeft() - 10f).coerceAtMost(364f)
        }
        height = ChildBasedSizeConstraint(3f) + INNER_PADDING.pixels
    } childOf boundingBox

    private val settingName by UIWrappedText(data.attributesExt.localizedName).constrain {
        width = 100.percent
        textScale = GuiScaleOffsetConstraint(1f)
        color = VigilancePalette.textHighlight.toConstraint()
    } childOf textBoundingBox

    init {
        UIWrappedText(data.attributesExt.localizedDescription).constrain {
            y = SiblingConstraint() + 3.pixels
            width = 100.percent
            color = VigilancePalette.text.toConstraint()
        } childOf textBoundingBox
    }

    private var hidden = data.isHidden()

    init {

        constrain {
            y = SiblingConstraint(8f)
            height = ChildBasedMaxSizeConstraint() + 2.pixels
        }

        component.onValueChange {
            data.setValue(it)
        }
        component childOf boundingBox
        component.setupParentListeners(this)
    }

    fun hideMaybe() {
        if (hidden) {
            if (!data.isHidden()) {
                hidden = false
                unhide()
            }
        } else if (data.isHidden()) {
            hidden = true
            hide(true)
        }
    }

    override fun closePopups(instantly: Boolean) {
        component.closePopups(instantly)
    }

    companion object {
        const val INNER_PADDING = 13f
    }
}
