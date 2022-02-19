package states.Location;

public class LegacyLocation {

    private AncapLocation location;

    public LegacyLocation(String str) {
        str = str.replace(",", ";");
        this.location = new AncapLocation(str);
    }

    public AncapLocation getLocation() {
        return location;
    }
}
