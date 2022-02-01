package states.Main.AncapStatesEvents;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import states.Main.Interceptable;

public class AncapStatesExplodeEvent extends Event implements Cancellable, Interceptable {

    public static final HandlerList handlers = new HandlerList();

    private Location loc;

    private Cancellable event;

    private boolean intercepted;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public AncapStatesExplodeEvent(Cancellable event, Location loc) {
        this.loc = loc;
        this.event = event;
    }

    public Location getLocation() {
        return this.loc;
    }

    public Cancellable getBukkitEvent() {
        return this.event;
    }

    @Override
    public boolean isCancelled() {
        return this.event.isCancelled();
    }

    @Override
    public void setCancelled(boolean b) {
        this.event.setCancelled(b);
    }

    @Override
    public boolean isIntercepted() {
        return this.intercepted;
    }

    @Override
    public void setIntercepted(boolean b) {
        this.intercepted = b;
    }
}
