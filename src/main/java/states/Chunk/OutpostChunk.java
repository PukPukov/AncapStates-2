package states.Chunk;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import states.States.City.City;
import states.Database.Database;

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

    public City getOwner() {
        Database statesBD = Database.STATES_DATABASE;
        String cityID = statesBD.getString("states.chunks."+this.id);
        if (cityID == null) {
            return null;
        }
        City owner = new City(cityID);
        return owner;
    }

    @Override
    public String toString() {
        return id;
    }
}
