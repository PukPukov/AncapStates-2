package ru.ancap.states.commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.library.Balance;
import ru.ancap.library.StringAPI;
import ru.ancap.states.AncapStates;
import ru.ancap.states.board.Board;
import ru.ancap.states.id.ID;
import ru.ancap.states.message.ErrorMessage;
import ru.ancap.states.message.LStateMessage;
import ru.ancap.states.name.Name;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.states.Nation.Nation;
import ru.ancap.states.states.Nation.NationColor;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.city.RequestState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  NationCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender_, Command command, String alias, String[] args) {
        Player player = (Player) sender_; 
        AncapStatesPlayer caller = AncapStatesPlayer.get(player);
        List<String> subcommands = new ArrayList(Arrays.asList(
                "info",
                "new",
                "delete",
                "deposit",
                "withdraw",
                "join",
                "leave",
                "kick",
                "acceptrequest",
                "declinerequest",
                "invite",
                "revokeinvite",
                "board",
                "minister",
                "rename",
                "capital",
                "flag",
                "taxes"
        ));
        List<AncapStatesPlayer> onlinePlayers = List.of(AncapStates.getPlayerMap().getOnlinePlayers());
        List<City> cities = AncapStates.getCityMap().getCities();
        List<Nation> nations = AncapStates.getCityMap().getNations();
        List<String> citiesNames = new ArrayList<>();
        List<String> nationsNames = new ArrayList<>();
        List<String> onlinePlayersNames = new ArrayList<>();
        for (City city : cities) {
            citiesNames.add(city.getName());
        }
        for (Nation nation : nations) {
            nationsNames.add(nation.getName());
        }
        for (AncapStatesPlayer ancapStatesPlayer : onlinePlayers) {
            onlinePlayersNames.add(ancapStatesPlayer.getName());
        }
        if (args.length == 1) {
            return subcommands;
        }
        if (args[0].equals("info")) {
            return nationsNames;
        }
        if (caller.isFree()) {
            return List.of("");
        }
        City city = caller.getCity();
        if (args[0].equals("new")) {
            return List.of("<название нации>");
        }
        if (args[0].equals("deposit") || args[0].equals("withdraw")) {
            if (args.length == 2) {
                return Arrays.asList("iron", "diamond", "netherite");
            }
            if (args.length == 3) {
                return List.of("<сумма>");
            }
        }
        if (args[0].equals("join")) {
            return nationsNames;
        }
        if (args[0].equals("leave") || args[0].equals("delete")) {
            return List.of("");
        }
        if (args[0].equals("kick")) {
            if (city.isFree()) {
                return List.of("");
            }
            List<City> nationCities = city.getNation().getCities();
            List<String> nationCitiesNames = new ArrayList<>();
            for (City nationCity : nationCities) {
                citiesNames.add(nationCity.getName());
            }
            return nationCitiesNames;
        }
        if (args[0].equals("acceptrequest") || args[0].equals("declinerequest")) {
            if (city.isFree()) {
                return List.of("");
            }
            List<City> requestingCities = city.getNation().getRequestingCities();
            List<String> requestingNames = new ArrayList<>();
            for (City requesting : requestingCities) {
                requestingNames.add(requesting.getName());
            }
            return requestingNames;
        }
        if (args[0].equals("invite")) {
            if (city.isFree()) {
                return List.of("");
            }
            List<City> nationCities = city.getNation().getCities();
            List<String> nationCitiesNames = new ArrayList<>();
            for (City nationCity : nationCities) {
                nationCitiesNames.add(nationCity.getName());
            }
            citiesNames.removeAll(nationCitiesNames);
            return citiesNames;
        }
        if (args[0].equals("revokeinvite")) {
            if (city.isFree()) {
                return List.of("");
            }
            List<City> invitedCities = city.getNation().getInvitedCities();
            List<String> invitedNames = new ArrayList<>();
            for (City invited : invitedCities) {
                invitedNames.add(invited.getName());
            }
            return invitedNames;
        }
        if (args[0].equals("board")) {
            return List.of("<текст>");
        }
        if (args[0].equals("minister")) {
            if (city.isFree()) {
                return List.of("");
            }
            List<String> ministerSubcommands = new ArrayList(Arrays.asList(
                    "set",
                    "remove"
            ));
            if (args.length == 2) {
                return ministerSubcommands;
            }
            List<AncapStatesPlayer> residents = city.getNation().getResidents();
            List<String> residentsNames = new ArrayList<>();
            for (AncapStatesPlayer resident : residents) {
                residentsNames.add(resident.getName());
            }
            List<AncapStatesPlayer> ministers = city.getNation().getMinisters();
            List<String> ministersNames = new ArrayList<>();
            for (AncapStatesPlayer minister : ministers) {
                ministersNames.add(minister.getName());
            }
            if (args[1].equals("set")) {
                residentsNames.removeAll(ministersNames);
                return residentsNames;
            }
            if (args[1].equals("remove")) {
                return ministersNames;
            }
        }
        if (args[0].equals("rename")) {
            return List.of("<новое название>");
        }
        if (args[0].equals("capital")) {
            if (city.isFree()) {
                return List.of("");
            }
            List<City> nationCities = city.getNation().getCities();
            List<String> nationCitiesNames = new ArrayList<>();
            for (City nationCity : nationCities) {
                nationCitiesNames.add(nationCity.getName());
            }
            nationCitiesNames.remove(city.getNation().getCapital().getName());
            return nationCitiesNames;
        }
        if (args[0].equals("taxes")) {
            if (args.length == 2) {
                return List.of("<железа>");
            }
            if (args.length == 3) {
                return List.of("<алмазов>");
            }
            if (args.length == 4) {
                return List.of("<незерита>");
            }
        }
        if (args[0].equals("flag")) {
            if (city.isFree()) {
                return List.of("");
            }
            List<String> flagSubCommands = new ArrayList(Arrays.asList(
                    "add",
                    "remove"
            ));
            List<String> flagsList = new ArrayList(Arrays.asList(
                    "FREE_TO_JOIN"
            ));
            List<String> nationFlags = city.getNation().getFlags();
            if (args.length == 2) {
                return flagSubCommands;
            }
            if (args[1].equals("add")) {
                flagsList.removeAll(nationFlags);
                return flagsList;
            }
            if (args[1].equals("remove")) {
                return nationFlags;
            }
        }
        return List.of("");
    }

    @Override
    public synchronized boolean onCommand(@NotNull CommandSender sender_, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender_; 
        AncapStatesPlayer caller = AncapStatesPlayer.get(player);
        if (args.length == 0) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            caller.sendMessage(caller.getCity().getNation().getInfo().getMessage());
            return true;
        }
        if (args[0].equals("info")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (!new Nation(ID.getNationID(name)).exists()) {
                CallableMessage message = ErrorMessage.NATION_DOESNT_EXISTS;
                caller.sendMessage(message);
                return true;
            }
            caller.sendMessage(new Nation(ID.getNationID(name)).getInfo().getMessage());
            return true;
        }
        if (args[0].equals("new")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (!city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_NOT_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!city.haveNationCreatingFee()) {
                CallableMessage message = ErrorMessage.NO_NATION_CREATION_FEE;
                caller.sendMessage(message);
                return true;
            }
            String name = Name.getName(args);
            if (!Name.canBeDefinedWith(name)) {
                CallableMessage message = ErrorMessage.ILLEGAL_NAME;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = new Nation(ID.getNationID(name));
            if (nation.exists()) {
                CallableMessage message = ErrorMessage.NATION_EXISTS;
                caller.sendMessage(message);
                return true;
            }
            city.grabNationCreationFee();
            nation.create(city, name);
            return true;
        }
        if (args[0].equals("deposit")) {
            if (args.length<3) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String type = args[1];
            if (! NumberUtils.isNumber(args[2]) || (!type.equals("iron") && !type.equals("diamond") && !type.equals("netherite"))) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = city.getNation();
            int amount = Math.abs(Integer.parseInt(args[2]));
            if (caller.getBalance().get(type) < amount) {
                CallableMessage message = ErrorMessage.NOT_ENOUGH_MONEY;
                caller.sendMessage(message);
                return true;
            }
            caller.depositNation(nation, amount, type);
            //noinspection deprecation fuck paper
            CallableMessage message = LStateMessage.NATION_PLAYER_DEPOSITED(caller.getName(), amount, "<lang:"+ Material.valueOf(type).getTranslationKey()+">");
            nation.sendMessage(message);
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
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMinister() && !caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = city.getNation();
            int amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            if (nation.getBalance().get(type) < amount) {
                CallableMessage message = ErrorMessage.NOT_ENOUGH_MONEY;
                caller.sendMessage(message);
                return true;
            }
            caller.withdrawNation(nation, amount, type);
            //noinspection deprecation fuck paper
            CallableMessage message = LStateMessage.NATION_PLAYER_WITHDRAWED(caller.getName(), amount, "<lang:"+ Material.valueOf(type).getTranslationKey()+">");
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("delete")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            if (caller.getCity().isFree()) {
                caller.sendMessage(ErrorMessage.CITY_IS_FREE);
                return true;
            }
            if (!caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            if (args.length < 2 || !args[1].equals("confirm")) {
                CallableMessage message = ErrorMessage.PLEASE_CONFIRM;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = caller.getNation();
            nation.remove();
            return true;
        }
        if (args[0].equals("join")) {
            if (args.length < 2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (!city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_NOT_FREE;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = new Nation(ID.getNationID(name));
            if (!nation.exists()) {
                CallableMessage message = ErrorMessage.NO_SUCH_NATION;
                caller.sendMessage(message);
                return true;
            }
            if (!nation.freeToJoin() && !city.isInvitedTo(nation)) {
                CallableMessage message = ErrorMessage.NATION_ISNT_FREE_TO_JOIN;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            city.event().affiliate(nation, RequestState.REQUEST(caller.online()));
            return true;
        }
        if (args[0].equals("declineinvite")) {
            if (args.length < 2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            Nation integrating = new Nation(ID.getNationID(name));
            if (!integrating.isInviting(city)) {
                CallableMessage message = ErrorMessage.NOT_INVITING_IN_NATION;
                caller.sendMessage(message);
                return true;
            }
            city.declineInviteFrom(integrating);
            CallableMessage cityMessage = LStateMessage.CITY_DECLINED_NATION_INVITE(integrating.getName());
            CallableMessage nationMessage = LStateMessage.NATION_CITY_DECLINED_INVITE(city.getName());
            integrating.sendMessage(nationMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("request")) {
            if (args.length < 2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (!city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_NOT_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            Nation requesting = new Nation(ID.getNationID(name));
            if (!requesting.exists()) {
                CallableMessage message = ErrorMessage.NATION_DOESNT_EXISTS;
                caller.sendMessage(message);
                return true;
            }
            if (city.isRequestingTo(requesting)) {
                CallableMessage message = ErrorMessage.YOUR_CITY_IS_ALREADY_REQUESTING_TO_THAT_NATION;
                caller.sendMessage(message);
                return true;
            }
            city.addRequestTo(requesting);
            CallableMessage requestMessage = LStateMessage.CITY_REQUESTED_TO_NATION(requesting.getName());
            CallableMessage nationMessage = LStateMessage.NATION_RECIEVED_REQUEST(city.getName());
            requesting.sendMessage(nationMessage);
            city.sendMessage(requestMessage);
            return true;
        }
        if (args[0].equals("revokerequest")) {
            if (args.length < 2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            Nation requesting = new Nation(ID.getNationID(name));
            if (!city.isRequestingTo(requesting)) {
                CallableMessage message = ErrorMessage.YOU_NOT_REQUESTING_TO_INTEGRATE_IN_THIS_NATION;
                caller.sendMessage(message);
                return true;
            }
            city.removeRequestTo(requesting);
            CallableMessage requestMessage = LStateMessage.CITY_REMOVED_REQUEST_TO_NATION(requesting.getName());
            CallableMessage nationMessage = LStateMessage.NATION_CITY_REMOVED_REQUEST(city.getName());
            requesting.sendMessage(nationMessage);
            city.sendMessage(requestMessage);
            return true;
        }
        if (args[0].equals("leave")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            CallableMessage message = LStateMessage.NATION_CITY_LEAVED(city.getName());
            Nation nation = city.getNation();
            nation.sendMessage(message);
            city.event().affiliate(null, RequestState.REQUEST(caller.online()));
            return true;
        }
        if (args[0].equals("kick")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isAssistant() && !caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            City kicked = new City(ID.getCityID(name));
            kicked.event().affiliate(null, RequestState.REQUEST(caller.online()));
            return true;
        }
        if (args[0].equals("invite")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMinister() && !caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String cityName = args[1];
            City invited = new City(ID.getCityID(cityName));
            if (!invited.exists()) {
                CallableMessage message = ErrorMessage.CITY_DOESNT_EXIST;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = caller.getNation();
            if (invited.isInvitedTo(nation)) {
                CallableMessage message = ErrorMessage.THAT_CITY_IS_ALREADY_INVITED_TO_YOUR_NATION;
                caller.sendMessage(message);
                return true;
            }
            nation.addInviteTo(invited);
            CallableMessage cityMessage = LStateMessage.CITY_NATION_INVITED(nation.getName());
            CallableMessage nationMessage = LStateMessage.NATION_INVITED_CITY(invited.getName());
            nation.sendMessage(nationMessage);
            invited.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("revokeinvite")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMinister() && !caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String cityName = args[1];
            City invited = new City(ID.getCityID(cityName));
            Nation nation = caller.getNation();
            if (!nation.isInviting(invited)) {
                CallableMessage message = ErrorMessage.CITY_DOESNT_INVITED;
                caller.sendMessage(message);
                return true;
            }
            nation.removeInviteTo(invited);
            CallableMessage cityMessage = LStateMessage.CITY_NATION_REVOKED_INVITE(nation.getName());
            CallableMessage nationMessage = LStateMessage.NATION_REVOKED_INVITE_TO_CITY(invited.getName());
            nation.sendMessage(nationMessage);
            invited.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("acceptrequest")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMinister() && !caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String cityName = args[1];
            City accepted = new City(ID.getCityID(cityName));
            Nation nation = caller.getNation();
            if (!accepted.isRequestingTo(nation)) {
                CallableMessage message = ErrorMessage.CITY_DOESNT_REQUESTING;
                caller.sendMessage(message);
                return true;
            }
            accepted.event().affiliate(nation, RequestState.REQUEST(caller.online()));
            return true;
        }
        if (args[0].equals("declinerequest")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMinister() && !caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String cityName = args[1];
            City accepted = new City(ID.getCityID(cityName));
            Nation nation = caller.getNation();
            if (!accepted.isRequestingTo(nation)) {
                CallableMessage message = ErrorMessage.CITY_DOESNT_REQUESTING;
                caller.sendMessage(message);
                return true;
            }
            accepted.removeRequestTo(nation);
            CallableMessage requestMessage = LStateMessage.CITY_NATION_DECLINED_REQUEST(nation.getName());
            CallableMessage nationMessage = LStateMessage.NATION_DECLINED_CITY_REQUEST(accepted.getName());
            nation.sendMessage(nationMessage);
            accepted.sendMessage(requestMessage);
            return true;
        }
        if (args[0].equals("minister")) {
            if (args.length<3) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = caller.getNation();
            AncapStatesPlayer minister = AncapStatesPlayer.findByNameFor(args[2], caller);
            if (!minister.isCitizenOf(nation)) {
                CallableMessage message = ErrorMessage.HE_ISNT_CITIZEN_OF_YOUR_NATION;
                caller.sendMessage(message);
                return true;
            }
            if (args[1].equals("set")) {
                if (minister.isMinister()) {
                    CallableMessage message = ErrorMessage.HE_IS_ALREADY_MINISTER;
                    caller.sendMessage(message);
                    return true;
                }
                nation.addMinister(minister);
                CallableMessage message = LStateMessage.NATION_NEW_MINISTER(minister.getName());
                nation.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                if (!minister.isMinister()) {
                    CallableMessage message = ErrorMessage.HE_ISNT_MINISTER;
                    caller.sendMessage(message);
                    return true;
                }
                nation.removeMinister(minister);
                CallableMessage message = LStateMessage.NATION_REMOVED_MINISTER(minister.getName());
                nation.sendMessage(message);
                return true;
            }
        }
        if (args[0].equals("capital")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            if (args.length < 3 || !args[2].equals("confirm")) {
                CallableMessage message = ErrorMessage.PLEASE_CONFIRM;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = caller.getNation();
            String cityName = args[1];
            City capital = new City(ID.getCityID(cityName));
            if (!capital.exists() || !capital.isIntegratedIn(nation)) {
                CallableMessage message = ErrorMessage.NO_SUCH_CITY;
                caller.sendMessage(message);
                return true;
            }
            nation.setCapital(capital);
            CallableMessage message = LStateMessage.NATION_NEW_CAPITAL(capital.getName());
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("color")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isAssistant() && !caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            args[1] = args[1].trim().toUpperCase();
            if (!StringAPI.isHex(args[1])) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = caller.getNation();
            NationColor color = new NationColor(args[1]);
            nation.setColor(color);
            CallableMessage message = LStateMessage.NATION_SETTED_COLOR(color.getColor());
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("rename")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String name = Name.getName(args);
            if (!Name.canBeDefinedWith(name)) {
                CallableMessage message = ErrorMessage.ILLEGAL_NAME;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = caller.getNation();
            Nation checkNation = new Nation(ID.getCityID(name));
            if (checkNation.exists()) {
                CallableMessage message = ErrorMessage.NATION_EXISTS;
                caller.sendMessage(message);
                return true;
            }
            nation.setName(name);
            CallableMessage message = LStateMessage.NATION_NEW_NAME(name);
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("board")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isAssistant() && !caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String name = Name.getName(args);
            if (!Name.canBeDefinedWith(name)) {
                CallableMessage message = ErrorMessage.ILLEGAL_NAME;
                caller.sendMessage(message);
                return true;
            }
            Board board = new Board(args);
            Nation nation = caller.getNation();
            nation.setBoard(board.toString());
            CallableMessage message = LStateMessage.NATION_NEW_BOARD(board.toString());
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("flag")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isLeader()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            if (args.length < 3) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String name = Name.getName(args, 2);
            if (!Name.canBeDefinedWith(name)) {
                CallableMessage message = ErrorMessage.ILLEGAL_NAME;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = city.getNation();
            if (args[1].equals("set")) {
                String flag = args[2];
                nation.setFlag(flag);
                CallableMessage message = LStateMessage.NATION_ADDED_FLAG(flag);
                nation.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                String flag = args[2];
                nation.removeFlag(flag);
                CallableMessage message = LStateMessage.NATION_REMOVED_FLAG(flag);
                nation.sendMessage(message);
                return true;
            }
        }
        if (args[0].equals("taxes")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.isFree()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = city.getNation();
            if (args.length == 1) {
                CallableMessage message = nation.getInfo().getMessage();
                caller.sendMessage(message);
                return true;
            }
            if (args[1].equals("set")) {
                if (args.length != 5) {
                    CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                    caller.sendMessage(message);
                    return true;
                }
                if (!NumberUtils.isNumber(args[2]) || !NumberUtils.isNumber(args[3]) || !NumberUtils.isNumber(args[4])) {
                    CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                    caller.sendMessage(message);
                    return true;
                }
                if (!caller.isLeader()) {
                    CallableMessage message = ErrorMessage.NO_PERMS;
                    caller.sendMessage(message);
                    return true;
                }
                double iron = Double.parseDouble(args[2]);
                double diamond = Double.parseDouble(args[3]);
                double netherite = Double.parseDouble(args[4]);
                if (iron < 0 || diamond < 0 || netherite < 0) {
                    CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                    caller.sendMessage(message);
                    return true;
                }
                Balance tax = new Balance(iron, diamond, netherite);
                nation.setTax(tax);
                CallableMessage message = LStateMessage.NEW_NATION_TAX;
                nation.sendMessage(message);
                return true;
            }
        }
        return false;
    }
}