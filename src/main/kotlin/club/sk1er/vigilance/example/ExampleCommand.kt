package club.sk1er.vigilance.example

import club.sk1er.vigilance.gui.SettingsGui
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import java.util.*

class ExampleCommand : CommandBase() {
    override fun getCommandName() = "config"

    override fun getCommandUsage(sender: ICommandSender?) = "/config - open example gui"

    override fun getRequiredPermissionLevel() = 0

    override fun processCommand(sender: ICommandSender?, args: Array<String>) {
        ExampleConfig.randomData = UUID.randomUUID().toString()
        ExampleMod.gui = SettingsGui(ExampleConfig)
    }
}