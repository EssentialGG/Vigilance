package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.gui.components.Button
import club.sk1er.vigilance.gui.components.DropDown
import net.minecraft.client.Minecraft
import java.awt.Color

class SelectSetting(name: String, description: String, selected: Int, selections: List<String>) : SettingObject() {
    private var opened = false

    override fun getHeight(): Float {
        return drawBox.getHeight() + 5
    }

    override fun getBottom() = getTop() + getHeight()

    private val drawBox = UIBlock().constrain {
        height = ChildBasedSizeConstraint()
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

    private val button = Button(selections[selected], Button.RECTANGLE_TRANSPARENT)


    private val dropDown = DropDown()

    init {
        button.onClick {
            opened = !opened
            if (opened) dropDown.open() else dropDown.close()
        }.constrain {
            x = 5.pixels(true)
            y = CenterConstraint()
            width = 50.pixels()
            height = 20.pixels()
        } childOf drawBox

        dropDown.constrain {
            x = SiblingConstraint()
            y = CenterConstraint()
        } childOf drawBox
        selections.forEach {
            //dropDown.addElement(Button(it, Button.RECTANGLE_GRAY))
            dropDown.addElement(UIText(it))
        }
    }

    override fun animateIn() {
        super.animateIn()
        drawBox.constrain { y = 10.pixels() }
        drawBox.animate {
            setYAnimation(Animations.OUT_EXP, 0.5f, 0.pixels())
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 0, 0, 100).asConstraint())
        }
        title.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.asConstraint())}
        text.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.asConstraint()) }
        button.fadeIn()
    }

    override fun animateOut() {
        super.animateOut()
        drawBox.constrain { y = 0.pixels() }
        drawBox.animate {
            setYAnimation(Animations.OUT_EXP, 0.5f, (-10).pixels())
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 0, 0, 0).asConstraint())
        }
        title.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(255, 255, 255, 10).asConstraint())}
        text.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(255, 255, 255, 10).asConstraint()) }
        button.fadeOut()
    }
}