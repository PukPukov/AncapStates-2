package states.States.City;

import AncapLibrary.Economy.Balance;
import AncapLibrary.Message.Message;
import states.Dynmap.DynmapDescription;
import states.Message.InfoMessage;
import states.Player.AncapStatesPlayer;
import states.States.Nation.Nation;

public class CityInfo {



    private String name;
    private int residentsAmount;
    private AncapStatesPlayer mayor;
    private AncapStatesPlayer[] residents;
    private Nation nation;
    private String nationName;
    private String residentsNames;
    private int assistantsAmount;
    private String assistantsNames;
    private AllowLevel allowLevel;
    private String board;
    private Balance balance;
    private Balance taxes;

    public CityInfo(City city) {
        this.name = city.getName();
        this.residentsAmount = city.getResidents().length;
        this.nation = city.getNation();
        if (nation != null) {
            nationName = nation.getName();
        } else {
            nationName = "отсутствует";
        }
        this.mayor = city.getMayor();
        this.residents = city.getResidents();
        residentsNames = this.mayor.getName();
        for (int i=1; i<residents.length; i++) {
            this.residentsNames = this.residentsNames+", "+residents[i].getName();
        }
        this.assistantsNames = city.getAssistantsNames();
        if (assistantsNames == null) {
            assistantsNames = "отсутствует";
        }
        this.assistantsAmount = city.getAssistants().length;
        this.allowLevel = city.getAllowLevel();
        this.board = city.getBoard();
        if (board == null) {
            board = "Сообщение не установлено";
        }
        this.balance = city.getBalance();
        this.taxes = city.getTax();
    }

    public InfoMessage getMessage() {
        Message[] messages = new Message[13];
        Message line = InfoMessage.LINE;
        Message space = InfoMessage.SPACE;
        messages[0] = line;
        messages[1] = InfoMessage.CITY_NAME(this.name);
        messages[2] = space;
        messages[3] = InfoMessage.CITY_BOARD(this.board);
        messages[4] = space;
        messages[5] = balance.getMessage();
        messages[6] = taxes.getTaxMessage();
        messages[7] = InfoMessage.CITY_MAYOR(this.mayor.getName());
        messages[8] = InfoMessage.CITY_NATION(this.nationName);
        messages[9] = InfoMessage.CITY_RESIDENTS(this.residentsAmount, this.residentsNames);
        messages[10] = InfoMessage.CITY_ASSISTANTS(this.assistantsAmount, this.assistantsNames);
        messages[11] = InfoMessage.CITY_ALLOWLEVEL(this.allowLevel.getInt());
        messages[12] = line;
        InfoMessage infoMessage = new InfoMessage(messages);
        return infoMessage;
    }

// заебало меня это
// пошло в пизду
// не будет интернационализации

    public DynmapDescription toDynmapFormat() {
        String[] strings = new String[10];
        strings[0] = "<center><strong>"+this.name+"</strong></center>";
        strings[1] = "<center><i>"+this.board+"</i></center>";
        strings[2] = "Мэр города: "+this.mayor.getName()+"";
        strings[3] = "Казна: ";
        strings[4] = "       <span style=\"color: #333333;\">Незерита: <span style=\"color: #000000;\"><em>"+this.balance.getNetherite()+"</em></span></span>";
        strings[5] = "       <span style=\"color: #34d4f7;\">Алмазов: <span style=\"color: #000000;\"><em>"+this.balance.getDiamond()+"</em></span></span>";
        strings[6] = "       <span style=\"color: #c2c2c2;\">Железа: <span style=\"color: #000000;\"><em>"+this.balance.getIron()+"</em></span></span>";
        strings[7] = "Нация: "+this.nationName;
        strings[8] = "Ассистенты ("+assistantsAmount+"): "+this.assistantsNames;
        strings[9] = "Жители ("+residentsAmount+"): "+this.residentsNames;
        return new DynmapDescription(this.name, strings);
    }
}