package states.City;

import library.Hexagon;
import org.bukkit.Chunk;
import org.bukkit.Location;
import states.States.AncapStates;
import states.Chunk.OutpostChunk;
import states.Chunk.PrivateChunk;
import states.Database.Database;
import states.Nation.Nation;
import states.Player.AncapPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class CityMap {

    private static Database statesDB = Database.STATES_DATABASE;

    private static HashMap<String, City> positionsMap = new HashMap<>();

    public static City getCity(Location loc) {
        Hexagon hexagon = AncapStates.grid.getHexagon(loc);
        OutpostChunk outpostChunk = new OutpostChunk(loc);
        String cityIDString = statesDB.getString("states.hexagons."+hexagon.toString()+".owner");
        if (cityIDString == null) {
            return outpostChunk.getOwner();
        }
        return getCity(cityIDString);
    }

    public static City getOutpostChunkOwner(Location loc) {
        OutpostChunk outpostChunk = new OutpostChunk(loc);
        return outpostChunk.getOwner();
    }

    public static City getCity(Hexagon hexagon) {
        String idString = statesDB.getString("states.hexagons."+hexagon+".owner");
        if (idString == null) {
            return null;
        }
        return new City(idString);
    }

    private static City getCity(String cityIDString) {
        if (cityIDString == null) {
            return null;
        }
        return new City(cityIDString);
    }

    public static City[] getCities() {
        String[] ids = statesDB.getStringList("states.city");
        ArrayList<City> cities = new ArrayList<>();
        for (int i = 0; i<ids.length; i++) {
            if (!statesDB.isSet("states.city."+ids[i]+".name")) {
                continue;
            }
            cities.add(new City(ids[i]));
        }
        return cities.toArray(new City[0]);
    }

    public static AncapPlayer getPrivateChunkOwner(Location loc) {
        City city = getCity(loc);
        PrivateChunk privateChunk = new PrivateChunk(city, getChunkID(loc));
        return privateChunk.getOwner();
    }

    public static String getChunkID(Location loc) {
        Chunk chunk = loc.getChunk();
        return chunk.getX()+";"+chunk.getZ();
    }

    public static Nation[] getNations() {
        String[] ids = statesDB.getStringList("states.nation");
        Nation[] nations = new Nation[ids.length];
        for (int i = 0; i<ids.length; i++) {
            nations[i] = new Nation(ids[i]);
        }
        return nations;
    }

    public static HashMap<String, City> getPositionsMap() {
        return positionsMap;
    }
}
