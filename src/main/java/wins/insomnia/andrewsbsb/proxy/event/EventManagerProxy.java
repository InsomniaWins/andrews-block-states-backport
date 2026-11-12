package wins.insomnia.andrewsbsb.proxy.event;

import java.util.HashMap;

import wins.insomnia.andrewsbsb.AndrewsBSB;
import wins.insomnia.andrewsbsb.event.EventHandler;
import wins.insomnia.andrewsbsb.proxy.ModProxy;

public abstract class EventManagerProxy extends ModProxy {

    public static final String ID = "event_manager";

    public EventManagerProxy() {
        super(ID);
    }

    protected void registerEventHandler(HashMap<String, EventHandler> handlerMap, EventHandler eventHandler) {
        if (handlerMap == null) {
            AndrewsBSB.LOG.error("Tried registering event handler '{}' to null handler map!", eventHandler);
            return;
        }

        if (eventHandler == null) {
            AndrewsBSB.LOG.error("Tried registering null event handler!");
            return;
        }

        handlerMap.put(eventHandler.getId(), eventHandler);
    }

    public abstract void registerEventHandlers(HashMap<String, EventHandler> handlerMap);

}
