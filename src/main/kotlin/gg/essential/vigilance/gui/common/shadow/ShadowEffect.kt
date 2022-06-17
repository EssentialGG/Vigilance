package gg.essential.vigilance.gui.common.shadow

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.effects.Effect
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.universal.UMatrixStack
import gg.essential.vigilance.gui.VigilancePalette
import java.awt.Color


internal class ShadowEffect(
    shadowColor: Color = VigilancePalette.getComponentBackground()
) : Effect() {

    private val shadowColorState = BasicState(shadowColor).map { it }

    override fun beforeDraw(matrixStack: UMatrixStack) {
        when (val boundComponent = boundComponent) {
            is UIBlock -> {
                // Don't draw if the boundComponent wouldn't draw
                if (boundComponent.getColor().alpha == 0) {
                    return
                }
                val x = boundComponent.getLeft().toDouble()
                val y = boundComponent.getTop().toDouble()
                val x2 = boundComponent.getRight().toDouble()
                val y2 = boundComponent.getBottom().toDouble()

                val color = shadowColorState.get()

                UIBlock.drawBlock(matrixStack, color, x+1, y+1, x2+1, y2+1)
            }
            is UIImage -> {
                boundComponent.drawImage(
                    matrixStack,
                    boundComponent.getLeft() + 1.0,
                    boundComponent.getTop() + 1.0,
                    boundComponent.getWidth().toDouble(),
                    boundComponent.getHeight().toDouble(),
                    shadowColorState.get()
                )
            }
            else -> {
                throw UnsupportedOperationException("Shadow effect cannot be applied to ${getDebugInfo()}")
            }
        }
    }

    fun rebindColor(state: State<Color>) = apply {
        shadowColorState.rebind(state)
    }

    private fun getDebugInfo(): String {
        return boundComponent.componentName + " " + boundComponent.javaClass.name
    }
}