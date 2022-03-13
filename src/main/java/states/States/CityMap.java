package states.States;

import Database.Database;
import library.Hexagon;
import org.bukkit.Chunk;
import org.bukkit.Location;
import states.Chunk.OutpostChunk;
import states.Chunk.PrivateChunk;
import states.Main.AncapStates;
import states.Player.AncapStatesPlayer;
import states.States.City.City;
import states.States.Nation.Nation;
import states.StatesExceptions.CityException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CityMap {

    private Database statesDB;

    private HashMap<String, City> positionsMap = new HashMap<>();

    public CityMap() {
        statesDB = AncapStates.getMainDatabase();
    }

    public City getCity(Location loc) {
        Hexagon hexagon = AncapStates.grid.getHexagon(loc);
        OutpostChunk outpostChunk = new OutpostChunk(loc);
        String cityIDString = this.statesDB.getString("states.hexagons."+hexagon.toString()+".owner");
        if (cityIDString == null) {
            return outpostChunk.getOwner();
        }
        return getCity(cityIDString);
    }

    public City getOutpostChunkOwner(Location loc) {
        OutpostChunk outpostChunk = new OutpostChunk(loc);
        return outpostChunk.getOwner();
    }

    public City getCity(Hexagon hexagon) {
        String idString = this.statesDB.getString("states.hexagons."+hexagon+".owner");
        if (idString == null) {
            return null;
        }
        return new City(idString);
    }

    private City getCity(String cityIDString) {
        if (cityIDString == null) {
            return null;
        }
        return new City(cityIDString);
    }

    public City[] getCities() {
        String[] ids = statesDB.getKeys("states.city");
        ArrayList<City> cities = new ArrayList<>();
        for (int i = 0; i<ids.length; i++) {
            if (!statesDB.isSet("states.city."+ids[i]+".name")) {
                continue;
            }
            cities.add(new City(ids[i]));
        }
        return cities.toArray(new City[0]);
    }

    public AncapStatesPlayer getPrivateChunkOwner(Location loc) {
        City city = getCity(loc);
        PrivateChunk privateChunk = new PrivateChunk(city, getChunkID(loc));
        return privateChunk.getOwner();
    }

    public String getChunkID(Location loc) {
        Chunk chunk = loc.getChunk();
        return chunk.getX()+";"+chunk.getZ();
    }

    public Nation[] getNations() {
        String[] ids = statesDB.getKeys("states.nation");
        Nation[] nations = new Nation[ids.length];
        for (int i = 0; i<ids.length; i++) {
            nations[i] = new Nation(ids[i]);
        }
        return nations;
    }

    public HashMap<String, City> getPositionsMap() {
        return positionsMap;
    }

    public boolean isAtSameCity(Location[] locations) {
        if (locations.length == 0) {
            throw new CityException("Locations list cant be empty!");
        }
        City city = this.getCity(locations[0]);
        for (Location location : locations) {
            City city1 = this.getCity(location);
            if (!Objects.equals(city, city1)) {
                return false;
            }
        }
        return true;
    }

    public boolean isAtCity(Location[] locations) {
        for (Location location : locations) {
            City city = this.getCity(location);
            if (city == null) {
                return false;
            }
        }
        return true;
    }
}
