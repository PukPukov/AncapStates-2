package states.Message;

import org.bukkit.Bukkit;

public class Message {

    private String[] messages;

    public Message(String[] messages) {
        this.messages = messages;
    }

    public Message() {
    }

    public String[] getStrings() {
        return this.messages;
    }

    protected Message format(String... strings) {
        for (int i = 0; i<this.messages.length; i++) {
            for (int j = 0; j<strings.length; j++) {
                if ((this.messages[i]) == null) {
                    continue;
                }
                if (strings[j] == null) {
                    strings[j] = "нет";
                }
                this.messages[i] = this.messages[i].replaceFirst("%s", strings[j]);
            }
        }
        return new Message(this.messages);
    }

    protected void setStrings(String[] messages) {
        this.messages = messages;
    }
}
