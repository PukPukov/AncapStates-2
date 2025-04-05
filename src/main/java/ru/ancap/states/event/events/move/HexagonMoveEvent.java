package ru.ancap.states.event.events.move;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import ru.ancap.hexagon.Hexagon;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class HexagonMoveEvent extends Event implements Cancellable {
    
    private final PlayerMoveEvent move;
    @Delegate @Getter(AccessLevel.PRIVATE)
    private final Cancellable moveCancellable;
    
    private final Hexagon from;
    private final Hexagon to;
    
    public HexagonMoveEvent(PlayerMoveEvent move, Hexagon from, Hexagon to) {
        this.move = move;
        this.moveCancellable = move;
        this.from = from;
        this.to = to;
    }
    
    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return getHandlerList();}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
    
}