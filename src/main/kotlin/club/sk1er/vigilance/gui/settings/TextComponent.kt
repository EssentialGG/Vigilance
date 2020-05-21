package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UITextInput
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.vigilance.gui.SettingsGui
import net.minecraft.client.Minecraft
import java.awt.Color

class TextComponent(initial: String, placeholder: String, wrap: Boolean) : SettingComponent() {
    private val textHolder = UIBlock(Color(33, 34, 38)).constrain {
        width = ChildBasedSizeConstraint() + 6.pixels()
        height = ChildBasedSizeConstraint() + 6.pixels()
    } childOf this effect OutlineEffect(Color(80, 80, 80), 0.5f)

    private val textInput = UITextInput(placeholder, wrapped = wrap).constrain {
        x = 3.pixels()
        y = 3.pixels()
        width = 50.pixels()
    } childOf textHolder effect ScissorEffect()

    init {
        textInput.maxWidth = basicWidthConstraint { this@TextComponent.parent.getWidth() / 2 }
        textInput.minWidth = 50.pixels()
        textInput.text = initial
        textInput.onUpdate { newText ->
            changeValue(newText)
        }

        textInput.onMouseClick { event ->
            event.stopPropagation()
            (Minecraft.getMinecraft().currentScreen as? SettingsGui)?.hideSearch()

            textInput.active = true
        }

        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }
    }

    override fun closePopups() {
        textInput.active = false
    }
}