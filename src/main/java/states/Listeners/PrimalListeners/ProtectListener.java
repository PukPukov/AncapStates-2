package states.Listeners.PrimalListeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import states.States.AncapStates;
import states.Player.AncapPlayer;

public class ProtectListener implements Listener {
    @EventHandler (priority = EventPriority.LOW)
    public void damage(EntityDamageByEntityEvent e) {
        if(!(e.getDamager().getType() == EntityType.PLAYER)){
            return;
        }
        Location interacted = e.getEntity().getLocation();
        AncapPlayer player = new AncapPlayer(e.getDamager().getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
        }
    }
    @EventHandler (priority = EventPriority.LOW)
    public void abuseDamageArrow(EntityDamageByEntityEvent e) {
        EntityType type = e.getDamager().getType();
        if(type != EntityType.ARROW){
            return;
        }
        Arrow a = (Arrow) e.getDamager();
        if(!(((Entity) a.getShooter()).getType() == EntityType.PLAYER)){
            return;
        }
        Location interacted = e.getEntity().getLocation();
        AncapPlayer player = new AncapPlayer(((Player) a.getShooter()).getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
        }
    }
    @EventHandler (priority = EventPriority.LOW)
    public void abuseDamageSpectralArrow(EntityDamageByEntityEvent e) {
        EntityType type = e.getDamager().getType();
        if(type != EntityType.SPECTRAL_ARROW){
            return;
        }
        SpectralArrow a = (SpectralArrow) e.getDamager();
        if(!(((Entity) a.getShooter()).getType() == EntityType.PLAYER)){
            return;
        }
        Location interacted = e.getEntity().getLocation();
        AncapPlayer player = new AncapPlayer(((Player) a.getShooter()).getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
        }
    }
    @EventHandler (priority = EventPriority.LOW)
    public void abuseDamageSnowball(EntityDamageByEntityEvent e) {
        EntityType type = e.getDamager().getType();
        if(type != EntityType.SNOWBALL){
            return;
        }
        Snowball a = (Snowball) e.getDamager();
        if(!(((Entity) a.getShooter()).getType() == EntityType.PLAYER)){
            return;
        }
        Location interacted = e.getEntity().getLocation();
        AncapPlayer player = new AncapPlayer(((Player) a.getShooter()).getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
        }
    }
    @EventHandler (priority = EventPriority.LOW)
    public void abuseDamageTrident(EntityDamageByEntityEvent e) {
        EntityType type = e.getDamager().getType();
        if(type != EntityType.TRIDENT){
            return;
        }
        Trident a = (Trident) e.getDamager();
        if(!(((Entity) a.getShooter()).getType() == EntityType.PLAYER)){
            return;
        }
        Location interacted = e.getEntity().getLocation();
        AncapPlayer player = new AncapPlayer(((Player) a.getShooter()).getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
        }
    }
    @EventHandler (priority = EventPriority.LOW)
    public void blockBreak(BlockBreakEvent e) {
        Location interacted = e.getBlock().getLocation();
        AncapPlayer player = new AncapPlayer(e.getPlayer().getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
        }
    }
    @EventHandler (priority = EventPriority.LOW)
    public void blockPlayer(BlockPlaceEvent e) {
        Location interacted = e.getBlock().getLocation();
        AncapPlayer player = new AncapPlayer(e.getPlayer().getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
        }
    }
    @EventHandler (priority = EventPriority.LOW)
    public void blockPlayer(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        if (b == null) {
            return;
        }
        Location interacted = b.getLocation();
        AncapPlayer player = new AncapPlayer(e.getPlayer().getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
        }
    }
    @EventHandler (priority = EventPriority.LOW)
    public void projectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }
        Location interacted = e.getLocation();
        AncapPlayer player = new AncapPlayer(((Player) e.getEntity().getShooter()).getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void projectileLandBlock(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }
        Block b = e.getHitBlock();
        if (b == null) {
            return;
        }
        Location interacted = b.getLocation();
        AncapPlayer player = new AncapPlayer(((Player) e.getEntity().getShooter()).getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
            e.getEntity().remove();
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void projectileLandEntity(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }
        Entity entity = e.getHitEntity();
        if (entity == null) {
            return;
        }
        Location interacted = entity.getLocation();
        AncapPlayer player = new AncapPlayer(((Player) e.getEntity().getShooter()).getName());
        if (!AncapStates.canInteract(player, interacted)) {
            e.setCancelled(true);
            e.getEntity().remove();
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void projectileLandEntityAbuse(PotionSplashEvent e) {
        ThrownPotion potion = e.getPotion();
        Entity[] entities = e.getAffectedEntities().toArray(new Entity[0]);
        if (entities.length == 0) {
            return;
        }
        for (int i = 0; i<entities.length; i++) {
            Location interacted = entities[i].getLocation();
            AncapPlayer player = new AncapPlayer(((Player) potion.getShooter()).getName());
            if (!AncapStates.canInteract(player, interacted)) {
                e.setCancelled(true);
                break;
            }
        }
    }
}
