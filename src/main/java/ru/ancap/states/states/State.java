package ru.ancap.states.states;

import ru.ancap.library.BalanceHolder;

public interface State extends Subject, BalanceHolder {
    
    StateName name();

}
