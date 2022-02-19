package states.Location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import states.Location.Exceptions.AncapLocationException;

public class AncapLocation {

    private Location location;

    public AncapLocation(String locationString) {
        this.validateLocation(locationString);
        String[] strings = locationString.split(";");
        World world = Bukkit.getWorld(strings[0]);
        double x = Double.parseDouble(strings[1]);
        double y = Double.parseDouble(strings[2]);
        double z = Double.parseDouble(strings[3]);
        this.location = new Location(world, x, y, z);
    }

    public AncapLocation(Location loc) {
        this.location = loc;
    }

    private void validateLocation(String locationString) {
        String[] strings = locationString.split(";");
        if (strings.length != 4) {
            throw new AncapLocationException("AncapLocation can't be defined by "+locationString+": illegal amount of values ("+locationString.length()+"), expecting 4");
        }
        if (locationString.matches("[0-9;.]+")) {
            throw new AncapLocationException("AncapLocation can't be defined by "+locationString+": illegal characters (only numbers, points and semicolons allowed)");
        }
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return location.getWorld().getName()+";"+
                location.getBlockX()+";"+
                location.getBlockY()+";"+
                location.getBlockZ();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!AncapLocation.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        AncapLocation other = (AncapLocation)obj;
        return other.getLocation().equals(this.location);
    }
}
