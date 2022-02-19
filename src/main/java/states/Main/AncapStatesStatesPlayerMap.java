package states.Main;

import states.Player.AncapStatesPlayer;

public class AncapStatesStatesPlayerMap extends states.Player.AncapStatesPlayerMap {

    private static final AncapStatesStatesPlayerMap INSTANCE = new AncapStatesStatesPlayerMap();

    public static AncapStatesStatesPlayerMap getInstance() {
        return INSTANCE;
    }

    public void checkCityMove() {
        AncapStatesPlayer[] players = this.getOnlinePlayers();
        for (AncapStatesPlayer player : players) {
            player.checkCityMove();
        }
    }
}
