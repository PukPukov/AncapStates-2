package ru.ancap.states.states;

import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.communicate.message.CallableMessage;

public interface Subject {

    String type();
    String id();

    void affiliate(@Nullable Subject affiliate);
    Subject affiliate();
    void sendMessage(CallableMessage message);
    String simpleName();
    
}