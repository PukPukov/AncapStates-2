package ru.ancap.states.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.city.RequestState;

@RequiredArgsConstructor
public class HexagonClaimEvent extends Event implements Cancellable {

    @Setter @Getter private boolean cancelled;
    
    @NotNull  private final Player               claimer;
    @NotNull  private final City                 city;
    @NotNull  private final Hexagon              hexagon;
              private final RequestState<Player> requestState;
              
    public Player               claimer      () { return this.claimer;      }
    public City                 city         () { return this.city;         }
    public Hexagon              hexagon      () { return this.hexagon;      }
    public RequestState<Player> requestState () { return this.requestState; }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return getHandlerList();}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}