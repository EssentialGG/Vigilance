package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.state.toConstraint
import gg.essential.elementa.transitions.RecursiveFadeInTransition
import gg.essential.elementa.transitions.RecursiveFadeOutTransition
import gg.essential.vigilance.data.PropertyData
import gg.essential.vigilance.gui.settings.SettingComponent

class DataBackedSetting(internal val data: PropertyData, internal val component: SettingComponent) : Setting() {
    private val boundingBox by UIBlock(VigilancePalette.darkHighlightState.toConstraint()).constrain {
        x = 1.pixels()
        y = 1.pixels()
        width = RelativeConstraint(1f) - 10.pixels()
        height = ChildBasedMaxSizeConstraint() + INNER_PADDING.pixels()
    } childOf this effect OutlineEffect(VigilancePalette.getDivider(), 1f).bindColor(VigilancePalette.dividerState)

    private val textBoundingBox by UIContainer().constrain {
        x = INNER_PADDING.pixels()
        y = INNER_PADDING.pixels()
        width = basicWidthConstraint { component ->
            val endPos = ((boundingBox.children - component).map { it.getLeft() }.min() ?: boundingBox.getRight())
            endPos - component.getLeft() - 10f
        }
        height = ChildBasedSizeConstraint(3f) + INNER_PADDING.pixels()
    } childOf boundingBox

    private val settingName by UIWrappedText(data.attributes.name).constrain {
        width = RelativeConstraint(1f)
        textScale = 1.5f.pixels()
        color = VigilancePalette.brightTextState.toConstraint()
        fontProvider = DefaultFonts.ELEMENTA_MINECRAFT_FONT_RENDERER
    } childOf textBoundingBox

    init {
        UIWrappedText(data.attributes.description).constrain {
            y = SiblingConstraint() + 3.pixels()
            width = RelativeConstraint(1f)
            color = VigilancePalette.midTextState.toConstraint()
            fontProvider = DefaultFonts.ELEMENTA_MINECRAFT_FONT_RENDERER
        } childOf textBoundingBox
    }

    private var hidden = data.isHidden()

    init {
        onMouseEnter {
            settingName.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.accentState.toConstraint())
            }
        }

        onMouseLeave {
            settingName.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.brightTextState.toConstraint())
            }
        }

        animateAfterUnhide {
            RecursiveFadeInTransition(.5f).transition(this@DataBackedSetting)
        }

        component.onValueChange {
            data.setValue(it)
        }
        component childOf boundingBox
    }

    fun hideMaybe() {
        if (hidden) {
            if (!data.isHidden()) {
                hidden = false
                unhide()
            }
        } else if (data.isHidden()) {
            hidden = true
            RecursiveFadeOutTransition(.5f).transition(this) {
                if (hidden) {
                    hide(true)
                }
            }
        }
    }

    override fun closePopups() {
        component.closePopups()
    }

    companion object {
        const val INNER_PADDING = 15f
    }
}
