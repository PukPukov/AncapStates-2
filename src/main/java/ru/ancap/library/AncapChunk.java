package ru.ancap.library;

import lombok.AllArgsConstructor;
import org.bukkit.Chunk;

@AllArgsConstructor
public class AncapChunk {
    
    private final long x;
    private final long z;
    
    public AncapChunk(Chunk chunk) {
        this(chunk.getX(), chunk.getZ());
    }
    
    public AncapChunk(String id) {
        String[] nodes = id.split(";");
        this.x = Long.parseLong(nodes[0]);
        this.z = Long.parseLong(nodes[1]);
    }

    public String getID() {
        return this.x+";"+this.z;
    }
    
    public String toString() {
        return this.getID();
    }
    
}
