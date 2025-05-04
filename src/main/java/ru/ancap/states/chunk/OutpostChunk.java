package ru.ancap.states.chunk;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.states.AncapStates;
import ru.ancap.states.states.city.City;

public class OutpostChunk {

    private String id;

    public OutpostChunk(String id) {
        this.id = id;
    }

    public OutpostChunk(Location loc) {
        this(loc.getChunk());
    }

    public OutpostChunk(Chunk chunk) {
        this(chunk.getX()+";"+chunk.getZ());
    }

    public OutpostChunk(Player p) {
        this(p.getLocation());
    }

    @Nullable
    public City getOwner() {
        PathDatabase statesBD = AncapStates.mainDatabase();
        String cityID = statesBD.readString("states.chunks."+this.id);
        if (cityID == null) {
            return null;
        }
        return new City(cityID);
    }
    
    public String id() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.id;
    }
}