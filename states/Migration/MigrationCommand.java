package states.Migration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MigrationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("AncapStates migration utility");
            return true;
        }
        if (sender.isOp()) {
            MigrationThread migrationThread = new MigrationThread(sender);
            if (args[0].equals("start")) {
                sender.sendMessage("Начат процесс миграции...");
                migrationThread.start();
            }
            if (args[0].equals("abort")) {
                migrationThread.stop();
                sender.sendMessage("Процесс миграции оборван");
            }
        }
        return true;
    }
}
