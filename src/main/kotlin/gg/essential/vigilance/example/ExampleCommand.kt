package gg.essential.vigilance.example

//#if MC<=11202
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer

class ExampleCommand : CommandBase() {
    //#if MC<=10809
    //$$ override fun getCommandName() = "config"
    //$$
    //$$ override fun getCommandUsage(sender: ICommandSender?) = "/config - open example gui"
    //$$
    //$$ override fun getRequiredPermissionLevel() = 0
    //$$
    //$$ override fun processCommand(sender: ICommandSender?, args: Array<String>) {
    //$$     ExampleMod.gui = ExampleConfig.gui()
    //$$ }
    //#else
    override fun getName() = "config"

    override fun getUsage(sender: ICommandSender) = "/config - open example gui"

    override fun getRequiredPermissionLevel() = 0

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        // ExampleConfig.randomData = UUID.randomUUID().toString()
        ExampleMod.gui = ExampleConfig.gui()
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
//$$ object ExampleCommand {
//$$     fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
//$$         dispatcher.register(CommandManager.literal("config")
//#else
//$$ import net.minecraft.command.CommandSource
//$$ import net.minecraft.command.Commands
//$$
//$$ object ExampleCommand {
//$$     fun register(dispatcher: CommandDispatcher<CommandSource?>) {
//$$         dispatcher.register(Commands.literal("config")
//#endif
//$$             .requires { it.hasPermissionLevel(0) }
//$$             .executes {
//$$                 // ExampleConfig.randomData = UUID.randomUUID().toString()
//$$                 ExampleMod.gui = ExampleConfig.gui()
//$$                 1
//$$             })
//$$     }
//$$ }
//#endif
