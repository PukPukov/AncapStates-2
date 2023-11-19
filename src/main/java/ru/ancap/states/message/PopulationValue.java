package ru.ancap.states.message;

import ru.ancap.states.AncapStates;

public class PopulationValue extends InfoMessage.WrapperValue {
    
    public PopulationValue(int population) {
        super(new InfoMessage.UnitValue(
            "population",
            new InfoMessage.UnitValue.Amount("people", population),
            new InfoMessage.UnitValue.Amount("percent-from-total-population", (((double) population) / AncapStates.globalPopulation()) * 100)
        ));
    }
    
}
