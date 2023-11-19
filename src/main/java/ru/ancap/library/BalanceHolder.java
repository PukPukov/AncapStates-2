package ru.ancap.library;

import ru.ancap.framework.database.nosql.PathDatabase;

public interface BalanceHolder {
    
    PathDatabase database();
    
    Balance balance();
    void setBalance(Balance balance);
    
    default void transferMoney(BalanceHolder other, Balance amount) {
        Balance thisBalance = this.balance();
        Balance otherBalance = other.balance();
        thisBalance.remove(amount);
        otherBalance.add(amount);
        this.setBalance(thisBalance);
        other.setBalance(otherBalance);
    }
    
}
