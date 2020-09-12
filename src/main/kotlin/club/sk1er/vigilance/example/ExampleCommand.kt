package club.sk1er.vigilance.example

import club.sk1er.vigilance.gui.SettingsGui
import club.sk1er.vigilance.gui.VigilancePalette
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

//#if MC>10809
//$$ import net.minecraft.server.MinecraftServer;
//#endif

class ExampleCommand : CommandBase() {
    //#if MC<=10809
    override fun getCommandName() = "config"

    override fun getCommandUsage(sender: ICommandSender?) = "/config - open example gui"

    override fun getRequiredPermissionLevel() = 0

    override fun processCommand(sender: ICommandSender?, args: Array<String>) {
        // ExampleConfig.randomData = UUID.randomUUID().toString()
        ExampleMod.gui = VigilancePalette.gui()
    }
    //#else
    //$$ override fun getName() = "example"
    //$$
    //$$ override fun getUsage(sender: ICommandSender) = "/example - open example gui"
    //$$
    //$$ override fun getRequiredPermissionLevel() = 0
    //$$
    //$$ override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
    //$$     // ExampleConfig.randomData = UUID.randomUUID().toString()
    //$$     ExampleMod.gui = SettingsGui(ExampleConfig)
    //$$ }
    //#endif
}