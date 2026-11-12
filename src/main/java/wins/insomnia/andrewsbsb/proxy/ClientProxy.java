package wins.insomnia.andrewsbsb.proxy;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import wins.insomnia.andrewsbsb.proxy.event.EventManagerClient;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        registerProxy(new EventManagerClient());
    }

    @Override
    public boolean isClientProxy() {
        return true;
    }
}
