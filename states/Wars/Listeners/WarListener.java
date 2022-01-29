package states.Wars.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import states.States.AncapStates;
import states.Player.AncapPlayer;
import states.Timer.Heartbeat.AncapHeartbeatEvent;
import states.Wars.AncapWars.AncapWars;
import states.Wars.WarPlayers.AncapWarrior;

public class WarListener implements Listener {

    @EventHandler
    public void onHeartbeat(AncapHeartbeatEvent e) {
        AncapPlayer[] players = AncapStates.getOnlinePlayers();
        for (AncapPlayer player : players) {
            if (AncapWars.isAtWar(player.getHexagon())) {
                AncapWarrior warrior = new AncapWarrior(player.getID());
                warrior.prepareToWar();
            }
        }
    }
}
