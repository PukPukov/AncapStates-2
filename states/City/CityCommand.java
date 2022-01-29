package states.City;

import library.Hexagon;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import states.States.AncapStates;
import states.Board.Board;
import states.Chunk.AncapChunk;
import states.Chunk.OutpostChunk;
import states.Economy.Balance;
import states.ID.ID;
import states.Message.ErrorMessage;
import states.Message.Message;
import states.Message.StateMessage;
import states.Name.Name;
import states.Nation.Nation;
import states.Player.AncapPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CityCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        AncapPlayer player = new AncapPlayer(sender.getName());
        List<String> subcommands = new ArrayList(Arrays.asList(
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
                "taxes"
        ));
        List<AncapPlayer> onlinePlayers = List.of(AncapStates.getOnlinePlayers());
        List<City> cities = List.of(AncapStates.getCities());
        List<Nation> nations = List.of(AncapStates.getNations());
        List<String> citiesNames = new ArrayList<>();
        List<String> nationsNames = new ArrayList<>();
        List<String> onlinePlayersNames = new ArrayList<>();
        for (City city : cities) {
            citiesNames.add(city.getName());
        }
        for (Nation nation : nations) {
            nationsNames.add(nation.getName());
        }
        for (AncapPlayer ancapPlayer : onlinePlayers) {
            onlinePlayersNames.add(ancapPlayer.getName());
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
            if (player.isFree()) {
                return List.of("");
            }
            List<AncapPlayer> cityResidents = Arrays.asList(player.getCity().getResidents());
            List<String> residentsNames = new ArrayList<>();
            for (AncapPlayer resident : cityResidents) {
                residentsNames.add(resident.getName());
            }
            return residentsNames;
        }
        if (args[0].equals("request")) {
            return citiesNames;
        }
        if (args[0].equals("revokerequest")) {
            List<City> requestingCities = Arrays.asList(player.getRequesting());
            List<String> requestingNames = new ArrayList<>();
            for (City requesting : requestingCities) {
                requestingNames.add(requesting.getName());
            }
            return requestingNames;
        }
        if (args[0].equals("acceptrequest") || args[0].equals("declinerequest")) {
            if (player.isFree()) {
                return List.of("");
            }
            List<AncapPlayer> requestingPlayers = Arrays.asList(player.getCity().getRequestingPlayers());
            List<String> requestingNames = new ArrayList<>();
            for (AncapPlayer requesting : requestingPlayers) {
                requestingNames.add(requesting.getName());
            }
            return requestingNames;
        }
        if (args[0].equals("invite")) {
            if (player.isFree()) {
                return List.of("");
            }
            AncapPlayer[] residents = player.getCity().getResidents();
            List<String> residentsNames = new ArrayList<>();
            for (AncapPlayer resident : residents) {
                residentsNames.add(resident.getName());
            }
            onlinePlayersNames.removeAll(residentsNames);
            return onlinePlayersNames;
        }
        if (args[0].equals("declineinvite")) {
            List<City> invitingCities = Arrays.asList(player.getInviting());
            List<String> invitingNames = new ArrayList<>();
            for (City inviting : invitingCities) {
                invitingNames.add(inviting.getName());
            }
            return invitingNames;
        }
        if (args[0].equals("revokeinvite")) {
            if (player.isFree()) {
                return List.of("");
            }
            List<AncapPlayer> invitedPlayers = Arrays.asList(player.getCity().getInvitedPlayers());
            List<String> invitedNames = new ArrayList<>();
            for (AncapPlayer invited : invitedPlayers) {
                invitedNames.add(invited.getName());
            }
            return invitedNames;
        }
        if (args[0].equals("board")) {
            return List.of("<текст>");
        }
        if (args[0].equals("assistant")) {
            if (player.isFree()) {
                return List.of("");
            }
            List<String> assistantSubcommands = new ArrayList(Arrays.asList(
                    "set",
                    "remove"
            ));
            if (args.length == 2) {
                return assistantSubcommands;
            }
            AncapPlayer[] residents = player.getCity().getResidents();
            List<String> residentsNames = new ArrayList<>();
            for (AncapPlayer resident : residents) {
                residentsNames.add(resident.getName());
            }
            AncapPlayer[] assistants = player.getCity().getAssistants();
            List<String> assistantsNames = new ArrayList<>();
            for (AncapPlayer assistant : assistants) {
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
            return List.of("<новое название>");
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
            List<AncapPlayer> friends = List.of(player.getFriends());
            List<String> friendsNames = new ArrayList<>();
            for (AncapPlayer friend : friends) {
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
            if (player.isFree()) {
                return List.of("");
            }
            AncapPlayer[] cityResidents = player.getCity().getResidents();
            List<String> residentsNames = new ArrayList<>();
            for (AncapPlayer resident : cityResidents) {
                residentsNames.add(resident.getName());
            }
            residentsNames.remove(player.getCity().getMayor().getName());
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
            if (player.isFree()) {
                return List.of("");
            }
            List<String> flagSubCommands = new ArrayList(Arrays.asList(
                    "add",
                    "remove"
            ));
            List<String> flagsList = new ArrayList(Arrays.asList(
                    "FREE_TO_JOIN"
            ));
            List<String> cityFlags = List.of(player.getCity().getFlags());
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
    public synchronized boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        AncapPlayer player = new AncapPlayer(sender.getName());
        if (args.length == 0) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            player.sendMessage(player.getCity().getInfo().getMessage());
            return true;
        }
        if (args[0].equals("info")) {
            if (args.length<2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (!new City(ID.getCityID(name)).exists()) {
                Message message = ErrorMessage.CITY_DOESNT_EXIST;
                player.sendMessage(message);
                return true;
            }
            player.sendMessage(new City(ID.getCityID(name)).getInfo().getMessage());
            return true;
        }
        if (args[0].equals("new")) {
            if (args.length<2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (!player.isFree()) {
                Message message = ErrorMessage.NOT_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.haveCityCreationFee()) {
                Message message = ErrorMessage.NO_CITY_CREATION_FEE;
                player.sendMessage(message);
                return true;
            }
            if (player.getCityAtPosition() != null) {
                Bukkit.broadcastMessage(player.getCityAtPosition().toString());
                Message message = ErrorMessage.HERE_IS_OTHER_CITY;
                player.sendMessage(message);
                return true;
            }
            String name = Name.getName(args);
            if (!Name.canBeDefinedWith(name)) {
                Message message = ErrorMessage.ILLEGAL_NAME;
                player.sendMessage(message);
                return true;
            }
            City city = new City(ID.getCityID(name));
            if (city.exists()) {
                Message message = ErrorMessage.CITY_EXISTS;
                player.sendMessage(message);
                return true;
            }
            player.grabCityCreationFee();
            city.create(player, name);
            Message message = StateMessage.CITY_CREATE(player.getName(), name);
            AncapStates.sendMessage(message);
            return true;
        }
        if (args[0].equals("delete")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            Nation nation = player.getNation();
            if (nation != null) {
                if (nation.getCapital().equals(player.getCity())) {
                    Message message = ErrorMessage.CAPITAL_CANT_BE_DELETED;
                    player.sendMessage(message);
                    return true;
                }
            }
            if (args.length < 2 || !args[1].equals("confirm")) {
                Message message = ErrorMessage.PLEASE_CONFIRM;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            String name = city.getName();
            city.remove();
            Message message = StateMessage.CITY_REMOVE(name);
            AncapStates.sendMessage(message);
            return true;
        }
        if (args[0].equals("deposit")) {
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
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            int amount = Math.abs(Integer.parseInt(args[2]));
            City city = player.getCity();
            if (player.getBalance().getAmountForType(type) < amount) {
                Message message = ErrorMessage.NOT_ENOUGH_MONEY;
                player.sendMessage(message);
                return true;
            }
            player.depositCity(city, amount, type);
            Message cityMessage = StateMessage.CITY_PLAYER_DEPOSITED(player.getName(), amount, type);
            city.sendMessage(cityMessage);
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
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            int amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (city.getBalance().getAmountForType(type) < amount) {
                Message message = ErrorMessage.NOT_ENOUGH_MONEY;
                player.sendMessage(message);
                return true;
            }
            player.withdrawCity(city, amount, type);
            Message cityMessage = StateMessage.CITY_PLAYER_WITHDRAWED(player.getName(), amount, type);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("join")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            City city = new City(ID.getCityID(name));
            if (!player.isFree()) {
                Message message = ErrorMessage.NOT_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!city.exists()) {
                Message message = ErrorMessage.NO_SUCH_CITY;
                player.sendMessage(message);
                return true;
            }
            if (!city.freeToJoin() && !player.isInvitedTo(city)) {
                Message message = ErrorMessage.CITY_ISNT_FREE_TO_JOIN;
                player.sendMessage(message);
                return true;
            }
            city.addResident(player);
            Message message = StateMessage.CITY_PLAYER_JOINED(player.getName());
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("leave")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (player.isMayor()) {
                Message message = ErrorMessage.MAYOR_CANT_LEAVE;
                player.sendMessage(message);
                return true;
            }
            Message message = StateMessage.CITY_PLAYER_LEAVED(player.getName());
            City city = player.getCity();
            city.sendMessage(message);
            player.leaveCity();
            return true;
        }
        if (args[0].equals("kick")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (!player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            AncapPlayer kicked = new AncapPlayer(args[1]);
            if (!kicked.isResidentOf(city)) {
                Message message = ErrorMessage.NOT_RESIDENT;
                player.sendMessage(message);
                return true;
            }
            if (kicked.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            Message message = StateMessage.CITY_PLAYER_KICKED(kicked.getName());
            city.sendMessage(message);
            city.removeResident(kicked);
            return true;
        }
        if (args[0].equals("request")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (!player.isFree()) {
                Message message = ErrorMessage.NOT_FREE;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            City city = new City(ID.getCityID(name));
            if (!city.exists()) {
                Message message = ErrorMessage.NO_SUCH_CITY;
                player.sendMessage(message);
                return true;
            }
            if (city.freeToJoin()) {
                Message message = ErrorMessage.CITY_IS_FREE_TO_JOIN;
                player.sendMessage(message);
                return true;
            }
            if (player.isAskingToJoinIn(city)) {
                Message message = ErrorMessage.YOU_ARE_ALREADY_REQUESTING_TO_THAT_CITY;
                player.sendMessage(message);
                return true;
            }
            player.addRequestTo(city);
            Message playerMessage = StateMessage.PLAYER_REQUESTED_TO_CITY(city.getName());
            Message cityMessage = StateMessage.CITY_PLAYER_REQUESTED(player.getName());
            player.sendMessage(playerMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("revokerequest")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            City city = new City(ID.getCityID(name));
            if (!city.exists()) {
                Message message = ErrorMessage.NO_SUCH_CITY;
                player.sendMessage(message);
                return true;
            }
            if (!player.isAskingToJoinIn(city)) {
                Message message = ErrorMessage.YOU_NOT_ASKING_TO_JOIN_IN_CITY;
                player.sendMessage(message);
                return true;
            }
            player.removeRequestTo(city);
            Message playerMessage = StateMessage.PLAYER_REVOKE_REQUEST_TO_CITY(city.getName());
            Message cityMessage = StateMessage.CITY_PLAYER_REVOKE_REQUEST(player.getName());
            player.sendMessage(playerMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("acceptrequest")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (!player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            AncapPlayer accepted = new AncapPlayer(args[1]);
            if (!accepted.isAskingToJoinIn(city)) {
                Message message = ErrorMessage.NOT_ASKING_TO_JOIN_IN_CITY;
                player.sendMessage(message);
                return true;
            }
            city.addResident(accepted);
            Message message = StateMessage.CITY_PLAYER_JOINED(accepted.getName());
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("declinerequest")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (!player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            AncapPlayer declined = new AncapPlayer(args[1]);
            if (!declined.isAskingToJoinIn(city)) {
                Message message = ErrorMessage.NOT_ASKING_TO_JOIN_IN_CITY;
                player.sendMessage(message);
                return true;
            }
            city.declineRequestFrom(declined);
            Message playerMessage = StateMessage.PLAYER_CITY_DECLINED_REQUEST(city.getName());
            Message cityMessage = StateMessage.CITY_DECLINED_REQUEST(declined.getName());
            declined.sendMessage(playerMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("declineinvite")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            City joining = new City(ID.getCityID(name));
            if (!player.isInvitedTo(joining)) {
                Message message = ErrorMessage.NOT_INVITING_IN_CITY;
                player.sendMessage(message);
                return true;
            }
            player.declineInviteFrom(joining);
            Message playerMessage = StateMessage.PLAYER_DECLINED_CITY_INVITE(joining.getName());
            Message cityMessage = StateMessage.CITY_PLAYER_DECLINED_INVITE(player.getName());
            joining.sendMessage(cityMessage);
            player.sendMessage(playerMessage);
            return true;
        }
        if (args[0].equals("invite")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (!Name.canBeDefinedWith(name)) {
                Message message = ErrorMessage.ILLEGAL_NAME;
                player.sendMessage(message);
                return true;
            }
            AncapPlayer invited = new AncapPlayer(name);
            if (!invited.isFree()) {
                Message message = ErrorMessage.HE_ISNT_FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (invited.isInvitedTo(city)) {
                Message message = ErrorMessage.HE_IS_ALREADY_INVITED_TO_YOUR_CITY;
                player.sendMessage(message);
                return true;
            }
            city.addInviteTo(invited);
            Message inviteMessage = StateMessage.PLAYER_INVITED_TO_CITY(city.getName());
            invited.sendMessage(inviteMessage);
            Message cityMessage = StateMessage.CITY_INVITED_PLAYER(invited.getName());
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("revokeinvite")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isAssistant()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (!Name.canBeDefinedWith(name)) {
                Message message = ErrorMessage.ILLEGAL_NAME;
                player.sendMessage(message);
                return true;
            }
            AncapPlayer invited = new AncapPlayer(name);
            City city = player.getCity();
            if (!invited.isInvitedTo(city)) {
                Message message = ErrorMessage.HE_ISNT_INVITED;
                player.sendMessage(message);
                return true;
            }
            city.removeInviteTo(invited);
            Message inviteMessage = StateMessage.PLAYER_REMOVED_INVITE_TO_CITY(city.getName());
            Message cityMessage = StateMessage.CITY_REMOVED_INVITE_TO_PLAYER(invited.getName());
            invited.sendMessage(inviteMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("board")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (!player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            Board board = new Board(args);
            city.setBoard(board.toString());
            Message message = StateMessage.CITY_NEW_BOARD(board.toString());
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("assistant")) {
            if (args.length < 3) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String name = args[2];
            if (!Name.canBeDefinedWith(name)) {
                Message message = ErrorMessage.ILLEGAL_NAME;
                player.sendMessage(message);
                return true;
            }
            AncapPlayer assistant = new AncapPlayer(name);
            if (!assistant.isResidentOf(city)) {
                Message message = ErrorMessage.HE_ISNT_RESIDENT_OF_YOUR_CITY;
                player.sendMessage(message);
                return true;
            }
            if (args[1].equals("set")) {
                if (assistant.isAssistant()) {
                    Message message = ErrorMessage.HE_IS_ALREADY_ASSISTANT;
                    player.sendMessage(message);
                    return true;
                }
                city.addAssistant(assistant);
                Message message = StateMessage.CITY_NEW_ASSISTANT(assistant.getName());
                city.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                if (!assistant.isAssistant()) {
                    Message message = ErrorMessage.HE_ISNT_ASSISTANT;
                    player.sendMessage(message);
                    return true;
                }
                city.removeAssistant(assistant);
                Message message = StateMessage.CITY_REMOVED_ASSISTANT(assistant.getName());
                city.sendMessage(message);
                return true;
            }
        }
        if (args[0].equals("spawn")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.canTeleportToCitySpawn()) {
                Message message = ErrorMessage.CANT_TELEPORT_TO_CITY_SPAWN;
                player.sendMessage(message);
                return true;
            }
            player.citySpawn();
            City city = player.getCity();
            Message message = StateMessage.PLAYER_TELEPORTED_TO_CITY_SPAWN(city.getName());
            player.sendMessage(message);
            return true;
        }
        if (args[0].equals("allow")) {
            if (args.length < 3) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            String allowLevelName = args[1];
            if (!AllowLevel.canBeDefinedWith(allowLevelName)) {
                Message message = ErrorMessage.NO_SUCH_ALLOW_LEVEL;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            AllowLevel allowLevel = new AllowLevel(allowLevelName);
            city.setAllowLevel(allowLevel);
            Message message = StateMessage.ALLOW_LEVEL_CHANGED(String.valueOf(allowLevel.getInt()));
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("rename")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String name = Name.getName(args);
            if (!Name.canBeDefinedWith(name)) {
                Message message = ErrorMessage.ILLEGAL_NAME;
                player.sendMessage(message);
                return true;
            }
            City checkCity = new City(ID.getCityID(name));
            if (checkCity.exists()) {
                Message message = ErrorMessage.CITY_EXISTS;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            city.setName(name);
            Message message = StateMessage.CITY_NEW_NAME(name);
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("limit")) {
            if (args.length < 3) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (args[1].equals("personal")) {
                if (args.length < 4) {
                    Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                    player.sendMessage(message);
                    return true;
                }

                AncapPlayer limited = new AncapPlayer(args[2]);
                if (!NumberUtils.isNumber(args[3])) {
                    Message message = ErrorMessage.ILLEGAL_ARGS;
                    player.sendMessage(message);
                    return true;
                }
                int limit = Integer.parseInt(args[3]);
                city.setLimit(limited, limit);
                Message message = StateMessage.CITY_NEW_PERSONAL_LIMIT(limited.getName(), String.valueOf(limit));
                city.sendMessage(message);
                return true;
            }
            String limitTypeName = args[1].toLowerCase();
            if (!limitTypeName.equals("resident") && !limitTypeName.equals("assistant")) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            if (!NumberUtils.isNumber(args[2])) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            int limit = Integer.parseInt(args[2]);
            LimitType limitType = new LimitType(limitTypeName);
            city.setLimit(limitType, limit);
            Message message = StateMessage.CITY_NEW_LIMIT(limitType.toString(), String.valueOf(limit));
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("chunk")) {
            if (args.length < 3) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            AncapChunk ancapChunk = player.getChunk();
            if (args[1].equals("private")) {
                if (args[2].equals("claim")) {
                    if (!player.canClaimPrivateChunks()) {
                        Message message = ErrorMessage.NO_PERMS;
                        player.sendMessage(message);
                        return true;
                    }
                    if (player.getPrivateChunk().getOwner() != null) {
                        Message message = ErrorMessage.PRIVATE_CHUNK_IS_ALREADY_CLAIMED;
                        player.sendMessage(message);
                        return true;
                    }
                    if (!city.equals(player.getCityAtPosition())) {
                        Message message = ErrorMessage.ITS_NOT_YOUR_CITY;
                        player.sendMessage(message);
                        return true;
                    }
                    if (!player.canClaimNewPrivateChunk()) {
                        Message message = ErrorMessage.CANT_CLAIM_NEW_PRIVATE_CHUNK;
                        player.sendMessage(message);
                        return true;
                    }
                    city.addPrivateChunk(player, ancapChunk);
                    Message message = StateMessage.CITY_PRIVATE_CHUNK_CLAIMED(player.getName(), ancapChunk.toString());
                    city.sendMessage(message);
                    return true;
                }
                if (args[2].equals("unclaim")) {
                    if (player.getPrivateChunk() == null) {
                        Message message = ErrorMessage.PRIVATE_CHUNK_ISNT_CLAIMED;
                        player.sendMessage(message);
                        return true;
                    }
                    AncapPlayer owner = player.getPrivateChunk().getOwner();
                    if (!owner.equals(player)) {
                        Message message = ErrorMessage.ITS_NOT_YOUR_CHUNK;
                        player.sendMessage(message);
                        return true;
                    }
                    city.removePrivateChunk(ancapChunk);
                    return true;
                }
            }
            if (args[1].equals("outpost")) {
                if (!player.isAssistant() && !player.isMayor()) {
                    Message message = ErrorMessage.NO_PERMS;
                    player.sendMessage(message);
                    return true;
                }
                if (args[2].equals("claim")) {
                    if (player.getOutpostChunk().getOwner() != null) {
                        Message message = ErrorMessage.OUTPOST_CHUNK_IS_ALREADY_CLAIMED;
                        player.sendMessage(message);
                        return true;
                    }
                    if (!city.canClaimNewOutpostChunk()) {
                        Message message = ErrorMessage.CANT_CLAIM_NEW_OUTPOST_CHUNK;
                        player.sendMessage(message);
                        return true;
                    }
                    city.addOutpostChunk(ancapChunk);
                    Message message = StateMessage.CITY_CLAIMED_NEW_OUTPOST_CHUNK(ancapChunk.toString());
                    city.sendMessage(message);
                    return true;
                }
                if (args[2].equals("unclaim")) {
                    OutpostChunk outpostChunk = player.getOutpostChunk();
                    if (outpostChunk.getOwner() == null) {
                        Message message = ErrorMessage.OUTPOST_CHUNK_ISNT_CLAIMED;
                        player.sendMessage(message);
                        return true;
                    }
                    if (!outpostChunk.getOwner().equals(city)) {
                        Message message = ErrorMessage.ITS_NOT_YOUR_CITY_CHUNK;
                        player.sendMessage(message);
                        return true;
                    }
                    city.removeOutpostChunk(ancapChunk);
                    Message message = StateMessage.CITY_CLAIMED_NEW_OUTPOST_CHUNK(ancapChunk.toString());
                    city.sendMessage(message);
                    return true;
                }
            }
        }
        if (args[0].equals("friend")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String name = Name.getName(args, 2);
            if (!Name.canBeDefinedWith(name)) {
                Message message = ErrorMessage.ILLEGAL_NAME;
                player.sendMessage(message);
                return true;
            }
            AncapPlayer friend = new AncapPlayer(args[2]);
            if (args[1].equals("add")) {
                player.addFriend(friend);
                Message message = StateMessage.PLAYER_NEW_FRIEND(friend.getName());
                player.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                player.removeFriend(friend);
                Message message = StateMessage.PLAYER_NEW_FRIEND(friend.getName());
                player.sendMessage(message);
                return true;
            }
        }
        if (args[0].equals("claim")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (!city.haveHexagonClaimingFee()) {
                Message message = ErrorMessage.NOT_ENOUGH_MONEY;
                player.sendMessage(message);
                return true;
            }
            Hexagon hexagon = AncapStates.grid.getHexagon(player);
            if (CityMap.getCity(hexagon) != null) {
                Message message = ErrorMessage.HEXAGON_IS_ALREADY_CLAIMED;
                player.sendMessage(message);
                return true;
            }
            if (!city.canAttach(hexagon)) {
                Message message = ErrorMessage.HEXAGON_ISNT_CONNECTING_TO_CITY;
                player.sendMessage(message);
                return true;
            }
            city.grabHexagonClaimingFee();
            city.addHexagon(hexagon);
            Message message = StateMessage.CITY_CLAIMED_NEW_HEXAGON(String.valueOf(hexagon.toString()));
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("unclaim")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            Hexagon hexagon = AncapStates.grid.getHexagon(player);
            if (!Objects.equals(CityMap.getCity(hexagon), player.getCity())) {
                Message message = ErrorMessage.ITS_NOT_YOUR_CITY_HEXAGON;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (city.getHomeHexagon().equals(hexagon)) {
                Message message = ErrorMessage.HOME_HEXAGON_CANT_BE_UNCLAIMED;
                player.sendMessage(message);
                return true;
            }
            city.giveHexagonUnclaimingRepayment();
            city.removeHexagon(hexagon);
            Message message = StateMessage.CITY_UNCLAIMED_HEXAGON(String.valueOf(hexagon.toString()));
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("mayor")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            if (args.length < 3 || !args[2].equals("confirm")) {
                Message message = ErrorMessage.PLEASE_CONFIRM;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            String name = args[1];
            if (!Name.canBeDefinedWith(name)) {
                Message message = ErrorMessage.ILLEGAL_NAME;
                player.sendMessage(message);
                return true;
            }
            AncapPlayer mayor = new AncapPlayer(name);
            if (!mayor.isResidentOf(city)) {
                Message message = ErrorMessage.HE_ISNT_RESIDENT_OF_YOUR_CITY;
                player.sendMessage(message);
                return true;
            }
            city.setMayor(mayor);
            Message message = StateMessage.CITY_NEW_MAYOR(mayor.getName());
            city.sendMessage(message);
            return true;
        }
        if (args[0].equals("flag")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            if (args.length < 3) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String name = Name.getName(args, 2);
            if (!Name.canBeDefinedWith(name)) {
                Message message = ErrorMessage.ILLEGAL_NAME;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (args[1].equals("set")) {
                String flag = args[2];
                city.setFlag(flag);
                Message message = StateMessage.CITY_ADDED_FLAG(flag);
                city.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                String flag = args[2];
                city.removeFlag(flag);
                Message message = StateMessage.CITY_REMOVED_FLAG(flag);
                city.sendMessage(message);
                return true;
            }
        }
        if (args[0].equals("taxes")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (args.length == 1) {
                Message message = city.getInfo().getMessage();
                player.sendMessage(message);
                return true;
            }
            if (args.length != 4) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            if (!NumberUtils.isNumber(args[1]) || !NumberUtils.isNumber(args[2]) || !NumberUtils.isNumber(args[3])) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            double iron = Double.parseDouble(args[1]);
            double diamond = Double.parseDouble(args[2]);
            double netherite = Double.parseDouble(args[3]);
            if (iron < 0 || diamond < 0 || netherite < 0) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            Balance tax = new Balance(iron, diamond, netherite);
            city.setTax(tax);
            Message message = StateMessage.NEW_CITY_TAX;
            city.sendMessage(message);
            return true;
        }
        return false;
    }
}
