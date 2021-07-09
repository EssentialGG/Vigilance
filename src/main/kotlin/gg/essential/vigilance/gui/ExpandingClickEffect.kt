package gg.essential.vigilance.gui

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UICircle
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.Effect
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.events.UIClickEvent
import gg.essential.elementa.utils.withAlpha
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class ExpandingClickEffect @JvmOverloads constructor(
    private val color: Color,
    private val animationTime: Float = 0.2f,
    private val scissorBoundingBox: UIComponent? = null
) : Effect() {
    private var state = State.NotActive
    private var targetRadius = -1f
    private var radiusDelta = -1f
    private lateinit var scissorEffect: ScissorEffect
    private lateinit var lastClick: UIClickEvent

    private var circle = UICircle(color = color)

    override fun setup() {
        scissorEffect = ScissorEffect(scissorBoundingBox ?: boundComponent)
        circle effect scissorEffect
        circle childOf Window.of(boundComponent)
        Window.enqueueRenderOperation { circle.hide(true) }

        fun onClickHandler(clickEvent: UIClickEvent) {
            lastClick = clickEvent
            circle.unhide()
            state = State.Expanding

            val left = boundComponent.getLeft()
            val top = boundComponent.getTop()
            val right = boundComponent.getRight()
            val bottom = boundComponent.getBottom()

            val center = clickEvent.absoluteX to clickEvent.absoluteY
            targetRadius = max(
                max(
                    distance(center, left to top),
                    distance(center, left to bottom)
                ),
                max(
                    distance(center, right to top),
                    distance(center, right to bottom)
                )
            ) + 2f
            radiusDelta = targetRadius / animationTime / Window.of(boundComponent).animationFPS

            circle.constrain {
                x = clickEvent.absoluteX.pixels()
                y = clickEvent.absoluteY.pixels()
                color = this@ExpandingClickEffect.color.toConstraint()
                radius = 0.pixels()
            }
        }

        boundComponent.onLeftClick { onClickHandler(it) }

        circle.onLeftClick {
            onClickHandler(it)
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
                val alpha = currentColor.alpha - 2
                if (alpha <= 0) {
                    state = State.NotActive
                    targetRadius = -1f
                    radiusDelta = -1f
                    circle.hide(true)
                } else {
                    circle.setColor(currentColor.withAlpha(alpha).toConstraint())
                }
            }
        }
    }

    override fun beforeDraw() {
        if (state != State.NotActive)
            circle.draw()
    }

    enum class State {
        NotActive,
        Expanding,
        Expanded,
    }

    companion object {
        private fun distance(p1: Pair<Float, Float>, p2: Pair<Float, Float>) =
            sqrt((p1.first - p2.first).pow(2f) + (p1.second - p2.second).pow(2f))
    }
}
