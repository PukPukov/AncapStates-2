package states.Listeners.AncapStatesEventsListeners;

import AncapLibrary.AncapEvents.AncapExplodeEvent;
import AncapLibrary.AncapEvents.AncapPVPEvent;
import AncapLibrary.AncapEvents.AncapWorldInteractEvent;
import AncapLibrary.AncapEvents.AncapWorldSelfDestructEvent;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import states.Main.AncapStates;
import states.Player.AncapStatesPlayer;

public class AncapStatesEventsListener implements Listener {

    @EventHandler
    public void onWorldInteract(AncapWorldInteractEvent e) {
        if (e.isIntercepted()) {
            return;
        }
        AncapStatesPlayer player = new AncapStatesPlayer(e.getPlayer().getName());
        Location loc = e.getLocation();
        if (!player.canInteract(loc)) {
            this.cancelInteract(e);
        }
    }

    private void cancelInteract(AncapWorldInteractEvent e) {
        e.setCancelled(true);
        this.removeProjectile(e);
    }

    private void removeProjectile(AncapWorldInteractEvent e) {
        Cancellable bukkitEvent = e.getBukkitEvent();
        if (bukkitEvent instanceof ProjectileHitEvent) {
            ((ProjectileHitEvent) bukkitEvent).getEntity().remove();
        }
    }

    @EventHandler
    public void onPVP(AncapPVPEvent e) {
        if (e.isIntercepted()) {
            return;
        }
        if (AncapStates.getCityMap().isAtCity(e.getLocations())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(AncapExplodeEvent e) {
        if (e.isIntercepted()) {
            return;
        }
        Location loc = e.getLocation();
        if (AncapStates.getCityMap().getCity(loc) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSelfDestruct(AncapWorldSelfDestructEvent e) {
        if (e.isIntercepted()) {
            return;
        }
        if (!AncapStates.getCityMap().isAtSameCity(e.getLocations())) {
            e.setCancelled(true);
        }
    }
}
