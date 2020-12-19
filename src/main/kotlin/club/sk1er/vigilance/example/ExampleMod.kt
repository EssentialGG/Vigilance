package club.sk1er.vigilance.example

import club.sk1er.elementa.effects.StencilEffect
import club.sk1er.mods.core.universal.UMinecraft
import club.sk1er.mods.core.universal.UScreen
import club.sk1er.vigilance.Vigilance

//#if FORGE
//#if MC<=11202
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
//#else
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent
//$$ import net.minecraftforge.event.TickEvent
//$$ import net.minecraftforge.common.MinecraftForge
//$$ import net.minecraftforge.fml.common.Mod
//$$
//#if MC>=11602
//$$ import net.minecraftforge.event.RegisterCommandsEvent
//#else
//$$ import net.minecraftforge.fml.event.server.FMLServerStartingEvent
//#endif
//#endif
//#else
//#if FABRIC
//$$ import net.fabricmc.api.ModInitializer
//$$ import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
//#endif
//#endif

//#if MC<=11202
@Mod(modid = ExampleMod.MOD_ID, version = ExampleMod.MOD_VERSION)
//#else
//$$ @Mod(value = ExampleMod.MOD_ID)
//#endif
//#if FABRIC
//$$ class ExampleMod implements ModInitializer {
//#else
class ExampleMod {
    //#endif
    //#if FABRIC
    //$$ override fun onInitialize() {
    //$$     Vigilance.initialize()
    //$$     ExampleConfig.preload()
    //$$     MinecraftForge.EVENT_BUS.register(this)
    //$$     ClientCommandHandler.instance.registerCommand(ExampleCommand())
    //$$ }
    //#else
    //#if MC<=11202
    @EventHandler
    fun init(event: FMLInitializationEvent) {
        Vigilance.initialize()
        ExampleConfig.preload()
        MinecraftForge.EVENT_BUS.register(this)
        ClientCommandHandler.instance.registerCommand(ExampleCommand())
    }
    //#else
    //$$ init {
    //$$     Vigilance.initialize()
    //$$     ExampleConfig.preload()
    //$$     MinecraftForge.EVENT_BUS.register(this)
    //$$ }
    //$$
    //#if MC>=11602
    //$$ @SubscribeEvent
    //$$ fun registerCommands(event: RegisterCommandsEvent) {
    //$$     ExampleCommand.register(event.dispatcher)
    //$$ }
    //#else
    //$$ @SubscribeEvent
    //$$ fun serverStarting(event: FMLServerStartingEvent) {
    //$$     ExampleCommand.register(event.commandDispatcher)
    //$$ }
    //#endif
    //#endif
    //#endif

    @SubscribeEvent
    fun tick(event: TickEvent.ClientTickEvent) {
        if (gui != null) {
            try {
                UMinecraft.getMinecraft().displayGuiScreen(gui)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            gui = null
        }
    }

    companion object {
        const val MOD_ID = "vigilance_examplemod"
        const val MOD_VERSION = "1.0"
        var gui: UScreen? = null
    }
}
