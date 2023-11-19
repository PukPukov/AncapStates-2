package ru.ancap.states.message;

import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.WrapperMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.states.AncapStates;

public class StateMessage extends WrapperMessage {
    
    public StateMessage(CallableMessage inner) {
        super(new LAPIMessage(
            AncapStates.class, "state.form",
            new Placeholder("message", inner)
        ));
    }
    
}
