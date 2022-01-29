package states.Hexagons;

import library.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import states.Player.AncapPlayer;

public class MinecraftHexagonalGrid extends HexagonalGrid {

    public static final MinecraftHexagonalGrid CLASSIC_100 = new MinecraftHexagonalGrid(Orientation.FLAT, new Point(0,0), new Point(100,100), new Morton64(2, 32));

    public MinecraftHexagonalGrid(Orientation orientation, Point origin, Point size, Morton64 mort) {
        super(orientation, origin, size, mort);
    }

    public Hexagon getHexagon(Player p) {
        Location loc = p.getLocation();
        return this.getHexagon(loc);
    }

    public Hexagon getHexagon(AncapPlayer ancapPlayer) {
        Player p = ancapPlayer.getPlayer();
        return this.getHexagon(p);
    }

    public Hexagon getHexagon(Location loc) {
        Point point = new Point(loc.getX(), loc.getZ());
        return this.getHexagon(point);
    }
}
