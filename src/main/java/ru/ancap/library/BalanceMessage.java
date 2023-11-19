package ru.ancap.library;

import ru.ancap.framework.communicate.message.ChatBook;
import ru.ancap.framework.communicate.message.ColoredMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.message.WrapperMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.states.AncapStates;

public class BalanceMessage extends WrapperMessage {
    
    public BalanceMessage(Balance balance) {
        super(new ChatBook<>(balance.data().entrySet(), entry -> new LAPIMessage(
            AncapStates.class, "balance.form-entry",
            new Placeholder("name", new ColoredMessage(
                new LAPIMessage(AncapStates.class, "balance.currencies."+entry.getKey()+".name"),
                new LAPIMessage(AncapStates.class, "balance.currencies."+entry.getKey()+".color")
            )),
            new Placeholder("amount", new Message(entry.getValue()))
        )));
    }
    
}
