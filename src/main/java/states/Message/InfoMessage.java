package states.Message;

import AncapLibrary.Message.Message;
import states.Config.Config;

import java.util.ArrayList;
import java.util.Arrays;

public class InfoMessage extends Message {

    public static final InfoMessage LINE = new InfoMessage("line");

    public static final Message SPACE = new InfoMessage("space");

    public static final Message HERE_INFO = new InfoMessage("here_info");

    private String[] messages;

    public InfoMessage(String path) {
        Config messagesConfig = Config.MESSAGES_CONFIGURATION;
        this.messages = new String[1];
        this.messages[0] = messagesConfig.getString("info." + path);
        super.setStrings(this.messages);
    }

    public InfoMessage(Message[] messages) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i<messages.length; i++) {
            String[] strings1 = messages[i].getStrings();
            strings.addAll(Arrays.asList(strings1));
        }
        this.messages = strings.toArray(new String[0]);
        super.setStrings(this.messages);
    }

    public static Message PLAYER_NAME(String name) {
        return new InfoMessage("player_name").format(name);
    }

    public static Message PLAYER_CITY(String name) {
        return new InfoMessage("player_city").format(name);
    }

    public static Message PLAYER_NATION(String name) {
        return new InfoMessage("player_nation").format(name);
    }

    public static Message PLAYER_FRIENDS(String friendsNames) {
        return new InfoMessage("player_friends").format(friendsNames);
    }

    public static Message PLAYER_INVITES(String invitingNames) {
        return new InfoMessage("player_invites").format(invitingNames);
    }

    public static Message PLAYER_REQUESTS(String requestingNames) {
        return new InfoMessage("player_requests").format(requestingNames);
    }

    public static Message ANCAPSTATES_VERSION(String version) {
        return new InfoMessage("ancapstates_version").format(version);
    }

    public static Message CITY_BOARD(String board) {
        return new InfoMessage("city_board").format(board);
    }

    public static Message CITY_NAME(String name) {
        return new InfoMessage("city_name").format(name);
    }

    public static Message CITY_MAYOR(String name) {
        return new InfoMessage("city_mayor").format(name);
    }

    public static Message CITY_RESIDENTS(int residentsAmount, String residentsNames) {
        return new InfoMessage("city_residents").format(String.valueOf(residentsAmount), residentsNames);
    }

    public static Message CITY_ASSISTANTS(int assistantsAmount, String assistantsNames) {
        return new InfoMessage("city_assistants").format(String.valueOf(assistantsAmount), assistantsNames);
    }

    public static Message CITY_ALLOWLEVEL(int anInt) {
        return new InfoMessage("city_allowlevel").format(String.valueOf(anInt));
    }

    public static Message NATION_NAME(String name) {
        return new InfoMessage("nation_name").format(name);
    }

    public static Message NATION_BOARD(String board) {
        return new InfoMessage("nation_board").format(board);
    }

    public static Message NATION_CAPITAL(String name) {
        return new InfoMessage("nation_capital").format(name);
    }

    public static Message NATION_CITIES(int citiesAmount, String citiesString) {
        return new InfoMessage("nation_cities").format(String.valueOf(citiesAmount), citiesString);
    }

    public static Message NATION_MINISTERS(int ministersAmount, String ministersString) {
        return new InfoMessage("nation_ministers").format(String.valueOf(ministersAmount), ministersString);
    }

    public static Message CITY_NATION(String name) {
        return new InfoMessage("city_nation").format(name);
    }

    public static Message HERE_CITY(String name) {
        return new InfoMessage("here_city").format(name);
    }

    public static Message HERE_OUTPOST(String outpostChunkOwnerStatus) {
        return new InfoMessage("here_outpost").format(outpostChunkOwnerStatus);
    }

    public static Message HERE_PRIVATE_CHUNK(String privateChunkOwnerStatus) {
        return new InfoMessage("here_private_chunk").format(privateChunkOwnerStatus);
    }
}
