package states.Hexagons;

import library.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import states.Player.AncapStatesPlayer;

public class AncapHexagonalGrid extends HexagonalGrid {

    public static final AncapHexagonalGrid CLASSIC_100 = new AncapHexagonalGrid(Orientation.FLAT, new Point(0,0), new Point(100,100), new Morton64(2, 32));

    public AncapHexagonalGrid(Orientation orientation, Point origin, Point size, Morton64 mort) {
        super(orientation, origin, size, mort);
    }

    public Hexagon getHexagon(Player p) {
        Location loc = p.getLocation();
        return this.getHexagon(loc);
    }

    public Hexagon getHexagon(AncapStatesPlayer ancapStatesPlayer) {
        Player p = ancapStatesPlayer.getPlayer();
        return this.getHexagon(p);
    }

    public Hexagon getHexagon(Location loc) {
        Point point = new Point(loc.getX(), loc.getZ());
        return this.getHexagon(point);
    }
}
