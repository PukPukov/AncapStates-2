package ru.ancap.states.states.city;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.null_.SafeNull;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.ModifierBundle;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.library.Balance;
import ru.ancap.states.AncapStates;
import ru.ancap.states.dynmap.DynmapDescription;
import ru.ancap.states.message.InfoMessage;
import ru.ancap.states.message.LayeredModifies;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.Nation.Nation;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CityInfo {

    private final String name;
    private final int residentsAmount;
    private final String mayorName;
    private final @Nullable String nationName;
    private final String residentsNames;
    private final int assistantsAmount;
    private final @Nullable String assistantsNames;
    private final AllowLevel allowLevel;
    private final @Nullable String board;
    private final Balance balance;
    private final Balance taxes;
    private final int hexagons;

    public CityInfo(City city) {
        this.name = city.getName();
        this.residentsAmount = city.getResidents().size();
        this.nationName = SafeNull.function(city.getNation(), Nation::getName);
        this.mayorName = city.getMayor().getName();
        String residentsNames = this.mayorName;
        List<AncapStatesPlayer> residents = city.getResidents();
        for (int i=1; i<residents.size(); i++) {
            residentsNames = residentsNames+", "+residents.get(i).getName();
        }
        this.hexagons = city.getTerritories().size();
        this.residentsNames = residentsNames;
        this.assistantsNames = city.getAssistants().stream()
            .map(AncapStatesPlayer::getName)
            .reduce((assistant1, assistant2) -> assistant1 + ", " + assistant2)
            .orElse(null);
        this.assistantsAmount = city.getAssistants().size();
        this.allowLevel = city.getAllowLevel();
        this.board = city.getBoard();
        this.balance = city.getBalance();
        this.taxes = city.getTax();
    }

    public CallableMessage getMessage() {
        return new InfoMessage(
            "city",
            new LayeredModifies(Map.of(
                InfoMessage.DATA_LAYER, new ModifierBundle(List.of(
                    new Placeholder("name", this.name),
                    new Placeholder("board", this.board != null ? new Message(this.board) : new LAPIMessage(AncapStates.class, "board.not-setted"))
                ))
            )),
            new InfoMessage.Values(List.of(
                new InfoMessage.Value("treasury", new StateBalanceMessage(this.balance)),
                new InfoMessage.Value("taxes", new StateBalanceMessage(this.taxes)),
                new InfoMessage.Value("mayor", new Message(this.mayorName)),
                new InfoMessage.UnitValue("territory", new InfoMessage.UnitValue.Amount("hexagons", this.hexagons)),
                new InfoMessage.Value("nation", this.nationName != null ? new Message(this.nationName) : new LAPIMessage(AncapStates.class, "empty-one")),
                new InfoMessage.CountedValue("residents", this.residentsAmount, new Message(this.residentsNames)),
                new InfoMessage.CountedValue("assistants", this.assistantsAmount, this.assistantsNames != null ? 
                    new Message(this.assistantsNames) : 
                    new LAPIMessage(AncapStates.class, "empty-many")
                ),
                new InfoMessage.Value("allow-level", new Message(this.allowLevel.getInt()))
            ))
        );
    }

// заебало меня это
// пошло в пизду
// не будет интернационализации
    
// Спустя 1,5 года
// Будет.

    public DynmapDescription toDynmapFormat() {
        String board = this.board != null ? this.board : "Сообщение не установлено";
        String assistantsNames = this.assistantsNames != null ? this.assistantsNames : "отсутствуют";
        String[] strings = new String[10];
        strings[0] = "<center><strong>"+this.name+"</strong></center>";
        strings[1] = "<center><i>"+DynmapDescription.removeHtmlTags(board)+"</i></center>";
        strings[2] = "Мэр города: "+this.mayorName+"";
        strings[3] = "Казна: ";
        strings[4] = "       <span style=\"color: #333333;\">Незерита: <span style=\"color: #000000;\"><em>"+this.balance.getNetherite()+"</em></span></span>";
        strings[5] = "       <span style=\"color: #34d4f7;\">Алмазов: <span style=\"color: #000000;\"><em>"+this.balance.getDiamond()+"</em></span></span>";
        strings[6] = "       <span style=\"color: #c2c2c2;\">Железа: <span style=\"color: #000000;\"><em>"+this.balance.getIron()+"</em></span></span>";
        strings[7] = "Нация: "+this.nationName;
        strings[8] = "Ассистенты ("+this.assistantsAmount+"): "+assistantsNames;
        strings[9] = "Жители ("+this.residentsAmount+"): "+this.residentsNames;
        return new DynmapDescription(this.name, strings);
    }
    
}