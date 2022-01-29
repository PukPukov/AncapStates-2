package states.Timer.Heartbeat.Exceptions;

public class AncapHeartbeatAlreadyStartedException extends RuntimeException {
    public AncapHeartbeatAlreadyStartedException(String message) {
        super(message);
    }
}
