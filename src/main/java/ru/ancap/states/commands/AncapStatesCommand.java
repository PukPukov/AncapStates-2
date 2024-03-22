package ru.ancap.states.commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.MultilineMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.library.Balance;
import ru.ancap.library.BalanceMessage;
import ru.ancap.states.AncapStates;
import ru.ancap.states.dynmap.DynmapDrawer;
import ru.ancap.states.fees.ASFees;
import ru.ancap.states.id.ID;
import ru.ancap.states.message.ErrorMessage;
import ru.ancap.states.message.LStateMessage;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.Nation.Nation;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.city.RequestState;

import java.util.ArrayList;
import java.util.List;

public class AncapStatesCommand implements CommandExecutor, TabCompleter {

    private PathDatabase statesDB = AncapStates.getMainDatabase();;

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<AncapStatesPlayer> onlinePlayers = List.of(AncapStates.getPlayerMap().getOnlinePlayers());
        List<City> cities = AncapStates.getCityMap().getCities();
        List<Nation> nations = AncapStates.getCityMap().getNations();
        List<String> citiesNames = new ArrayList<>();
        List<String> nationsNames = new ArrayList<>();
        List<String> onlinePlayersNames = new ArrayList<>();
        List<String> subcommands = List.of(
            "exchange",
            "withdraw",
            "send",
            "stats",
            "top",
            "create-test-city",
            "create-test-nation",
            "redraw",
            "fees",
            "admin"
        );
        if (args.length == 1) return subcommands;
        if (args[0].equals("exchange")) return List.of("");
        if (args[0].equals("withdraw")) {
            if (args.length == 2) {
                List<String> firstLayerWithdrawSubcommands = List.of(
                    "iron",
                    "diamond",
                    "netherite"
                );
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
                List<String> types = List.of(
                    "iron",
                    "diamond",
                    "netherite"
                );
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
                List<String> first = List.of(
                    "cities",
                    "nations"
                );
                return first;
            }
            if (args.length == 3) {
                List<String> second = List.of(
                    "population",
                    "territory"
                );
                return second;
            }
        }
        if (args[0].equals("admin")) {
            if (args.length == 2) {
                List<String> first = List.of(
                    "city",
                    "nation"
                );
                return first;
            }
            if (args[1].equals("city")) {
                if (args.length == 3) return List.of(
                    "addresident",
                    "mayor",
                    "delete"
                );
                if (args[2].equals("addresident")) {
                    if (args.length == 4) return List.of("<город>");
                    if (args.length == 5) return List.of("<житель>");
                }
                if (args[2].equals("mayor")) {
                    if (args.length == 4) return List.of("<город>");
                    if (args.length == 5) return List.of("<житель>");
                }
                if (args[2].equals("delete")) {
                    if (args.length == 4) return List.of("<город>");
                }
            }
            if (args[1].equals("nation")) {
                if (args.length == 3) return List.of(
                    "addcity",
                    "capital",
                    "delete"
                );
                if (args[2].equals("addcity")) {
                    if (args.length == 4) return List.of("<нация>");
                    if (args.length == 5) return List.of("<город>");
                }
                if (args[2].equals("capital")) {
                    if (args.length == 4) return List.of("<нация>");
                    if (args.length == 5) return List.of("<столица>");
                }
                if (args[2].equals("delete")) {
                    if (args.length == 4) return List.of("<нация>");
                }
            }
        }
        return null;
    }

    /**
     * You can add to this list anything
     */
    public static final List<CallableMessage> feesMessages = new ArrayList<>(List.of(
        new LAPIMessage(AncapStates.class, "fees.city-creation", new Placeholder("fee", new BalanceMessage(ASFees.CITY_CREATION))),
        new LAPIMessage(AncapStates.class, "fees.nation-creation", new Placeholder("fee", new BalanceMessage(ASFees.NATION_CREATION))),
        new LAPIMessage(AncapStates.class, "fees.hexagon-claim", new Placeholder("fee", new BalanceMessage(ASFees.HEXAGON_CLAIM))),
        new LAPIMessage(AncapStates.class, "fees.city-teleport", new Placeholder("fee", new BalanceMessage(ASFees.CITY_TELEPORT)))
    ));

    @Override
    public boolean onCommand(@NotNull CommandSender sender_, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender_; 
        AncapStatesPlayer caller = AncapStatesPlayer.get(player);
        if (args.length == 0) {
            caller.sendMessage(AncapStates.getInfo().getMessage());
            return true;
        }
        if (args[0].equals("fees")) { synchronized (feesMessages) {
            Communicator.of(sender_).message(new MultilineMessage(feesMessages.toArray(new CallableMessage[0])));
        }}
        if (player.isOp()) {
            if (args[0].equals("redraw")) {
                DynmapDrawer.redrawDynmap();
                return true;
            }
            if (args[0].equals("newday")) {
                return true;
            }
            if (args[0].equals("savedb")) {
                AncapStates.getMainDatabase().save();
            }
            if (args[0].equals("admin")) {
                if (args.length == 1) {
                    return true;
                }
                if (args[1].equals("city")) {
                    City city = new City(ID.getCityID(args[2]));
                    if (args[3].equals("addresident")) {
                        AncapStatesPlayer ancapStatesPlayer = AncapStatesPlayer.findByNameFor(args[4], caller);
                        city.addResident(ancapStatesPlayer);
                    }
                    if (args[3].equals("mayor")) {
                        AncapStatesPlayer ancapStatesPlayer = AncapStatesPlayer.findByNameFor(args[4], caller);
                        city.setMayor(ancapStatesPlayer);
                    }
                    if (args[3].equals("delete")) {
                        city.remove();
                    }
                }
                if (args[1].equals("nation")) {
                    Nation nation = new Nation(ID.getNationID(args[2]));
                    if (args[3].equals("addcity")) {
                        City city = new City(ID.getCityID(args[4]));
                        city.event().affilate(nation, RequestState.REQUEST(caller.online()));
                    }
                    if (args[3].equals("capital")) {
                        City city = new City(ID.getCityID(args[4]));
                        nation.setCapital(city);
                    }
                    if (args[3].equals("delete")) {
                        nation.remove();
                    }
                }
            }
        }
        if (args[0].equals("info")) {
            return true;
        }
        if (args[0].equals("exchange")) {
            ItemStack stack = caller.online().getItemInHand();
            Material material = stack.getType();
            int count = stack.getAmount();
            Balance balance = caller.getBalance();
            String type;
            if (material.equals(Material.DIAMOND)) {
                balance.add(Balance.DIAMOND, count);
                type = "diamond";
            } else if (material.equals(Material.NETHERITE_INGOT)) {
                balance.add(Balance.NETHERITE, count);
                type = "netherite_ingot";
            } else if (material.equals(Material.IRON_INGOT)) {
                balance.add(Balance.IRON, count);
                type = "iron_ingot";
            } else {
                CallableMessage message = ErrorMessage.YOU_CANT_DEPOSIT_THIS_TYPE;
                caller.sendMessage(message);
                return true;
            }
            caller.setBalance(balance);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:clean "+caller.getName()+" "+type+" "+count);
            CallableMessage message = LStateMessage.DEPOSITED(String.valueOf(count), type);
            caller.sendMessage(message);
            return true;
        }
        if (args[0].equals("send")) {
            if (args.length<4) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String type = args[2];
            if (!NumberUtils.isNumber(args[3]) || (!type.equals("iron") && !type.equals("diamond") && !type.equals("netherite"))) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            double amount = Double.parseDouble(args[3]);
            AncapStatesPlayer recipient = AncapStatesPlayer.findByNameFor(args[1], caller);
            if (caller.getBalance().get(type) < amount) {
                CallableMessage message = ErrorMessage.NOT_ENOUGH_MONEY;
                caller.sendMessage(message);
                return true;
            }
            if (recipient.equals(caller)) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            if (recipient.online() == null) {
                CallableMessage message = ErrorMessage.CANT_SEND_TO_OFFLINE_PLAYER;
                caller.sendMessage(message);
                return true;
            }
            if (recipient.online().getLocation().distance(caller.online().getLocation()) > 10) {
                CallableMessage message = ErrorMessage.CANT_SEND_SO_FAR;
                caller.sendMessage(message);
                return true;
            }
            caller.transferMoney(recipient, amount, type);
            CallableMessage recipientMessage = LStateMessage.PLAYER_RECIEVED_MONEY(caller.getName(), amount, type);
            recipient.sendMessage(recipientMessage);
            CallableMessage senderMessage = LStateMessage.PLAYER_SENDED_MONEY(recipient.getName(), amount, type);
            caller.sendMessage(senderMessage);
            return true;
        }
        if (args[0].equals("withdraw")) {
            if (args.length<3) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String type = args[1];
            if (!NumberUtils.isNumber(args[2]) || (!type.equals("iron") && !type.equals("diamond") && !type.equals("netherite"))) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            int amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            if (caller.getBalance().get(type) < amount) {
                CallableMessage message = ErrorMessage.NOT_ENOUGH_MONEY;
                caller.sendMessage(message);
                return true;
            }
            Balance balance = caller.getBalance();
            if (type.equals("iron")) {
                balance.remove(Balance.IRON, amount);
                type = "iron_ingot";
            }
            if (type.equals("netherite")) {
                balance.remove(Balance.NETHERITE, amount);
                type = "netherite_ingot";
            }
            if (type.equals("diamond")) {
                balance.remove(Balance.DIAMOND, amount);
            }
            caller.setBalance(balance);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:give "+caller.getName()+" "+type+" "+amount);
            CallableMessage message = LStateMessage.WITHDRAWED(String.valueOf(amount), type);
            caller.sendMessage(message);
            return true;
        }
        if (args[0].equals("stats")) {
            String border = "§8-------------------------------------------------------";
            player.sendMessage(border);
            player.sendMessage("§6Статистика государств");
            player.sendMessage("");
            player.sendMessage("§6Всего городов§8: §7"+AncapStates.getCityMap().getCities().size());
            player.sendMessage("§6Всего наций§8: §7"+AncapStates.getCityMap().getNations().size());
            player.sendMessage(border);
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
        if (args[0].equals("create-test-city")) {
            if (!player.isOp()) {
                player.sendMessage("§4Это тестовая команда и она не предназначена для скота");
                return true;
            }
            caller.createTestCity();
        }
        if (args[0].equals("create-test-nation")) {
            if (!player.isOp()) {
                player.sendMessage("§4Это тестовая команда и она не предназначена для скота");
                return true;
            }
            caller.createTestNation();
        }
        return false;
    }
}