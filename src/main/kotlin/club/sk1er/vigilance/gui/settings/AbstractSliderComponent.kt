package club.sk1er.vigilance.gui.settings

import club.sk1er.elementa.constraints.ChildBasedMaxSizeConstraint
import club.sk1er.elementa.constraints.ChildBasedSizeConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.animate
import club.sk1er.elementa.dsl.constrain
import club.sk1er.elementa.dsl.pixels

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
        slider.onMouseEnter {
            animate {
                setWidthAnimation(Animations.OUT_EXP, .25f, 100.pixels())
            }
            expanded = true
        }.onMouseLeave {
            if (!mouseHeld) {
                animate {
                    setWidthAnimation(Animations.OUT_EXP, .25f, 60.pixels())
                }
                expanded = false
            }
        }.onMouseClick {
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