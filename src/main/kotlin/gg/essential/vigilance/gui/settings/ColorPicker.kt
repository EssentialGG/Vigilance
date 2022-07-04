package gg.essential.vigilance.gui.settings

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import gg.essential.universal.USound
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color
import java.util.*
import kotlin.math.roundToInt

class ColorPicker(initial: Color, allowAlpha: Boolean) : UIContainer() {

    private var currentHue: Float
    private var currentSaturation: Float
    private var currentBrightness: Float
    private var currentAlpha = initial.alpha / 255f

    init {
        val hsb = Color.RGBtoHSB(initial.red, initial.green, initial.blue, null)
        currentHue = hsb[0]
        currentSaturation = hsb[1]
        currentBrightness = hsb[2]
    }

    private var onValueChange: (Color) -> Unit = { }
    private var draggingHue = false
    private var draggingPicker = false

    private val bigPickerBox = UIBlock().constrain {
        width = 80.percent
        height = if (allowAlpha) 80.percent else 100.percent
        color = VigilancePalette.componentBorder.toConstraint()
    } childOf this

    private val pickerIndicator = UIContainer().constrain {
        x = (RelativeConstraint(currentSaturation) - 3.5f.pixels).coerceIn(2.pixels, 2.pixels(alignOpposite = true))
        y = (RelativeConstraint(1f - currentBrightness) - 3.5f.pixels).coerceIn(2.pixels, 2.pixels(alignOpposite = true))
        width = 3.pixels
        height = 3.pixels
    } effect OutlineEffect(Color.WHITE, 1f)

    private val huePickerLine = UIBlock().constrain {
        x = 85.percent
        width = FillConstraint(false)
        height = if (allowAlpha) 80.percent else 100.percent
        color = VigilancePalette.componentBorder.toConstraint()
    } childOf this

    private val hueIndicator = VigilancePalette.ARROW_LEFT_4X7.create().constrain {
        x = (-4).pixels(alignOpposite = true)
        y = RelativeConstraint(currentHue) - 3.pixels
    }

    private val alphaSlider = Slider(currentAlpha).constrain {
        x = CenterConstraint() boundTo bigPickerBox
        y = SiblingConstraint(5f)
        width = 80.percent
        height = FillConstraint(false)
    }

    private val alphaText = UIText(getFormattedAlpha()).constrain {
        x = 85.percent
        y = CenterConstraint() boundTo alphaSlider
        textScale = (2f / 3f).pixels
        color = VigilancePalette.text.toConstraint()
        fontProvider = getFontProvider()
    }

    init {
        if (allowAlpha) {
            alphaSlider childOf this
            alphaText childOf this

            alphaSlider.onValueChange { newAlpha ->
                currentAlpha = newAlpha
                alphaText.setText(getFormattedAlpha())

                recalculateColor()
            }
        }

        huePickerLine.addChild(object : UIComponent() {
            override fun draw(matrixStack: UMatrixStack) {
                super.beforeDraw(matrixStack)

                drawHueLine(matrixStack, this)

                super.draw(matrixStack)
            }
        }.constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 100.percent - 2.pixels
            height = 100.percent - 2.pixels
        }).addChild(hueIndicator)

        huePickerLine.onLeftClick { event ->
            USound.playButtonPress()
            draggingHue = true
            currentHue = (event.relativeY - 1f) / huePickerLine.getHeight()
            updateHueIndicator()
        }.onMouseDrag { _, mouseY, _ ->
            if (!draggingHue) return@onMouseDrag

            currentHue = ((mouseY - 1f) / huePickerLine.getHeight()).coerceIn(0f..1f)
            updateHueIndicator()
        }.onMouseRelease {
            draggingHue = false
        }

        bigPickerBox.addChild(object : UIComponent() {
            override fun draw(matrixStack: UMatrixStack) {
                super.beforeDraw(matrixStack)

                drawColorPicker(matrixStack, this)

                super.draw(matrixStack)
            }
        }.constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 100.percent - 2.pixels
            height = 100.percent - 2.pixels
        }).addChild(pickerIndicator)

        bigPickerBox.onLeftClick { event ->
            USound.playButtonPress()
            draggingPicker = true

            currentSaturation = event.relativeX / bigPickerBox.getWidth()
            currentBrightness = 1f - (event.relativeY / bigPickerBox.getHeight())
            updatePickerIndicator()
        }.onMouseDrag { mouseX, mouseY, _ ->
            if (!draggingPicker) return@onMouseDrag

            currentSaturation = (mouseX / bigPickerBox.getWidth()).coerceIn(0f..1f)
            currentBrightness = 1f - ((mouseY / bigPickerBox.getHeight()).coerceIn(0f..1f))
            updatePickerIndicator()
        }.onMouseRelease {
            draggingPicker = false
        }
    }

    private fun updateHueIndicator() {
        hueIndicator.setY(RelativeConstraint(currentHue.coerceAtMost(0.98f)) - 3.pixels)

        recalculateColor()
    }

    private fun updatePickerIndicator() {
        pickerIndicator.setX(
            (RelativeConstraint(currentSaturation) - 2.5f.pixels).coerceIn(2.pixels, 2.pixels(alignOpposite = true))
        )
        pickerIndicator.setY(
            (RelativeConstraint(1f - currentBrightness) - 2.5f.pixels).coerceIn(2.pixels, 2.pixels(alignOpposite = true))
        )

        recalculateColor()
    }

    private fun recalculateColor() {
        onValueChange(getCurrentColor())
    }

    fun getCurrentColor(): Color {
        return Color(
            (Color.HSBtoRGB(currentHue, currentSaturation, currentBrightness) and 0xffffff) or ((currentAlpha * 255f).roundToInt() shl 24),
            true
        )
    }

    fun setHSB(hue: Float, sat: Float, bright: Float) {
        currentHue = hue
        currentSaturation = sat
        currentBrightness = bright

        updateHueIndicator()
        updatePickerIndicator()
        recalculateColor()
    }

    fun setAlpha(alpha: Float) {
        currentAlpha = alpha
        alphaSlider.setCurrentPercentage(alpha)
        recalculateColor()
    }

    fun onValueChange(listener: (Color) -> Unit) {
        onValueChange = listener
    }

    private fun drawColorPicker(matrixStack: UMatrixStack, component: UIComponent) {
        val left = component.getLeft().toDouble()
        val top = component.getTop().toDouble()
        val right = component.getRight().toDouble()
        val bottom = component.getBottom().toDouble()

        setupDraw()
        val graphics = UGraphics.getFromTessellator()
        graphics.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_COLOR)

        val height = bottom - top

        for (x in 0..49) {
            val curLeft = left + (right - left).toFloat() * x.toFloat() / 50f
            val curRight = left + (right - left).toFloat() * (x.toFloat() + 1) / 50f

            var first = true
            for (y in 0..50) {
                val yPos = top + (y.toFloat() * height / 50.0)
                val color = getColor(x.toFloat() / 50f, 1 - y.toFloat() / 50f, currentHue)

                if (!first) {
                    drawVertex(graphics, matrixStack, curLeft, yPos, color)
                    drawVertex(graphics, matrixStack, curRight, yPos, color)
                }

                if (y < 50) {
                    drawVertex(graphics, matrixStack, curRight, yPos, color)
                    drawVertex(graphics, matrixStack, curLeft, yPos, color)
                }
                first = false
            }

        }

        graphics.drawDirect()
        cleanupDraw()
    }

    private fun getColor(x: Float, y: Float, hue: Float): Color {
        return Color(Color.HSBtoRGB(hue, x, y))
    }

    private fun drawHueLine(matrixStack: UMatrixStack, component: UIComponent) {
        val left = component.getLeft().toDouble()
        val top = component.getTop().toDouble()
        val right = component.getRight().toDouble()
        val height = component.getHeight().toDouble()

        setupDraw()
        val graphics = UGraphics.getFromTessellator()

        graphics.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_COLOR)

        var first = true
        for ((i, color) in hueColorList.withIndex()) {
            val yPos = top + (i.toFloat() * height / 50.0)
            if (!first) {
                drawVertex(graphics, matrixStack, left, yPos, color)
                drawVertex(graphics, matrixStack, right, yPos, color)
            }

            drawVertex(graphics, matrixStack, right, yPos, color)
            drawVertex(graphics, matrixStack, left, yPos, color)

            first = false
        }

        graphics.drawDirect()
        cleanupDraw()
    }

    private fun setupDraw() {
        UGraphics.enableBlend()
        UGraphics.disableAlpha()
        UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
        UGraphics.shadeModel(7425)
    }

    private fun cleanupDraw() {
        UGraphics.shadeModel(7424)
        UGraphics.disableBlend()
        UGraphics.enableAlpha()
    }

    private fun drawVertex(graphics: UGraphics, matrixStack: UMatrixStack, x: Double, y: Double, color: Color) {
        graphics
            .pos(matrixStack, x, y, 0.0)
            .color(color.red.toFloat() / 255f, color.green.toFloat() / 255f, color.blue.toFloat() / 255f, 1f)
            .endVertex()
    }

    private fun getFormattedAlpha(): String {
        return "%.2f".format(Locale.US, alphaSlider.getCurrentPercentage())
    }

    companion object {
        private val hueColorList: List<Color> = (0..50).map { i -> Color(Color.HSBtoRGB(i / 50f, 1f, 0.7f)) }
    }
}
