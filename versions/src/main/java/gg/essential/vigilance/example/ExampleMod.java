package gg.essential.vigilance.example;

import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.effects.StencilEffect;
import gg.essential.universal.UScreen;
import gg.essential.vigilance.Vigilance;
import gg.essential.vigilance.example.ExampleConfig;

//#if FORGE
//#if MC<=11202
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
//#else
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent;
//$$ import net.minecraftforge.event.TickEvent;
//$$ import net.minecraftforge.common.MinecraftForge;
//$$ import net.minecraftforge.fml.common.Mod;
//$$
//#endif
//#else
//#if FABRIC
//$$ import net.fabricmc.api.ClientModInitializer;
//$$ import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//#endif
//#endif

//#if FABRIC
//$$ public class ExampleMod implements ClientModInitializer {
//#else
//#if MC<=11202
@Mod(modid = ExampleMod.MOD_ID, version = ExampleMod.MOD_VERSION)
//#else
//$$ @Mod(value = ExampleMod.MOD_ID)
//#endif
public class ExampleMod {
//#endif

    public static final String MOD_ID = "examplemod";
    public static final String MOD_VERSION = "1.0";

    //#if FABRIC
    //$$ public void onInitializeClient() {
    //$$     init();
    //$$     ClientTickEvents.START_CLIENT_TICK.register(it -> tick());
    //$$ }
    //#else
    //#if MC<=11202
    @EventHandler
    public void init(FMLInitializationEvent event) {
        init();
        MinecraftForge.EVENT_BUS.register(this);
    }
    //#else
    //$$ {
    //$$     init();
    //$$     MinecraftForge.EVENT_BUS.register(this);
    //$$ }
    //$$
    //#endif

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) { tick(); }
    //#endif

    private void init() {
        Vigilance.initialize();
        ExampleConfig.INSTANCE.preload();
        StencilEffect.enableStencil();
    }

    private void tick() {
        if (!(UScreen.getCurrentScreen() instanceof UScreen)) {
            UScreen.displayScreen((WindowScreen) ExampleConfig.INSTANCE.gui());
        }
    }
}
