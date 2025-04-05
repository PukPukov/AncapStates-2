package ru.ancap.states.listeners.TimerListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.ancap.framework.api.event.events.time.classic.FastTimerTenMinutesEvent;
import ru.ancap.framework.api.event.events.time.heartbeat.AncapHeartbeatEvent;
import ru.ancap.states.AncapStates;

public class TimerListener implements Listener {

    @EventHandler
    public void onCityMove(AncapHeartbeatEvent e) {
        if (!AncapStates.CONFIGURATION.getBoolean("fastTimers.secondTimer.cityMoveTimerEnabled")) {
            return;
        }
    }

    @EventHandler
    public void onTenMinutes(FastTimerTenMinutesEvent e) {
        
    }
    
}