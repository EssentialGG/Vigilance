package gg.essential.vigilance.command

import gg.essential.vigilance.Vigilance
import gg.essential.vigilance.VigilanceConfig

//#if MC<=11202
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

//#if MC>=11202
import net.minecraft.server.MinecraftServer
//#endif

class PaletteCommand : CommandBase() {
    //#if MC<=10809
    //$$ override fun getCommandName() = "vigilance"
    //$$
    //$$ override fun getCommandUsage(sender: ICommandSender?) = "/vigilance - opens Vigilance settings"
    //$$
    //$$ override fun getRequiredPermissionLevel() = 0
    //$$
    //$$ override fun processCommand(sender: ICommandSender?, args: Array<String>) {
    //$$     Vigilance.gui = VigilanceConfig.gui()
    //$$ }
    //#else
    override fun getName() = "vigilance"

    override fun getUsage(sender: ICommandSender) = "/vigilance - opens Vigilance settings"

    override fun getRequiredPermissionLevel() = 0

    override fun execute(server: MinecraftServer?, sender: ICommandSender, args: Array<String>) {
        Vigilance.gui = VigilanceConfig.gui()
    }
    //#endif
}
//#else
//$$ import com.mojang.brigadier.CommandDispatcher
//$$ import com.mojang.brigadier.context.CommandContext
//#if FABRIC
//$$ import net.minecraft.server.command.CommandSource
//$$ import net.minecraft.server.command.ServerCommandSource
//$$ import net.minecraft.server.command.CommandManager
//$$
//$$ object PaletteCommand {
//$$     fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
//$$         dispatcher.register(CommandManager.literal("vigilance")
//#else
//$$ import net.minecraft.command.CommandSource
//$$ import net.minecraft.command.Commands
//$$
//$$ object PaletteCommand {
//$$     fun register(dispatcher: CommandDispatcher<CommandSource?>) {
//$$         dispatcher.register(Commands.literal("vigilance")
//#endif
//$$             .requires { it.hasPermissionLevel(0) }
//$$             .executes {
//$$                 Vigilance.gui = VigilanceConfig.gui()
//$$                 1
//$$             })
//$$     }
//$$ }
//#endif
