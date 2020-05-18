package club.sk1er.vigilance.gui

import club.sk1er.vigilance.data.PropertyData

abstract class DataBackedSetting(private val data: PropertyData) : Setting() {
}