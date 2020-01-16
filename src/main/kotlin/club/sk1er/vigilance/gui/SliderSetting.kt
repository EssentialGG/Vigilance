package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.CenterConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.PixelConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.components.Slider
import net.minecraft.client.Minecraft
import java.awt.Color

class SliderSetting(name: String, description: String, private val prop: PropertyData) : SettingObject() {
    private val drawBox = UIBlock().constrain {
        height = ChildBasedSizeConstraint() + 15.pixels()
        width = RelativeConstraint()
        color = Color(0, 0, 0, 0).asConstraint()
    } childOf this

    private val title = UIText(name).constrain {
        x = 3.pixels()
        y = 3.pixels()
        width = PixelConstraint(Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) * 2f)
        height = 18.pixels()
        color = Color(255, 255, 255, 10).asConstraint()
    } childOf drawBox

    private val text = UIText(description).constrain {
        x = 3.pixels()
        y = 25.pixels()
        color = Color(255, 255, 255, 10).asConstraint()
    } childOf drawBox

    private val slider = Slider(prop)

    private val minText = UIText(prop.property.min.toString()).constrain {
        x = RelativeConstraint(1.25f) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(prop.property.min.toString()).pixels()
        y = CenterConstraint()
    } childOf slider

    private val maxText = UIText(prop.property.max.toString()).constrain {
        x = RelativeConstraint(2.25f) + Minecraft.getMinecraft().fontRendererObj.getStringWidth(prop.property.max.toString()).pixels()
        y = CenterConstraint()
    } childOf slider

    private val currentText = (UIText(prop.getValue<Int>().toString()).constrain {
        y = CenterConstraint() + (10).pixels()
    } childOf slider) as UIText

    init {
        slider childOf drawBox
        onMouseDrag { mouseX, _, _ ->
            val tmp = prop.getValue<Int>()
            currentText.setText(tmp.toString())
            currentText.animate {
                setXAnimation(Animations.OUT_EXP, 0.5f, (mouseX - slider.knob.getRadius() / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(tmp.toString()) / 2).pixels().minMax(0.pixels(), 0.pixels(true)))
            }
        }
    }

    override fun animateIn() {
        super.animateIn()
        drawBox.constrain { y = 10.pixels() }
        drawBox.animate {
            setYAnimation(Animations.OUT_EXP, 0.5f, 0.pixels())
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 0, 0, 100).asConstraint())
        }
        title.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.asConstraint()) }
        text.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.asConstraint()) }
        slider.fadeIn()
    }

    override fun animateOut() {
        super.animateOut()
        drawBox.constrain { y = 0.pixels() }
        drawBox.animate {
            setYAnimation(Animations.OUT_EXP, 0.5f, (-10).pixels())
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 0, 0, 0).asConstraint())
        }
        title.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(255, 255, 255, 10).asConstraint()) }
        text.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(255, 255, 255, 10).asConstraint()) }
        slider.fadeOut()
    }
}