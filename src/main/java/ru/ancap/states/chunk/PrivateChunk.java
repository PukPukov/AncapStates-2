package ru.ancap.states.chunk;

import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.library.AncapChunk;
import ru.ancap.states.AncapStates;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.city.City;

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
        PathDatabase statesBD = AncapStates.getMainDatabase();
        String ownerId = statesBD.readString("states.city."+this.city.getID()+".chunks."+this.id);
        if (ownerId == null) {
            return null;
        }
        return AncapStatesPlayer.findByID(ownerId);
    }

    @Override
    public String toString() {
        return this.id;
    }

    public AncapChunk asAncapChunk() {
        return new AncapChunk(this.id);
    }
    
}
