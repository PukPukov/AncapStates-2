package ru.ancap.states.main;

import ru.ancap.framework.communicate.message.Message;
import ru.ancap.states.AncapStates;
import ru.ancap.states.message.InfoMessage;
import ru.ancap.states.message.LayeredModifies;

import java.util.List;
import java.util.Map;

public class AncapStatesInfo {

    private final String version;

    public AncapStatesInfo() {
        this.version = AncapStates.instance().getDescription().getVersion();
    }

    public InfoMessage getMessage() {
        return new InfoMessage(
            "ancap-states",
            new LayeredModifies(Map.of()),
            new InfoMessage.Values(List.of(
                new InfoMessage.Value("developer", new Message("PukPukov")),
                new InfoMessage.Value("version", new Message(this.version))
            ))
        );
    }
}
