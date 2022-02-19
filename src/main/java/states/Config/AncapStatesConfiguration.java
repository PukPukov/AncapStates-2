package states.Config;

public class AncapStatesConfiguration {

    private Config config = Config.BASE_CONFIG;

    public AncapStatesConfiguration() {
    }

    private static class SingletonHolder {
        public static final AncapStatesConfiguration HOLDER_INSTANCE = new AncapStatesConfiguration();
    }

    public static AncapStatesConfiguration getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public boolean isCityMoveTimerEnabled() {
        return config.getString("fastTimers.secondTimer.cityMoveTimerEnabled").equals("true");
    }
}
