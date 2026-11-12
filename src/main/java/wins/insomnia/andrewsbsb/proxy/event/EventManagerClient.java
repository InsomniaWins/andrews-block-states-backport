package wins.insomnia.andrewsbsb.proxy.event;

import java.util.HashMap;

import wins.insomnia.andrewsbsb.event.EventHandler;

public class EventManagerClient extends EventManagerCommon {

    @Override
    public void registerEventHandlers(HashMap<String, EventHandler> handlerMap) {

    }

    @Override
    public boolean isClientProxy() {
        return false;
    }
}
