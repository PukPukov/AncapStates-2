package ru.ancap.states.message;

import ru.ancap.states.AncapStates;

public class TerritoryValue extends InfoMessage.WrapperValue {
    
    public TerritoryValue(int hexagons) {
        super(new InfoMessage.UnitValue(
            "territory",
            new InfoMessage.UnitValue.Amount("hexagons", hexagons),
            new InfoMessage.UnitValue.Amount("kilometers-fact", factTerritory(hexagons)),
            new InfoMessage.UnitValue.Amount("kilometers-territorial", terrTerritory(hexagons)),
            new InfoMessage.UnitValue.Amount("percent-from-land", ((double) hexagons) / ((double) AncapStates.landTerritory) * 100)
        ));
    }

    private static double terrTerritory(int hexagons) {
        return factTerritory(hexagons) * (Math.pow(AncapStates.mapScape, 2));
    }

    private static double factTerritory(int hexagons) {
        double side = AncapStates.gridHexagonSideSize;
        double inMeters = ((3 * Math.sqrt(3) * Math.pow(side, 2)) / 2) * hexagons;
        double inKilometers = inMeters / 1_000_000;
        return inKilometers;
    }
    

}
