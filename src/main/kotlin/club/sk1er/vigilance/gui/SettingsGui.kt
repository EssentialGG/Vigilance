package club.sk1er.vigilance.gui

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.components.Window
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.vigilance.data.Category
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Mouse
import java.awt.Color

class SettingsGui(private val categories: List<Category>) : GuiScreen() {
    private val window = Window()

    init {
        val categories = UIBlock().constrain {
            x = PixelConstraint(-window.getWidth() / 3)
            width = RelativeConstraint(1 / 3f)
            height = RelativeConstraint()
            color = Color(0, 0, 0, 150).asConstraint()
        } childOf window

        val categoryTitle = UIBlock().constrain {
            y = 10.pixels()
            x = CenterConstraint()
            width = 0.pixels()
            height = 36.pixels()
        }.enableEffects(ScissorEffect()) childOf categories

        UIText("Settings").constrain {
            width = MaxSizeConstraint(PixelConstraint("Settings".width() * 4f), RelativeConstraint().constrainTo(categories))
            height = 36.pixels()
        } childOf categoryTitle

        val categoryHolder = UIBlock().constrain {
            x = CenterConstraint()
            y = 50.pixels()
            width = RelativeConstraint(0.9f)
            height = ChildBasedSizeConstraint()
        } childOf categories

        val settingsBox = UIBlock().constrain {
            x = PixelConstraint(-window.getWidth() * 2 / 3, true)
            width = RelativeConstraint(2 / 3f)
            height = RelativeConstraint()
            color = Color(0, 0, 0, 100).asConstraint()
        } childOf window

        this.categories.map { GUICategory.fromCategoryData(it, settingsBox) }.forEach { it childOf categoryHolder }

        ////////////////
        // ANIMATIONS //
        ////////////////

        settingsBox.animate { setXAnimation(Animations.OUT_EXP, 0.5f, 0.pixels(true)) }

        categories.animate {
            setXAnimation(Animations.OUT_EXP, 0.5f, 0.pixels())

            onComplete {
                categoryTitle.animate { setWidthAnimation(Animations.OUT_EXP, 0.5f, ChildBasedSizeConstraint()) }
                categoryHolder.children.forEachIndexed { index, uiComponent ->
                    uiComponent.animate {
                        setWidthAnimation(Animations.OUT_QUAD, 0.5f, RelativeConstraint(), delay = 0.1f * index)
                        setXAnimation(Animations.OUT_EXP, 0.5f, 0.pixels(), delay = 0.1f * index)
                    }
                }
            }
        }

        categoryHolder.childrenOfType<GUICategory>().first().select()
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        window.mouseClick(mouseX, mouseY, mouseButton)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)
        window.mouseRelease()
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
        window.mouseDrag(mouseX, mouseY, clickedMouseButton)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        window.draw()
    }

    override fun handleMouseInput() {
        super.handleMouseInput()
        val delta = Mouse.getEventDWheel().coerceIn(-1, 1)
        window.mouseScroll(delta)
    }

    class GUICategory(string: String, settingsBox: UIComponent) : UIBlock() {
        private val settings = mutableListOf<SettingObject>()
        var selected = false

        private val text = UIText(string).constrain {
            width = PixelConstraint(Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) * 2f)
            height = 18.pixels()
        } childOf this

        private val selBlock = UIBlock().constrain {
            color = Color(255, 255, 255, 255).asConstraint()
            y = SiblingConstraint()
            height = 2.pixels()
        } childOf this

        private val settingsBlock = SettingsBlock().constrain {
            width = RelativeConstraint()
            height = RelativeConstraint()
        } childOf settingsBox

        init {
            setY(SiblingConstraint())
            setX((-10).pixels())
            setHeight(ChildBasedSizeConstraint() + 8.pixels())

            enableEffects(ScissorEffect())

            onMouseEnter {
                text.animate { setXAnimation(Animations.OUT_EXP, 0.5f, 10.pixels()) }
            }

            onMouseLeave {
                text.animate { setXAnimation(Animations.OUT_BOUNCE, 0.5f, 0.pixels()) }
            }

            onMouseClick { _, _, _ ->
                parent.children.forEach {
                    it as GUICategory
                    if (it == this && !it.selected) select()
                    else if (it != this && it.selected) it.deselect()
                }
            }
        }

        fun select() = apply {
            selected = true
            selBlock.animate { setWidthAnimation(Animations.OUT_EXP, 0.5f, RelativeConstraint()) }
            settings.forEach { it.animateIn() }
        }

        fun deselect() = apply {
            selected = false
            selBlock.animate { setWidthAnimation(Animations.OUT_EXP, 0.5f, 0.pixels()) }
            settings.forEach { it.animateOut() }
        }

        fun addSetting(setting: SettingObject) = apply {
            settings.add(setting)
            settingsBlock.addChild(setting)
        }

        companion object {
            fun fromCategoryData(category: Category, settingsBox: UIComponent): GUICategory {
                val guiCat = GUICategory(category.name, settingsBox)

                category.items.forEach {
                    guiCat.addSetting(it.toSettingsObject())
                }

                return guiCat
            }
        }
    }

    private class SettingsBlock : UIComponent() {
        var scrolled = 0

        init {
            onMouseScroll(::scroll)
        }

        private fun scroll(delta: Int) {
            if (delta == 0 && !(children.first() as SettingObject).selected) return
            scrolled += delta * 50

            if (scrolled <= 0) {
                children.first().animate {
                    setYAnimation(Animations.OUT_EXP, 0.5f, scrolled.pixels())
                }
            } else {
                children.first().animate {
                    setYAnimation(Animations.OUT_EXP, 0.3f, (scrolled / 4).pixels())
                }
            }

            if (scrolled > 0) {
                scrolled = 0
                children.first().animate {
                    setYAnimation(Animations.OUT_BOUNCE, 0.5f, 0.pixels(), delay = 0.1f)
                }
            }
        }
    }
}