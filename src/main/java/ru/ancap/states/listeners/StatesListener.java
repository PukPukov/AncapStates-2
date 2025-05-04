package ru.ancap.states.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.ancap.commons.debug.AncapDebug;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.states.AncapStates;
import ru.ancap.states.event.events.CityDeleteEvent;
import ru.ancap.states.event.events.CityFoundEvent;
import ru.ancap.states.event.events.NationDeleteEvent;
import ru.ancap.states.event.events.NationFoundEvent;
import ru.ancap.states.message.LStateMessage;

public class StatesListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void on(CityFoundEvent event) {
        event.getHost().initialize(event.getCreator(), event.getName());
        for (Hexagon hexagon : event.getHost().getHomeHexagon().neighbors(1)){
            if (AncapStates.cityMap().getCity(hexagon) != null) continue;
            event.getHost().addHexagon(hexagon);
            break; // claim only one
        }
        CallableMessage message = LStateMessage.CITY_CREATE(event.getCreator().getName(), event.getName());
        AncapStates.sendMessage(message);
    }

    @EventHandler(ignoreCancelled = true)
    public void on(CityDeleteEvent event) {
        CallableMessage message = LStateMessage.CITY_REMOVE(event.getCity().getName());
        event.getCity().delete();
        AncapStates.sendMessage(message);
    }

    @EventHandler(ignoreCancelled = true)
    public void on(NationFoundEvent event) {
        event.getHost().initialize(event.getCreator(), event.getName());
        CallableMessage message = LStateMessage.NATION_CREATE(event.getCreator().getName(), event.getName());
        AncapStates.sendMessage(message);
    }

    @EventHandler(ignoreCancelled = true)
    public void on(NationDeleteEvent event) {
        String name = event.getNation().getName();
        AncapDebug.debug("nation id", event.getNation().getID());
        AncapDebug.debug("exists", event.getNation().exists());
        AncapDebug.debug("cities", event.getNation().getCities());
        AncapDebug.debug("name", name);
        CallableMessage message = LStateMessage.NATION_REMOVE(event.getNation().getName());
        AncapStates.sendMessage(message);
        event.getNation().delete();
    }
    
}