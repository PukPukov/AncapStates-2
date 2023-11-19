package ru.ancap.states.states.event;

import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.states.states.Subject;
import ru.ancap.states.states.city.RequestState;

import java.util.Optional;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class SubjectChangeAffiliationEvent extends Event implements Cancellable {
    
    @Setter @Getter private boolean cancelled;
    
    private final           Subject subject;
    private final @Nullable Subject newAffiliate;
    private final RequestState<Player> requestState;
    
    public Subject                   subject () { return                     this.subject;       }
    public Optional<Subject>    newAffiliate () { return Optional.ofNullable(this.newAffiliate); }
    public RequestState<Player> requestState () { return                     this.requestState;  }
    
    public ChangeType changeType() {
        return this.newAffiliate == null ? ChangeType.LEAVE : ChangeType.JOIN;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return getHandlerList();}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
    
}
