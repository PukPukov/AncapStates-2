package states.Main.AncapStatesEvents;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import states.Main.Interceptable;
import states.Player.AncapPlayer;

public class AncapStatesWorldInteractEvent extends Event implements Interceptable, Cancellable {

    private boolean intercepted;

    private final Cancellable event;

    private final AncapPlayer player;

    private final Location loc;

    public AncapStatesWorldInteractEvent(Cancellable event, AncapPlayer player, Location loc) {
        this.event = event;
        this.player = player;
        this.loc = loc;
    }

    public static final HandlerList handlers = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.event.isCancelled();
    }

    @Override
    public void setCancelled(boolean b) {
        this.event.setCancelled(b);
    }

    public Cancellable getBukkitEvent() {
        return this.event;
    }

    public AncapPlayer getPlayer() {
        return this.player;
    }

    public Location getLocation() {
        return this.loc;
    }

    @Override
    public void setIntercepted(boolean b) {
        this.intercepted = b;
    }

    @Override
    public boolean isIntercepted() {
        return this.intercepted;
    }
}
