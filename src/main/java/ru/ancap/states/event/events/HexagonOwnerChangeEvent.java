package ru.ancap.states.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.city.RequestState;

@RequiredArgsConstructor
public class HexagonOwnerChangeEvent extends Event implements Cancellable {

    @Setter @Getter private boolean cancelled;
    
    @NotNull  private final Hexagon hexagon;
    @Nullable private final City    newOwner;
              private final RequestState<Player> requestState;
    
    public Hexagon              hexagon      () { return this.hexagon;      }
    public City                 newOwner     () { return this.newOwner;     }
    public RequestState<Player> requestState () { return this.requestState; }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return getHandlerList();}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}
