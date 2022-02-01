package states.Wars.Listeners.TimerListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import states.Timer.Heartbeat.AncapHeartbeatEvent;
import states.Wars.AncapWars.AncapWars;
import states.Wars.WarHexagons.WarHexagon;
import states.Wars.WarPlayers.AncapWarrior;

public class HeartbeatListener implements Listener {

    @EventHandler
    public void onHeartbeat(AncapHeartbeatEvent e) {
        AncapWarrior[] warriors = AncapWars.getOnlinePlayers();
        for (AncapWarrior warrior : warriors) {
            if (new WarHexagon(warrior.getHexagon()).atWar()) {
                warrior.prepareToWar();
            }
        }
    }
}
