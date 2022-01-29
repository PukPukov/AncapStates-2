package states.Player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        AncapPlayer player = new AncapPlayer(sender.getName());
        if (args.length == 0) {
            player.sendMessage(player.getInfo().getMessage());
            return true;
        }
        if (args[0].equals("info")) {
            AncapPlayer informed = new AncapPlayer(args[1]);
            player.sendMessage(informed.getInfo().getMessage());
            return true;
        }
        return false;
    }
}