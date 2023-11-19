package ru.ancap.states.states.Nation;

import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.ModifierBundle;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.library.Balance;
import ru.ancap.states.AncapStates;
import ru.ancap.states.message.InfoMessage;
import ru.ancap.states.message.LayeredModifies;
import ru.ancap.states.message.PopulationValue;
import ru.ancap.states.message.TerritoryValue;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.city.StateBalanceMessage;

import java.util.List;
import java.util.Map;

public class NationInfo {

    private final String name;
    private final String capitalName;
    private final String leaderName;
    private final @Nullable String board;
    private final String citiesNames;
    private final @Nullable String ministersString;
    private final int citiesAmount;
    private final int ministersAmount;
    private final Balance balance;
    private final Balance taxes;
    private final int territoriesSize;
    private final int population;

    public NationInfo(Nation nation) {
        this.name = nation.getName();
        this.board = nation.getBoard();
        this.capitalName = nation.getCapital().getName();
        this.leaderName = nation.getCapital().getMayor().getName();
        this.citiesNames = nation.getCities().stream()
            .map(City::getName)
            .reduce((one, two) -> one + ", " +two)
            .orElseThrow();
        this.citiesAmount = nation.getCities().size();
        this.ministersString = nation.getMinisters().stream()
            .map(AncapStatesPlayer::getName)
            .reduce((one, two) -> one + ", " +two)
            .orElse(null);
        this.ministersAmount = nation.getMinisters().size();
        this.balance = nation.getBalance();
        this.taxes = nation.getTax();
        this.territoriesSize = nation.getTerritories().size();
        this.population = nation.getResidents().size();
    }

    public CallableMessage getMessage() {
        return new InfoMessage(
            "nation",
            new LayeredModifies(Map.of(
                InfoMessage.DATA_LAYER, new ModifierBundle(List.of(
                    new Placeholder("name", this.name),
                    new Placeholder("board", this.board != null ? new Message(this.board) : new LAPIMessage(AncapStates.class, "board.not-setted"))
                ))
            )),
            new InfoMessage.Values(List.of(
                new InfoMessage.Value("treasury", new StateBalanceMessage(this.balance)),
                new InfoMessage.Value("taxes", new StateBalanceMessage(this.taxes)),
                new InfoMessage.Value("capital", new Message(this.capitalName)),
                new InfoMessage.Value("leader", new Message(this.leaderName)),
                new TerritoryValue(this.territoriesSize),
                new PopulationValue(this.population),
                new InfoMessage.CountedValue("ministers", this.ministersAmount, this.ministersString != null ?
                    new Message(this.ministersString) :
                    new LAPIMessage(AncapStates.class, "empty-many")
                ),
                new InfoMessage.CountedValue("cities", this.citiesAmount, new Message(this.citiesNames))
            ))
        );
    }
    
}
