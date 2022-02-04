package states.Listeners.AncapStatesEventsListeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import states.Main.AncapStates;
import states.Main.AncapStatesEvents.AncapStatesExplodeEvent;
import states.Main.AncapStatesEvents.AncapStatesPVPEvent;
import states.Main.AncapStatesEvents.AncapStatesWorldInteractEvent;
import states.Main.AncapStatesEvents.AncapStatesWorldSelfDestructEvent;
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

    @EventHandler
    public void onPVP(AncapStatesPVPEvent e) {
        if (e.isIntercepted()) {
            return;
        }
        if (AncapStates.getCityMap().isAtCity(e.getLocations())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(AncapStatesExplodeEvent e) {
        if (e.isIntercepted()) {
            return;
        }
        Location loc = e.getLocation();
        if (AncapStates.getCityMap().getCity(loc) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSelfDestruct(AncapStatesWorldSelfDestructEvent e) {
        if (e.isIntercepted()) {
            return;
        }
        if (!AncapStates.getCityMap().isAtSameCity(e.getLocations())) {
            e.setCancelled(true);
        }
    }
}
