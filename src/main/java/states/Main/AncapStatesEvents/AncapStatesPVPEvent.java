package states.Main.AncapStatesEvents;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import states.Main.Interceptable;

public class AncapStatesPVPEvent extends Event implements Interceptable, Cancellable {

    private Location[] locations = new Location[2];

    private Cancellable event;

    private boolean intercepted;

    public AncapStatesPVPEvent(Cancellable event, Location loc0, Location loc1) {
        this.locations[0] = loc0;
        this.locations[1] = loc1;
        this.event = event;
    }

    public static final HandlerList handlers = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Location[] getLocations() {
        return this.locations;
    }

    public Cancellable getBukkitEvent() {
        return this.event;
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean b) {
        event.setCancelled(b);
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
