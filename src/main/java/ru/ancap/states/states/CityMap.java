package ru.ancap.states.states;

import lombok.NonNull;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.states.AncapStates;
import ru.ancap.states.chunk.OutpostChunk;
import ru.ancap.states.chunk.PrivateChunk;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.Nation.Nation;
import ru.ancap.states.states.city.City;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CityMap {

    private PathDatabase statesDB;

    private HashMap<String, City> positionsMap = new HashMap<>();

    public CityMap() {
        statesDB = AncapStates.getMainDatabase();
    }

    @Nullable
    public City getCity(@NonNull Location loc) {
        Hexagon hexagon = AncapStates.grid.hexagon(loc);
        OutpostChunk outpostChunk = new OutpostChunk(loc);
        String cityIDString = this.statesDB.readString("states.hexagons."+hexagon.code()+".owner");
        if (cityIDString == null) {
            return outpostChunk.getOwner();
        }
        return this.getCity(cityIDString);
    }

    public City getOutpostChunkOwner(Location loc) {
        OutpostChunk outpostChunk = new OutpostChunk(loc);
        return outpostChunk.getOwner();
    }

    public City getCity(Hexagon hexagon) {
        String idString = this.statesDB.readString("states.hexagons."+hexagon.code()+".owner");
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

    public List<City> getCities() {
        return this.statesDB.inner("states.city").keys().stream()
            .filter(id -> this.statesDB.isSet("states.city."+id+".name"))
            .map(City::new)
            .toList();
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

    public List<Nation> getNations() {
        return this.statesDB.inner("states.nation").keys().stream()
            .map(Nation::new)
            .toList();
    }

    public HashMap<String, City> getPositionsMap() {
        return positionsMap;
    }

    public boolean isAtSameCity(Location active, List<Location> passive) {
        if (passive == null) {
            throw new IllegalStateException();
        }
        City city = this.getCity(active);
        for (Location location : passive) {
            City otherCity = this.getCity(location);
            if (!Objects.equals(city, otherCity)) return false;
        }
        return true;
    }

    public boolean isAtCity(Location location) {
        return this.getCity(location) != null;
    }
    
}
