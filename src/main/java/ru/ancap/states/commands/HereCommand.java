package ru.ancap.states.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ancap.states.here.HereInfo;
import ru.ancap.states.message.InfoMessage;
import ru.ancap.states.player.AncapStatesPlayer;

public class HereCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender_, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender_; 
        HereInfo info = new HereInfo(player);
        InfoMessage message = info.getMessage();
        AncapStatesPlayer caller = AncapStatesPlayer.get(player);
        caller.sendMessage(message);
        return true;
    }
}
