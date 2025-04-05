package ru.ancap.states.listeners.AncapStatesEventsListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.ancap.states.event.events.move.CityMoveEvent;

public class CityMoveListener implements Listener {
    
    @EventHandler
    public void on(CityMoveEvent event) {
        event.getPlayer().sendJoinTitle(event.getTo());
    }
    
}