package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.components.UITextInput
import club.sk1er.elementa.components.UIWrappedText
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.vigilance.data.PropertyData
import net.minecraft.client.Minecraft
import java.awt.Color

// i have literally no idea what im doing.
class TextInputSetting(name: String, description: String, private val prop: PropertyData) : SettingObject(prop) {
    private val drawBox = UIBlock().constrain {
        height = ChildBasedSizeConstraint()
        width = RelativeConstraint()
        color = Color(0, 0, 0, 0).asConstraint()
    } childOf this

    private val title = UIText(name).constrain {
        x = 3.pixels()
        y = 3.pixels()
        width = PixelConstraint(Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) * 2f)
                .max(RelativeConstraint() - 6.pixels() as SizeConstraint)
        height = TextAspectConstraint()
        color = Color(255, 255, 255, 10).asConstraint()
    } childOf drawBox

    private val text = UIWrappedText(description).constrain {
        x = 3.pixels()
        y = 25.pixels()
        color = Color(255, 255, 255, 10).asConstraint()
        width = FillConstraint() - 50.pixels()
    } childOf drawBox

    private val textBox = UIBlock(Color(0, 0, 0, 0)).constrain {
        x = SiblingConstraint() - 80.pixels()
        y = SiblingConstraint() - 50.pixels()
        width = ChildBasedSizeConstraint() + 4.pixels()
        height = ChildBasedSizeConstraint() + 4.pixels()
    }.onMouseClick { _, _, _ ->
        textInput.active = true
    } effect ScissorEffect() childOf drawBox

    private val textInput = UITextInput(prop.getAsString(), wrapped = false, shadow = true).onUpdate { newValue ->
        prop.setValue(newValue)
    }

    init {
        textInput childOf textBox

        textInput.minWidth = 100.pixels()
        textInput.maxWidth = RelativeConstraint().to(drawBox) - 4.pixels() as WidthConstraint
        textInput.constrain {
            x = 2.pixels()
            y = CenterConstraint()
        }

        this.onMouseClick { _, _, _ ->
            textInput.active = false
        }

        textInput.setColor(Color(255, 255, 255, 10).asConstraint())
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
        textBox.animate {
            setYAnimation(Animations.OUT_EXP, 0.5f, 10.pixels())
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 0, 0, 200).asConstraint())
        }
        textInput.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.asConstraint()) }
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
        textBox.animate {
            setYAnimation(Animations.OUT_EXP, 0.5f, (-10).pixels())
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color(0, 0, 0, 0).asConstraint())
        }
        textInput.animate { setColorAnimation(Animations.OUT_EXP, 0.5f, Color(255, 255, 255, 10).asConstraint()) }
    }
}