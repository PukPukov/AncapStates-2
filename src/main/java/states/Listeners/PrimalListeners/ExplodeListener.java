package states.Listeners.PrimalListeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import states.City.CityMap;

public class ExplodeListener implements Listener {
    @EventHandler
    public void explodeEvent(EntityExplodeEvent e) {
        Location loc = e.getLocation();
        if (CityMap.getCity(loc) != null) {
            e.setCancelled(true);
        }
    }
}
