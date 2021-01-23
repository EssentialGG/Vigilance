package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIWrappedText
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.settings.SettingComponent

class DataBackedSetting(data: PropertyData, private val component: SettingComponent) : Setting() {
    protected val boundingBox = UIBlock(VigilancePalette.DARK_HIGHLIGHT).constrain {
        x = 1.pixels()
        y = 1.pixels()
        width = RelativeConstraint(1f) - 10.pixels()
        height = ChildBasedMaxSizeConstraint() + INNER_PADDING.pixels()
    } childOf this effect OutlineEffect(VigilancePalette.DIVIDER, 1f)

    private val textBoundingBox = UIContainer().constrain {
        x = INNER_PADDING.pixels()
        y = INNER_PADDING.pixels()
        width = basicWidthConstraint { component ->
            val endPos = ((boundingBox.children - component).map { it.getLeft() }.min() ?: boundingBox.getRight())
            endPos - component.getLeft() - 10f
        }
        height = ChildBasedSizeConstraint(3f) + INNER_PADDING.pixels()
    } childOf boundingBox

    private val settingName = UIWrappedText(data.attributes.name).constrain {
        width = RelativeConstraint(1f)
        textScale = 1.5f.pixels()
        color = VigilancePalette.BRIGHT_TEXT.toConstraint()
    } childOf textBoundingBox

    private val settingDescription = UIWrappedText(data.attributes.description).constrain {
        y = SiblingConstraint() + 3.pixels()
        width = RelativeConstraint(1f)
        color = VigilancePalette.MID_TEXT.toConstraint()
    } childOf textBoundingBox

    init {
        onMouseEnter {
            settingName.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.ACCENT.toConstraint())
            }
        }

        onMouseLeave {
            settingName.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.BRIGHT_TEXT.toConstraint())
            }
        }

        component.onValueChange {
            data.setValue(it)
        }
        component childOf boundingBox
    }

    override fun closePopups() {
        component.closePopups()
    }

    companion object {
        const val INNER_PADDING = 15f
    }
}
