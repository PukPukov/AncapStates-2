package ru.ancap.states.player;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.library.Balance;
import ru.ancap.library.BalanceHolder;
import ru.ancap.states.AncapStates;

import java.util.HashMap;

public class AncapPlayer implements BalanceHolder {
    
    private final @NotNull  String id;
    private final @Nullable String realName;
    
    private final PathDatabase statesDB = AncapStates.getMainDatabase();
    
    @SneakyThrows
    public AncapPlayer(Player player) {
        this(Identifier.of(player), player.getName());
    }
    
    protected AncapPlayer(String id) {
        this.id = id;
        this.realName = null;
    }
    
    protected AncapPlayer(String id, String name) {
        this.id = id;
        this.realName = name;
        this.setup();
    }
    
    public void setMeta(String field, String str) {
        this.statesDB.write("states.player." + this.getID() + "." + field, str);
    }
    
    public String getMeta(String field) {
        PathDatabase var10000 = this.statesDB;
        String var10001 = this.getID();
        return var10000.readString("states.player." + var10001 + "." + field);
    }
    
    public Balance getBalance() {
        return new Balance(this);
    }
    
    public void setBalance(Balance balance) {
        this.statesDB.write("states.player." + this.getID() + ".balance", balance, Balance.SERIALIZE_WORKER);
    }
    
    public Location getLocation() {
        return this.online().getLocation();
    }
    
    public void sendMessage(CallableMessage message) {
        if (this.online() == null) return;
        Communicator.of(this.online()).message(message);
    }
    
    public void sendMessage(String string) {
        try {
            Player p = this.online();
            p.sendMessage(string.replace("&", "§"));
        } catch (Exception var3) {
        }

    }
    
    public void playSound(Sound sound) {
        try {
            Player p = this.online();
            p.playSound(p.getLocation(), sound, 1000.0F, 1.0F);
        } catch (Exception ignored) {
        }

    }
    
    @Nullable
    public Player online() {
        return Bukkit.getPlayer(this.getName());
    }
    
    public String getName() {
        if (this.realName != null) {
            return this.realName;
        }
        Player player = Bukkit.getPlayer(this.getID());
        if (player != null) {
            return player.getName();
        }
        String name = this.statesDB.readString("states.player." + this.id + ".name");
        if (name != null) {
            return name;
        }
        return this.id;
    }
    
    public void setup() {
        if (!this.created()) {
            this.statesDB.write("states.player." + this.id + ".id", this.id);
            this.tryFillName();
            this.statesDB.write("states.player." + this.id + ".balance", new Balance(new HashMap<>()), Balance.SERIALIZE_WORKER);
        }
        if (!this.realName()) this.tryFillName();
    }

    private void tryFillName() {
        if (this.realName != null) {
            this.statesDB.write("states.player." + this.id + ".name", this.realName);
            this.statesDB.write("states.player." + this.id + ".real-name", true);
        } else {
            Player online = Bukkit.getPlayer(this.id);
            if (online != null) {
                this.statesDB.write("states.player." + this.id + ".name", online.getName());
                this.statesDB.write("states.player." + this.id + ".real-name", true);
            } else {
                this.statesDB.write("states.player." + this.id + ".name", this.id);
                this.statesDB.write("states.player." + this.id + ".real-name", false);
            }
        }
    }

    private boolean realName() {
        return this.statesDB.readBoolean("states.player." + this.id + ".real-name");
    }

    public String getID() {
        return this.id;
    }
    
    public boolean created() {
        return this.statesDB.isSet("states.player." + this.id);
    }
    
    public void updateName() {
        this.statesDB.write("states.player." + this.id + ".name", this.online().getName());
    }
    
    public String toString() {
        return "AncapPlayer{" + this.getID() + "}";
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!AncapPlayer.class.isAssignableFrom(obj.getClass())) {
            return false;
        } else {
            AncapPlayer other = (AncapPlayer)obj;
            return other.getID().equals(this.getID());
        }
    }
    
    public boolean haveFlag(String string) {
        return this.statesDB.contains("states.player."+this.getID()+".flags", string, true);
    }

    @Override
    public PathDatabase database() {
        return this.statesDB.inner("states.player."+this.getID());
    }

    @Override
    public Balance balance() {
        return this.getBalance();
    }

}
