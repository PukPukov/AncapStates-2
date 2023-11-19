package ru.ancap.states.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.states.states.city.City;

@RequiredArgsConstructor
@Getter
public class CityDeleteEvent extends Event implements Cancellable {

    @Setter
    private boolean cancelled;
    private final City city;

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return getHandlerList();}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}
