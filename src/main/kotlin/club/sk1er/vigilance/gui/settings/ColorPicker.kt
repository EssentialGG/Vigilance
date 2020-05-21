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
import club.sk1er.mods.core.universal.UniversalGraphicsHandler
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color

class ColorPicker(initial: Color, allowAlpha: Boolean) : UIContainer() {
    private var currentHue = Color.RGBtoHSB(initial.red, initial.green, initial.blue, null)[0]
    private var onValueChange: (Color) -> Unit = { }

    private var draggingHue = false

    private val bigPickerBox = UIBlock(OUTLINE_COLOR).constrain {
        width = RelativeConstraint(0.8f)
        height = RelativeConstraint(if (allowAlpha) 0.8f else 1f)
    } childOf this

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

    private val alphaSlider = Slider(initial.alpha / 255f).constrain {
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

            alphaSlider.onValueChange {
                // TODO
                alphaText.setText(getFormattedAlpha())
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
        })
    }

    private fun updateHueIndicator() {
        hueIndicator.setY(RelativeConstraint(currentHue) - 7.5f.pixels())
    }

    fun getCurrentColor(): Color {
        // TODO
        return Color.WHITE
    }

    fun onValueChange(listener: (Color) -> Unit) {
        onValueChange = listener
    }

    private fun drawColorPicker() {
        val left = (bigPickerBox.getLeft() + 1f).toDouble()
        val top = (bigPickerBox.getTop() + 1f).toDouble()
        val right = (bigPickerBox.getRight() - 1f).toDouble()
        val bottom = (bigPickerBox.getBottom() - 1f).toDouble()

        setupDraw()
        val graphics = UniversalGraphicsHandler.getFromTessellator()
        graphics.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)

        val currentHueColor = Color(Color.HSBtoRGB(currentHue, 1f, 0.5f))

        drawVertex(graphics, right, top, currentHueColor)
        drawVertex(graphics, left, top, Color.WHITE)
        drawVertex(graphics, left, bottom, Color.BLACK)
        drawVertex(graphics, right, bottom, Color.BLACK)

        cleanupDraw()
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
        // TODO: Make Universal
        GlStateManager.disableAlpha()
        UniversalGraphicsHandler.tryBlendFuncSeparate(770, 771, 1, 0)
        // TODO: Make Universal
        GlStateManager.shadeModel(7425)
    }

    private fun cleanupDraw() {
        UniversalGraphicsHandler.draw()

        // TODO: Make Universal
        GlStateManager.shadeModel(7424)
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