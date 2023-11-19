package ru.ancap.states.commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.library.AncapChunk;
import ru.ancap.library.Balance;
import ru.ancap.states.AncapStates;
import ru.ancap.states.board.Board;
import ru.ancap.states.chunk.OutpostChunk;
import ru.ancap.states.id.ID;
import ru.ancap.states.message.ErrorMessage;
import ru.ancap.states.message.LStateMessage;
import ru.ancap.states.name.Name;
import ru.ancap.states.ocean.Ocean;
import ru.ancap.states.player.AncapStatesPlayer;
import ru.ancap.states.player.NotEnoughMoneyException;
import ru.ancap.states.states.Nation.Nation;
import ru.ancap.states.states.city.AllowLevel;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.city.LimitType;
import ru.ancap.states.states.city.RequestState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CityCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender_, Command command, String alias, String[] args) {
        Player player = (Player) sender_; 
        AncapStatesPlayer caller = AncapStatesPlayer.get(player);
        List<String> subcommands = List.of(
            "info",
            "new",
            "delete",
            "deposit",
            "withdraw",
            "join",
            "leave",
            "kick",
            "request",
            "revokerequest",
            "acceptrequest",
            "declinerequest",
            "invite",
            "declineinvite",
            "revokeinvite",
            "board",
            "assistant",
            "spawn",
            "allow",
            "rename",
            "limit",
            "chunk",
            "friend",
            "claim",
            "unclaim",
            "mayor",
            "flag",
            "taxes",
            "color"
        );
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
            onlinePlayersNames.add(ancapStatesPlayer.getID());
        }
        if (args.length == 1) {
            return subcommands;
        }
        if (args[0].equals("new")) {
            return List.of("<название города>");
        }
        if (args[0].equals("info")) {
            return citiesNames;
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
            return citiesNames;
        }
        if (args[0].equals("leave") || args[0].equals("spawn") || args[0].equals("delete") || args[0].equals("claim") || args[0].equals("unclaim")) {
            return List.of("");
        }
        if (args[0].equals("kick")) {
            if (caller.isFree()) {
                return List.of("");
            }
            List<AncapStatesPlayer> cityResidents = caller.getCity().getResidents();
            List<String> residentsNames = new ArrayList<>();
            for (AncapStatesPlayer resident : cityResidents) {
                residentsNames.add(resident.getName());
            }
            return residentsNames;
        }
        if (args[0].equals("request")) {
            return citiesNames;
        }
        if (args[0].equals("revokerequest")) {
            List<City> requestingCities = caller.getRequesting();
            List<String> requestingNames = new ArrayList<>();
            for (City requesting : requestingCities) {
                requestingNames.add(requesting.getName());
            }
            return requestingNames;
        }
        if (args[0].equals("acceptrequest") || args[0].equals("declinerequest")) {
            if (caller.isFree()) {
                return List.of("");
            }
            List<AncapStatesPlayer> requestingPlayers = caller.getCity().getRequestingPlayers();
            List<String> requestingNames = new ArrayList<>();
            for (AncapStatesPlayer requesting : requestingPlayers) {
                requestingNames.add(requesting.getName());
            }
            return requestingNames;
        }
        if (args[0].equals("invite")) {
            if (caller.isFree()) {
                return List.of("");
            }
            List<AncapStatesPlayer> residents = caller.getCity().getResidents();
            List<String> residentsNames = new ArrayList<>();
            for (AncapStatesPlayer resident : residents) {
                residentsNames.add(resident.getName());
            }
            onlinePlayersNames.removeAll(residentsNames);
            return onlinePlayersNames;
        }
        if (args[0].equals("declineinvite")) {
            List<City> invitingCities = caller.getInviting();
            List<String> invitingNames = new ArrayList<>();
            for (City inviting : invitingCities) {
                invitingNames.add(inviting.getName());
            }
            return invitingNames;
        }
        if (args[0].equals("revokeinvite")) {
            if (caller.isFree()) {
                return List.of("");
            }
            List<AncapStatesPlayer> invitedPlayers = caller.getCity().getInvitedPlayers();
            List<String> invitedNames = new ArrayList<>();
            for (AncapStatesPlayer invited : invitedPlayers) {
                invitedNames.add(invited.getName());
            }
            return invitedNames;
        }
        if (args[0].equals("board")) {
            return List.of("<текст>");
        }
        if (args[0].equals("assistant")) {
            if (caller.isFree()) {
                return List.of("");
            }
            List<String> assistantSubcommands = new ArrayList(Arrays.asList(
                    "set",
                    "remove"
            ));
            if (args.length == 2) {
                return assistantSubcommands;
            }
            List<AncapStatesPlayer> residents = caller.getCity().getResidents();
            List<String> residentsNames = new ArrayList<>();
            for (AncapStatesPlayer resident : residents) {
                residentsNames.add(resident.getName());
            }
            List<AncapStatesPlayer> assistants = caller.getCity().getAssistants();
            List<String> assistantsNames = new ArrayList<>();
            for (AncapStatesPlayer assistant : assistants) {
                assistantsNames.add(assistant.getName());
            }
            if (args[1].equals("set")) {
                residentsNames.removeAll(assistantsNames);
                return residentsNames;
            }
            if (args[1].equals("remove")) {
                return assistantsNames;
            }
        }
        if (args[0].equals("allow")) {
            List<String> allowSubcommands = new ArrayList(Arrays.asList(
                    "mayor",
                    "assistants",
                    "residents",
                    "nation-leader",
                    "nation-ministers",
                    "nation",
                    "licentiate",
                    "everyone"
            ));
            return allowSubcommands;
        }
        if (args[0].equals("rename")) {
            return List.of("<новое_название>");
        }
        if (args[0].equals("limit")) {
            List<String> limitSubcommands = new ArrayList(Arrays.asList(
                    "personal",
                    "resident",
                    "assistant"
            ));
            if (args.length == 2) {
                return limitSubcommands;
            }
            if (args[1].equals("personal")) {
                if (args.length == 3) {
                    return List.of("<ник>");
                }
                if (args.length == 4) {
                    return List.of("<количество>");
                }
            }
            if (args[1].equals("resident") || args[1].equals("assistant")) {
                return List.of("<количество>");
            }
        }
        if (args[0].equals("chunk")) {
            List<String> firstLayerChunkSubCommands = new ArrayList(Arrays.asList(
                    "private",
                    "outpost"
            ));
            List<String> secondLayerChunkSubcommands = new ArrayList(Arrays.asList(
                    "claim",
                    "unclaim"
            ));
            if (args.length == 2) {
                return firstLayerChunkSubCommands;
            }
            if (args[1].equals("private") || args[1].equals("outpost")) {
                return secondLayerChunkSubcommands;
            }
        }
        if (args[0].equals("friend")) {
            List<String> friendSubCommands = new ArrayList(Arrays.asList(
                    "add",
                    "remove"
            ));
            if (args.length == 2) {
                return friendSubCommands;
            }
            List<AncapStatesPlayer> friends = caller.getFriends();
            List<String> friendsNames = new ArrayList<>();
            for (AncapStatesPlayer friend : friends) {
                friendsNames.add(friend.getName());
            }
            if (args[1].equals("add")) {
                onlinePlayersNames.removeAll(friendsNames);
                return onlinePlayersNames;
            }
            if (args[1].equals("remove")) {
                return friendsNames;
            }
        }
        if (args[0].equals("mayor")) {
            if (caller.isFree()) {
                return List.of("");
            }
            List<AncapStatesPlayer> cityResidents = caller.getCity().getResidents();
            List<String> residentsNames = new ArrayList<>();
            for (AncapStatesPlayer resident : cityResidents) {
                residentsNames.add(resident.getName());
            }
            residentsNames.remove(caller.getCity().getMayor().getName());
            return residentsNames;
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
            if (caller.isFree()) {
                return List.of("");
            }
            List<String> flagSubCommands = new ArrayList(Arrays.asList(
                    "add",
                    "remove"
            ));
            List<String> flagsList = new ArrayList(Arrays.asList(
                    "FREE_TO_JOIN"
            ));
            List<String> cityFlags = caller.getCity().getFlags();
            if (args.length == 2) {
                return flagSubCommands;
            }
            if (args[1].equals("add")) {
                flagsList.removeAll(cityFlags);
                return flagsList;
            }
            if (args[1].equals("remove")) {
                return cityFlags;
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
            caller.sendMessage(caller.getCity().getInfo().getMessage());
            return true;
        }
        if (args[0].equals("info")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (!new City(ID.getCityID(name)).exists()) {
                CallableMessage message = ErrorMessage.CITY_DOESNT_EXIST;
                caller.sendMessage(message);
                return true;
            }
            caller.sendMessage(new City(ID.getCityID(name)).getInfo().getMessage());
            return true;
        }
        if (args[0].equals("new")) {
            if (args.length<2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isFree()) {
                CallableMessage message = ErrorMessage.NOT_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.haveCityCreationFee()) {
                CallableMessage message = ErrorMessage.NO_CITY_CREATION_FEE;
                caller.sendMessage(message);
                return true;
            }
            if (caller.getCityAtPosition() != null) {
                Bukkit.broadcastMessage(caller.getCityAtPosition().toString());
                CallableMessage message = ErrorMessage.HERE_IS_OTHER_CITY;
                caller.sendMessage(message);
                return true;
            }
            String name = Name.getName(args);
            if (!Name.canBeDefinedWith(name)) {
                CallableMessage message = ErrorMessage.ILLEGAL_NAME;
                caller.sendMessage(message);
                return true;
            }
            City city = new City(ID.getCityID(name));
            if (city.exists()) {
                CallableMessage message = ErrorMessage.CITY_EXISTS;
                caller.sendMessage(message);
                return true;
            }
            Player bukkitPlayer = caller.online();
            World world = bukkitPlayer.getWorld();
            Location playerLoc = bukkitPlayer.getLocation();
            Location highestBlockAt = new Location(
                playerLoc.getWorld(),
                playerLoc.getX(),
                world.getHighestBlockAt(playerLoc.getBlockX(), playerLoc.getBlockZ()).getY(),
                playerLoc.getZ()
            );
            if (Ocean.isOcean(world.getBiome(highestBlockAt))) {
                caller.sendMessage("Нельзя захватывать гексы в морях!");
                return true;
            }
            caller.grabCityCreationFee();
            city.create(caller, name);
            return true;
        }
        if (args[0].equals("delete")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            Nation nation = caller.getNation();
            if (nation != null) {
                if (nation.getCapital().equals(caller.getCity())) {
                    CallableMessage message = ErrorMessage.CAPITAL_CANT_BE_DELETED;
                    caller.sendMessage(message);
                    return true;
                }
            }
            if (args.length < 2 || !args[1].equals("confirm")) {
                CallableMessage message = ErrorMessage.PLEASE_CONFIRM;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            String name = city.getName();
            city.remove();
            return true;
        }
        if (args[0].equals("deposit")) {
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
            int amount = Math.abs(Integer.parseInt(args[2]));
            City city = caller.getCity();
            if (caller.getBalance().get(type) < amount) {
                CallableMessage message = ErrorMessage.NOT_ENOUGH_MONEY;
                caller.sendMessage(message);
                return true;
            }
            caller.depositCity(city, amount, type);
            CallableMessage cityMessage = LStateMessage.CITY_PLAYER_DEPOSITED(caller.getName(), amount, type);
            city.sendMessage(cityMessage);
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
            if (!caller.isAssistant() && !caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            int amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.getBalance().get(type) < amount) {
                CallableMessage message = ErrorMessage.NOT_ENOUGH_MONEY;
                caller.sendMessage(message);
                return true;
            }
            caller.withdrawCity(city, amount, type);
            CallableMessage cityMessage = LStateMessage.CITY_PLAYER_WITHDRAWED(caller.getName(), amount, type);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("join")) {
            if (args.length < 2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            City city = new City(ID.getCityID(name));
            if (!caller.isFree()) {
                CallableMessage message = ErrorMessage.NOT_FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!city.exists()) {
                CallableMessage message = ErrorMessage.NO_SUCH_CITY;
                caller.sendMessage(message);
                return true;
            }
            if (!city.freeToJoin() && !caller.isInvitedTo(city)) {
                CallableMessage message = ErrorMessage.CITY_ISNT_FREE_TO_JOIN;
                caller.sendMessage(message);
                return true;
            }
            city.addResident(caller);
            CallableMessage message = LStateMessage.CITY_PLAYER_JOINED(caller.getName());
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("leave")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isMayor()) {
                CallableMessage message = ErrorMessage.MAYOR_CANT_LEAVE;
                caller.sendMessage(message);
                return true;
            }
            CallableMessage message = LStateMessage.CITY_PLAYER_LEAVED(caller.getName());
            City city = caller.getCity();
            city.sendMessage(message);
            caller.leaveCity();
            return true;
        }
        if (args[0].equals("kick")) {
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
            if (!caller.isAssistant() && !caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            AncapStatesPlayer kicked = AncapStatesPlayer.findByNameFor(args[1], caller);
            if (!kicked.isResidentOf(city)) {
                CallableMessage message = ErrorMessage.NOT_RESIDENT;
                caller.sendMessage(message);
                return true;
            }
            if (kicked.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            CallableMessage message = LStateMessage.CITY_PLAYER_KICKED(kicked.getName());
            city.sendMessage(message);
            city.removeResident(kicked);
            return true;
        }
        if (args[0].equals("request")) {
            if (args.length < 2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isFree()) {
                CallableMessage message = ErrorMessage.NOT_FREE;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            City city = new City(ID.getCityID(name));
            if (!city.exists()) {
                CallableMessage message = ErrorMessage.NO_SUCH_CITY;
                caller.sendMessage(message);
                return true;
            }
            if (city.freeToJoin()) {
                CallableMessage message = ErrorMessage.CITY_IS_FREE_TO_JOIN;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isAskingToJoinIn(city)) {
                CallableMessage message = ErrorMessage.YOU_ARE_ALREADY_REQUESTING_TO_THAT_CITY;
                caller.sendMessage(message);
                return true;
            }
            caller.addRequestTo(city);
            CallableMessage playerMessage = LStateMessage.PLAYER_REQUESTED_TO_CITY(city.getName());
            CallableMessage cityMessage = LStateMessage.CITY_PLAYER_REQUESTED(caller.getName());
            caller.sendMessage(playerMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("revokerequest")) {
            if (args.length < 2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            City city = new City(ID.getCityID(name));
            if (!city.exists()) {
                CallableMessage message = ErrorMessage.NO_SUCH_CITY;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isAskingToJoinIn(city)) {
                CallableMessage message = ErrorMessage.YOU_NOT_ASKING_TO_JOIN_IN_CITY;
                caller.sendMessage(message);
                return true;
            }
            caller.removeRequestTo(city);
            CallableMessage playerMessage = LStateMessage.PLAYER_REVOKE_REQUEST_TO_CITY(city.getName());
            CallableMessage cityMessage = LStateMessage.CITY_PLAYER_REVOKE_REQUEST(caller.getName());
            caller.sendMessage(playerMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("acceptrequest")) {
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
            if (!caller.isAssistant() && !caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            AncapStatesPlayer accepted = AncapStatesPlayer.findByNameFor(args[1], caller);
            if (!accepted.isAskingToJoinIn(city)) {
                CallableMessage message = ErrorMessage.NOT_ASKING_TO_JOIN_IN_CITY;
                caller.sendMessage(message);
                return true;
            }
            city.addResident(accepted);
            CallableMessage message = LStateMessage.CITY_PLAYER_JOINED(accepted.getName());
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("declinerequest")) {
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
            if (!caller.isAssistant() && !caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            AncapStatesPlayer declined = AncapStatesPlayer.findByNameFor(args[1], caller);
            if (!declined.isAskingToJoinIn(city)) {
                CallableMessage message = ErrorMessage.NOT_ASKING_TO_JOIN_IN_CITY;
                caller.sendMessage(message);
                return true;
            }
            city.declineRequestFrom(declined);
            CallableMessage playerMessage = LStateMessage.PLAYER_CITY_DECLINED_REQUEST(city.getName());
            CallableMessage cityMessage = LStateMessage.CITY_DECLINED_REQUEST(declined.getName());
            declined.sendMessage(playerMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("declineinvite")) {
            if (args.length < 2) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            City joining = new City(ID.getCityID(name));
            if (!caller.isInvitedTo(joining)) {
                CallableMessage message = ErrorMessage.NOT_INVITING_IN_CITY;
                caller.sendMessage(message);
                return true;
            }
            caller.declineInviteFrom(joining);
            CallableMessage playerMessage = LStateMessage.PLAYER_DECLINED_CITY_INVITE(joining.getName());
            CallableMessage cityMessage = LStateMessage.CITY_PLAYER_DECLINED_INVITE(caller.getName());
            joining.sendMessage(cityMessage);
            caller.sendMessage(playerMessage);
            return true;
        }
        if (args[0].equals("invite")) {
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
            if (!caller.isAssistant() && !caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (!Name.canBeDefinedWith(name)) {
                CallableMessage message = ErrorMessage.ILLEGAL_NAME;
                caller.sendMessage(message);
                return true;
            }
            AncapStatesPlayer invited = AncapStatesPlayer.findByNameFor(name, caller);
            if (!invited.isFree()) {
                CallableMessage message = ErrorMessage.HE_ISNT_FREE;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (invited.isInvitedTo(city)) {
                CallableMessage message = ErrorMessage.HE_IS_ALREADY_INVITED_TO_YOUR_CITY;
                caller.sendMessage(message);
                return true;
            }
            city.addInviteTo(invited);
            CallableMessage inviteMessage = LStateMessage.PLAYER_INVITED_TO_CITY(city.getName());
            invited.sendMessage(inviteMessage);
            CallableMessage cityMessage = LStateMessage.CITY_INVITED_PLAYER(invited.getName());
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("revokeinvite")) {
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
            if (!caller.isAssistant()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (!Name.canBeDefinedWith(name)) {
                CallableMessage message = ErrorMessage.ILLEGAL_NAME;
                caller.sendMessage(message);
                return true;
            }
            AncapStatesPlayer invited = AncapStatesPlayer.findByNameFor(name, caller);
            City city = caller.getCity();
            if (!invited.isInvitedTo(city)) {
                CallableMessage message = ErrorMessage.HE_ISNT_INVITED;
                caller.sendMessage(message);
                return true;
            }
            city.removeInviteTo(invited);
            CallableMessage inviteMessage = LStateMessage.PLAYER_REMOVED_INVITE_TO_CITY(city.getName());
            CallableMessage cityMessage = LStateMessage.CITY_REMOVED_INVITE_TO_PLAYER(invited.getName());
            invited.sendMessage(inviteMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("board")) {
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
            if (!caller.isAssistant() && !caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            Board board = new Board(args);
            city.setBoard(board.toString());
            CallableMessage message = LStateMessage.CITY_NEW_BOARD(board.toString());
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("assistant")) {
            if (args.length < 3) {
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
            String name = args[2];
            if (!Name.canBeDefinedWith(name)) {
                CallableMessage message = ErrorMessage.ILLEGAL_NAME;
                caller.sendMessage(message);
                return true;
            }
            AncapStatesPlayer assistant = AncapStatesPlayer.findByNameFor(name, caller);
            if (!assistant.isResidentOf(city)) {
                CallableMessage message = ErrorMessage.HE_ISNT_RESIDENT_OF_YOUR_CITY;
                caller.sendMessage(message);
                return true;
            }
            if (args[1].equals("set")) {
                if (assistant.isAssistant()) {
                    CallableMessage message = ErrorMessage.HE_IS_ALREADY_ASSISTANT;
                    caller.sendMessage(message);
                    return true;
                }
                city.addAssistant(assistant);
                CallableMessage message = LStateMessage.CITY_NEW_ASSISTANT(assistant.getName());
                city.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                if (!assistant.isAssistant()) {
                    CallableMessage message = ErrorMessage.HE_ISNT_ASSISTANT;
                    caller.sendMessage(message);
                    return true;
                }
                city.removeAssistant(assistant);
                CallableMessage message = LStateMessage.CITY_REMOVED_ASSISTANT(assistant.getName());
                city.sendMessage(message);
                return true;
            }
        }
        if (args[0].equals("spawn")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            try {
                caller.tryCitySpawn();
            } catch (NotEnoughMoneyException exception) {
                CallableMessage message = ErrorMessage.CANT_TELEPORT_TO_CITY_SPAWN;
                caller.sendMessage(message);
                return true;
            }
            
            City city = caller.getCity();
            CallableMessage message = LStateMessage.PLAYER_TELEPORTED_TO_CITY_SPAWN(city.getName());
            caller.sendMessage(message);
            return true;
        }
        if (args[0].equals("allow")) {
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
            String allowLevelName = args[1];
            if (!AllowLevel.canBeDefinedWith(allowLevelName)) {
                CallableMessage message = ErrorMessage.NO_SUCH_ALLOW_LEVEL;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            AllowLevel allowLevel = new AllowLevel(allowLevelName);
            city.setAllowLevel(allowLevel);
            CallableMessage message = LStateMessage.ALLOW_LEVEL_CHANGED(String.valueOf(allowLevel.getInt()));
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("rename")) {
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
            if (!caller.isMayor()) {
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
            City checkCity = new City(ID.getCityID(name));
            if (checkCity.exists()) {
                CallableMessage message = ErrorMessage.CITY_EXISTS;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            city.setName(name);
            CallableMessage message = LStateMessage.CITY_NEW_NAME(name);
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("limit")) {
            if (args.length < 3) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (args[1].equals("personal")) {
                if (args.length < 4) {
                    CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                    caller.sendMessage(message);
                    return true;
                }

                AncapStatesPlayer limited = AncapStatesPlayer.findByNameFor(args[2], caller);
                if (! NumberUtils.isNumber(args[3])) {
                    CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                    caller.sendMessage(message);
                    return true;
                }
                int limit = Integer.parseInt(args[3]);
                city.setLimit(limited, limit);
                CallableMessage message = LStateMessage.CITY_NEW_PERSONAL_LIMIT(limited.getName(), String.valueOf(limit));
                city.sendMessage(message);
                return true;
            }
            String limitTypeName = args[1].toLowerCase();
            if (!limitTypeName.equals("resident") && !limitTypeName.equals("assistant")) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            if (!NumberUtils.isNumber(args[2])) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            int limit = Integer.parseInt(args[2]);
            LimitType limitType = new LimitType(limitTypeName);
            city.setLimit(limitType, limit);
            CallableMessage message = LStateMessage.CITY_NEW_LIMIT(limitType.toString(), String.valueOf(limit));
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("chunk")) {
            if (args.length < 3) {
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
            AncapChunk ancapChunk = caller.getChunk();
            if (args[1].equals("private")) {
                if (args[2].equals("claim")) {
                    if (!caller.canClaimPrivateChunks()) {
                        CallableMessage message = ErrorMessage.NO_PERMS;
                        caller.sendMessage(message);
                        return true;
                    }
                    if (caller.getPrivateChunk().getOwner() != null) {
                        CallableMessage message = ErrorMessage.PRIVATE_CHUNK_IS_ALREADY_CLAIMED;
                        caller.sendMessage(message);
                        return true;
                    }
                    if (!city.equals(caller.getCityAtPosition())) {
                        CallableMessage message = ErrorMessage.ITS_NOT_YOUR_CITY;
                        caller.sendMessage(message);
                        return true;
                    }
                    if (!caller.canClaimNewPrivateChunk()) {
                        CallableMessage message = ErrorMessage.CANT_CLAIM_NEW_PRIVATE_CHUNK;
                        caller.sendMessage(message);
                        return true;
                    }
                    city.addPrivateChunk(caller, ancapChunk);
                    CallableMessage message = LStateMessage.CITY_PRIVATE_CHUNK_CLAIMED(caller.getName(), ancapChunk.toString());
                    city.sendMessage(message);
                    return true;
                }
                if (args[2].equals("unclaim")) {
                    if (caller.getPrivateChunk() == null) {
                        CallableMessage message = ErrorMessage.PRIVATE_CHUNK_ISNT_CLAIMED;
                        caller.sendMessage(message);
                        return true;
                    }
                    AncapStatesPlayer owner = caller.getPrivateChunk().getOwner();
                    if (!owner.equals(caller) && !caller.isMayorOf(city)) {
                        CallableMessage message = ErrorMessage.ITS_NOT_YOUR_CHUNK;
                        caller.sendMessage(message);
                        return true;
                    }
                    city.removePrivateChunk(ancapChunk);
                    return true;
                }
            }
            if (args[1].equals("outpost")) {
                if (!caller.isAssistant() && !caller.isMayor()) {
                    CallableMessage message = ErrorMessage.NO_PERMS;
                    caller.sendMessage(message);
                    return true;
                }
                if (args[2].equals("claim")) {
                    if (caller.getOutpostChunk().getOwner() != null) {
                        CallableMessage message = ErrorMessage.OUTPOST_CHUNK_IS_ALREADY_CLAIMED;
                        caller.sendMessage(message);
                        return true;
                    }
                    if (!city.canClaimNewOutpostChunk()) {
                        CallableMessage message = ErrorMessage.CANT_CLAIM_NEW_OUTPOST_CHUNK;
                        caller.sendMessage(message);
                        return true;
                    }
                    city.addOutpostChunk(ancapChunk);
                    CallableMessage message = LStateMessage.CITY_CLAIMED_NEW_OUTPOST_CHUNK(ancapChunk.toString());
                    city.sendMessage(message);
                    return true;
                }
                if (args[2].equals("unclaim")) {
                    OutpostChunk outpostChunk = caller.getOutpostChunk();
                    if (outpostChunk.getOwner() == null) {
                        CallableMessage message = ErrorMessage.OUTPOST_CHUNK_ISNT_CLAIMED;
                        caller.sendMessage(message);
                        return true;
                    }
                    if (!outpostChunk.getOwner().equals(city)) {
                        CallableMessage message = ErrorMessage.ITS_NOT_YOUR_CITY_CHUNK;
                        caller.sendMessage(message);
                        return true;
                    }
                    city.removeOutpostChunk(ancapChunk);
                    CallableMessage message = LStateMessage.CITY_CLAIMED_NEW_OUTPOST_CHUNK(ancapChunk.toString());
                    city.sendMessage(message);
                    return true;
                }
            }
        }
        if (args[0].equals("friend")) {
            if (args.length < 2) {
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
            AncapStatesPlayer friend = AncapStatesPlayer.findByNameFor(args[2], caller);
            if (args[1].equals("add")) {
                caller.addFriend(friend);
                CallableMessage message = LStateMessage.PLAYER_NEW_FRIEND(friend.getName());
                caller.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                caller.removeFriend(friend);
                CallableMessage message = LStateMessage.PLAYER_NEW_FRIEND(friend.getName());
                caller.sendMessage(message);
                return true;
            }
        }
        if (args[0].equals("claim")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isAssistant() && !caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (!city.haveHexagonClaimingFee()) {
                CallableMessage message = ErrorMessage.NOT_ENOUGH_MONEY;
                caller.sendMessage(message);
                return true;
            }
            Hexagon hexagon = AncapStates.grid.hexagon(caller);
            if (AncapStates.getCityMap().getCity(hexagon) != null) {
                CallableMessage message = ErrorMessage.HEXAGON_IS_ALREADY_CLAIMED;
                caller.sendMessage(message);
                return true;
            }
            if (!city.canAttach(hexagon)) {
                CallableMessage message = ErrorMessage.HEXAGON_ISNT_CONNECTING_TO_CITY;
                caller.sendMessage(message);
                return true;
            }
            Player bukkitPlayer = caller.online();
            World world = bukkitPlayer.getWorld();
            Location playerLoc = bukkitPlayer.getLocation();
            Location highestBlockAt = new Location(
                    playerLoc.getWorld(),
                    playerLoc.getX(),
                    world.getHighestBlockAt(playerLoc.getBlockX(), playerLoc.getBlockZ()).getY(),
                    playerLoc.getZ()
            );
            if (Ocean.isOcean(world.getBiome(highestBlockAt))) {
                caller.sendMessage("Нельзя захватывать гексы в морях!");
                return true;
            }
            city.event().claim(caller.online(), hexagon, RequestState.REQUEST(caller.online()));
            return true;
        }
        if (args[0].equals("unclaim")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            Hexagon hexagon = AncapStates.grid.hexagon(caller);
            if (!Objects.equals(AncapStates.getCityMap().getCity(hexagon), caller.getCity())) {
                CallableMessage message = ErrorMessage.ITS_NOT_YOUR_CITY_HEXAGON;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            if (city.getHomeHexagon().equals(hexagon)) {
                CallableMessage message = ErrorMessage.HOME_HEXAGON_CANT_BE_UNCLAIMED;
                caller.sendMessage(message);
                return true;
            }
            city.event().remove(hexagon, RequestState.REQUEST(caller.online()));
            return true;
        }
        if (args[0].equals("mayor")) {
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
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            if (args.length < 3 || !args[2].equals("confirm")) {
                CallableMessage message = ErrorMessage.PLEASE_CONFIRM;
                caller.sendMessage(message);
                return true;
            }
            City city = caller.getCity();
            String name = args[1];
            if (!Name.canBeDefinedWith(name)) {
                CallableMessage message = ErrorMessage.ILLEGAL_NAME;
                caller.sendMessage(message);
                return true;
            }
            AncapStatesPlayer mayor = AncapStatesPlayer.findByNameFor(name, caller);
            if (!mayor.isResidentOf(city)) {
                CallableMessage message = ErrorMessage.HE_ISNT_RESIDENT_OF_YOUR_CITY;
                caller.sendMessage(message);
                return true;
            }
            city.setMayor(mayor);
            CallableMessage message = LStateMessage.CITY_NEW_MAYOR(mayor.getName());
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("flag")) {
            if (caller.isFree()) {
                CallableMessage message = ErrorMessage.FREE;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMayor()) {
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
            City city = caller.getCity();
            if (args[1].equals("set")) {
                String flag = args[2];
                city.setFlag(flag);
                CallableMessage message = LStateMessage.CITY_ADDED_FLAG(flag);
                city.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                String flag = args[2];
                city.removeFlag(flag);
                CallableMessage message = LStateMessage.CITY_REMOVED_FLAG(flag);
                city.sendMessage(message);
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
            if (args.length == 1) {
                CallableMessage message = city.getInfo().getMessage();
                caller.sendMessage(message);
                return true;
            }
            if (args.length != 4) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                caller.sendMessage(message);
                return true;
            }
            if (!NumberUtils.isNumber(args[1]) || !NumberUtils.isNumber(args[2]) || !NumberUtils.isNumber(args[3])) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            if (!caller.isMayor()) {
                CallableMessage message = ErrorMessage.NO_PERMS;
                caller.sendMessage(message);
                return true;
            }
            double iron = Double.parseDouble(args[1]);
            double diamond = Double.parseDouble(args[2]);
            double netherite = Double.parseDouble(args[3]);
            if (iron < 0 || diamond < 0 || netherite < 0) {
                CallableMessage message = ErrorMessage.ILLEGAL_ARGS;
                caller.sendMessage(message);
                return true;
            }
            Balance tax = new Balance(iron, diamond, netherite);
            city.setTax(tax);
            CallableMessage message = LStateMessage.NEW_CITY_TAX;
            city.sendMessage(message);
            return true;
        }
        return false;
    }
}
