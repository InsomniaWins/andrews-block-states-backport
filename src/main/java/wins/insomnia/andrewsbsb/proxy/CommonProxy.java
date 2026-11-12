package wins.insomnia.andrewsbsb.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import wins.insomnia.andrewsbsb.AndrewsBSB;
import wins.insomnia.andrewsbsb.event.EventHandler;
import wins.insomnia.andrewsbsb.proxy.event.EventManagerCommon;
import wins.insomnia.andrewsbsb.proxy.event.EventManagerProxy;

public class CommonProxy extends ModProxy {

    private final HashMap<String, ModProxy> proxyMap;

    public CommonProxy() {
        super("master");
        proxyMap = new HashMap<>();
    }

    protected void registerProxy(ModProxy proxy) {
        if (proxyMap.containsKey(proxy.getId())) {
            AndrewsBSB.LOG.warn("Overriding proxy '{}' with {}", proxy.getId(), proxy);
        }

        proxyMap.put(proxy.getId(), proxy);
    }

    public Optional<ModProxy> getProxy(String proxyId) {
        return Optional.ofNullable(proxyMap.get(proxyId));
    }

    public void preInit(FMLPreInitializationEvent event) {
        registerProxies();
        registerEventHandlers();
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}

    protected void registerProxies() {
        registerProxy(new EventManagerCommon());
    }

    protected void registerEventHandlers() {
        HashMap<String, EventHandler> eventHandlerMap = new HashMap<>();
        EventManagerProxy eventManagerProxy = (EventManagerProxy) (getProxy(EventManagerProxy.ID).orElse(null));
        if (eventManagerProxy != null) {
            eventManagerProxy.registerEventHandlers(eventHandlerMap);
        }

        for (Map.Entry<String, EventHandler> handlerEntry : eventHandlerMap.entrySet()) {
            EventHandler handler = handlerEntry.getValue();
            if (handler == null) continue;

            AndrewsBSB.LOG.info("Registering event handler '{}'", handler.getId());

            FMLCommonHandler.instance()
                .bus()
                .register(handler);
            MinecraftForge.EVENT_BUS.register(handler);
        }
    }

    @Override
    public boolean isClientProxy() {
        return false;
    }
}
