package club.sk1er.vigilance.gui

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.components.UIWrappedText
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.vigilance.data.PropertyData
import club.sk1er.vigilance.gui.components.Toggle
import net.minecraft.client.Minecraft
import java.awt.Color

class ToggleSetting(name: String, description: String, prop: PropertyData) : SettingObject(prop) {
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

        // TODO: This doesn't work, how can we scale the height appropriately so the text doesn't look really squished?
        height = TextAspectConstraint()
        color = Color(255, 255, 255, 10).asConstraint()
    } childOf drawBox

    private val text = UIWrappedText(description).constrain {
        x = 3.pixels()
        y = 25.pixels()
        width = FillConstraint() - 50.pixels()
        color = Color(255, 255, 255, 10).asConstraint()
    } childOf drawBox

    private val toggle = Toggle(prop.getAsBoolean()).onUpdate { newValue ->
        prop.setValue(newValue)
    }

    init {
        toggle childOf drawBox
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
        toggle.fadeIn()
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
        toggle.fadeOut()
    }
}