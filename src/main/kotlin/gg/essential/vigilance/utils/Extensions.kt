package gg.essential.vigilance.utils

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.GradientComponent
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.CopyConstraintFloat
import gg.essential.elementa.constraints.HeightConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.Effect
import gg.essential.elementa.events.UIClickEvent
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.UMouse
import gg.essential.universal.UResolution
import gg.essential.vigilance.gui.VigilancePalette
import java.awt.Color
import kotlin.reflect.KProperty

inline fun UIComponent.onLeftClick(crossinline method: UIComponent.(event: UIClickEvent) -> Unit) = onMouseClick {
    if (it.mouseButton == 0) {
        this.method(it)
    }
}

private fun UIComponent.isComponentInParentChain(target: UIComponent): Boolean {
    var component: UIComponent = this
    while (component.hasParent && component !is Window) {
        component = component.parent
        if (component == target)
            return true
    }

    return false
}

/**
 * Executes the supplied [block] on this component's animationFrame
 */
internal fun UIComponent.onAnimationFrame(block: () -> Unit) =
    enableEffect(object : Effect() {
        override fun animationFrame() {
            block()
        }
    })

/**
 * Returns a state representing whether this UIComponent is hovered
 *
 * [hitTest] will perform a hit test to make sure the user is actually hovered over this component
 * as compared to the mouse just being within its content bounds while being hovered over another
 * component rendered above this.
 *
 * [layoutSafe] will delay the state change until a time in which it is safe to make layout changes.
 * This option will induce an additional delay of one frame because the state is updated during the next
 * [Window.enqueueRenderOperation] after the hoverState changes.
 */
internal fun UIComponent.hoveredState(hitTest: Boolean = true, layoutSafe: Boolean = true): State<Boolean> {
    // "Unsafe" means that it is not safe to depend on this for layout changes
    val unsafeHovered = BasicState(false)

    // "Safe" because layout changes can directly happen when this changes (ie in onSetValue)
    val safeHovered = BasicState(false)

    // Performs a hit test based on the current mouse x / y
    fun hitTestHovered(): Boolean {
        // Positions the mouse in the center of pixels so isPointInside will
        // pass for items 1 pixel wide objects. See ElementaVersion v2 for more details
        val halfPixel = 0.5f / UResolution.scaleFactor.toFloat()
        val mouseX = UMouse.Scaled.x.toFloat() + halfPixel
        val mouseY = UMouse.Scaled.y.toFloat() + halfPixel
        return if (isPointInside(mouseX, mouseY)) {

            val window = Window.of(this)
            val hit = (window.hoveredFloatingComponent?.hitTest(mouseX, mouseY)) ?: window.hitTest(mouseX, mouseY)

            hit.isComponentInParentChain(this) || hit == this
        } else {
            false
        }
    }

    if (hitTest) {
        // It's possible the animation framerate will exceed that of the actual frame rate
        // Therefore, in order to avoid redundantly performing the hit test multiple times
        // in the same frame, this boolean is used to ensure that hit testing is performed
        // at most only a single time each frame
        var registerHitTest = true

        onAnimationFrame {
            if (registerHitTest) {
                registerHitTest = false
                Window.enqueueRenderOperation {
                    // The next animation frame should register another renderOperation
                    registerHitTest = true

                    // Since enqueueRenderOperation will keep polling the queue until there are no more items,
                    // the forwarding of any update to the safeHovered state will still happen this frame
                    unsafeHovered.set(hitTestHovered())
                }
            }
        }
    }
    onMouseEnter {
        if (hitTest) {
            unsafeHovered.set(hitTestHovered())
        } else {
            unsafeHovered.set(true)
        }
    }

    onMouseLeave {
        unsafeHovered.set(false)
    }

    return if (layoutSafe) {
        unsafeHovered.onSetValue {
            Window.enqueueRenderOperation {
                safeHovered.set(it)
            }
        }
        safeHovered
    } else {
        unsafeHovered
    }
}

internal fun <T : UIComponent> T.bindParent(parent: UIComponent, state: State<Boolean>, delayed: Boolean = false) =
    bindParent(state.map {
        if (it) parent else null
    }, delayed)

internal fun <T : UIComponent> T.bindParent(state: State<UIComponent?>, delayed: Boolean = false) = apply {
    state.onSetValueAndNow { parent ->
        val handleStateUpdate = {
            if (this.hasParent && this.parent != parent) {
                this.parent.removeChild(this)
            }
            if (parent != null && this !in parent.children) {
                parent.addChild(this)
            }
        }
        if (delayed) {
            Window.enqueueRenderOperation {
                handleStateUpdate()
            }
        } else {
            handleStateUpdate()
        }
    }
}

internal fun <T : UIComponent> T.centered(): T = apply {
    constrain {
        x = CenterConstraint()
        y = CenterConstraint()
    }
}

internal fun ScrollComponent.createScrollGradient(
    top: Boolean,
    heightSize: HeightConstraint,
    color: Color = VigilancePalette.getTextShadow(),
    maxGradient: Int = 204,
): GradientComponent {

    val percentState = BasicState(0f)

    this.addScrollAdjustEvent(false) { percent, _ ->
        percentState.set(percent)
    }

    val gradient = object : GradientComponent(
        color.withAlpha(0),
        color.withAlpha(0)
    ) {
        // Override because the gradient should be treated as if it does not exist from an input point of view
        override fun isPointInside(x: Float, y: Float): Boolean {
            return false
        }
    }.bindStartColor(percentState.map {
        if (top) {
            color.withAlpha((it * 1000).toInt().coerceIn(0..maxGradient))
        } else {
            color.withAlpha(0)
        }
    }).bindEndColor(percentState.map {
        if (top) {
            color.withAlpha(0)
        } else {
            color.withAlpha(((1 - it) * 1000).toInt().coerceIn(0..maxGradient))
        }
    }).constrain {
        y = 0.pixels(alignOpposite = !top) boundTo this@createScrollGradient
        x = CopyConstraintFloat() boundTo this@createScrollGradient
        width = CopyConstraintFloat() boundTo this@createScrollGradient
        height = heightSize
    } childOf parent
    return gradient
}

internal infix fun ScrollComponent.scrollGradient(heightSize: HeightConstraint) = apply {
    val topGradient = createScrollGradient(true, heightSize)
    createScrollGradient(false, 100.percent boundTo topGradient)
}

internal fun <T> State<T>.onSetValueAndNow(listener: (T) -> Unit) = onSetValue(listener).also { listener(get()) }

internal operator fun State<Boolean>.not() = map { !it }
internal fun State<String>.empty() = map{ it.isBlank()}
internal infix fun State<Boolean>.and(other: State<Boolean>) = zip(other).map { (a, b) -> a && b }
internal infix fun State<Boolean>.or(other: State<Boolean>) = zip(other).map { (a, b) -> a || b }

internal class ReadyOnlyState<T>(private val internalState: State<T>) : State<T>() {

    init {
        internalState.onSetValueAndNow {
            super.set(it)
        }
    }

    override fun get(): T {
        return internalState.get()
    }

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("This state is read-only", level = DeprecationLevel.ERROR)
    override fun set(value: T) {
        throw IllegalStateException("Cannot set read only value")
    }
}

internal operator fun <T> State<T>.getValue(obj: Any, property: KProperty<*>): T = get()
internal operator fun <T> State<T>.setValue(obj: Any, property: KProperty<*>, value: T) = set(value)

internal fun <T> State<T>.readOnly() = ReadyOnlyState(this)

internal fun <T> T.state() = BasicState(this)
