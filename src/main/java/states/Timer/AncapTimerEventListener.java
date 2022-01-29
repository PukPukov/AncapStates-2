package states.Timer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import states.Timer.Heartbeat.AncapHeartbeatEvent;
import states.Timer.TimerEvents.FastTimerTenMinutesEvent;
import states.Timer.TimerEvents.FastTimerTenSecondEvent;
import states.Timer.TimerEvents.SafeTimerOneDayEvent;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AncapTimerEventListener implements Listener {

    private int tenSecondsCount = 0;
    private int tenMinutesCount = 0;

    private int day;

    private final FastTimerTenSecondEvent fastTimerTenSecondEvent = new FastTimerTenSecondEvent();
    private final FastTimerTenMinutesEvent fastTimerTenMinutesEvent = new FastTimerTenMinutesEvent();
    private final SafeTimerOneDayEvent safeTimerOneDayEvent = new SafeTimerOneDayEvent();

    private final Calendar calendar = new GregorianCalendar();

    @EventHandler
    public void onHeartbeat(AncapHeartbeatEvent e) {
        if (this.tenSecondsCount>10) {
            Bukkit.getPluginManager().callEvent(fastTimerTenSecondEvent);
            this.tenSecondsCount = 0;
        }
        if (this.tenMinutesCount>600) {
            Bukkit.getPluginManager().callEvent(fastTimerTenMinutesEvent);
            this.tenMinutesCount = 0;
        }
        if (this.getDay() != this.day) {
            Bukkit.getPluginManager().callEvent(safeTimerOneDayEvent);
            this.day = this.getDay();
        }
        this.tickTimers();
    }

    private void tickTimers() {
        this.tenSecondsCount = this.tenSecondsCount+1;
        this.tenMinutesCount = this.tenMinutesCount+1;
    }

    private int getDay() {
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
}
