package wins.insomnia.andrewsbsb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import wins.insomnia.andrewsbsb.proxy.CommonProxy;

@Mod(
    modid = AndrewsBSB.MODID,
    version = Tags.VERSION,
    name = "Andrew's Block States Backport",
    acceptedMinecraftVersions = "[1.7.10]")
public class AndrewsBSB {

    public static final String MODID = "andrewsbsb";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(
        clientSide = "wins.insomnia.andrewsbsb.proxy.ClientProxy",
        serverSide = "wins.insomnia.andrewsbsb.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
