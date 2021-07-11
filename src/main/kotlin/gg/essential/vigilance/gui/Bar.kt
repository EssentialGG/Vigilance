package gg.essential.vigilance.gui

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.utils.invisible

abstract class Bar : UIBlock(VigilancePalette.getLightBackground()) {
    init {
        constrain {
            width = 100.percent()
            height = 30.pixels()
        }
    }

    open fun onShow() { }

    open fun onHide() { }

    protected fun makeIcon(icon: UIImage, iconWidth: Int, iconHeight: Int): UIComponent {
        val parent = UIBlock(VigilancePalette.getBrightHighlight().invisible()).constrain {
            x = SiblingConstraint()
            width = AspectConstraint()
            height = 100.percent()
        }

        val image = icon.constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = iconWidth.pixels()
            height = iconHeight.pixels()
        } childOf parent

        parent.onMouseEnter {
            parent.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getBrightHighlight().toConstraint())
            }
        }.onMouseLeave {
            parent.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getBrightHighlight().invisible().toConstraint())
            }
        }

        return parent
    }
}