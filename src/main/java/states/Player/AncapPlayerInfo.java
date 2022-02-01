package states.Player;

import states.States.City.City;
import states.Economy.Balance;
import states.Message.InfoMessage;
import states.Message.Message;
import states.States.Nation.Nation;

public class AncapPlayerInfo {

    String name;
    City city;
    String cityName;
    Nation nation;
    Balance balance;
    String nationName;
    String friendsNames;
    String invitingNames;
    String requestingNames;

    public AncapPlayerInfo(AncapPlayer player) {
        this.name = player.getName();
        this.city = player.getCity();
        if (this.city != null) {
            this.cityName = this.city.getName();
        } else {
            this.cityName = "отсутствует";
        }
        this.nation = player.getNation();
        if (this.nation != null) {
            this.nationName = this.nation.getName();
        } else {
            this.nationName = "отсутствует";
        }
        this.friendsNames = player.getFriendsNames();
        this.invitingNames = player.getInvitingNames();
        this.requestingNames = player.getRequestingNames();
        this.balance = player.getBalance();
    }

    public InfoMessage getMessage() {
        Message[] messages = new Message[9];
        Message line = InfoMessage.LINE;
        messages[0] = line;
        messages[1] = InfoMessage.PLAYER_NAME(this.name);
        messages[2] = this.balance.getMessage();
        messages[3] = InfoMessage.PLAYER_CITY(this.cityName);
        messages[4] = InfoMessage.PLAYER_NATION(this.nationName);
        messages[5] = InfoMessage.PLAYER_FRIENDS(this.friendsNames);
        messages[6] = InfoMessage.PLAYER_INVITES(this.invitingNames);
        messages[7] = InfoMessage.PLAYER_REQUESTS(this.requestingNames);
        messages[8] = line;
        InfoMessage infoMessage = new InfoMessage(messages);
        return infoMessage;
    }
}
