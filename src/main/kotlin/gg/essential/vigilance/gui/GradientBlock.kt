package gg.essential.vigilance.gui

import gg.essential.elementa.components.GradientComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UGraphics
import java.awt.Color

@Deprecated("Use Elementa's GradientComponent instead.", ReplaceWith("GradientComponent", "gg.essential.elementa.components.GradientComponent"))
class GradientBlock(
    private var startColor: Color,
    private var endColor: Color,
    private var direction: GradientDirection = GradientDirection.TopToBottom
) : UIBlock(Color(0, 0, 0, 0)) {
    fun getStartColor(startColor: Color) = startColor
    fun setStartColor(startColor: Color) = apply { this.startColor = startColor }

    fun getEndColor(endColor: Color) = endColor
    fun setEndColor(endColor: Color) = apply { this.endColor = endColor }

    fun getDirection() = direction
    fun setDirection(direction: GradientDirection) = apply { this.direction = direction }

    override fun draw() {
        beforeDraw()

        val x = this.getLeft().toDouble()
        val y = this.getTop().toDouble()
        val x2 = this.getRight().toDouble()
        val y2 = this.getBottom().toDouble()

        UGraphics.pushMatrix()
        UGraphics.popMatrix()
        GradientComponent.drawGradientBlock(x.toInt(), y.toInt(), x2.toInt(), y2.toInt(), startColor, endColor, direction.elementa)

        super.draw()
    }

    enum class GradientDirection {
        TopToBottom,
        BottomToTop,
        LeftToRight,
        RightToLeft,
    }

    companion object {
        private val GradientDirection.elementa get() = when (this) {
            GradientDirection.TopToBottom -> GradientComponent.GradientDirection.TOP_TO_BOTTOM
            GradientDirection.BottomToTop -> GradientComponent.GradientDirection.BOTTOM_TO_TOP
            GradientDirection.LeftToRight -> GradientComponent.GradientDirection.LEFT_TO_RIGHT
            GradientDirection.RightToLeft -> GradientComponent.GradientDirection.RIGHT_TO_LEFT
        }

        fun drawGradientRect(left: Int, top: Int, right: Int, bottom: Int, startColor: Color, endColor: Color, direction: GradientDirection) {
            GradientComponent.drawGradientBlock(left, top, right, bottom, startColor, endColor, direction.elementa)
        }
    }
}
