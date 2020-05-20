package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.constraints.AspectConstraint
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.vigilance.gui.SettingsGui
import java.awt.Color

class Slider(initialValue: Float) : UIContainer() {
    private var percentage = initialValue
    private var onValueChange: (Float) -> Unit = {}
    private var dragging = false
    private var grabOffset = 0f

    private val outerBox = UIContainer().constrain {
        x = 1.pixels()
        y = 1.pixels()
        width = RelativeConstraint(1f) - 2.pixels()
        height = RelativeConstraint(1f) - 2.pixels()
    } childOf this effect OutlineEffect(SettingsGui.GRAY_COLOR, 0.5f)

    private val completionBox = UIBlock(SettingsGui.ACCENT_COLOR).constrain {
        x = (-0.5f).pixels()
        y = (-0.5f).pixels()
        width = RelativeConstraint(percentage)
        height = RelativeConstraint(1f) + 1.pixels()
    } childOf outerBox

    private val grabBoxCenterConstraint = basicXConstraint { it.parent.getLeft() + it.getWidth() / 2 }

    val grabBox = UIBlock(SettingsGui.ACCENT_COLOR).constrain {
        x = RelativeConstraint(percentage) - grabBoxCenterConstraint
        y = CenterConstraint()
        width = AspectConstraint(1f)
        height = RelativeConstraint(1.5f)
    } childOf outerBox effect OutlineEffect(Color.BLACK, 0.5f)

    init {
        grabBox.onMouseClick { event ->
            if (event.mouseButton == 0) {
                dragging = true
                grabOffset = event.relativeX - (grabBox.getWidth() / 2)
                event.stopPropagation()
            }
        }.onMouseRelease {
            dragging = false
            grabOffset = 0f
        }.onMouseDrag { mouseX, _, _ ->
            if (!dragging) return@onMouseDrag

            val clamped = (mouseX + grabBox.getLeft() - grabOffset).coerceIn(outerBox.getLeft()..outerBox.getRight())
            val percentage = (clamped - outerBox.getLeft()) / outerBox.getWidth()
            setCurrentPercentage(percentage)
        }

        outerBox.onMouseClick { event ->
            if (event.mouseButton == 0) {
                val percentage = event.relativeX / outerBox.getWidth()
                setCurrentPercentage(percentage)
                dragging = true
            }
        }
    }

    fun getCurrentPercentage() = percentage

    fun setCurrentPercentage(newPercentage: Float, callListener: Boolean = true) {
        percentage = newPercentage.coerceIn(0f..1f)

        grabBox.setX(RelativeConstraint(percentage) - grabBoxCenterConstraint)
        completionBox.setWidth(RelativeConstraint(percentage))

        if (callListener) {
            onValueChange(percentage)
        }
    }

    fun onValueChange(listener: (Float) -> Unit) {
        onValueChange = listener
    }
}