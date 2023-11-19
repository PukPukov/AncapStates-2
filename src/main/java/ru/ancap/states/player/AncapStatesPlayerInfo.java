package ru.ancap.states.player;

import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.null_.SafeNull;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.library.Balance;
import ru.ancap.states.message.InfoMessage;
import ru.ancap.states.message.LayeredModifies;
import ru.ancap.states.states.Nation.Nation;
import ru.ancap.states.states.city.City;
import ru.ancap.states.states.city.StateBalanceMessage;

import java.util.List;
import java.util.Map;

public class AncapStatesPlayerInfo {

    private final String name;
    private final @Nullable String cityName;
    private final @Nullable String nationName;
    private final Balance balance;
    private final int friends;
    private final @Nullable String friendsNames;
    private final int inviting;
    private final @Nullable String invitingNames;
    private final int requesting;
    private final @Nullable String requestingNames;

    public AncapStatesPlayerInfo(AncapStatesPlayer player) {
        this.name = player.getName();
        this.cityName = SafeNull.function(player.getCity(), City::getName);
        this.nationName = SafeNull.function(player.getNation(), Nation::getName);
        String friendsNames = player.getFriendsNames();
        this.friendsNames = friendsNames.equals("") ? null : friendsNames;
        this.friends = player.getFriends().size();
        String invitingNames = player.getInvitingNames();
        this.invitingNames = invitingNames.equals("") ? null : invitingNames;
        this.inviting = player.getInviting().size();
        String requestingNames = player.getRequestingNames();
        this.requestingNames = requestingNames.equals("") ? null : requestingNames;
        this.requesting = player.getRequesting().size();
        this.balance = player.getBalance();
    }

    public InfoMessage getMessage() {
        return new InfoMessage(
            "player",
            new LayeredModifies(Map.of(
                InfoMessage.DATA_LAYER, new Placeholder("name", this.name)
            )),
            new InfoMessage.Values(List.of(
                new InfoMessage.Value("city", SafeNull.function(this.cityName, Message::new)),
                new InfoMessage.Value("nation", SafeNull.function(this.nationName, Message::new)),
                new InfoMessage.Value("balance", new StateBalanceMessage(this.balance)),
                new InfoMessage.CountedValue("friends", this.friends, SafeNull.function(this.friendsNames, Message::new)),
                new InfoMessage.CountedValue("invites-to-cities", this.inviting, SafeNull.function(this.invitingNames, Message::new)),
                new InfoMessage.CountedValue("requests-to-cities", this.requesting, SafeNull.function(this.requestingNames, Message::new))
            ))
        );
    }
}
