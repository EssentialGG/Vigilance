package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.SVGComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.FillConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.YConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.mods.core.universal.UniversalGraphicsHandler
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color

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

    private val bigPickerBox = UIBlock(OUTLINE_COLOR).constrain {
        width = RelativeConstraint(0.8f)
        height = RelativeConstraint(if (allowAlpha) 0.8f else 1f)
    } childOf this

    private val pickerIndicator = UIContainer().constrain {
        x = (RelativeConstraint(currentSaturation) - 3.5f.pixels()).minMax(2.pixels(), 2.pixels(true))
        y = (RelativeConstraint(1f - currentBrightness) - 3.5f.pixels()).minMax(2.pixels(), 2.pixels(true))
        width = 3.pixels()
        height = 3.pixels()
    } effect OutlineEffect(Color.WHITE, 1f)

    private val huePickerLine = UIBlock(OUTLINE_COLOR).constrain {
        x = RelativeConstraint(0.85f)
        width = FillConstraint()
        height = RelativeConstraint(if (allowAlpha) 0.8f else 1f)
    } childOf this

    private val hueIndicator = SVGComponent.ofResource("/vigilance/chevron-left.svg").constrain {
        x = (-7).pixels(true)
        y = RelativeConstraint(currentHue) - 7.5f.pixels()
        width = 15.pixels()
        height = 15.pixels()
    }

    private val alphaSlider = Slider(currentAlpha).constrain {
        x = RelativeConstraint(0.05f)
        y = RelativeConstraint(0.85f)
        width = RelativeConstraint(0.7f)
        height = FillConstraint()
    }

    private val alphaText = UIText(getFormattedAlpha()).constrain {
        x = RelativeConstraint(0.85f)
        y = CenterConstraint().to(alphaSlider) as YConstraint
        textScale = 0.5f.pixels()
        color = Color.WHITE.asConstraint()
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
            override fun draw() {
                drawHueLine()

                super.draw()
            }
        }).addChild(hueIndicator)

        huePickerLine.onMouseClick { event ->
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
            override fun draw() {
                drawColorPicker()

                super.draw()
            }
        }).addChild(pickerIndicator)

        bigPickerBox.onMouseClick { event ->
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
        hueIndicator.setY(RelativeConstraint(currentHue) - 7.5f.pixels())

        recalculateColor()
    }

    private fun updatePickerIndicator() {
        pickerIndicator.setX(
            (RelativeConstraint(currentSaturation) - 2.5f.pixels()).minMax(2.pixels(), 2.pixels(true))
        )
        pickerIndicator.setY(
            (RelativeConstraint(1f - currentBrightness) - 2.5f.pixels()).minMax(2.pixels(), 2.pixels(true))
        )

        recalculateColor()
    }

    private fun recalculateColor() {
        onValueChange(getCurrentColor())
    }

    fun getCurrentColor(): Color {
        return Color(
            (Color.HSBtoRGB(currentHue, currentSaturation, currentBrightness) and 0xffffff) or ((currentAlpha * 255f).toInt() shl 24),
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

    private fun drawColorPicker() {
        val left = (bigPickerBox.getLeft()).toDouble()
        val top = (bigPickerBox.getTop()).toDouble()
        val right = (bigPickerBox.getRight() - 2f).toDouble()
        val bottom = (bigPickerBox.getBottom() - 1f).toDouble()

        setupDraw()
        val graphics = UniversalGraphicsHandler.getFromTessellator()
        graphics.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)

        val height = bottom - top
        var first = true

        for (x in 1..50) {
            val curLeft = left + (right - left).toFloat() * x.toFloat() / 50f
            val curRight = left + (right - left).toFloat() * (x.toFloat() + 1) / 50f

            for (y in 1..50) {
                val yPos = top + (y.toFloat() * height / 50.0)
                val color = getColor(x.toFloat() / 50f, 1 - y.toFloat() / 50f, currentHue)

                if (!first) {
                    drawVertex(graphics, curLeft, yPos, color)
                    drawVertex(graphics, curRight, yPos, color)
                }

                drawVertex(graphics, curRight, yPos, color)
                drawVertex(graphics, curLeft, yPos, color)
                first = false
            }

        }

        cleanupDraw()
    }

    private fun getColor(x: Float, y: Float, hue: Float): Color {
        return Color(Color.HSBtoRGB(hue, x, y))
    }

    private fun drawHueLine() {
        val left = (huePickerLine.getLeft() + 1f).toDouble()
        val top = (huePickerLine.getTop() + 1f).toDouble()
        val right = (huePickerLine.getRight() - 1f).toDouble()
        val height = (huePickerLine.getHeight() - 1f).toDouble()

        setupDraw()
        val graphics = UniversalGraphicsHandler.getFromTessellator()

        graphics.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)

        var first = true
        for ((i, color) in hueColorList.withIndex()) {
            val yPos = top + (i.toFloat() * height / 50.0)
            if (!first) {
                drawVertex(graphics, left, yPos, color)
                drawVertex(graphics, right, yPos, color)
            }

            drawVertex(graphics, right, yPos, color)
            drawVertex(graphics, left, yPos, color)

            first = false
        }

        cleanupDraw()
    }

    private fun setupDraw() {
        UniversalGraphicsHandler.disableTexture2D()
        UniversalGraphicsHandler.enableBlend()
        UniversalGraphicsHandler.disableAlpha()
        UniversalGraphicsHandler.tryBlendFuncSeparate(770, 771, 1, 0)
        UniversalGraphicsHandler.shadeModel(7425)
    }

    private fun cleanupDraw() {
        UniversalGraphicsHandler.draw()
        UniversalGraphicsHandler.shadeModel(7424)
        UniversalGraphicsHandler.disableBlend()
        UniversalGraphicsHandler.enableAlpha()
        UniversalGraphicsHandler.enableTexture2D()
    }

    private fun drawVertex(graphics: UniversalGraphicsHandler, x: Double, y: Double, color: Color) {
        graphics
            .pos(x, y, 0.0)
            .color(color.red.toFloat() / 255f, color.green.toFloat() / 255f, color.blue.toFloat() / 255f, 1f)
            .endVertex()
    }

    private fun getFormattedAlpha(): String {
        return "%.2f".format(alphaSlider.getCurrentPercentage())
    }

    companion object {
        private val hueColorList: List<Color> = (0..49).map { i -> Color(Color.HSBtoRGB(i / 49f, 1f, 0.7f)) }
        private val OUTLINE_COLOR = Color(50, 59, 77)
    }
}