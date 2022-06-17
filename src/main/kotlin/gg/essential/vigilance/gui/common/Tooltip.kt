package gg.essential.vigilance.gui.common

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.elementa.state.toConstraint
import gg.essential.elementa.utils.ObservableClearEvent
import gg.essential.elementa.utils.ObservableRemoveEvent
import gg.essential.elementa.utils.roundToRealPixels
import gg.essential.universal.UMatrixStack
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onSetValueAndNow
import java.awt.Color
import java.util.Observer

internal abstract class Tooltip(private val logicalParent: UIComponent) : UIComponent() {

    var textColorState = BasicState(Color.WHITE)
    var textShadowState = BasicState(true)

    init {
        constrain {
            width = ChildBasedMaxSizeConstraint() + 8.pixels
            height = ChildBasedSizeConstraint() + 8.pixels
        }
    }

    val content by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint()
    } childOf this

    private var removalListeners = mutableListOf<() -> Unit>()

    fun bindVisibility(visible: State<Boolean>): Tooltip {
        visible.onSetValueAndNow {
            if (it) {
                showTooltip()
            } else {
                hideTooltip()
            }
        }
        return this
    }

    fun clearLines() {
        content.clearChildren()
    }

    fun addLine(text: String = "", configure: UIText.() -> Unit = {}) = apply {
        val component = UIText(text).bindShadow(textShadowState).constrain {
            y = SiblingConstraint(padding = 3f)
            color = textColorState.toConstraint()
        } childOf content
        component.configure()
    }

    fun bindLine(state: State<String>, configure: UIText.() -> Unit = {}): Tooltip {
        state.onSetValueAndNow {
            clearLines()
            addLine(it, configure)
        }
        return this
    }

    fun showTooltip(delayed: Boolean = true) {
        if (delayed) {
            return Window.enqueueRenderOperation { showTooltip(delayed = false) }
        }

        val window = Window.of(logicalParent)
        if (this in window.children) {
            return
        }

        window.addChild(this)
        setFloating(true)

        // When our logical parent is removed from the component tree, we also need to remove ourselves (our actual
        // parent is the window, so that is not going to happen by itself).
        // We need to do that asap because our constraints may depend on our logical parent and may error when evaluated
        // after our logical parent got removed.
        // Elementa has no unmount event, so instead we listen for changes to the children list of all our parents.
        fun UIComponent.onRemoved(listener: () -> Unit) {
            if (parent == this) {
                return
            }

            val observer = Observer { _, event ->
                if (event is ObservableClearEvent<*> || event is ObservableRemoveEvent<*> && event.element.value == this) {
                    listener()
                }
            }
            parent.children.addObserver(observer)
            removalListeners.add { parent.children.deleteObserver(observer) }

            parent.onRemoved(listener)
        }
        logicalParent.onRemoved {
            hideTooltip(delayed = false)
        }
    }

    fun hideTooltip(delayed: Boolean = true) {
        if (delayed) {
            return Window.enqueueRenderOperation { hideTooltip(delayed = false) }
        }

        val window = Window.ofOrNull(this) ?: return

        setFloating(false)
        window.removeChild(this)

        removalListeners.forEach { it() }
        removalListeners.clear()
    }

}

internal class EssentialTooltip(
    private val logicalParent: UIComponent,
    private val belowComponent: Boolean,
    private val notchSize: Int = 3,
    private val notchBoundToParent: Boolean = false,
) :
    Tooltip(logicalParent) {

    init {
        textColorState.set(VigilancePalette.getText())

        this effect OutlineEffect(VigilancePalette.getBlack(), 1f)

        constrain {
            width = ChildBasedMaxSizeConstraint() + 6.pixels
            height = ChildBasedSizeConstraint() + 4.pixels
        }
    }

    override fun draw(matrixStack: UMatrixStack) {
        super.beforeDraw(matrixStack)

        // Background
        UIBlock.drawBlock(
            matrixStack,
            VigilancePalette.getComponentBackground(),
            getLeft().toDouble(),
            getTop().toDouble(),
            getRight().toDouble(),
            getBottom().toDouble(),
        )

        super.draw(matrixStack)

        val center = if (notchBoundToParent) {
            (logicalParent.getLeft() + logicalParent.getRight()) / 2.0
        } else {
            (content.getLeft() + content.getRight()) / 2.0
        }.roundToRealPixels()

        if (belowComponent) {

            val top = (getTop().toDouble() + 1) //on top of the outline
            for (i in 1..notchSize) {
                UIBlock.drawBlock(
                    matrixStack,
                    VigilancePalette.getBlack(),
                    center - (notchSize - i) - 0.5,
                    top - 2 - i,
                    center + (notchSize - i) + 0.5,
                    top - i - 1
                )
            }

            for (i in 1..notchSize) {
                UIBlock.drawBlock(
                    matrixStack,
                    VigilancePalette.getComponentBackground(),
                    center - (notchSize - i) - 0.5,
                    top - 1 - i,
                    center + (notchSize - i) + 0.5,
                    top - i
                )
            }
        } else {
            val bottom = (getBottom().toDouble() - 1) //on top of the outline
            for (i in 1..notchSize) {
                UIBlock.drawBlock(
                    matrixStack,
                    VigilancePalette.getBlack(),
                    center - (notchSize - i) - 0.5,
                    bottom + i,
                    center + (notchSize - i) + 0.5,
                    bottom + i + 2
                )
            }

            for (i in 1..notchSize) {
                UIBlock.drawBlock(
                    matrixStack,
                    VigilancePalette.getComponentBackground(),
                    center - (notchSize - i) - 0.5,
                    bottom + i,
                    center + (notchSize - i) + 0.5,
                    bottom + i + 1
                )
            }
        }
    }
}

internal class VanillaTooltip(logicalParent: UIComponent) : Tooltip(logicalParent) {

    override fun draw(matrixStack: UMatrixStack) {
        beforeDraw(matrixStack)

        val l = getLeft().toDouble()
        val r = getRight().toDouble()
        val t = getTop().toDouble()
        val b = getBottom().toDouble()

        // Draw background
        UIBlock.drawBlock(matrixStack, BACKGROUND_COLOR, l + 1, t, r - 1, b) // Top to bottom
        UIBlock.drawBlock(matrixStack, BACKGROUND_COLOR, l, t + 1, l + 1, b - 1) // Left pixel row
        UIBlock.drawBlock(matrixStack, BACKGROUND_COLOR, r - 1, t + 1, r, b - 1) // Right pixel row

        // Draw the border, it gets darker from top to bottom
        UIBlock.drawBlock(matrixStack, BORDER_LIGHT, l + 1, t + 1, r - 1, t + 2) // Top border
        UIBlock.drawBlock(matrixStack, BORDER_DARK, l + 1, b - 2, r - 1, b - 1) // Bottom border
        GradientComponent.drawGradientBlock(
            matrixStack,
            l + 1,
            t + 2,
            l + 2,
            b - 2,
            BORDER_LIGHT,
            BORDER_DARK,
            GradientComponent.GradientDirection.TOP_TO_BOTTOM
        ) // Left border
        GradientComponent.drawGradientBlock(
            matrixStack,
            r - 2,
            t + 2,
            r - 1,
            b - 2,
            BORDER_LIGHT,
            BORDER_DARK,
            GradientComponent.GradientDirection.TOP_TO_BOTTOM
        ) // Right border

        super.draw(matrixStack)
    }

    companion object {
        private val BACKGROUND_COLOR = Color(16, 0, 16, 240)
        private val BORDER_LIGHT = Color(80, 0, 255, 80)
        private val BORDER_DARK = Color(40, 0, 127, 80)
    }
}
