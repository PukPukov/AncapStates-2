package ru.ancap.states.listeners.AncapStatesEventsListeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import ru.ancap.commons.Path;
import ru.ancap.framework.api.event.events.wrapper.PVPEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldInteractEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldSelfDestructEvent;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIDomain;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.states.AncapStates;
import ru.ancap.states.event.events.CityMoveEvent;
import ru.ancap.states.event.events.HexagonClaimEvent;
import ru.ancap.states.event.events.HexagonOwnerChangeEvent;
import ru.ancap.states.message.ErrorMessage;
import ru.ancap.states.message.LStateMessage;
import ru.ancap.states.message.StateMessage;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.Subject;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.event.SubjectChangeAffiliationEvent;

public class AncapStatesEventsListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void on(WorldInteractEvent event) {
        if (event.consumed()) return;
        else event.consume();
        AncapStatesPlayer player = AncapStatesPlayer.get(event.player());
        for (Location location : event.locations()) if (!player.canInteract(location)) {
            this.cancelInteract(event);
            CallableMessage message = ErrorMessage.CANT_INTERACT_THIS_BLOCK;
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

    @EventHandler
    public void on(CityMoveEvent event) {
        event.getPlayer().sendJoinTitle(event.getCity());
        event.getPlayer().online().setCollidable(event.getCity() != null);
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(HexagonOwnerChangeEvent event) {
        City city = AncapStates.getCityMap().getCity(event.hexagon());
        if (city != null) {
            city.removeHexagon(event.hexagon());
            city.sendMessage(LStateMessage.CITY_UNCLAIMED_HEXAGON(this.toReadable(event.hexagon())));
        }
        if (event.newOwner() != null) {
            event.newOwner().addHexagon(event.hexagon());
            event.newOwner().sendMessage(LStateMessage.CITY_CLAIMED_NEW_HEXAGON(this.toReadable(event.hexagon())));
        }
    }

    private String toReadable(Hexagon hexagon) {
        return hexagon.q()+";"+hexagon.r();
    }

    @EventHandler(ignoreCancelled = true)
    public void on(HexagonClaimEvent event) {
        if (new HexagonOwnerChangeEvent(event.hexagon(), event.city(), event.requestState()).callEvent()) {
            event.city().grabHexagonClaimingFee();
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void on(SubjectChangeAffiliationEvent event) {
        Subject affiliate = event.newAffiliate().orElse(event.subject().affiliate());
        String messageDomain = LAPIDomain.of(AncapStates.class, "state", "affiliation", event.subject().type()+"-"+affiliate.type(), event.changeType().toString());
        event.subject().sendMessage(new StateMessage(new LAPIMessage(Path.dot(messageDomain, "subject"),   new Placeholder("affiliate", affiliate      .simpleName()))));
        affiliate      .sendMessage(new StateMessage(new LAPIMessage(Path.dot(messageDomain, "affiliate"), new Placeholder("subject",   event.subject().simpleName()))));
        
        event.subject().affiliate(event.newAffiliate().orElse(null));
    }
    
}