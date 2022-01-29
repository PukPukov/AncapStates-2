package states.Message;

import states.Config.Config;

public class StateMessage extends Message {

    public static final Message NEW_DAY = new StateMessage("new_day");

    public static final Message NEW_NATION_TAX = new StateMessage("new_tax");

    public static final Message NEW_CITY_TAX = new StateMessage("new_city_tax");

    private String[] messages;

    private StateMessage(String path) {
        Config messagesConfig = Config.MESSAGES_CONFIGURATION;
        this.messages = new String[1];
        this.messages[0] = "&6&lГосударства >> &f"+messagesConfig.getString("statemessage." + path);
        String[] mess = this.messages;
        super.setStrings(mess);
    }

    public static Message CITY_CREATE(String creatorName, String cityName) {
        return new StateMessage("city_create").format(creatorName, cityName);
    }

    public static Message CITY_REMOVE(String name) {
        return new StateMessage("city_remove").format(name);
    }

    public static Message CITY_NEW_NAME(String name) {
        return new StateMessage("city_new_name").format(name);
    }

    public static Message CITY_NEW_ASSISTANT(String name) {
        return new StateMessage("city_new_assistant").format(name);
    }

    public static Message CITY_REMOVED_ASSISTANT(String name) {
        return new StateMessage("city_removed_assistant").format(name);
    }

    public static Message CITY_NEW_MAYOR(String name) {
        return new StateMessage("city_new_mayor").format(name);
    }

    public static Message CITY_NEW_BALANCE() {
        return new StateMessage("city_new_balance");
    }

    public static Message ALLOW_LEVEL_CHANGED(String allowLevel) {
        return new StateMessage("allow_level_changed").format(allowLevel);
    }

    public static Message CITY_JOINED_NATION(String name) {
        return new StateMessage("city_joined_nation").format(name);
    }

    public static Message CITY_NEW_BOARD(String board) {
        return new StateMessage("city_new_board").format(board);
    }

    public static Message CITY_PLAYER_JOINED(String name) {
        return new StateMessage("city_player_joined").format(name);
    }

    public static Message CITY_PLAYER_LEAVED(String name) {
        return new StateMessage("city_player_leaved").format(name);
    }

    public static Message PLAYER_INVITED_TO_CITY(String name) {
        return new StateMessage("player_invited_to_city").format(name);
    }

    public static Message CITY_INVITED_PLAYER(String name) {
        return new StateMessage("city_invited_player").format(name);
    }

    public static Message PLAYER_REMOVED_INVITE_TO_CITY(String name) {
        return new StateMessage("player_removed_invite_to_city").format(name);
    }

    public static Message CITY_REMOVED_INVITE_TO_PLAYER(String name) {
        return new StateMessage("city_removed_invite_to_player").format(name);
    }

    public static Message CITY_REQUESTED_TO_NATION(String name) {
        return new StateMessage("city_requested_to_nation").format(name);
    }

    public static Message NATION_RECIEVED_REQUEST(String name) {
        return new StateMessage("nation_recieved_request").format(name);
    }

    public static Message CITY_REMOVED_REQUEST_TO_NATION(String name) {
        return new StateMessage("city_removed_request_to_nation").format(name);
    }

    public static Message NATION_CITY_REMOVED_REQUEST(String name) {
        return new StateMessage("nation_city_removed_request").format(name);
    }

    public static Message PLAYER_CITY_DECLINED_REQUEST(String name) {
        return new StateMessage("player_city_declined_request").format(name);
    }

    public static Message CITY_DECLINED_REQUEST(String name) {
        return new StateMessage("city_declined_request").format(name);
    }

    public static Message CITY_DECLINED_NATION_INVITE(String name) {
        return new StateMessage("city_declined_nation_invite").format(name);
    }

    public static Message NATION_CITY_DECLINED_INVITE(String name) {
        return new StateMessage("nation_city_declined_invite").format(name);
    }

    public static Message CITY_NEW_PERSONAL_LIMIT(String limited, String valueOf) {
        return new StateMessage("city_new_personal_limit").format(limited, valueOf);
    }

    public static Message CITY_NEW_LIMIT(String toString, String valueOf) {
        return new StateMessage("city_new_limit").format(toString, valueOf);
    }

    public static Message CITY_PRIVATE_CHUNK_CLAIMED(String name, String toString) {
        return new StateMessage("city_private_chunk_claimed").format(name, toString);
    }

    public static Message CITY_PRIVATE_CHUNK_UNCLAIMED(String name, String toString) {
        return new StateMessage("city_private_chunk_unclaimed").format(name, toString);
    }

    public static Message CITY_CLAIMED_NEW_OUTPOST_CHUNK(String toString) {
        return new StateMessage("city_claimed_new_outpost_chunk").format(toString);
    }

    public static Message CITY_CLAIMED_NEW_HEXAGON(String valueOf) {
        return new StateMessage("city_claimed_new_hexagon").format(valueOf);
    }

    public static Message CITY_UNCLAIMED_HEXAGON(String valueOf) {
        return new StateMessage("city_unclaimed_hexagon").format(valueOf);
    }

    public static Message CITY_LEAVED_NATION(String name) {
        return new StateMessage("city_leaved_nation").format(name);
    }

    public static Message NATION_CITY_LEAVED(String name) {
        return new StateMessage("nation_city_leaved").format(name);
    }

    public static Message NATION_CREATE(String name, String name1) {
        return new StateMessage("nation_create").format(name, name1);
    }

    public static Message NATION_NEW_NAME(String name) {
        return new StateMessage("nation_new_name").format(name);
    }

    public static Message NATION_NEW_MINISTER(String name) {
        return new StateMessage("nation_new_minister").format(name);
    }

    public static Message NATION_REMOVED_MINISTER(String name) {
        return new StateMessage("nation_removed_minister").format(name);
    }

    public static Message NATION_NEW_CAPITAL(String name) {
        return new StateMessage("nation_new_capital").format(name);
    }

    public static Message NATION_NEW_BOARD(String board) {
        return new StateMessage("nation_new_board").format(board);
    }

    public static Message NATION_CITY_JOINED(String name) {
        return new StateMessage("nation_city_joined").format(name);
    }

    public static Message NATION_REMOVE(String name) {
        return new StateMessage("nation_remove").format(name);
    }

    public static Message CITY_ADDED_FLAG(String flag) {
        return new StateMessage("city_added_flag").format(flag);
    }

    public static Message CITY_REMOVED_FLAG(String flag) {
        return new StateMessage("city_removed_flag").format(flag);
    }

    public static Message CITY_NATION_REVOKED_INVITE(String name) {
        return new StateMessage("city_nation_revoked_invite").format(name);
    }

    public static Message NATION_REVOKED_INVITE_TO_CITY(String name) {
        return new StateMessage("nation_revoked_invite_to_city").format(name);
    }

    public static Message CITY_NATION_INVITED(String name) {
        return new StateMessage("city_nation_invited").format(name);
    }

    public static Message NATION_INVITED_CITY(String name) {
        return new StateMessage("nation_invited_city").format(name);
    }

    public static Message NATION_SETTED_COLOR(String color) {
        return new StateMessage("nation_setted_color").format(color);
    }

    public static Message PLAYER_REQUESTED_TO_CITY(String name) {
        return new StateMessage("player_requested_to_city").format(name);
    }

    public static Message CITY_PLAYER_REQUESTED(String name) {
        return new StateMessage("city_player_requested").format(name);
    }

    public static Message PLAYER_REVOKE_REQUEST_TO_CITY(String name) {
        return new StateMessage("player_revoke_request_to_city").format(name);
    }

    public static Message CITY_PLAYER_REVOKE_REQUEST(String name) {
        return new StateMessage("city_player_revoke_request").format(name);
    }

    public static Message PLAYER_TELEPORTED_TO_CITY_SPAWN(String name) {
        return new StateMessage("player_teleported_to_city_spawn").format(name);
    }

    public static Message PLAYER_NEW_FRIEND(String name) {
        return new StateMessage("player_new_friend").format(name);
    }

    public static Message PLAYER_FRIEND_REMOVED(String name) {
        return new StateMessage("player_friend_removed").format(name);
    }

    public static Message PLAYER_DECLINED_CITY_INVITE(String name) {
        return new StateMessage("player_declined_city_invite").format(name);
    }

    public static Message CITY_PLAYER_DECLINED_INVITE(String name) {
        return new StateMessage("city_player_declined_invite").format(name);
    }

    public static Message DEPOSITED(String valueOf, String type) {
        return new StateMessage("player_deposited").format(valueOf, type);
    }

    public static Message WITHDRAWED(String valueOf, String type) {
        return new StateMessage("player_withdrawed").format(valueOf, type);
    }

    public static Message CITY_PLAYER_DEPOSITED(String name, int amount, String type) {
        return new StateMessage("city_player_deposited").format(name, String.valueOf(amount), type);
    }

    public static Message CITY_PLAYER_WITHDRAWED(String name, int amount, String type) {
        return new StateMessage("city_player_withdrawed").format(name, String.valueOf(amount), type);
    }

    public static Message NATION_PLAYER_DEPOSITED(String name, int amount, String type) {
        return new StateMessage("nation_player_deposited").format(name, String.valueOf(amount), type);
    }

    public static Message NATION_PLAYER_WITHDRAWED(String name, int amount, String type) {
        return new StateMessage("nation_player_withdrawed").format(name, String.valueOf(amount), type);
    }

    public static Message CITY_PLAYER_KICKED(String name) {
        return new StateMessage("city_player_kicked").format(name);
    }

    public static Message NATION_ADDED_FLAG(String flag) {
        return new StateMessage("nation_added_flag").format(flag);
    }

    public static Message NATION_REMOVED_FLAG(String flag) {
        return new StateMessage("nation_removed_flag").format(flag);
    }

    public static Message NATION_CITY_KICKED(String name) {
        return new StateMessage("nation_city_kicked").format(name);
    }

    public static Message CITY_NATION_DECLINED_REQUEST(String name) {
        return new StateMessage("city_nation_declined_request").format(name);
    }

    public static Message NATION_DECLINED_CITY_REQUEST(String name) {
        return new StateMessage("nation_declined_city_request").format(name);
    }

    public static Message PLAYER_RECIEVED_MONEY(String name, double amount, String type) {
        return new StateMessage("player_recieved_money").format(name, String.valueOf(amount), type);
    }

    public static Message PLAYER_SENDED_MONEY(String name, double amount, String type) {
        return new StateMessage("player_sended_money").format(name, String.valueOf(amount), type);
    }

    public static Message PLAYER_CANT_PAY_TAXES_AND_KICKED(String name) {
        return new StateMessage("player_cant_pay_taxes_and_kicked").format(name);
    }

    public static Message CITY_DESTROYED_BY_TAXES(String name) {
        return new StateMessage("city_destroyed_by_taxes").format(name);
    }

    public static Message CITY_DESTROYED_BY_CORRUPTION(String name) {
        return new StateMessage("city_destroyed_by_corruption").format(name);
    }

    public static Message CITY_CANT_PAY_TAXES_AND_KICKED(String name) {
        return new StateMessage("city_cant_pay_taxes_and_kicked").format(name);
    }

    public static Message NATION_DESTROYED_BY_TAXES(String name) {
        return new StateMessage("nation_destroyed_by_taxes").format(name);
    }

    public static Message NATION_DESTROYED_BY_CORRUPTION(String name) {
        return new StateMessage("nation_destroyed_by_corruption").format(name);
    }
}
