package gg.essential.vigilance.gui.settings

import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.animate
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.USound
import gg.essential.vigilance.utils.onLeftClick

abstract class AbstractSliderComponent : SettingComponent() {
    protected abstract val slider: Slider
    private var expanded = false
    private var mouseHeld = false

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedMaxSizeConstraint()
        }
    }

    protected fun sliderInit() {
        Window.enqueueRenderOperation {
            parent.onMouseEnter {
                slider.animate {
                    setWidthAnimation(Animations.OUT_EXP, .25f, 100.pixels())
                }
                expanded = true
            }.onMouseLeave {
                if (!mouseHeld) {
                    slider.animate {
                        setWidthAnimation(Animations.OUT_EXP, .25f, 60.pixels())
                    }
                    expanded = false
                }
            }
        }

        slider.onLeftClick {
            USound.playButtonPress()
            mouseHeld = true
        }.onMouseRelease {
            mouseHeld = false
            if (expanded && !slider.isHovered()) {
                animate {
                    setWidthAnimation(Animations.OUT_EXP, .25f, 60.pixels())
                }
                expanded = false
            }
        }
    }

    fun incrementBy(inc: Float) {
        slider.setCurrentPercentage(slider.getCurrentPercentage() + inc)
    }
}