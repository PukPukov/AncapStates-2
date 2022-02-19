package states.Player;

import AncapLibrary.Player.AncapPlayerMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AncapStatesPlayerMap extends AncapPlayerMap {

    private static final AncapStatesPlayerMap INSTANCE = new AncapStatesPlayerMap();

    public static AncapStatesPlayerMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AncapStatesPlayer[] getOnlinePlayers() {
        Player[] bukkitPlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        AncapStatesPlayer[] players = new AncapStatesPlayer[bukkitPlayers.length];
        for (int i = 0; i<players.length; i++) {
            players[i] = new AncapStatesPlayer(bukkitPlayers[i].getName());
        }
        return players;
    }
}
