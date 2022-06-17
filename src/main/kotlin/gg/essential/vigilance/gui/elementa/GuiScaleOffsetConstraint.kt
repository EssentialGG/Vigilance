package gg.essential.vigilance.gui.elementa

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.ConstraintType
import gg.essential.elementa.constraints.HeightConstraint
import gg.essential.elementa.constraints.resolution.ConstraintVisitor
import gg.essential.universal.UResolution

class GuiScaleOffsetConstraint(val offset: Float = 1f) : HeightConstraint {
    override var cachedValue = 0f
    override var recalculate = true
    override var constrainTo: UIComponent? = null

    override fun getHeightImpl(component: UIComponent): Float {
        val scaleFactor = UResolution.scaleFactor
        return ((scaleFactor - offset).coerceAtLeast(1.0) / scaleFactor).toFloat()
    }

    override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {
    }
}