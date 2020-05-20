package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.components.UIWrappedText
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.constraints.WidthConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.settings.SettingComponent
import java.awt.Color

class DataBackedSetting(data: PropertyData, private val component: SettingComponent) : Setting() {
    protected val boundingBox = UIBlock(Color(33, 34, 38)).constrain {
        x = 1.pixels()
        y = 1.pixels()
        width = RelativeConstraint(1f) - 4.pixels()
    } childOf this effect OutlineEffect(Color(80, 80, 80), 1f)

    private val textBoundingBox = UIContainer().constrain {
        x = INNER_PADDING.pixels()
        y = INNER_PADDING.pixels()
        width = basicWidthConstraint { component ->
            val endPos = ((boundingBox.children - component).map { it.getLeft() }.min() ?: boundingBox.getRight())
            endPos - component.getLeft() - 10f
        }
        height = ChildBasedSizeConstraint(3f) + INNER_PADDING.pixels()
    } childOf boundingBox

    private val settingName = UIWrappedText(data.property.name).constrain {
        width = RelativeConstraint(1f)
        textScale = 1.5f.pixels()
        color = Color.WHITE.asConstraint()
    } childOf textBoundingBox

    private val settingDescription = UIWrappedText(data.property.description).constrain {
        y = SiblingConstraint() + 3.pixels()
        width = RelativeConstraint(1f)
        color = Color(127, 127, 127).asConstraint()
    } childOf textBoundingBox

    init {
        boundingBox.constrain {
            height = (RelativeConstraint(1f).to(textBoundingBox) min RelativeConstraint(1f).to(component)) + 10.pixels()
        }

        onMouseEnter {
            settingName.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, SettingsGui.ACCENT_COLOR.asConstraint())
            }
        }

        onMouseLeave {
            settingName.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.asConstraint())
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
        const val INNER_PADDING = 10f
    }
}