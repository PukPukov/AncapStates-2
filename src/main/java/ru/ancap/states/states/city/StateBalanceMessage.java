package ru.ancap.states.states.city;

import ru.ancap.framework.communicate.message.AtNextLine;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.WrapperMessage;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.library.Balance;
import ru.ancap.library.BalanceMessage;
import ru.ancap.states.AncapStates;

public class StateBalanceMessage extends WrapperMessage {
    
    public StateBalanceMessage(Balance balance) {
        super(constructorHelper(balance));
    }

    private static CallableMessage constructorHelper(Balance balance) {
        if (balance.isEmpty()) return new LAPIMessage(AncapStates.class, "balance.empty");
        else return new AtNextLine(new BalanceMessage(balance));
    }

}
