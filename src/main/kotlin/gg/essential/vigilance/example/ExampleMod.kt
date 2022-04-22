package gg.essential.vigilance.example

import gg.essential.universal.UScreen
import gg.essential.vigilance.Vigilance

//#if FORGE
//#if MC<=11202
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
//#endif
//#else
//#if FABRIC
//$$ import net.fabricmc.api.ModInitializer
//$$ import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
//#endif
//#endif

//#if FABRIC
//$$ class ExampleMod : ModInitializer {
//#else
//#if MC<=11202
@Mod(modid = ExampleMod.MOD_ID, version = ExampleMod.MOD_VERSION)
//#else
//$$ @Mod(value = ExampleMod.MOD_ID)
//#endif
class ExampleMod {
    //#endif
    //#if FABRIC
    //$$ override fun onInitialize() {
    //$$     Vigilance.initialize()
    //$$     ExampleConfig.preload()
    //$$     ClientTickEvents.START_CLIENT_TICK.register { tick() }
    //$$ }
    //#else
    //#if MC<=11202
    @EventHandler
    fun init(event: FMLInitializationEvent) {
        Vigilance.initialize()
        ExampleConfig.preload()
        MinecraftForge.EVENT_BUS.register(this)
    }
    //#else
    //$$ init {
    //$$     Vigilance.initialize()
    //$$     ExampleConfig.preload()
    //$$     MinecraftForge.EVENT_BUS.register(this)
    //$$ }
    //#endif

    @SubscribeEvent
    fun tick(event: TickEvent.ClientTickEvent) = tick()
    //#endif

    private fun tick() {
        if (UScreen.currentScreen !is UScreen) {
            UScreen.displayScreen(ExampleConfig.gui())
        }
    }

    companion object {
        const val MOD_ID = "vigilance_examplemod"
        const val MOD_VERSION = "1.0"
    }
}
