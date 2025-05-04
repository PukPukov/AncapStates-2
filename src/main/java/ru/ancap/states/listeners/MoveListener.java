package ru.ancap.states.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.ancap.states.event.events.CityFoundEvent;
import ru.ancap.states.event.events.move.CityMoveEvent;
import ru.ancap.states.event.events.move.HexagonMoveEvent;
import ru.ancap.states.hexagons.AncapHexagonalGrid;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.CityMap;

import java.util.Objects;

@RequiredArgsConstructor
public class MoveListener implements Listener {
    
    private final AncapHexagonalGrid grid;
    private final CityMap cityMap;
    
    @EventHandler
    public void on(PlayerMoveEvent event) {
        var from = this.grid.hexagon(event.getFrom());
        var to = this.grid.hexagon(event.getTo());
        if (from.equals(to)) return;
        Bukkit.getPluginManager().callEvent(new HexagonMoveEvent(event, from, to));
    }
    
    @EventHandler
    public void on(HexagonMoveEvent event) {
        var from = this.cityMap.getCity(event.from());
        var to = this.cityMap.getCity(event.to());
        if (Objects.equals(from, to)) return;
        
        Bukkit.getPluginManager().callEvent(new CityMoveEvent(AncapStatesPlayer.get(event.move().getPlayer()), from, to));
    }
    
    @EventHandler
    public void on(CityFoundEvent event) {
        
    }
    
    @EventHandler
    public void on(CityMoveEvent event) {
        event.getPlayer().sendJoinTitle(event.getTo());
    }
    
    
}