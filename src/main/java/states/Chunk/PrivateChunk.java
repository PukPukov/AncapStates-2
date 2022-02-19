package states.Chunk;

import states.States.City.City;
import states.Database.Database;
import states.Player.AncapStatesPlayer;

public class PrivateChunk  {

    private City city;
    private String id;

    public PrivateChunk(City city, String id) {
        this.city = city;
        this.id = id;
    }

    public AncapStatesPlayer getOwner() {
        if (city == null) {
            return null;
        }
        Database statesBD = Database.STATES_DATABASE;
        String player = statesBD.getString("states.city."+this.city.getID()+".chunks."+this.id);
        if (player == null) {
            return null;
        }
        AncapStatesPlayer owner = new AncapStatesPlayer(player);
        return owner;
    }

    @Override
    public String toString() {
        return id;
    }
}
