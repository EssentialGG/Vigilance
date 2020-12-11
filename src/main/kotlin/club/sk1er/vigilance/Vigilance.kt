package club.sk1er.vigilance

import club.sk1er.elementa.effects.StencilEffect
import club.sk1er.mods.core.universal.UniversalMinecraft
import club.sk1er.mods.core.universal.UniversalScreen
import club.sk1er.vigilance.command.PaletteCommand
import club.sk1er.vigilance.gui.VigilancePalette

//#if FORGE
//#if MC<=11202
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
//#else
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent
//$$ import net.minecraftforge.event.TickEvent
//$$ import net.minecraftforge.common.MinecraftForge
//$$
//#if MC>=11602
//$$ import net.minecraftforge.event.RegisterCommandsEvent
//#else
//$$ import net.minecraftforge.fml.event.server.FMLServerStartingEvent
//#endif
//#endif
//#else
//#if FABRIC
//$$ import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
//#endif
//#endif

object Vigilance {
    private var initialized = false
    internal var gui: UniversalScreen? = null

    @JvmStatic
    fun initialize() {
        if (initialized)
            return

        initialized = true
        StencilEffect.enableStencil()
        VigilancePalette.preload()
        MinecraftForge.EVENT_BUS.register(this)
        //#if FABRIC || MC<=11202
        ClientCommandHandler.instance.registerCommand(PaletteCommand())
        //#endif
    }

    //#if FORGE
    //#if MC>=11602
    //$$ @SubscribeEvent
    //$$ fun registerCommands(event: RegisterCommandsEvent) {
    //$$     PaletteCommand.register(event.dispatcher)
    //$$ }
    //#elseif MC>=11502
    //$$ @SubscribeEvent
    //$$ fun serverStarting(event: FMLServerStartingEvent) {
    //$$     PaletteCommand.register(event.commandDispatcher)
    //$$ }
    //#endif
    //#endif

    @SubscribeEvent
    fun tick(event: TickEvent.ClientTickEvent) {
        if (gui != null) {
            try {
                UniversalMinecraft.getMinecraft().displayGuiScreen(gui)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            gui = null
        }
    }
}
