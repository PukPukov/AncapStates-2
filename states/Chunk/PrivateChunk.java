package states.Chunk;

import states.City.City;
import states.Database.Database;
import states.ID.ID;
import states.Player.AncapPlayer;

public class PrivateChunk  {

    private City city;
    private String id;

    public PrivateChunk(City city, String id) {
        this.city = city;
        this.id = id;
    }

    public AncapPlayer getOwner() {
        if (city == null) {
            return null;
        }
        Database statesBD = Database.STATES_DATABASE;
        String player = statesBD.getString("states.city."+this.city.getID()+".chunks."+this.id);
        if (player == null) {
            return null;
        }
        AncapPlayer owner = new AncapPlayer(player);
        return owner;
    }

    @Override
    public String toString() {
        return id;
    }
}
