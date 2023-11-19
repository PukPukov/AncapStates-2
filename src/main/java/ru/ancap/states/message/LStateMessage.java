package ru.ancap.states.message;

import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.modifier.Modifier;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.states.AncapStates;

public class LStateMessage {

    public static final CallableMessage NEW_DAY = new LStateMessage("new_day").format();

    public static final CallableMessage NEW_NATION_TAX = new LStateMessage("new_tax").format();

    public static final CallableMessage NEW_CITY_TAX = new LStateMessage("new_city_tax").format();

    public static CallableMessage CITY_CREATE(String creatorName, String cityName) {
        return new LStateMessage("city_create").format(creatorName, cityName);
    }

    public static CallableMessage CITY_REMOVE(String name) {
        return new LStateMessage("city_remove").format(name);
    }

    public static CallableMessage CITY_NEW_NAME(String name) {
        return new LStateMessage("city_new_name").format(name);
    }

    public static CallableMessage CITY_NEW_ASSISTANT(String name) {
        return new LStateMessage("city_new_assistant").format(name);
    }

    public static CallableMessage CITY_REMOVED_ASSISTANT(String name) {
        return new LStateMessage("city_removed_assistant").format(name);
    }

    public static CallableMessage CITY_NEW_MAYOR(String name) {
        return new LStateMessage("city_new_mayor").format(name);
    }

    public static CallableMessage ALLOW_LEVEL_CHANGED(String allowLevel) {
        return new LStateMessage("allow_level_changed").format(allowLevel);
    }

    public static CallableMessage CITY_JOINED_NATION(String name) {
        return new LStateMessage("city_joined_nation").format(name);
    }

    public static CallableMessage CITY_NEW_BOARD(String board) {
        return new LStateMessage("city_new_board").format(board);
    }

    public static CallableMessage CITY_PLAYER_JOINED(String name) {
        return new LStateMessage("city_player_joined").format(name);
    }

    public static CallableMessage CITY_PLAYER_LEAVED(String name) {
        return new LStateMessage("city_player_leaved").format(name);
    }

    public static CallableMessage PLAYER_INVITED_TO_CITY(String name) {
        return new LStateMessage("player_invited_to_city").format(name);
    }

    public static CallableMessage CITY_INVITED_PLAYER(String name) {
        return new LStateMessage("city_invited_player").format(name);
    }

    public static CallableMessage PLAYER_REMOVED_INVITE_TO_CITY(String name) {
        return new LStateMessage("player_removed_invite_to_city").format(name);
    }

    public static CallableMessage CITY_REMOVED_INVITE_TO_PLAYER(String name) {
        return new LStateMessage("city_removed_invite_to_player").format(name);
    }

    public static CallableMessage CITY_REQUESTED_TO_NATION(String name) {
        return new LStateMessage("city_requested_to_nation").format(name);
    }

    public static CallableMessage NATION_RECIEVED_REQUEST(String name) {
        return new LStateMessage("nation_recieved_request").format(name);
    }

    public static CallableMessage CITY_REMOVED_REQUEST_TO_NATION(String name) {
        return new LStateMessage("city_removed_request_to_nation").format(name);
    }

    public static CallableMessage NATION_CITY_REMOVED_REQUEST(String name) {
        return new LStateMessage("nation_city_removed_request").format(name);
    }

    public static CallableMessage PLAYER_CITY_DECLINED_REQUEST(String name) {
        return new LStateMessage("player_city_declined_request").format(name);
    }

    public static CallableMessage CITY_DECLINED_REQUEST(String name) {
        return new LStateMessage("city_declined_request").format(name);
    }

    public static CallableMessage CITY_DECLINED_NATION_INVITE(String name) {
        return new LStateMessage("city_declined_nation_invite").format(name);
    }

    public static CallableMessage NATION_CITY_DECLINED_INVITE(String name) {
        return new LStateMessage("nation_city_declined_invite").format(name);
    }

    public static CallableMessage CITY_NEW_PERSONAL_LIMIT(String limited, String valueOf) {
        return new LStateMessage("city_new_personal_limit").format(limited, valueOf);
    }

    public static CallableMessage CITY_NEW_LIMIT(String toString, String valueOf) {
        return new LStateMessage("city_new_limit").format(toString, valueOf);
    }

    public static CallableMessage CITY_PRIVATE_CHUNK_CLAIMED(String name, String toString) {
        return new LStateMessage("city_private_chunk_claimed").format(name, toString);
    }

    public static CallableMessage CITY_PRIVATE_CHUNK_UNCLAIMED(String name, String toString) {
        return new LStateMessage("city_private_chunk_unclaimed").format(name, toString);
    }

    public static CallableMessage CITY_CLAIMED_NEW_OUTPOST_CHUNK(String toString) {
        return new LStateMessage("city_claimed_new_outpost_chunk").format(toString);
    }

    public static CallableMessage CITY_CLAIMED_NEW_HEXAGON(String valueOf) {
        return new LStateMessage("city_claimed_new_hexagon").format(valueOf);
    }

    public static CallableMessage CITY_UNCLAIMED_HEXAGON(String valueOf) {
        return new LStateMessage("city_unclaimed_hexagon").format(valueOf);
    }

    public static CallableMessage CITY_LEAVED_NATION(String name) {
        return new LStateMessage("city_leaved_nation").format(name);
    }

    public static CallableMessage NATION_CITY_LEAVED(String name) {
        return new LStateMessage("nation_city_leaved").format(name);
    }

    public static CallableMessage NATION_CREATE(String name, String name1) {
        return new LStateMessage("nation_create").format(name, name1);
    }

    public static CallableMessage NATION_NEW_NAME(String name) {
        return new LStateMessage("nation_new_name").format(name);
    }

    public static CallableMessage NATION_NEW_MINISTER(String name) {
        return new LStateMessage("nation_new_minister").format(name);
    }

    public static CallableMessage NATION_REMOVED_MINISTER(String name) {
        return new LStateMessage("nation_removed_minister").format(name);
    }

    public static CallableMessage NATION_NEW_CAPITAL(String name) {
        return new LStateMessage("nation_new_capital").format(name);
    }

    public static CallableMessage NATION_NEW_BOARD(String board) {
        return new LStateMessage("nation_new_board").format(board);
    }

    public static CallableMessage NATION_CITY_JOINED(String name) {
        return new LStateMessage("nation_city_joined").format(name);
    }

    public static CallableMessage NATION_REMOVE(String name) {
        return new LStateMessage("nation_remove").format(name);
    }

    public static CallableMessage CITY_ADDED_FLAG(String flag) {
        return new LStateMessage("city_added_flag").format(flag);
    }

    public static CallableMessage CITY_REMOVED_FLAG(String flag) {
        return new LStateMessage("city_removed_flag").format(flag);
    }

    public static CallableMessage CITY_NATION_REVOKED_INVITE(String name) {
        return new LStateMessage("city_nation_revoked_invite").format(name);
    }

    public static CallableMessage NATION_REVOKED_INVITE_TO_CITY(String name) {
        return new LStateMessage("nation_revoked_invite_to_city").format(name);
    }

    public static CallableMessage CITY_NATION_INVITED(String name) {
        return new LStateMessage("city_nation_invited").format(name);
    }

    public static CallableMessage NATION_INVITED_CITY(String name) {
        return new LStateMessage("nation_invited_city").format(name);
    }

    public static CallableMessage NATION_SETTED_COLOR(String color) {
        return new LStateMessage("nation_setted_color").format(color);
    }

    public static CallableMessage PLAYER_REQUESTED_TO_CITY(String name) {
        return new LStateMessage("player_requested_to_city").format(name);
    }

    public static CallableMessage CITY_PLAYER_REQUESTED(String name) {
        return new LStateMessage("city_player_requested").format(name);
    }

    public static CallableMessage PLAYER_REVOKE_REQUEST_TO_CITY(String name) {
        return new LStateMessage("player_revoke_request_to_city").format(name);
    }

    public static CallableMessage CITY_PLAYER_REVOKE_REQUEST(String name) {
        return new LStateMessage("city_player_revoke_request").format(name);
    }

    public static CallableMessage PLAYER_TELEPORTED_TO_CITY_SPAWN(String name) {
        return new LStateMessage("player_teleported_to_city_spawn").format(name);
    }

    public static CallableMessage PLAYER_NEW_FRIEND(String name) {
        return new LStateMessage("player_new_friend").format(name);
    }

    public static CallableMessage PLAYER_FRIEND_REMOVED(String name) {
        return new LStateMessage("player_friend_removed").format(name);
    }

    public static CallableMessage PLAYER_DECLINED_CITY_INVITE(String name) {
        return new LStateMessage("player_declined_city_invite").format(name);
    }

    public static CallableMessage CITY_PLAYER_DECLINED_INVITE(String name) {
        return new LStateMessage("city_player_declined_invite").format(name);
    }

    public static CallableMessage DEPOSITED(String valueOf, String type) {
        return new LStateMessage("player_deposited").format(valueOf, type);
    }

    public static CallableMessage WITHDRAWED(String valueOf, String type) {
        return new LStateMessage("player_withdrawed").format(valueOf, type);
    }

    public static CallableMessage CITY_PLAYER_DEPOSITED(String name, int amount, String type) {
        return new LStateMessage("city_player_deposited").format(name, String.valueOf(amount), type);
    }

    public static CallableMessage CITY_PLAYER_WITHDRAWED(String name, int amount, String type) {
        return new LStateMessage("city_player_withdrawed").format(name, String.valueOf(amount), type);
    }

    public static CallableMessage NATION_PLAYER_DEPOSITED(String name, int amount, String type) {
        return new LStateMessage("nation_player_deposited").format(name, String.valueOf(amount), type);
    }

    public static CallableMessage NATION_PLAYER_WITHDRAWED(String name, int amount, String type) {
        return new LStateMessage("nation_player_withdrawed").format(name, String.valueOf(amount), type);
    }

    public static CallableMessage CITY_PLAYER_KICKED(String name) {
        return new LStateMessage("city_player_kicked").format(name);
    }

    public static CallableMessage NATION_ADDED_FLAG(String flag) {
        return new LStateMessage("nation_added_flag").format(flag);
    }

    public static CallableMessage NATION_REMOVED_FLAG(String flag) {
        return new LStateMessage("nation_removed_flag").format(flag);
    }

    public static CallableMessage NATION_CITY_KICKED(String name) {
        return new LStateMessage("nation_city_kicked").format(name);
    }

    public static CallableMessage CITY_NATION_DECLINED_REQUEST(String name) {
        return new LStateMessage("city_nation_declined_request").format(name);
    }

    public static CallableMessage NATION_DECLINED_CITY_REQUEST(String name) {
        return new LStateMessage("nation_declined_city_request").format(name);
    }

    public static CallableMessage PLAYER_RECIEVED_MONEY(String name, double amount, String type) {
        return new LStateMessage("player_recieved_money").format(name, String.valueOf(amount), type);
    }

    public static CallableMessage PLAYER_SENDED_MONEY(String name, double amount, String type) {
        return new LStateMessage("player_sended_money").format(name, String.valueOf(amount), type);
    }

    public static CallableMessage PLAYER_CANT_PAY_TAXES_AND_KICKED(String name) {
        return new LStateMessage("player_cant_pay_taxes_and_kicked").format(name);
    }
    
    public static CallableMessage CITY_DESTROYED_BY_TAXES(String name) {
        return new LStateMessage("city_destroyed_by_taxes").format(name);
    }
    
    public static CallableMessage CITY_DESTROYED_BY_CORRUPTION(String name) {
        return new LStateMessage("city_destroyed_by_corruption").format(name);
    }
    
    public static CallableMessage CITY_CANT_PAY_TAXES_AND_KICKED(String name) {
        return new LStateMessage("city_cant_pay_taxes_and_kicked").format(name);
    }
    
    public static CallableMessage NATION_DESTROYED_BY_TAXES(String name) {
        return new LStateMessage("nation_destroyed_by_taxes").format(name);
    }
    
    public static CallableMessage NATION_DESTROYED_BY_CORRUPTION(String name) {
        return new LStateMessage("nation_destroyed_by_corruption").format(name);
    }
    
    private final String id;
    
    public LStateMessage(String id) {
        this.id = id;
    }
    
    public CallableMessage format(String... strings) {
        return new LAPIMessage(
            AncapStates.class, "state.form",
            new Placeholder("message", new LAPIMessage(AncapStates.class, "state.messages."+this.id)),
            new StringFormat((Object[]) strings)
        );
    }

    private record StringFormat(Object... formats) implements Modifier {

        @Override
        public String apply(String base, String identifier) {
            return String.format(base, this.formats);
        }

    }
    
}
