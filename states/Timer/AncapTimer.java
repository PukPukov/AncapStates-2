package states.Timer;

import states.States.AncapStates;

public class AncapTimer {

    public void startTimers() {
        AncapStates.getInstance().getServer().getPluginManager().registerEvents(new AncapTimerEventListener(), AncapStates.getInstance());
    }
}
