package states.Timer.Heartbeat;

import states.States.AncapStates;
import states.Timer.AncapTimer;
import states.Timer.Heartbeat.Exceptions.AncapHeartbeatAlreadyStartedException;

public class AncapHeartbeat {

    private static boolean started = false;

    public void start() {
        if (started) {
            throw new AncapHeartbeatAlreadyStartedException("AncapHeartbeat is already started");
        }
        AncapHeartbeatRunnable runnable = new AncapHeartbeatRunnable();
        runnable.runTaskTimer(AncapStates.getInstance(), 0, 20L);
        AncapTimer timer = new AncapTimer();
        timer.startTimers();
        started = true;
    }

}
