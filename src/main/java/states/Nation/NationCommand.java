package states.Nation;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import states.API.SMassiveAPI;
import states.API.StringAPI;
import states.States.AncapStates;
import states.Board.Board;
import states.City.City;
import states.Economy.Balance;
import states.ID.ID;
import states.Message.ErrorMessage;
import states.Message.Message;
import states.Message.StateMessage;
import states.Name.Name;
import states.Player.AncapPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static states.Nation.Nation.log;

public class  NationCommand implements CommandExecutor, TabCompleter {

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
        if (args[0].equals("info")) {
            return nationsNames;
        }
        if (player.isFree()) {
            return List.of("");
        }
        City city = player.getCity();
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
            List<City> nationCities = Arrays.asList(city.getNation().getCities());
            List<String> nationCitiesNames = new ArrayList<>();
            for (City nationCity : nationCities) {
                citiesNames.add(city.getName());
            }
            return nationCitiesNames;
        }
        if (args[0].equals("acceptrequest") || args[0].equals("declinerequest")) {
            if (city.isFree()) {
                return List.of("");
            }
            List<City> requestingCities = Arrays.asList(city.getNation().getRequestingCities());
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
            City[] nationCities = city.getNation().getCities();
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
            List<City> invitedCities = Arrays.asList(city.getNation().getInvitedCities());
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
            AncapPlayer[] residents = city.getNation().getResidents();
            List<String> residentsNames = new ArrayList<>();
            for (AncapPlayer resident : residents) {
                residentsNames.add(resident.getName());
            }
            AncapPlayer[] ministers = city.getNation().getMinisters();
            List<String> ministersNames = new ArrayList<>();
            for (AncapPlayer minister : ministers) {
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
            City[] nationCities = city.getNation().getCities();
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
            List<String> nationFlags = List.of(city.getNation().getFlags());
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
    public synchronized boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        AncapPlayer player = new AncapPlayer(sender.getName());
        if (args.length == 0) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            player.sendMessage(player.getCity().getNation().getInfo().getMessage());
            return true;
        }
        if (args[0].equals("info")) {
            if (args.length<2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (!new Nation(ID.getNationID(name)).exists()) {
                Message message = ErrorMessage.NATION_DOESNT_EXISTS;
                player.sendMessage(message);
                return true;
            }
            player.sendMessage(new Nation(ID.getNationID(name)).getInfo().getMessage());
            return true;
        }
        if (args[0].equals("new")) {
            if (args.length<2) {
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
            if (!city.isFree()) {
                Message message = ErrorMessage.CITY_IS_NOT_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!city.haveNationCreatingFee()) {
                Message message = ErrorMessage.NO_NATION_CREATION_FEE;
                player.sendMessage(message);
                return true;
            }
            String name = Name.getName(args);
            if (!Name.canBeDefinedWith(name)) {
                Message message = ErrorMessage.ILLEGAL_NAME;
                player.sendMessage(message);
                return true;
            }
            Nation nation = new Nation(ID.getNationID(name));
            if (nation.exists()) {
                Message message = ErrorMessage.NATION_EXISTS;
                player.sendMessage(message);
                return true;
            }
            city.grabNationCreationFee();
            nation.create(city, name);
            Message message = StateMessage.NATION_CREATE(city.getName(), name);
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
            City city = player.getCity();
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            Nation nation = city.getNation();
            int amount = Math.abs(Integer.parseInt(args[2]));
            if (player.getBalance().getAmountForType(type) < amount) {
                Message message = ErrorMessage.NOT_ENOUGH_MONEY;
                player.sendMessage(message);
                return true;
            }
            player.depositNation(nation, amount, type);
            Message message = StateMessage.NATION_PLAYER_DEPOSITED(player.getName(), amount, type);
            nation.sendMessage(message);
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
            City city = player.getCity();
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMinister() && !player.isLeader()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            Nation nation = city.getNation();
            int amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            if (nation.getBalance().getAmountForType(type) < amount) {
                Message message = ErrorMessage.NOT_ENOUGH_MONEY;
                player.sendMessage(message);
                return true;
            }
            player.withdrawNation(nation, amount, type);
            Message message = StateMessage.NATION_PLAYER_WITHDRAWED(player.getName(), amount, type);
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("delete")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isLeader()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            if (args.length < 2 || !args[1].equals("confirm")) {
                Message message = ErrorMessage.PLEASE_CONFIRM;
                player.sendMessage(message);
                return true;
            }
            Nation nation = player.getNation();
            Message message = StateMessage.NATION_REMOVE(nation.getName());
            AncapStates.sendMessage(message);
            nation.remove();
            return true;
        }
        if (args[0].equals("join")) {
            if (args.length < 2) {
                Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (!city.isFree()) {
                Message message = ErrorMessage.CITY_IS_NOT_FREE;
                player.sendMessage(message);
                return true;
            }
            Nation nation = new Nation(ID.getNationID(name));
            if (!nation.exists()) {
                Message message = ErrorMessage.NO_SUCH_NATION;
                player.sendMessage(message);
                return true;
            }
            if (!nation.freeToJoin() && !city.isInvitedTo(nation)) {
                Message message = ErrorMessage.NATION_ISNT_FREE_TO_JOIN;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            nation.addCity(city);
            Message message = StateMessage.NATION_CITY_JOINED(city.getName());
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("declineinvite")) {
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
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            Nation integrating = new Nation(ID.getNationID(name));
            if (!integrating.isInviting(city)) {
                Message message = ErrorMessage.NOT_INVITING_IN_NATION;
                player.sendMessage(message);
                return true;
            }
            city.declineInviteFrom(integrating);
            Message cityMessage = StateMessage.CITY_DECLINED_NATION_INVITE(integrating.getName());
            Message nationMessage = StateMessage.NATION_CITY_DECLINED_INVITE(city.getName());
            integrating.sendMessage(nationMessage);
            city.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("request")) {
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
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            Nation requesting = new Nation(ID.getNationID(name));
            if (!requesting.exists()) {
                Message message = ErrorMessage.NATION_DOESNT_EXISTS;
                player.sendMessage(message);
                return true;
            }
            if (city.isRequestingTo(requesting)) {
                Message message = ErrorMessage.YOUR_CITY_IS_ALREADY_REQUESTING_TO_THAT_NATION;
                player.sendMessage(message);
                return true;
            }
            city.addRequestTo(requesting);
            Message requestMessage = StateMessage.CITY_REQUESTED_TO_NATION(requesting.getName());
            Message nationMessage = StateMessage.NATION_RECIEVED_REQUEST(city.getName());
            requesting.sendMessage(nationMessage);
            city.sendMessage(requestMessage);
            return true;
        }
        if (args[0].equals("revokerequest")) {
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
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            Nation requesting = new Nation(ID.getNationID(name));
            if (!city.isRequestingTo(requesting)) {
                Message message = ErrorMessage.YOU_NOT_REQUESTING_TO_INTEGRATE_IN_THIS_NATION;
                player.sendMessage(message);
                return true;
            }
            city.removeRequestTo(requesting);
            Message requestMessage = StateMessage.CITY_REMOVED_REQUEST_TO_NATION(requesting.getName());
            Message nationMessage = StateMessage.NATION_CITY_REMOVED_REQUEST(city.getName());
            requesting.sendMessage(nationMessage);
            city.sendMessage(requestMessage);
            return true;
        }
        if (args[0].equals("leave")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            Message message = StateMessage.NATION_CITY_LEAVED(city.getName());
            Nation nation = city.getNation();
            nation.sendMessage(message);
            city.leaveNation();
            return true;
        }
        if (args[0].equals("kick")) {
            if (args.length<2) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isAssistant() && !player.isMayor()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String name = args[1];
            City kicked = new City(ID.getCityID(name));
            Nation nation = city.getNation();
            Message message = StateMessage.NATION_CITY_KICKED(kicked.getName());
            nation.sendMessage(message);
            nation.removeCity(kicked);
            return true;
        }
        if (args[0].equals("invite")) {
            if (args.length<2) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMinister() && !player.isLeader()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String cityName = args[1];
            City invited = new City(ID.getCityID(cityName));
            if (!invited.exists()) {
                Message message = ErrorMessage.CITY_DOESNT_EXIST;
                player.sendMessage(message);
                return true;
            }
            Nation nation = player.getNation();
            if (invited.isInvitedTo(nation)) {
                Message message = ErrorMessage.THAT_CITY_IS_ALREADY_INVITED_TO_YOUR_NATION;
                player.sendMessage(message);
                return true;
            }
            nation.addInviteTo(invited);
            Message cityMessage = StateMessage.CITY_NATION_INVITED(nation.getName());
            Message nationMessage = StateMessage.NATION_INVITED_CITY(invited.getName());
            nation.sendMessage(nationMessage);
            invited.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("revokeinvite")) {
            if (args.length<2) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMinister() && !player.isLeader()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String cityName = args[1];
            City invited = new City(ID.getCityID(cityName));
            Nation nation = player.getNation();
            if (!nation.isInviting(invited)) {
                Message message = ErrorMessage.CITY_DOESNT_INVITED;
                player.sendMessage(message);
                return true;
            }
            nation.removeInviteTo(invited);
            Message cityMessage = StateMessage.CITY_NATION_REVOKED_INVITE(nation.getName());
            Message nationMessage = StateMessage.NATION_REVOKED_INVITE_TO_CITY(invited.getName());
            nation.sendMessage(nationMessage);
            invited.sendMessage(cityMessage);
            return true;
        }
        if (args[0].equals("acceptrequest")) {
            if (args.length<2) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMinister() && !player.isLeader()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String cityName = args[1];
            City accepted = new City(ID.getCityID(cityName));
            Nation nation = player.getNation();
            if (!accepted.isRequestingTo(nation)) {
                Message message = ErrorMessage.CITY_DOESNT_REQUESTING;
                player.sendMessage(message);
                return true;
            }
            nation.addCity(accepted);
            Message message = StateMessage.NATION_CITY_JOINED(accepted.getName());
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("declinerequest")) {
            if (args.length<2) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isMinister() && !player.isLeader()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            String cityName = args[1];
            City accepted = new City(ID.getCityID(cityName));
            Nation nation = player.getNation();
            if (!accepted.isRequestingTo(nation)) {
                Message message = ErrorMessage.CITY_DOESNT_REQUESTING;
                player.sendMessage(message);
                return true;
            }
            accepted.removeRequestTo(nation);
            Message requestMessage = StateMessage.CITY_NATION_DECLINED_REQUEST(nation.getName());
            Message nationMessage = StateMessage.NATION_DECLINED_CITY_REQUEST(accepted.getName());
            nation.sendMessage(nationMessage);
            accepted.sendMessage(requestMessage);
            return true;
        }
        if (args[0].equals("minister")) {
            if (args.length<3) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isLeader()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            Nation nation = player.getNation();
            AncapPlayer minister = new AncapPlayer(args[2]);
            if (!minister.isCitizenOf(nation)) {
                Message message = ErrorMessage.HE_ISNT_CITIZEN_OF_YOUR_NATION;
                player.sendMessage(message);
                return true;
            }
            if (args[1].equals("set")) {
                if (!minister.isMinister()) {
                    Message message = ErrorMessage.HE_IS_ALREADY_MINISTER;
                    player.sendMessage(message);
                    return true;
                }
                nation.addMinister(minister);
                Message message = StateMessage.NATION_NEW_MINISTER(minister.getName());
                nation.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                if (!minister.isMinister()) {
                    Message message = ErrorMessage.HE_ISNT_MINISTER;
                    player.sendMessage(message);
                    return true;
                }
                nation.removeMinister(minister);
                Message message = StateMessage.NATION_REMOVED_MINISTER(minister.getName());
                nation.sendMessage(message);
                return true;
            }
        }
        if (args[0].equals("capital")) {
            if (args.length<2) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isLeader()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            if (args.length < 3 || !args[2].equals("confirm")) {
                Message message = ErrorMessage.PLEASE_CONFIRM;
                player.sendMessage(message);
                return true;
            }
            Nation nation = player.getNation();
            String cityName = args[1];
            City capital = new City(ID.getCityID(cityName));
            if (!capital.exists() || !capital.isIntegratedIn(nation)) {
                Message message = ErrorMessage.NO_SUCH_CITY;
                player.sendMessage(message);
                return true;
            }
            nation.setCapital(capital);
            Message message = StateMessage.NATION_NEW_CAPITAL(capital.getName());
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("color")) {
            if (args.length<2) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isAssistant() && !player.isLeader()) {
                Message message = ErrorMessage.NO_PERMS;
                player.sendMessage(message);
                return true;
            }
            if (!StringAPI.isHex(args[1])) {
                Message message = ErrorMessage.ILLEGAL_ARGS;
                player.sendMessage(message);
                return true;
            }
            Nation nation = player.getNation();
            NationColor color = new NationColor(args[1]);
            nation.setColor(color);
            Message message = StateMessage.NATION_SETTED_COLOR(color.getColor());
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("rename")) {
            if (args.length<2) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isLeader()) {
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
            Nation nation = player.getNation();
            Nation checkNation = new Nation(ID.getCityID(name));
            if (checkNation.exists()) {
                Message message = ErrorMessage.NATION_EXISTS;
                player.sendMessage(message);
                return true;
            }
            nation.setName(name);
            Message message = StateMessage.NATION_NEW_NAME(name);
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("board")) {
            if (args.length<2) {
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isAssistant() && !player.isMayor()) {
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
            Board board = new Board(args);
            Nation nation = player.getNation();
            nation.setBoard(board.toString());
            Message message = StateMessage.NATION_NEW_BOARD(board.toString());
            nation.sendMessage(message);
            return true;
        }
        if (args[0].equals("flag")) {
            if (player.isFree()) {
                Message message = ErrorMessage.FREE;
                player.sendMessage(message);
                return true;
            }
            City city = player.getCity();
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            if (!player.isLeader()) {
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
            Nation nation = city.getNation();
            if (args[1].equals("set")) {
                String flag = args[2];
                nation.setFlag(flag);
                Message message = StateMessage.NATION_ADDED_FLAG(flag);
                nation.sendMessage(message);
                return true;
            }
            if (args[1].equals("remove")) {
                String flag = args[2];
                nation.removeFlag(flag);
                Message message = StateMessage.NATION_REMOVED_FLAG(flag);
                nation.sendMessage(message);
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
            if (city.isFree()) {
                Message message = ErrorMessage.CITY_IS_FREE;
                player.sendMessage(message);
                return true;
            }
            Nation nation = city.getNation();
            if (args.length == 1) {
                Message message = nation.getInfo().getMessage();
                player.sendMessage(message);
                return true;
            }
            if (args[1].equals("set")) {
                if (args.length != 5) {
                    Message message = ErrorMessage.ILLEGAL_ARGS_NUMBER;
                    player.sendMessage(message);
                    return true;
                }
                if (!NumberUtils.isNumber(args[2]) || !NumberUtils.isNumber(args[3]) || !NumberUtils.isNumber(args[4])) {
                    Message message = ErrorMessage.ILLEGAL_ARGS;
                    player.sendMessage(message);
                    return true;
                }
                if (!player.isLeader()) {
                    Message message = ErrorMessage.NO_PERMS;
                    player.sendMessage(message);
                    return true;
                }
                double iron = Double.parseDouble(args[2]);
                double diamond = Double.parseDouble(args[3]);
                double netherite = Double.parseDouble(args[4]);
                if (iron < 0 || diamond < 0 || netherite < 0) {
                    Message message = ErrorMessage.ILLEGAL_ARGS;
                    player.sendMessage(message);
                    return true;
                }
                Balance tax = new Balance(iron, diamond, netherite);
                nation.setTax(tax);
                Message message = StateMessage.NEW_NATION_TAX;
                nation.sendMessage(message);
                return true;
            }
        }
        return false;
    }
}
