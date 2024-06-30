package ru.ancap.states.listeners.AncapStatesEventsListeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.ancap.framework.api.event.events.wrapper.PVPEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldInteractEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldSelfDestructEvent;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.states.AncapStates;
import ru.ancap.states.message.ErrorMessage;
import ru.ancap.states.player.AncapStatesPlayer;

public class ProtectListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void on(WorldInteractEvent event) {
        if (event.consumed()) return;
        else event.consume();
        AncapStatesPlayer player = AncapStatesPlayer.get(event.player());
        if (event.bukkit() instanceof PlayerInteractEvent) return;
        for (Location location : event.locations()) if (!player.canInteract(location)) {
            this.cancelInteract(event);
            CallableMessage message = ErrorMessage.REGION_PROTECTION_FORBIDDEN_ACTION;
            player.sendMessage(message);
        }
    }

    private void cancelInteract(WorldInteractEvent event) {
        event.setCancelled(true);
        this.removeProjectile(event);
    }

    private void removeProjectile(WorldInteractEvent event) {
        if (event.bukkit() instanceof ProjectileHitEvent hitEvent) hitEvent.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(PVPEvent event) {
        if (event.consumed()) return;
        else event.consume();
        for (Player attacked : event.attacked()) if (AncapStates.getCityMap().isAtCity(attacked.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(BlockExplodeEvent event) {
        if (AncapStates.getCityMap().getCity(event.getBlock().getLocation()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(EntityExplodeEvent event) {
        if (AncapStates.getCityMap().getCity(event.getEntity().getLocation()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(WorldSelfDestructEvent event) {
        if (event.consumed()) return;
        else event.consume();
        if (!AncapStates.getCityMap().isAtSameCity(event.active(), event.passive())) event.setCancelled(true);
    }
    
}