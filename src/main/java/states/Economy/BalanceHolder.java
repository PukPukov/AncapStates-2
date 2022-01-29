package states.Economy;

public interface BalanceHolder {

    public Balance getBalance();

    public void setBalance(Balance balance);

    public default void transferMoney(BalanceHolder recipient, Balance balance) {
        Balance nationBalance = this.getBalance();
        Balance recipientBalance = recipient.getBalance();
        nationBalance.remove(balance);
        recipientBalance.add(balance);
        this.setBalance(nationBalance);
        recipient.setBalance(balance);
    }
}
