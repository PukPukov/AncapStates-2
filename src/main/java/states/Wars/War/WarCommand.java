package states.Wars.War;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import states.Wars.WarHexagons.WarHexagon;
import states.Wars.WarPlayers.AncapStatesWarrior;

public class WarCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        AncapStatesWarrior warrior = new AncapStatesWarrior(sender.getName());
        WarHexagon hexagon = warrior.getHexagon();
        if (args.length == 0) {

        }
        if (args[0].equals("declare")) {
            if (!warrior.canDeclareWar(hexagon)) {
                return true;
            }
        }
        if (args[0].equals("peace")) {
            if (!warrior.canOfferPeace(hexagon)) {
                return true;
            }
        }
        if (args[0].equals("attack")) {
            if (!warrior.canAttack(hexagon)) {
                return true;
            }
        }
        return false;
    }
}
