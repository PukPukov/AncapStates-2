package states.Listeners.AncapStatesEventsListeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import states.Main.AncapStates;
import states.Main.AncapStatesEvents.AncapStatesWorldInteractEvent;
import states.Player.AncapPlayer;

public class AncapStatesEventsListener implements Listener {

    @EventHandler
    public void onWorldInteract(AncapStatesWorldInteractEvent e) {
        if (e.isIntercepted()) {
            return;
        }
        AncapPlayer player = e.getPlayer();
        Location loc = e.getLocation();
        if (!AncapStates.canInteract(player, loc)) {
            e.setCancelled(true);
        }
    }
}
