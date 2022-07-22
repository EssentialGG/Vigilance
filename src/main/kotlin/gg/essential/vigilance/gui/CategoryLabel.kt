package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.USound
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.gui.elementa.GuiScaleOffsetConstraint
import gg.essential.vigilance.utils.onLeftClick

class CategoryLabel(private val gui: SettingsGui, private val category: Category) : UIContainer() {

    private val text by UIText(category.name).constrain {
        y = CenterConstraint()
        textScale = GuiScaleOffsetConstraint(1f)
        color = VigilancePalette.text.toConstraint()
    } childOf this

    var isSelected = false

    init {
        constrain {
            y = SiblingConstraint()
            width = ChildBasedMaxSizeConstraint()
            height = ChildBasedSizeConstraint() + 4.pixels
        }

        onLeftClick {
            if (!isSelected) {
                USound.playButtonPress()
                select()
            }
        }

        onMouseEnter {
            if (!isSelected) {
                text.animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.primary.toConstraint())
                }
            }
        }

        onMouseLeave {
            if (!isSelected) {
                text.animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.text.toConstraint())
                }
            }
        }
    }

    fun select() {
        gui.selectCategory(category)

        isSelected = true
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.primary.toConstraint())
        }
    }

    fun deselect() {
        isSelected = false
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.text.toConstraint())
        }
    }
}
