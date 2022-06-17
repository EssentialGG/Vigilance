package gg.essential.vigilance.gui.common

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.USound
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.gui.common.shadow.ShadowIcon
import gg.essential.vigilance.utils.*
import gg.essential.vigilance.utils.ImageFactory
import gg.essential.vigilance.utils.and
import gg.essential.vigilance.utils.hoveredState
import java.awt.Color

internal class IconButton (
    imageFactory: State<ImageFactory>,
    tooltipText: State<String>,
    enabled: State<Boolean>,
    buttonText: State<String>,
    iconShadow: State<Boolean>,
    textShadow: State<Boolean>
) : UIBlock() {

    private val iconState = imageFactory.map { it }
    private val iconShadowState = iconShadow.map { it }
    private val tooltipState = tooltipText.map { it }
    private val enabledState = enabled.map { it }
    private val buttonTextState = buttonText.map { it }
    private val textShadowState = textShadow.map { it }

    constructor(
        imageFactory: ImageFactory,
        buttonText: String = "",
        tooltipText: String = "",
        buttonShadow: Boolean = true,
        textShadow: Boolean = true,
    ) : this(
        BasicState(imageFactory),
        BasicState(tooltipText),
        BasicState(true),
        BasicState(buttonText),
        BasicState(buttonShadow),
        BasicState(textShadow)
    )


    private val hovered = hoveredState()

    private val icon by ShadowIcon(iconState, iconShadowState).constrain {
        //x constraint set in init
        y = CenterConstraint()
    }.rebindPrimaryColor(VigilancePalette.getTextColor(hovered, enabledState)) childOf this

    private val tooltip = EssentialTooltip(this, belowComponent = true).constrain {
        x = CenterConstraint() boundTo this@IconButton
        y = SiblingConstraint(5f) boundTo this@IconButton
    }.bindVisibility(hovered and !tooltipText.empty())

    private val buttonText by UIText().bindText(buttonTextState)
        .bindShadow(textShadowState)
        .bindShadowColor(BasicState(VigilancePalette.getTextShadow()))
        .setColor(VigilancePalette.getTextColor(hovered, enabledState).toConstraint())
        .constrain {
            //Height is forced at 9 pixels because UIText will add 1 pixel for the shadow
            //As a result, the vertical alignment would be off for this specific use case
            height = 9.pixels
            x = SiblingConstraint(5f)
            y = CenterConstraint()
        }.bindParent(this, !buttonText.empty())

    init {
        setColor(VigilancePalette.getButtonColor(hovered, enabledState).toConstraint())

        constrain {
            width = ChildBasedSizeConstraint() + 10.pixels
            height = ChildBasedMaxSizeConstraint() + 10.pixels
        }
        onLeftClick {
            if (enabledState.get()) {
                USound.playButtonPress()
                it.stopPropagation()
            }
        }

        buttonTextState.empty().onSetValueAndNow {
            if (it) {
                icon.setX(CenterConstraint() - if (icon.getWidth() % 2f == 0f) 1.pixel else 0.pixels)
            } else {
                icon.setX(5.pixels)
            }
        }

        tooltip.bindLine(tooltipState)
    }

    fun rebindIcon(imageFactory: State<ImageFactory>): IconButton {
        iconState.rebind(imageFactory)
        return this
    }

    fun rebindTooltipText(tooltipText: State<String>): IconButton {
        tooltipState.rebind(tooltipText)
        return this
    }

    fun rebindEnabled(enabled: State<Boolean>): IconButton {
        enabledState.rebind(enabled)
        return this
    }

    fun rebindTextShadow(shadow: State<Boolean>): IconButton {
        textShadowState.rebind(shadow)
        return this
    }

    fun rebindIconColor(color: State<Color>): IconButton {
        icon.rebindPrimaryColor(color)
        return this
    }

    fun onActiveClick(action: () -> Unit): IconButton {
        onLeftClick {
            if (enabledState.get()) {
                action()
            }
        }
        return this
    }

}