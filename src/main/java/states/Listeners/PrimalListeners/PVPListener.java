package states.Listeners.PrimalListeners;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import states.City.CityMap;
import states.States.AncapStates;

public class PVPListener implements Listener {
    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity().getType() == EntityType.PLAYER)) {
            return;
        }
        if (!(e.getDamager().getType() == EntityType.PLAYER)) {
            return;
        }
        Location loc0 = e.getEntity().getLocation();
        Location loc1 = e.getDamager().getLocation();
        if (AncapStates.getCityMap().getCity(loc0) != null || AncapStates.getCityMap().getCity(loc1) != null) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void damageAbuse(EntityDamageByEntityEvent e) {
        if (!(e.getEntity().getType() == EntityType.PLAYER)) {
            return;
        }
        if (!(e.getDamager() instanceof Projectile)) {
            return;
        }
        Projectile projectile = (Projectile) e.getDamager();
        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player) projectile.getShooter();
        Location loc0 = e.getEntity().getLocation();
        Location loc1 = player.getLocation();
        if (AncapStates.getCityMap().getCity(loc0) != null || AncapStates.getCityMap().getCity(loc1) != null) {
            e.setCancelled(true);
        }
    }
}
