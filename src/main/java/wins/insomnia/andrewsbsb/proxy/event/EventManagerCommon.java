package wins.insomnia.andrewsbsb.proxy.event;

import java.util.HashMap;

import wins.insomnia.andrewsbsb.event.EventHandler;
import wins.insomnia.andrewsbsb.event.handler.BlockStateHandler;

public class EventManagerCommon extends EventManagerProxy {

    @Override
    public void registerEventHandlers(HashMap<String, EventHandler> handlerMap) {
        registerEventHandler(handlerMap, new BlockStateHandler());
    }

    @Override
    public boolean isClientProxy() {
        return false;
    }
}
