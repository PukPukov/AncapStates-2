package states.Wars.WarStates.WarCities;

import states.States.City.City;
import states.Wars.WarStates.WarState;

public class WarCity extends City implements WarState {

    public WarCity(String id) {
        super(id);
    }

    @Override
    public void declareWar(WarState state) {

    }

    @Override
    public void offerPeace(WarState state) {

    }
}
