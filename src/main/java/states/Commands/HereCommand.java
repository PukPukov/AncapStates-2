package states.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import states.Here.HereInfo;
import states.Message.InfoMessage;
import states.Player.AncapStatesPlayer;

public class HereCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        HereInfo info = new HereInfo(((Player) sender).getLocation());
        InfoMessage message = info.getMessage();
        AncapStatesPlayer player = new AncapStatesPlayer((Player) sender);
        player.sendMessage(message);
        return true;
    }
}
