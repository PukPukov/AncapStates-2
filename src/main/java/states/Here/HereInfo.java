package states.Here;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import states.City.City;
import states.City.CityMap;
import states.Message.InfoMessage;
import states.Message.Message;
import states.Player.AncapPlayer;
import states.States.AncapStates;

public class HereInfo {

    private String chunk;
    private City city;
    private String cityName;
    private City outpostChunkOwner;
    private String outpostChunkOwnerStatus;
    private AncapPlayer privateChunkOwner;
    private String privateChunkOwnerStatus;

    public HereInfo(Location loc) {
        AncapStates ancapStates = (AncapStates) Bukkit.getPluginManager().getPlugin("AncapStates");
        CityMap cityMap = ancapStates.getCityMap();
        Chunk hereChunk = loc.getChunk();
        this.chunk = hereChunk.getX()+";"+hereChunk.getZ();
        this.city = cityMap.getCity(loc);
        if (city == null) {
            this.cityName = "пустошь";
        } else {
            cityName = city.getName();
        }
        this.outpostChunkOwner = cityMap.getOutpostChunkOwner(loc);
        this.outpostChunkOwnerStatus = "пустошь";
        if (city != null) {
            outpostChunkOwnerStatus = "домен";
            if (outpostChunkOwner != null) {
                outpostChunkOwnerStatus = "внешние владения";
            }
        }
        this.privateChunkOwner = cityMap.getPrivateChunkOwner(loc);
        this.privateChunkOwnerStatus = "пустошь";
        if (city != null) {
            privateChunkOwnerStatus = "городской чанк";
            if (privateChunkOwner != null) {
                privateChunkOwnerStatus = "принадлежит игроку "+privateChunkOwner.getName();
            }
        }
    }

    public InfoMessage getMessage() {
        Message[] messages = new Message[7];
        Message line = InfoMessage.LINE;
        Message space = InfoMessage.SPACE;
        messages[0] = line;
        messages[1] = InfoMessage.HERE_INFO;
        messages[2] = space;
        messages[3] = InfoMessage.HERE_CITY(this.cityName);
        messages[4] = InfoMessage.HERE_OUTPOST(this.outpostChunkOwnerStatus);
        messages[5] = InfoMessage.HERE_PRIVATE_CHUNK(this.privateChunkOwnerStatus);
        messages[6] = line;
        return new InfoMessage(messages);
    }
}
