package states.Chunk;

public class AncapChunk {

    private String id;

    public AncapChunk(String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
