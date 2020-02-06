package club.sk1er.vigilance.gui

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.*
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.elementa.effects.StencilEffect
import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Category
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.awt.Color
import java.net.URL

class SettingsGui(private val config: Vigilant) : GuiScreen() {
    private val window = Window()
    private val categories = config.getCategories()

    private val search = UIContainer()
    private val searchInput = UITextInput("Search", false)

    init {
        StencilEffect.enableStencil()

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
        } effect ScissorEffect() childOf categories

        UIText("Settings").constrain {
            width = PixelConstraint("Settings".width() * 4f) max RelativeConstraint().to(categories)
            height = TextAspectConstraint()
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

        this.categories.map { GUICategory.fromCategoryData(it, settingsBox, window) }.forEach { it childOf categoryHolder }

        searchInput.constrain {
            x = 5.pixels()
            y = CenterConstraint()
            width = 100.pixels()
            height = 9.pixels()
        } as UITextInput
        searchInput.minWidth = 75.pixels()
        searchInput.maxWidth = 125.pixels()

        search.constrain {
            y = 5.pixels()
            x = 0.pixels(true)
            width = 20.pixels()
            height = 20.pixels()
        }.addChildren(
            // search icon
            UIContainer().constrain {
                width = 20.pixels()
                height = 20.pixels()
            }.addChild(
                UIImage.ofURL(URL("https://i.imgur.com/2jRqkVW.png")).constrain {
                    x = CenterConstraint()
                    y = CenterConstraint()
                    width = 15.pixels()
                    height = 15.pixels()
                }
            ),

            // text box
            UIRoundedRectangle(10f).constrain {
                x = SiblingConstraint()
                width = ChildBasedSizeConstraint() + 10.pixels()
                height = 20.pixels()
                color = Color(0, 0, 0, 175).asConstraint()
            }.addChild(searchInput) effect StencilEffect()
        ) childOf window

        search.onMouseClick { _, _, _ ->
            searchInput.active = true
            search.animate {
                setWidthAnimation(Animations.OUT_EXP, 1f, ChildBasedSizeConstraint() + 5.pixels())
            }
        }.onMouseEnter {
            if (searchInput.active) return@onMouseEnter
            search.animate {
                setWidthAnimation(Animations.OUT_EXP, 1f, 65.pixels())
            }
        }.onMouseLeave {
            if (searchInput.active) return@onMouseLeave
            search.animate {
                setWidthAnimation(Animations.OUT_EXP, 1f, 20.pixels())
            }
        }


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

        searchInput.onActivate { searchTerm ->
            // TODO: Display this search!
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        searchInput.active = false
        window.mouseClick(mouseX, mouseY, mouseButton)
        if (searchInput.active) return
        search.animate {
            setWidthAnimation(Animations.OUT_EXP, 1f, 20.pixels())
        }
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

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)
        window.keyType(typedChar, keyCode)
    }

    override fun initGui() {
        super.initGui()
        Keyboard.enableRepeatEvents(true)
    }

    override fun onGuiClosed() {
        super.onGuiClosed()
        Keyboard.enableRepeatEvents(false)
    }

    class GUICategory(string: String, settingsBox: UIComponent, window: Window) : UIBlock() {
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

        private val settingsBlock = ScrollComponent()

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

            settingsBlock.constrain {
                width = RelativeConstraint()
                height = RelativeConstraint()
            } childOf settingsBox
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
            setting childOf settingsBlock
        }

        companion object {
            fun fromCategoryData(category: Category, settingsBox: UIComponent, window: Window): GUICategory {
                val guiCat = GUICategory(category.name, settingsBox, window)

                category.items.forEach {
                    guiCat.addSetting(it.toSettingsObject())
                }

                return guiCat
            }
        }
    }
}