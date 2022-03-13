package states.States;

import AncapLibrary.Library.BalanceHolder;
import AncapLibrary.Message.Message;

public interface AncapState extends BalanceHolder {

    void sendMessage(Message message);

}
