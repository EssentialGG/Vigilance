package club.sk1er.vigilance.gui.modal

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.utils.invisible
import club.sk1er.vigilance.gui.VigilancePalette

class ConfirmationModal(
    confirmationText: String,
    private var onConfirmAction: () -> Unit = {}
) : Modal() {
    private val content = UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint() + 45.pixels()
    } childOf container

    private val confirmationTextComponent = UIText(confirmationText).constrain {
        x = CenterConstraint()
        y = 25.pixels()
        color = VigilancePalette.ACCENT.toConstraint()
    } childOf content

    private val confirmTextComponent = UIText("Confirm").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = TextAspectConstraint()
        color = VigilancePalette.DARK_TEXT.toConstraint()
    }

    private val confirmContainer = UIBlock(VigilancePalette.WARNING.invisible()).constrain {
        width = ChildBasedSizeConstraint() + 10.pixels()
        height = FillConstraint()
    }

    private val cancelContainer = UIBlock(VigilancePalette.HIGHLIGHT.invisible()).constrain {
        x = SiblingConstraint() + 5.pixels()
        width = ChildBasedSizeConstraint() + 10.pixels()
        height = FillConstraint()
    }

    private val cancelTextComponent = UIText("Cancel").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = TextAspectConstraint()
        color = VigilancePalette.DARK_TEXT.toConstraint()
    }

    init {
        val buttonContainer = UIContainer().constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + 20.pixels()
            width = ChildBasedSizeConstraint()
            height = 30.pixels()
        } childOf content

        confirmContainer.onMouseClick {
            onConfirmAction()
            fadeOut()
        }.onMouseEnter {
            if (isAnimating)
                return@onMouseEnter
            animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.WARNING.toConstraint())
            }
            confirmTextComponent.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.BRIGHT_TEXT.toConstraint())
            }
        }.onMouseLeave {
            if (isAnimating)
                return@onMouseLeave
            animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.WARNING.invisible().toConstraint())
            }
            confirmTextComponent.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.DARK_TEXT.toConstraint())
            }
        } as UIBlock childOf buttonContainer

        confirmTextComponent childOf confirmContainer

        cancelContainer.onMouseClick {
            fadeOut()
        }.onMouseEnter {
            if (isAnimating)
                return@onMouseEnter
            animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.HIGHLIGHT.toConstraint())
            }
            cancelTextComponent.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.BRIGHT_TEXT.toConstraint())
            }
        }.onMouseLeave {
            if (isAnimating)
                return@onMouseLeave
            animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.HIGHLIGHT.invisible().toConstraint())
            }
            cancelTextComponent.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.DARK_TEXT.toConstraint())
            }
        } as UIBlock childOf buttonContainer

        cancelTextComponent childOf cancelContainer
    }

    fun onConfirm(onConfirmAction: () -> Unit) = apply {
        this.onConfirmAction = onConfirmAction
    }
}
