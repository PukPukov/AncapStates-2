package states.Listeners.PrimalListeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import states.Main.AncapStatesEvents.AncapStatesWorldSelfDestructEvent;

public class SelfDestructListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void damage(EntityDamageByEntityEvent e) {
        if(!(e.getEntity().getType() == EntityType.PLAYER)){
            return;
        }
        Location interacted = e.getDamager().getLocation();
        Location interacting = e.getEntity().getLocation();
        Event event = new AncapStatesWorldSelfDestructEvent(e, interacted, interacting);
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void pistonExtend(BlockPistonExtendEvent e) {
        this.piston(e);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void pistonRetract(BlockPistonExtendEvent e) {
        this.piston(e);
    }

    public void piston(BlockPistonEvent e) {
        Block firstBlock = e.getBlock();
        Block secondBlock = firstBlock.getRelative(e.getDirection());
        Location pistonBaseLoc = firstBlock.getLocation();
        Location pistonPlateLoc = secondBlock.getLocation();
        Event event = new AncapStatesWorldSelfDestructEvent(e, pistonBaseLoc, pistonPlateLoc);
        Bukkit.getPluginManager().callEvent(event);
    }
}
