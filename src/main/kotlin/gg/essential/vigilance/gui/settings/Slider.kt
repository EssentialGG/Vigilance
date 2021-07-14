package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color

class Slider(initialValue: Float) : UIContainer() {
    private var percentage = initialValue
    private var onValueChange: (Float) -> Unit = {}
    private var dragging = false
    private var grabOffset = 0f

    private val outerBox = UIContainer().constrain {
        x = basicXConstraint {
            this@Slider.getLeft() + 1f + this@Slider.getHeight() * 0.75f
        }
        y = 1.pixels()
        width = basicWidthConstraint {
            this@Slider.getWidth() - 2f - this@Slider.getHeight() * 1.5f
        }
        height = RelativeConstraint(0.5f)
    } childOf this effect OutlineEffect(VigilancePalette.getBrightDivider(), 1f).bindColor(VigilancePalette.brightDividerState)

    private val completionBox = UIBlock().constrain {
        x = (-0.5f).pixels()
        y = (-0.5f).pixels()
        width = RelativeConstraint(percentage)
        height = RelativeConstraint(1f) + 1.pixels()
        color = VigilancePalette.accentState.toConstraint()
    } childOf outerBox

    val grabBox = UIBlock().constrain {
        x = basicXConstraint { completionBox.getRight() - it.getWidth() / 2f }
        y = CenterConstraint() boundTo outerBox
        width = AspectConstraint(1f)
        height = 100.percent()
        color = VigilancePalette.accentState.toConstraint()
    } childOf this effect OutlineEffect(Color.BLACK, 1f)

    init {
        grabBox.onLeftClick { event ->
            dragging = true
            grabOffset = event.relativeX - (grabBox.getWidth() / 2)
            event.stopPropagation()
        }.onMouseRelease {
            dragging = false
            grabOffset = 0f
        }.onMouseDrag { mouseX, _, _ ->
            if (!dragging) return@onMouseDrag

            val clamped = (mouseX + grabBox.getLeft() - grabOffset).coerceIn(outerBox.getLeft()..outerBox.getRight())
            val percentage = (clamped - outerBox.getLeft()) / outerBox.getWidth()
            setCurrentPercentage(percentage)
        }

        outerBox.onLeftClick { event ->
            val percentage = event.relativeX / outerBox.getWidth()
            setCurrentPercentage(percentage)
            dragging = true
        }
    }

    fun getCurrentPercentage() = percentage

    fun setCurrentPercentage(newPercentage: Float, callListener: Boolean = true) {
        percentage = newPercentage.coerceIn(0f..1f)

        completionBox.setWidth(RelativeConstraint(percentage))

        if (callListener) {
            onValueChange(percentage)
        }
    }

    fun onValueChange(listener: (Float) -> Unit) {
        onValueChange = listener
    }
}
