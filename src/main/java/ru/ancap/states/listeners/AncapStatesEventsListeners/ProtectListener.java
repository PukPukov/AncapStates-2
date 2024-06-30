package ru.ancap.states.listeners.AncapStatesEventsListeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import ru.ancap.framework.api.event.events.wrapper.PVPEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldInteractEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldSelfDestructEvent;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.states.AncapStates;
import ru.ancap.states.message.ErrorMessage;
import ru.ancap.states.player.AncapStatesPlayer;

public class ProtectListener implements Listener {

    /*
    что нужно разрешить
    !+ взаимодействие с верстаком, сундуком края, столом картографа, столом кузнеца, чародейским столом (PlayerInteractEvent RIGHT_CLICK_BLOCK тип)
    !+ взаимодействие с дверями, люками, кнопками, плитами, калитками (PlayerInteractEvent RIGHT_CLICK_BLOCK тип)
    + установка лодки (PlayerInteractEvent RIGHT_CLICK_BLOCK WATER BOAT)
    + поломка лодки вообще не вызывает ивента, как ни странно
    + взаимодействие с лодкой (PlayerInteractEntityEvent BOAT)
    !+ запуск эндер пёрла (ProjectileLaunchEvent ENDER_PEARL)
    + запуск фейерверка (ProjectileLaunchEvent FIREWORK) 
      + попробовать сделать так чтобы только невзрывающийся фейерверк можно было запускать
     */
    
    @EventHandler(priority = EventPriority.HIGH)
    public void on(WorldInteractEvent event) {
        if (event.consumed()) return;
        else event.consume();
        AncapStatesPlayer player = AncapStatesPlayer.get(event.player());
        switch (event.bukkit()) {
            case PlayerInteractEvent interactEvent -> {
                switch (interactEvent.getAction()) {
                    case RIGHT_CLICK_AIR, LEFT_CLICK_AIR -> {
                        return;
                    }
                    case RIGHT_CLICK_BLOCK -> {
                        Block clickedBlock = interactEvent.getClickedBlock();
                        if (clickedBlock == null) return;
                        Material type = clickedBlock.getType();
                        switch (type) {
                            case 
                                CRAFTING_TABLE, ENDER_CHEST, CARTOGRAPHY_TABLE, SMITHING_TABLE, ENCHANTING_TABLE,
                                 
                                IRON_DOOR, COPPER_DOOR, EXPOSED_COPPER_DOOR, OXIDIZED_COPPER_DOOR, WAXED_COPPER_DOOR, WAXED_EXPOSED_COPPER_DOOR,
                                WAXED_OXIDIZED_COPPER_DOOR, WAXED_WEATHERED_COPPER_DOOR, WEATHERED_COPPER_DOOR,
                                OAK_DOOR, SPRUCE_DOOR, BIRCH_DOOR, JUNGLE_DOOR, ACACIA_DOOR, DARK_OAK_DOOR, MANGROVE_DOOR,
                                CHERRY_DOOR, BAMBOO_DOOR, CRIMSON_DOOR, WARPED_DOOR,
                                 
                                 
                                IRON_TRAPDOOR, COPPER_TRAPDOOR, EXPOSED_COPPER_TRAPDOOR, OXIDIZED_COPPER_TRAPDOOR, WAXED_COPPER_TRAPDOOR,
                                WAXED_EXPOSED_COPPER_TRAPDOOR, WAXED_OXIDIZED_COPPER_TRAPDOOR, WAXED_WEATHERED_COPPER_TRAPDOOR,
                                WEATHERED_COPPER_TRAPDOOR, OAK_TRAPDOOR, SPRUCE_TRAPDOOR, BIRCH_TRAPDOOR, JUNGLE_TRAPDOOR, ACACIA_TRAPDOOR,
                                DARK_OAK_TRAPDOOR, MANGROVE_TRAPDOOR, CHERRY_TRAPDOOR, BAMBOO_TRAPDOOR, CRIMSON_TRAPDOOR, WARPED_TRAPDOOR,
                                 
                                STONE_BUTTON, POLISHED_BLACKSTONE_BUTTON, OAK_BUTTON, SPRUCE_BUTTON, BIRCH_BUTTON, JUNGLE_BUTTON, ACACIA_BUTTON,
                                DARK_OAK_BUTTON, MANGROVE_BUTTON, CHERRY_BUTTON, BAMBOO_BUTTON, CRIMSON_BUTTON, WARPED_BUTTON,
                                 
                                HEAVY_WEIGHTED_PRESSURE_PLATE, LIGHT_WEIGHTED_PRESSURE_PLATE, STONE_PRESSURE_PLATE, POLISHED_BLACKSTONE_PRESSURE_PLATE,
                                OAK_PRESSURE_PLATE, SPRUCE_PRESSURE_PLATE, BIRCH_PRESSURE_PLATE, JUNGLE_PRESSURE_PLATE, ACACIA_PRESSURE_PLATE,
                                DARK_OAK_PRESSURE_PLATE, MANGROVE_PRESSURE_PLATE, CHERRY_PRESSURE_PLATE, BAMBOO_PRESSURE_PLATE, 
                                CRIMSON_PRESSURE_PLATE, WARPED_PRESSURE_PLATE,
                                
                                ACACIA_FENCE_GATE, BAMBOO_FENCE_GATE, BIRCH_FENCE_GATE, CHERRY_FENCE_GATE, CRIMSON_FENCE_GATE, DARK_OAK_FENCE_GATE,
                                JUNGLE_FENCE_GATE, MANGROVE_FENCE_GATE, OAK_FENCE_GATE, SPRUCE_FENCE_GATE, WARPED_FENCE_GATE,
                                
                                DECORATED_POT,
                                
                                WATER // boats
                                -> {
                                return;
                            }
                            default -> {}
                        }
                    }
                }
            }
            case PlayerInteractEntityEvent interactEntityEvent -> {
                switch (interactEntityEvent.getRightClicked().getType()) {
                    case BOAT, HORSE -> { return; }
                }
            }
            case ProjectileLaunchEvent projectileLaunchEvent -> {
                Entity entity = projectileLaunchEvent.getEntity();
                switch (entity.getType()) {
                    case ENDER_PEARL -> {
                        return;
                    }
                    case FIREWORK_ROCKET -> {
                        Firework firework = ((Firework) entity);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();
                        if (!fireworkMeta.hasEffects()) return;
                    }
                }
            }
            case ProjectileHitEvent projectileHitEvent -> {
                if (projectileHitEvent.getEntity().getType() == EntityType.ENDER_PEARL) {
                    Entity hitEntity = projectileHitEvent.getHitEntity();
                    if (hitEntity == null) return;
                    else if (hitEntity.getType() != EntityType.END_CRYSTAL) return;
                }
            }
            case EntityDamageByEntityEvent entityDamageByEntityEvent -> {
                Entity damaged = entityDamageByEntityEvent.getEntity();
                if (damaged instanceof Enemy || damaged instanceof Boss) return;
                if (entityDamageByEntityEvent.getDamager().getType() == EntityType.ENDER_PEARL) return;
            }
            default -> {}
        }
        for (Location location : event.locations()) if (!player.canInteract(location)) {
            this.cancelInteract(event);
            CallableMessage message = ErrorMessage.REGION_PROTECTION_FORBIDDEN_ACTION;
            player.sendMessage(message);
        }
    }

    private void cancelInteract(WorldInteractEvent event) {
        event.setCancelled(true);
        this.removeProjectile(event);
    }

    private void removeProjectile(WorldInteractEvent event) {
        if (event.bukkit() instanceof ProjectileHitEvent hitEvent) hitEvent.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(PVPEvent event) {
        if (event.consumed()) return;
        else event.consume();
        for (Player attacked : event.attacked()) if (AncapStates.getCityMap().isAtCity(attacked.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(BlockExplodeEvent event) {
        if (AncapStates.getCityMap().getCity(event.getBlock().getLocation()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(EntityExplodeEvent event) {
        if (AncapStates.getCityMap().getCity(event.getEntity().getLocation()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(WorldSelfDestructEvent event) {
        if (event.consumed()) return;
        else event.consume();
        if (!AncapStates.getCityMap().isAtSameCity(event.active(), event.passive())) event.setCancelled(true);
    }
    
}