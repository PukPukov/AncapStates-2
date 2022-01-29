package states.Migration;

import org.bukkit.command.CommandSender;

public class MigrationThread extends Thread {

    private CommandSender sender;

    public MigrationThread(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        long date = System.currentTimeMillis();
        Migrator migrator = new Migrator();
        migrator.startMigration();
        long date1 = System.currentTimeMillis();
        long timeElapsed = date1-date;
        sender.sendMessage("Миграция завершена за "+timeElapsed+"ms");
    }
}
