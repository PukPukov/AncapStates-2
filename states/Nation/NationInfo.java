package states.Nation;

import states.City.City;
import states.Economy.Balance;
import states.Message.InfoMessage;
import states.Message.Message;
import states.Player.AncapPlayer;

public class NationInfo {

    private String name;
    private City capital;
    private String board;
    private City[] cities;
    private String citiesString;
    private int citiesAmount;
    private String ministersString;
    private int ministersAmount;
    private Balance balance;
    private Balance taxes;

    public NationInfo(Nation nation) {
        name = nation.getName();
        capital = nation.getCapital();
        board = nation.getBoard();
        if (board == null) {
            board = "Сообщение не установлено";
        }
        City[] cities = nation.getCities();
        citiesString = cities[0].getName();
        for (int i=1; i<cities.length; i++) {
            this.citiesString = this.citiesString+", "+cities[i].getName();
        }
        citiesAmount = nation.getCities().length;
        ministersString = nation.getMinistersString();
        if (ministersString == null) {
            ministersString = "отсутствует";
        }
        ministersAmount = nation.getMinisters().length;
        balance = nation.getBalance();
        taxes = nation.getTax();
    }

    public Message getMessage() {
        Message[] messages = new Message[11];
        Message line = InfoMessage.LINE;
        Message space = InfoMessage.SPACE;
        messages[0] = line;
        messages[1] = InfoMessage.NATION_NAME(this.name);
        messages[2] = space;
        messages[3] = InfoMessage.NATION_BOARD(this.board);
        messages[4] = space;
        messages[5] = balance.getMessage();
        messages[6] = taxes.getTaxMessage();
        messages[7] = InfoMessage.NATION_CAPITAL(this.capital.getName());
        messages[8] = InfoMessage.NATION_CITIES(this.citiesAmount, this.citiesString);
        messages[9] = InfoMessage.NATION_MINISTERS(this.ministersAmount, this.ministersString);
        messages[10] = line;
        InfoMessage infoMessage = new InfoMessage(messages);
        return infoMessage;
    }
}
