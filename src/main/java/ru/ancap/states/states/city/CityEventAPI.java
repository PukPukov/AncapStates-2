package ru.ancap.states.states.city;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.states.event.events.HexagonClaimEvent;
import ru.ancap.states.event.events.HexagonOwnerChangeEvent;
import ru.ancap.states.states.Nation.Nation;
import ru.ancap.states.states.event.SubjectChangeAffiliationEvent;

@AllArgsConstructor
public class CityEventAPI {
    
    private final City city;
    
    public void remove(Hexagon hexagon, RequestState<Player> requestState) {
        new HexagonOwnerChangeEvent(hexagon, null, requestState).callEvent();
    }
    
    public void add(Hexagon hexagon, RequestState<Player> requestState) {
        new HexagonOwnerChangeEvent(hexagon, this.city, requestState).callEvent();
    }

    public void claim(Player claimer, Hexagon hexagon, RequestState<Player> requestState) {
        new HexagonClaimEvent(claimer, this.city, hexagon, requestState).callEvent();
    }
    
    public void affiliate(@Nullable Nation nation, RequestState<Player> requestState) {
        new SubjectChangeAffiliationEvent(this.city, nation, requestState).callEvent();
    }
    
}