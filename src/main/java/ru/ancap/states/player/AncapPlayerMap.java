package ru.ancap.states.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AncapPlayerMap {

    public AncapPlayer[] getOnlinePlayers() {
        Player[] bukkitPlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        AncapPlayer[] players = new AncapPlayer[bukkitPlayers.length];

        for(int i = 0; i < players.length; ++i) {
            players[i] = new AncapPlayer(bukkitPlayers[i]);
        }

        return players;
    }
    
}
