package states.Listeners.TimerListeners;

import AncapLibrary.Timer.Heartbeat.AncapHeartbeatEvent;
import AncapLibrary.Timer.TimerEvents.FastTimerTenMinutesEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import states.Main.AncapStates;

public class TimerListener implements Listener {

    @EventHandler
    public void onCityMove(AncapHeartbeatEvent e) {
        if (!AncapStates.getConfiguration().isCityMoveTimerEnabled()) {
            return;
        }
        AncapStates.getPlayerMap().checkCityMove();
    }

    @EventHandler
    public void onTenMinutes(FastTimerTenMinutesEvent e) {

    }
}
