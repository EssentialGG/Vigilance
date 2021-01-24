package club.sk1er.vigilance.gui

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIRoundedRectangle
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.FillConstraint
import club.sk1er.elementa.constraints.RelativeConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.events.UIClickEvent
import java.awt.Color

open class HighlightedBlock(
    protected val backgroundColor: Color,
    protected val highlightColor: Color = backgroundColor,
    protected val backgroundHoverColor: Color = backgroundColor,
    protected val highlightHoverColor: Color = highlightColor,
    protected val blockRadius: Float = 0f,
    protected val outlineWidth: Float = 1f,
    protected val clickBehavior: ClickBehavior = ClickBehavior.None
) : UIContainer() {
    protected var clicked = false
    protected var clickAction: (UIClickEvent) -> Unit = {}

    val parentContainer = makeComponent(highlightColor).constrain {
        width = 100.percent()
        height = 100.percent()
    }

    val contentContainer = makeComponent(backgroundColor).constrain {
        x = outlineWidth.pixels()
        y = outlineWidth.pixels()
        width = 100.percent() - (outlineWidth * 2f).pixels()
        height = 100.percent() - (outlineWidth * 2f).pixels()
    } childOf parentContainer

    init {
        constrain {
            width = FillConstraint()
            height = FillConstraint()
        }

        super.addChild(parentContainer)

        onMouseEnter {
            highlight()
        }

        onMouseLeave {
            unhighlight()
        }

        onMouseClick {
            clickAction(it)
            clicked = true

            if (clickBehavior == ClickBehavior.Unhighlight) {
                unhighlight()
            }
        }
    }

    fun constrainBasedOnChildren() {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        parentContainer.constrain {
            width = ChildBasedSizeConstraint() + (outlineWidth * 2f).pixels()
            height = ChildBasedSizeConstraint() + (outlineWidth * 2f).pixels()
        }

        contentContainer.constrain {
            x = outlineWidth.pixels()
            y = outlineWidth.pixels()
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }
    }

    override fun addChild(component: UIComponent) = apply {
        component childOf contentContainer
    }

    fun onClick(action: (UIClickEvent) -> Unit) = apply {
        clickAction = action
    }

    protected open fun highlight() {
        parentContainer.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, highlightHoverColor.toConstraint())
        }
        contentContainer.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, backgroundHoverColor.toConstraint())
        }
    }

    protected open fun unhighlight() {
        if (clicked && clickBehavior == ClickBehavior.StayHighlighted)
            return

        parentContainer.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, highlightColor.toConstraint())
        }
        contentContainer.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, backgroundColor.toConstraint())
        }
    }

    private fun makeComponent(blockColor: Color) = if (blockRadius == 0f) {
        UIBlock(blockColor)
    } else UIRoundedRectangle(blockRadius).constrain {
        color = blockColor.toConstraint()
    }

    enum class ClickBehavior {
        Unhighlight,
        StayHighlighted,
        None,
    }
}
