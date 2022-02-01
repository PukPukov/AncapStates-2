package states.Wars.WarHexagons;

import library.Hexagon;
import library.HexagonalGrid;
import states.Hexagons.AncapHexagon;

public class WarHexagon extends AncapHexagon {

    public WarHexagon(HexagonalGrid grid, long q, long r) {
        super(grid, q, r);
    }

    public WarHexagon(Hexagon hexagon) {
        super(hexagon.getGrid(), hexagon.getQ(), hexagon.getR());
    }

    public boolean atWar() {
        return false;
    }
}
