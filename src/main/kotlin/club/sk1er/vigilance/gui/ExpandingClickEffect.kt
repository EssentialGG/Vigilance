package club.sk1er.vigilance.gui

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UICircle
import club.sk1er.elementa.components.Window
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.Effect
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.elementa.events.UIClickEvent
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
        circle.hide(true)

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
                color = this@ExpandingClickEffect.color.asConstraint()
                radius = 0.pixels()
            }
        }

        boundComponent.onMouseClick { onClickHandler(it) }

        circle.onMouseClick {
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
                    circle.setColor(currentColor.withAlpha(alpha).asConstraint())
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
