package ru.ancap.states.here;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.ancap.commons.list.merge.MergeList;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.states.AncapStates;
import ru.ancap.states.message.InfoMessage;
import ru.ancap.states.message.LayeredModifies;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.CityMap;
import ru.ancap.states.states.city.City;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class HereInfo {

    private final Player caller;
    private String chunk;
    private City city;
    private String cityName;
    private City outpostChunkOwner;
    private String outpostChunkOwnerStatus;
    private AncapStatesPlayer privateChunkOwner;
    private String privateChunkOwnerStatus;
    
    private static final Map<String, Function<Player, List<InfoMessage.ValueLike>>> addons = new ConcurrentHashMap<>();

    public HereInfo(Player player) {
        this.caller = player;
        Location location = player.getLocation();
        CityMap cityMap = AncapStates.getCityMap();
        Chunk hereChunk = location.getChunk();
        this.chunk = hereChunk.getX()+";"+hereChunk.getZ();
        this.city = cityMap.getCity(location);
        if (city == null) {
            this.cityName = "пустошь";
        } else {
            cityName = city.getName();
        }
        this.outpostChunkOwner = cityMap.getOutpostChunkOwner(location);
        this.outpostChunkOwnerStatus = "пустошь";
        if (city != null) {
            outpostChunkOwnerStatus = "домен";
            if (outpostChunkOwner != null) {
                outpostChunkOwnerStatus = "внешние владения";
            }
        }
        this.privateChunkOwner = cityMap.getPrivateChunkOwner(location);
        this.privateChunkOwnerStatus = "пустошь";
        if (this.city != null) {
            this.privateChunkOwnerStatus = "городской чанк";
            if (this.privateChunkOwner != null) {
                this.privateChunkOwnerStatus = "принадлежит игроку "+this.privateChunkOwner.getName();
            }
        }
    }
    
    public static void registerAddon(String name, Function<Player, List<InfoMessage.ValueLike>> addons) {
        HereInfo.addons.put(name, addons);
    }

    public InfoMessage getMessage() {
        return new InfoMessage(
            "here",
            new LayeredModifies(Map.of()),
            new InfoMessage.Values(new MergeList<>(List.of(
                new InfoMessage.Value("city", new Message(this.cityName)),
                new InfoMessage.Value("outpost", new Message(this.outpostChunkOwnerStatus)),
                new InfoMessage.Value("private-chunk-status", new Message(this.privateChunkOwnerStatus))),
                HereInfo.addons.values().stream().flatMap(supplier -> supplier.apply(this.caller).stream()).toList()
            ))
        );
    }
}
