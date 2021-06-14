package gg.essential.vigilance.utils

import gg.essential.elementa.UIComponent
import gg.essential.elementa.events.UIClickEvent

inline fun UIComponent.onLeftClick(crossinline method: UIComponent.(event: UIClickEvent) -> Unit) = onMouseClick {
    if (it.mouseButton == 0) {
        this.method(it)
    }
}