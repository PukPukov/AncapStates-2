package ru.ancap.states.here;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.ancap.commons.list.merge.MergeList;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.additional.LAPIMessage;
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
    private CallableMessage cityName;
    private City outpostChunkOwner;
    private CallableMessage outpostChunkOwnerStatus;
    private AncapStatesPlayer privateChunkOwner;
    private CallableMessage privateChunkOwnerStatus;
    
    private static final Map<String, Function<Player, List<InfoMessage.ValueLike>>> addons = new ConcurrentHashMap<>();

    public HereInfo(Player player) {
        this.caller = player;
        String identifier = Identifier.of(player);
        Location location = player.getLocation();
        CityMap cityMap = AncapStates.cityMap();
        Chunk hereChunk = location.getChunk();
        this.chunk = hereChunk.getX()+";"+hereChunk.getZ();
        this.city = cityMap.getCity(location);
        if (city == null) {
            this.cityName = new LAPIMessage(AncapStates.class, "info.here.wilderness");
        } else {
            cityName = new Message(this.city.getName());
        }
        this.outpostChunkOwner = cityMap.getOutpostChunkOwner(location);
        this.outpostChunkOwnerStatus = new LAPIMessage(AncapStates.class, "info.here.wilderness");
        if (city != null) {
            outpostChunkOwnerStatus = new LAPIMessage(AncapStates.class, "info.here.domain");
            if (outpostChunkOwner != null) {
                outpostChunkOwnerStatus = new LAPIMessage(AncapStates.class, "info.here.outer-possessions");
            }
        }
        this.privateChunkOwner = cityMap.getPrivateChunkOwner(location);
        this.privateChunkOwnerStatus = new LAPIMessage(AncapStates.class, "info.here.wilderness");
        if (this.city != null) {
            this.privateChunkOwnerStatus = new LAPIMessage(AncapStates.class, "info.here.city-chunk");
            if (this.privateChunkOwner != null) {
                this.privateChunkOwnerStatus = new LAPIMessage(
                    AncapStates.class, "info.here.private-chunk",
                    new Placeholder("player", this.privateChunkOwner.getName())
                );
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
                new InfoMessage.Value("city", this.cityName),
                new InfoMessage.Value("outpost", this.outpostChunkOwnerStatus),
                new InfoMessage.Value("private-chunk-status", this.privateChunkOwnerStatus)),
                HereInfo.addons.values().stream().flatMap(supplier -> supplier.apply(this.caller).stream()).toList()
            ))
        );
    }
}