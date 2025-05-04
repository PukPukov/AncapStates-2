package ru.ancap.states.commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ancap.states.AncapStates;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender_, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender_; 
        if (!player.isOp()) return false;
        if (args[0].equals("chunk")) {
            Chunk chunk = player.getLocation().getChunk();
            player.sendMessage(chunk.getX()+";"+chunk.getZ());
        }
        if (args[0].equals("msg1")) {
            Bukkit.broadcastMessage("§6§lГосударства >> §fНация Сотрапия была разрушена нацией Бретонь");
        }
        if (args[0].equals("on")) {
            AncapStates.instance().enableTest();
        }
        if (args[0].equals("off")) {
            AncapStates.instance().disableTest();
        }

        return true;
    }
}