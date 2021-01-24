package club.sk1er.vigilance.gui.modal

import club.sk1er.elementa.components.*
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.transitions.RecursiveFadeInTransition
import club.sk1er.elementa.transitions.RecursiveFadeOutTransition
import club.sk1er.elementa.utils.withAlpha
import club.sk1er.vigilance.gui.HighlightedBlock
import club.sk1er.vigilance.gui.VigilancePalette

open class Modal : UIBlock(VigilancePalette.MODAL_BACKGROUND.withAlpha(150)) {
    protected var isAnimating = false

    protected val container = HighlightedBlock(
        backgroundColor = VigilancePalette.DARK_BACKGROUND,
        highlightColor = VigilancePalette.DARK_HIGHLIGHT,
        highlightHoverColor = VigilancePalette.ACCENT
    ).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
    } childOf this

    init {
        container.constrainBasedOnChildren()
        container.contentContainer.constrain {
            width = ChildBasedSizeConstraint() + 20.pixels()
            height = ChildBasedSizeConstraint() + 20.pixels()
        }

        onMouseClick { event ->
            if (!isAnimating && event.target == this)
                fadeOut()
        }

        onKeyType { _, keyCode ->
            if (!isAnimating && keyCode == 1) // Escape
                fadeOut()
        }
    }

    override fun afterInitialization() {
        constrain {
            x = 0.pixels() boundTo Window.of(this@Modal)
            y = 0.pixels() boundTo Window.of(this@Modal)
            width = 100.percent() boundTo Window.of(this@Modal)
            height = 100.percent() boundTo Window.of(this@Modal)
        }

        hide(instantly = true)
    }

    open fun fadeIn() {
        if (isAnimating)
            return

        isAnimating = true
        unhide()
        setFloating(true)
        RecursiveFadeInTransition(0.5f).transition(this) {
            isAnimating = false
        }
    }

    open fun fadeOut() {
        if (isAnimating)
            return

        isAnimating = true
        RecursiveFadeOutTransition(0.5f).transition(this) {
            hide(instantly = true)
            setFloating(false)
            isAnimating = false
        }
    }
}
