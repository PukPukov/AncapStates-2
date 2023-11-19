package ru.ancap.states.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender_, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        Player player = (Player) sender_; 
        AncapStatesPlayer caller = AncapStatesPlayer.get(player);
        if (args.length == 0) {
            caller.sendMessage(caller.getInfo().getMessage());
            return true;
        }
        if (args[0].equals("info")) {
            AncapStatesPlayer informed = AncapStatesPlayer.findByNameFor(args[1], caller);
            caller.sendMessage(informed.getInfo().getMessage());
            return true;
        }
        return false;
    }
}