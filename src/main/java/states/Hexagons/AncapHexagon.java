package states.Hexagons;

import library.Hexagon;
import library.HexagonalGrid;

public class AncapHexagon extends Hexagon {
    public AncapHexagon(HexagonalGrid grid, long q, long r) {
        super(grid, q, r);
    }

    public AncapHexagon(Hexagon hexagon) {
        super(hexagon);
    }
}
