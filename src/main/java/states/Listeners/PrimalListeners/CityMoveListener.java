package states.Listeners.PrimalListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import states.States.AncapStates;
import states.Player.AncapPlayer;
import states.Timer.Heartbeat.AncapHeartbeatEvent;

public class CityMoveListener implements Listener {

    @EventHandler
    public void onHeartbeat(AncapHeartbeatEvent e) {
        AncapPlayer[] players = AncapStates.getOnlinePlayers();
        for (AncapPlayer player : players) {
            player.checkCityMove();
        }
    }
}
