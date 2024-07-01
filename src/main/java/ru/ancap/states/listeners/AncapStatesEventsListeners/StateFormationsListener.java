package ru.ancap.states.listeners.AncapStatesEventsListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.ancap.commons.Path;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIDomain;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.states.AncapStates;
import ru.ancap.states.event.events.HexagonClaimEvent;
import ru.ancap.states.event.events.HexagonOwnerChangeEvent;
import ru.ancap.states.message.LStateMessage;
import ru.ancap.states.message.StateMessage;
import ru.ancap.states.states.Subject;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.event.SubjectChangeAffiliationEvent;

public class StateFormationsListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(HexagonOwnerChangeEvent event) {
        City city = AncapStates.cityMap().getCity(event.hexagon());
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