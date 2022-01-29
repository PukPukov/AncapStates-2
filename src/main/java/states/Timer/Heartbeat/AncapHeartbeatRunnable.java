package states.Timer.Heartbeat;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AncapHeartbeatRunnable extends BukkitRunnable {
    @Override
    public void run() {
        AncapHeartbeatEvent event = new AncapHeartbeatEvent();
        Bukkit.getPluginManager().callEvent(event);
    }
}
