package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UICircle
import club.sk1er.elementa.components.Window
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.Effect
import club.sk1er.elementa.effects.StencilEffect
import java.awt.Color
import kotlin.math.max

class ClickEffect(private val color: Color, private val animationTime: Float = 0.5f) : Effect() {
    private var state = State.NotActive
    private var targetRadius = -1f
    private var radiusDelta = -1f
    private lateinit var stencilEffect: StencilEffect

    private var circle = UICircle(color = color).constrain {
        y = CenterConstraint()
    }

    override fun setup() {
        circle childOf Window.of(boundComponent)
        stencilEffect = StencilEffect()
        stencilEffect.bindComponent(boundComponent)

        boundComponent.onMouseClick { clickEvent ->
            state = State.Expanding
            circle.constrain {
                x = clickEvent.relativeX.pixels()
                targetRadius = max(clickEvent.relativeX, boundComponent.getWidth() - clickEvent.relativeX) * 1.1f
                radiusDelta = targetRadius / animationTime / Window.of(boundComponent).animationFPS
            }
        }
    }

    override fun animationFrame() {
        when (state) {
            State.NotActive -> {}
            State.Expanding -> {
                val newRadius = circle.getRadius() + radiusDelta
                if (newRadius >= targetRadius) {
                    state = State.Expanded
                } else {
                    circle.setRadius(newRadius.pixels())
                }
            }
            State.Expanded -> {
                val currentColor = circle.getColor()
                val alpha = currentColor.alpha - 1
                if (alpha <= 0) {
                    state = State.NotActive
                    targetRadius = -1f
                    radiusDelta = -1f
                } else {
                    circle.setColor(currentColor.withAlpha(currentColor.alpha - 1).asConstraint())
                }
            }
        }
    }

    override fun beforeDraw() {
        if (state != State.NotActive) {
            stencilEffect.beforeDraw()
            stencilEffect.beforeChildrenDraw()
            circle.draw()
            stencilEffect.afterDraw()
        }
    }

    enum class State {
        NotActive,
        Expanding,
        Expanded,
    }
}
