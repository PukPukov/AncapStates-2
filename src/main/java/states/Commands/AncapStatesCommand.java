package states.Commands;

import AncapLibrary.Economy.Balance;
import AncapLibrary.Message.Message;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import states.Database.Database;
import states.Dynmap.DynmapDrawer;
import states.Main.AncapStates;
import states.Message.ErrorMessage;
import states.Message.StateMessage;
import states.Player.AncapStatesPlayer;
import states.States.City.City;
import states.States.Nation.Nation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AncapStatesCommand implements CommandExecutor, TabCompleter {

    private Database statesDB = Database.STATES_DATABASE;

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<AncapStatesPlayer> onlinePlayers = List.of(AncapStates.getPlayerMap().getOnlinePlayers());
        List<City> cities = List.of(AncapStates.getCityMap().getCities());
        List<Nation> nations = List.of(AncapStates.getCityMap().getNations());
        List<String> citiesNames = new ArrayList<>();
        List<String> nationsNames = new ArrayList<>();
        List<String> onlinePlayersNames = new ArrayList<>();
        List<String> subcommands = new ArrayList(Arrays.asList(
                "exchange",
                "withdraw",
                "send",
                "stats",
                "top"
        ));
        if (args.length == 1) {
            return subcommands;
        }
        if (args[0].equals("exchange")) {
            return List.of("");
        }
        if (args[0].equals("withdraw")) {
            if (args.length == 2) {
                List<String> firstLayerWithdrawSubcommands = new ArrayList(Arrays.asList(
                        "iron",
                        "diamond",
                        "netherite"
                ));
                return firstLayerWithdrawSubcommands;
            }
            if (args.length == 3) {
                return List.of("<число>");
            }
        }
        if (args[0].equals("send")) {
            if (args.length == 2) {
                return onlinePlayersNames;
            }
            if (args.length == 3) {
                List<String> types = new ArrayList(Arrays.asList(
                        "iron",
                        "diamond",
                        "netherite"
                ));
                return types;
            }
            if (args.length == 4) {
                return List.of("<число>");
            }
        }
        if (args[0].equals("stats")) {
            return List.of("");
        }
        if (args[0].equals("top")) {
            if (args.length == 2) {
                List<String> first = new ArrayList(Arrays.asList(
                        "cities",
                        "nations"
                ));
                return first;
            }
            if (args.length == 3) {
                List<String> second = new ArrayList(Arrays.asList(
                        "population",
                        "territory"
                ));
                return second;
            }
        }
        return null;
    }

    @Override
    public synchronized boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        AncapStatesPlayer player = new AncapStatesPlayer(sender.getName());
        if (args.length == 0) {
            player.sendMessage(AncapStates.getInfo().getMessage());
            return true;
        }
        if (sender.isOp()) {
            if (args[0].equals("redraw")) {
                DynmapDrawer.redrawDynmap();
                return true;
            }
            if (args[0].equals("newday")) {
                AncapStates.startNewDay();
                return true;
            }
            if (args[0].equals("savedb")) {
                statesDB.save();
            }
        }
        if (args[0].equals("info")) {
            return true;
        }
        if (args[0].equals("exchange")) {
            ItemStack stack = player.getPlayer().getItemInHand();
            Material material = stack.getType();
            int count = stack.getAmount();
            Balance balance = player.getBalance();
            String type;
            if (material.equals(Material.DIAMOND)) {
                balance.addDiamond(count);
                type = "diamond";
            } else if (material.equals(Material.NETHERITE_INGOT)) {
                balance.addNetherite(count);
                type = "netherite_ingot";
            } else if (material.equals(Material.IRON_INGOT)) {
                balance.addIron(count);
                type = "iron_ingot";
            } else {
                Message message = ErrorMessage.YOU_CANT_DEPOSIT_THIS_TYPE;
                player.sendMessage(message);
                return true;
            }
            player.setBalance(balance);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:clear "+player.getName()+" "+type+" "+count);
            Message message = StateMessage.DEPOSITED(String.valueOf(count), type);
            player.sendMessage(message);
            return true;
        }
        if (args[0].equals("send")) {
            if (args.length<4) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String type = args[2];
            if (!NumberUtils.isNumber(args[3]) || (!type.equals("iron") && !type.equals("diamond") && !type.equals("netherite"))) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            double amount = Double.parseDouble(args[3]);
            AncapStatesPlayer recipient = new AncapStatesPlayer(args[1]);
            if (player.getBalance().getAmountForType(type) < amount) {
                Message message = ErrorMessage.NOT_ENOUGH_MONEY;
                player.sendMessage(message);
                return true;
            }
            if (recipient.equals(player)) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            if (recipient.getPlayer() == null) {
                Message message = ErrorMessage.CANT_SEND_TO_OFFLINE_PLAYER;
                player.sendMessage(message);
                return true;
            }
            if (recipient.getPlayer().getLocation().distance(player.getPlayer().getLocation()) > 10) {
                Message message = ErrorMessage.CANT_SEND_SO_FAR;
                player.sendMessage(message);
                return true;
            }
            player.transferMoney(recipient, amount, type);
            Message recipientMessage = StateMessage.PLAYER_RECIEVED_MONEY(player.getName(), amount, type);
            recipient.sendMessage(recipientMessage);
            Message senderMessage = StateMessage.PLAYER_SENDED_MONEY(recipient.getName(), amount, type);
            player.sendMessage(senderMessage);
            return true;
        }
        if (args[0].equals("withdraw")) {
            if (args.length<3) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String type = args[1];
            if (!NumberUtils.isNumber(args[2]) || (!type.equals("iron") && !type.equals("diamond") && !type.equals("netherite"))) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            int amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            if (player.getBalance().getAmountForType(type) < amount) {
                Message message = ErrorMessage.NOT_ENOUGH_MONEY;
                player.sendMessage(message);
                return true;
            }
            Balance balance = player.getBalance();
            if (type.equals("iron")) {
                balance.removeIron(amount);
                type = "iron_ingot";
            }
            if (type.equals("netherite")) {
                balance.removeNetherite(amount);
                type = "netherite_ingot";
            }
            if (type.equals("diamond")) {
                balance.removeDiamond(amount);
            }
            player.setBalance(balance);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+player.getName()+" "+type+" "+amount);
            Message message = StateMessage.WITHDRAWED(String.valueOf(amount), type);
            player.sendMessage(message);
            return true;
        }
        if (args[0].equals("stats")) {
            String border = "§8-------------------------------------------------------";
            sender.sendMessage(border);
            sender.sendMessage("§6Статистика плагина AncapStates");
            sender.sendMessage("");
            sender.sendMessage("§6Всего городов§8: §7"+AncapStates.getCityMap().getCities().length);
            sender.sendMessage("§6Всего наций§8: §7"+AncapStates.getCityMap().getNations().length);
            sender.sendMessage(border);
            return true;
        }
        if (args[0].equals("top")) {
            if (args[1].equals("cities")) {
                if (args[2].equals("population")) {

                }
                if (args[2].equals("territories")) {

                }
            }
            if (args[1].equals("nation")) {
                if (args[2].equals("population")) {

                }
                if (args[2].equals("territories")) {

                }
            }
        }
        return false;
    }
}
