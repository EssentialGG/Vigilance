package gg.essential.vigilance.gui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.data.Category
import java.awt.Color

class CategoryLabel(private val gui: SettingsGui, private val category: Category) : UIContainer() {
    private val text = UIText(category.name).constrain {
        color = VigilancePalette.midTextState.toConstraint()
        textScale = 1.pixels()
    } childOf this

    var isSelected = false

    init {
        constrain {
            y = SiblingConstraint(15f)
            width = ChildBasedMaxSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        onMouseClick {
            if (!isSelected) {
                select()
            }
        }

        onMouseEnter {
            if (!isSelected) {
                text.animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.accentState.toConstraint())
                }
            }
        }

        onMouseLeave {
            if (!isSelected) {
                text.animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.midTextState.toConstraint())
                }
            }
        }
    }

    fun select() {
        gui.selectCategory(category)

        isSelected = true
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.accentState.toConstraint())
        }
    }

    fun deselect() {
        isSelected = false
        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.midTextState.toConstraint())
        }
    }
}
