package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIBlock
import club.sk1er.mods.core.universal.UniversalGraphicsHandler
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color

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

        UniversalGraphicsHandler.pushMatrix()
        drawGradientRect(x.toInt(), y.toInt(), x2.toInt(), y2.toInt(), startColor, endColor, direction)
        UniversalGraphicsHandler.popMatrix()

        super.draw()
    }

    enum class GradientDirection {
        TopToBottom,
        BottomToTop,
        LeftToRight,
        RightToLeft,
    }

    companion object {
        fun drawGradientRect(left: Int, top: Int, right: Int, bottom: Int, startColor: Color, endColor: Color, direction: GradientDirection) {
            UniversalGraphicsHandler.disableTexture2D()
            UniversalGraphicsHandler.enableBlend()
            UniversalGraphicsHandler.disableAlpha()
            UniversalGraphicsHandler.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
            UniversalGraphicsHandler.shadeModel(GL11.GL_SMOOTH)

            val (topLeft, topRight, bottomLeft, bottomRight) = when (direction) {
                GradientDirection.TopToBottom -> arrayOf(startColor, startColor, endColor, endColor)
                GradientDirection.BottomToTop -> arrayOf(endColor, endColor, startColor, startColor)
                GradientDirection.LeftToRight -> arrayOf(startColor, endColor, endColor, startColor)
                GradientDirection.RightToLeft -> arrayOf(endColor, startColor, startColor, endColor)
            }

            val tessellator = UniversalGraphicsHandler.getFromTessellator()
            tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)
            tessellator.pos(right.toDouble(), top.toDouble(), 0.0).color(topRight).endVertex()
            tessellator.pos(left.toDouble(), top.toDouble(), 0.0).color(topLeft).endVertex()
            tessellator.pos(left.toDouble(), bottom.toDouble(), 0.0).color(bottomLeft).endVertex()
            tessellator.pos(right.toDouble(), bottom.toDouble(), 0.0).color(bottomRight).endVertex()
            UniversalGraphicsHandler.draw()

            UniversalGraphicsHandler.shadeModel(GL11.GL_FLAT)
            UniversalGraphicsHandler.disableBlend()
            UniversalGraphicsHandler.enableAlpha()
            UniversalGraphicsHandler.enableTexture2D()
        }
    }
}
