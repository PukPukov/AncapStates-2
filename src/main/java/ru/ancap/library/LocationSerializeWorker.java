package ru.ancap.library;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import ru.ancap.framework.database.nosql.SerializeWorker;

public class LocationSerializeWorker extends SerializeWorker.AbstractSerializeWorker<Location> {
    
    public static final LocationSerializeWorker INSTANCE = new LocationSerializeWorker();

    public LocationSerializeWorker() {
        super(Location.class);
    }

    @Override
    public String serialize(Location location) {
        return location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ();
    }

    @Override
    public Location deserialize(String serialized) {
        String[] parts = serialized.split(";");
        return new Location(
            Bukkit.getWorld("world"),
            Double.parseDouble(parts[0]),
            Double.parseDouble(parts[1]),
            Double.parseDouble(parts[2])
        );
    }
    
}
