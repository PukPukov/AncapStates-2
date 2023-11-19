package ru.ancap.states.hexagons;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.ancap.states.AncapStates;

public class Codeficator {

    public static long hexagon(Player player) {
        return Codeficator.hexagon(player.getLocation());
    }

    public static long hexagon(Location location) {
        return AncapStates.grid.hexagon(location).code();
    }

}
