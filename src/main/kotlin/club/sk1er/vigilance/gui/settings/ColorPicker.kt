package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.FillConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.YConstraint
import club.sk1er.elementa.dsl.asConstraint
import club.sk1er.elementa.dsl.childOf
import club.sk1er.elementa.dsl.constrain
import club.sk1er.elementa.dsl.pixels
import club.sk1er.mods.core.universal.UniversalGraphicsHandler
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color


class ColorPicker(initial: Color, allowAlpha: Boolean) : UIComponent() {
    private var currentHue = Color.RGBtoHSB(initial.red, initial.green, initial.blue, null)[0]
    private var onValueChange: (Color) -> Unit = { }

    private val bigPickerBox = UIBlock(OUTLINE_COLOR).constrain {
        width = RelativeConstraint(0.8f)
        height = RelativeConstraint(if (allowAlpha) 0.8f else 1f)
    } childOf this

    private val huePickerLine = UIBlock(OUTLINE_COLOR).constrain {
        x = RelativeConstraint(0.85f)
        width = FillConstraint()
        height = RelativeConstraint(if (allowAlpha) 0.8f else 1f)
    } childOf this

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
    }

    fun getCurrentColor(): Color {
        // TODO
        return Color.WHITE
    }

    fun onValueChange(listener: (Color) -> Unit) {
        onValueChange = listener
    }

    override fun draw() {
        drawHueLine()

        super.draw()
    }

    private fun drawHueLine() {
        val left = (huePickerLine.getLeft() + 1f).toDouble() - 5
        val top = (huePickerLine.getTop() + 1f).toDouble()
        val right = (huePickerLine.getRight() - 1f).toDouble() + 5
        val height = (huePickerLine.getHeight() - 2f).toDouble()
        UniversalGraphicsHandler.disableTexture2D()
        UniversalGraphicsHandler.enableBlend()
        // TODO: Make Universal
        GlStateManager.disableAlpha()
        UniversalGraphicsHandler.tryBlendFuncSeparate(770, 771, 1, 0)
        // TODO: Make Universal
        GlStateManager.shadeModel(7425)

        val graphics = UniversalGraphicsHandler.getFromTessellator()

        graphics.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)
        var first = true
        for ((i, color) in hueColorList.withIndex()) {
            val yPos = top + (i.toFloat() * height / 50.0)
            if (!first) {
                graphics
                        .pos(left, yPos, 0.0)
                        .color(color.red.toFloat() / 255f, color.green.toFloat() / 255f, color.blue.toFloat() / 255f, 1f)
                        .endVertex()

                graphics
                        .pos(right, yPos, 0.0)
                        .color(color.red.toFloat() / 255f, color.green.toFloat() / 255f, color.blue.toFloat() / 255f, 1f)
                        .endVertex()
            }
            graphics
                    .pos(right, yPos, 0.0)
                    .color(color.red.toFloat() / 255f, color.green.toFloat() / 255f, color.blue.toFloat() / 255f, 1f)
                    .endVertex()
            graphics
                    .pos(left, yPos, 0.0)
                    .color(color.red.toFloat() / 255f, color.green.toFloat() / 255f, color.blue.toFloat() / 255f, 1f)
                    .endVertex()

            first = false;

        }
        UniversalGraphicsHandler.draw()

        // TODO: Make Universal
        GlStateManager.shadeModel(7424)
        UniversalGraphicsHandler.disableBlend()
        UniversalGraphicsHandler.enableAlpha()
        UniversalGraphicsHandler.enableTexture2D()
    }

    private fun getFormattedAlpha(): String {
        return "%.2f".format(alphaSlider.getCurrentPercentage())
    }

    companion object {
        private val hueColorList: List<Color> = (0..49).map { i -> Color(Color.HSBtoRGB(i / 49f, 1f, 0.7f)) }
        private val OUTLINE_COLOR = Color(50, 59, 77)
    }
}