package ru.ancap.states.hexagons;

import lombok.experimental.Delegate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.ancap.hexagon.GridOrientation;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.hexagon.HexagonalGrid;
import ru.ancap.hexagon.common.Point;
import ru.ancap.states.player.AncapStatesPlayer;

public class AncapHexagonalGrid {
    
    @Delegate
    private final HexagonalGrid base;

    public AncapHexagonalGrid(GridOrientation orientation, Point size, Point origin) {
        this.base = new HexagonalGrid(orientation, size, origin);
    }

    public Hexagon hexagon(Player p) {
        Location loc = p.getLocation();
        return this.hexagon(loc);
    }

    public Hexagon hexagon(AncapStatesPlayer ancapStatesPlayer) {
        Player p = ancapStatesPlayer.online();
        if (p == null) throw new IllegalStateException("Trying to get hexagon of offline player!");
        return this.hexagon(p);
    }

    public Hexagon hexagon(Location loc) {
        Point point = new Point(loc.getX(), loc.getZ());
        return this.hexagon(point);
    }
    
}