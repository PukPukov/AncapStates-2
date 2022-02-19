package states.Wars.Listeners.TimerListeners;

import AncapLibrary.Timer.Heartbeat.AncapHeartbeatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import states.Wars.AncapWars.AncapWars;
import states.Wars.WarHexagons.WarHexagon;
import states.Wars.WarPlayers.AncapStatesWarrior;

public class HeartbeatListener implements Listener {

    @EventHandler
    public void onHeartbeat(AncapHeartbeatEvent e) {
        AncapStatesWarrior[] warriors = AncapWars.getOnlinePlayers();
        for (AncapStatesWarrior warrior : warriors) {
            if (new WarHexagon(warrior.getHexagon()).atWar()) {
                warrior.prepareToWar();
            }
        }
    }
}
