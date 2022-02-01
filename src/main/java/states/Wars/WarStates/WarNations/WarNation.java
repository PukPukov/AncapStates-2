package states.Wars.WarStates.WarNations;

import states.States.Nation.Nation;
import states.Wars.WarStates.WarState;

public class WarNation extends Nation implements WarState {

    public WarNation(String id) {
        super(id);
    }

    @Override
    public void declareWar(WarState state) {

    }

    @Override
    public void offerPeace(WarState state) {

    }
}
