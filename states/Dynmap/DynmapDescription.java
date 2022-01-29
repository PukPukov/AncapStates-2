package states.Dynmap;

public class DynmapDescription {

    private String name;
    private String[] description;

    public DynmapDescription(String name, String[] description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String[] getStrings() {
        return this.description;
    }
}
