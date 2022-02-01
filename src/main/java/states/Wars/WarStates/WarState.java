package states.Wars.WarStates;

import states.States.AncapState;

public interface WarState extends AncapState {

    public void declareWar(WarState state);
    public void offerPeace(WarState state);
}
