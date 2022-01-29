package states.States;

import org.bukkit.Bukkit;
import states.Message.InfoMessage;
import states.Message.Message;

public class AncapStatesInfo {

    private final String version;

    public AncapStatesInfo() {
        this.version = Bukkit.getPluginManager().getPlugin("AncapStates").getDescription().getVersion();
    }

    public InfoMessage getMessage() {
        Message[] messages = new Message[3];
        Message line = InfoMessage.LINE;
        messages[0] = line;
        messages[1] = InfoMessage.ANCAPSTATES_VERSION(this.version);
        messages[2] = line;
        InfoMessage infoMessage = new InfoMessage(messages);
        return infoMessage;
    }
}
