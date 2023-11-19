package ru.ancap.states.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.states.states.Nation.Nation;

@RequiredArgsConstructor
@Getter
public class NationDeleteEvent extends Event implements Cancellable {

    @Setter
    private boolean cancelled;
    private final Nation nation;

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return getHandlerList();}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}
