package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.USound
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick

class Slider(initialValue: Float) : UIContainer() {

    private var percentage = initialValue
    private var onValueChange: (Float) -> Unit = {}
    private var dragging = false
    private var grabOffset = 0f

    private val outerBox = UIContainer().constrain {
        x = basicXConstraint {
            this@Slider.getLeft() + 1f + this@Slider.getHeight() * 0.75f
        }
        y = CenterConstraint()
        width = basicWidthConstraint {
            this@Slider.getWidth() - 2f - this@Slider.getHeight() * 1.5f
        }
        height = 50.percent
    } childOf this effect OutlineEffect(VigilancePalette.getComponentBorder(), 1f).bindColor(VigilancePalette.componentBorder)

    private val completionBox = UIBlock().constrain {
        width = RelativeConstraint(percentage)
        height = 100.percent
        color = VigilancePalette.green.toConstraint()
    } childOf outerBox

    val grabBox = UIBlock().constrain {
        x = basicXConstraint { completionBox.getRight() - it.getWidth() / 2f }
        y = CenterConstraint() boundTo outerBox
        width = AspectConstraint(1f)
        height = 100.percent
        color = VigilancePalette.green.toConstraint()
    } childOf this effect OutlineEffect(VigilancePalette.getBlack(), 1f)

    init {
        grabBox.onLeftClick { event ->
            USound.playButtonPress()
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
            USound.playButtonPress()
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
