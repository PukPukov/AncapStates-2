package ru.ancap.states.event.events.move;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.city.City;

@Getter
@RequiredArgsConstructor
public class CityMoveEvent extends Event {
    
    private final @NotNull AncapStatesPlayer player;
    private final @Nullable City from;
    private final @Nullable City to;

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return getHandlerList();}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}