package ru.ancap.states.message;

import ru.ancap.commons.Path;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.WrapperMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.states.AncapStates;

public class ErrorMessage extends WrapperMessage {
    
    public static final CallableMessage NO_PERMS = new ErrorMessage("no_perms");
    public static final CallableMessage NOT_FREE = new ErrorMessage("not_free");
    public static final CallableMessage FREE = new ErrorMessage("free");
    public static final CallableMessage ILLEGAL_ARGS_NUMBER = new ErrorMessage("illegal_args_number");
    public static final CallableMessage CITY_DOESNT_EXIST = new ErrorMessage("city_doesnt_exist");
    public static final CallableMessage NO_CITY_CREATION_FEE = new ErrorMessage("no_city_creation_fee");
    public static final CallableMessage CAPITAL_CANT_BE_DELETED = new ErrorMessage("capital_cant_be_deleted");
    public static final CallableMessage PLEASE_CONFIRM = new ErrorMessage("please_confirm");
    public static final CallableMessage NO_SUCH_CITY = new ErrorMessage("no_such_city");
    public static final CallableMessage CITY_ISNT_FREE_TO_JOIN = new ErrorMessage("city_isnt_free_to_join");
    public static final CallableMessage MAYOR_CANT_LEAVE = new ErrorMessage("mayor_cant_leave");
    public static final CallableMessage CITY_IS_FREE_TO_JOIN = new ErrorMessage("city_is_free_to_join");
    public static final CallableMessage YOU_NOT_ASKING_TO_JOIN_IN_CITY = new ErrorMessage("you_not_asking_to_join_in_city");
    public static final CallableMessage NOT_ASKING_TO_JOIN_IN_CITY = new ErrorMessage("not_asking_to_join_in_city");
    public static final CallableMessage NOT_RESIDENT = new ErrorMessage("not_resident");
    public static final CallableMessage CANT_TELEPORT_TO_CITY_SPAWN = new ErrorMessage("cant_teleport_to_city_spawn");
    public static final CallableMessage NO_SUCH_ALLOW_LEVEL = new ErrorMessage("no_such_allow_level");
    public static final CallableMessage ILLEGAL_ARGS = new ErrorMessage("illegal_args");
    public static final CallableMessage PRIVATE_CHUNK_IS_ALREADY_CLAIMED = new ErrorMessage("private_chunk_is_already_claimed");
    public static final CallableMessage ITS_NOT_YOUR_CITY = new ErrorMessage("its_not_your_city");
    public static final CallableMessage CANT_CLAIM_NEW_PRIVATE_CHUNK = new ErrorMessage("cant_claim_new_private_chunk");
    public static final CallableMessage PRIVATE_CHUNK_ISNT_CLAIMED = new ErrorMessage("private_chunk_isnt_claimed");
    public static final CallableMessage ITS_NOT_YOUR_CHUNK = new ErrorMessage("its_not_your_chunk");
    public static final CallableMessage OUTPOST_CHUNK_IS_ALREADY_CLAIMED = new ErrorMessage("outpost_chunk_is_already_claimed");
    public static final CallableMessage CANT_CLAIM_NEW_OUTPOST_CHUNK = new ErrorMessage("cant_claim_new_outpost_chunk");
    public static final CallableMessage OUTPOST_CHUNK_ISNT_CLAIMED = new ErrorMessage("outpost_chunk_isnt_claimed");
    public static final CallableMessage NOT_ENOUGH_MONEY = new ErrorMessage("not_enough_money");
    public static final CallableMessage HEXAGON_IS_ALREADY_CLAIMED = new ErrorMessage("hexagon_is_already_claimed");
    public static final CallableMessage HEXAGON_ISNT_CONNECTING_TO_CITY = new ErrorMessage("hexagon_isnt_connecting_to_city");
    public static final CallableMessage HOME_HEXAGON_CANT_BE_UNCLAIMED = new ErrorMessage("home_hexagon_cant_be_unclaimed");
    public static final CallableMessage NOT_INVITING_IN_NATION = new ErrorMessage("not_inviting_in_nation");
    public static final CallableMessage CITY_IS_FREE = new ErrorMessage("city_is_free");
    public static final CallableMessage CITY_IS_NOT_FREE = new ErrorMessage("city_is_not_free");
    public static final CallableMessage NO_NATION_CREATION_FEE = new ErrorMessage("no_nation_creation_fee");
    public static final CallableMessage ILLEGAL_NAME = new ErrorMessage("illegal_name");
    public static final CallableMessage CITY_EXISTS = new ErrorMessage("city_exists");
    public static final CallableMessage NATION_EXISTS = new ErrorMessage("nation_exists");
    public static final CallableMessage NATION_DOESNT_EXISTS = new ErrorMessage("nation_doesnt_exists");
    public static final CallableMessage NO_SUCH_NATION = new ErrorMessage("no_such_nation");
    public static final CallableMessage HE_ISNT_FREE = new ErrorMessage("he_isnt_free");
    public static final CallableMessage HE_ISNT_INVITED = new ErrorMessage("he_isnt_invited");
    public static final CallableMessage CITY_DOESNT_INVITED = new ErrorMessage("city_doesnt_invited");
    public static final CallableMessage CITY_DOESNT_REQUESTING = new ErrorMessage("city_doesnt_requesting");
    public static final CallableMessage HE_ISNT_CITIZEN_OF_YOUR_NATION = new ErrorMessage("he_isnt_citizen_of_your_nation");
    public static final CallableMessage HE_IS_ALREADY_MINISTER = new ErrorMessage("he_is_already_minister");
    public static final CallableMessage HE_ISNT_RESIDENT_OF_YOUR_CITY = new ErrorMessage("he_isnt_resident_of_your_city");
    public static final CallableMessage HE_IS_ALREADY_ASSISTANT = new ErrorMessage("he_is_already_assistant");
    public static final CallableMessage HE_ISNT_ASSISTANT = new ErrorMessage("he_isnt_assistant");
    public static final CallableMessage HE_ISNT_MINISTER = new ErrorMessage("he_isnt_minister");
    public static final CallableMessage YOU_NOT_REQUESTING_TO_INTEGRATE_IN_THIS_NATION = new ErrorMessage("you_not_requesting_to_integrate_in_this_nation");
    public static final CallableMessage ITS_NOT_YOUR_CITY_CHUNK = new ErrorMessage("its_not_your_city_chunk");
    public static final CallableMessage ITS_NOT_YOUR_CITY_HEXAGON = new ErrorMessage("its_not_your_city_hexagon");
    public static final CallableMessage NATION_ISNT_FREE_TO_JOIN = new ErrorMessage("nation_isnt_free_to_join");
    public static final CallableMessage REGION_PROTECTION_FORBIDDEN_ACTION = new ErrorMessage("region_protection_forbidden_action");
    public static final CallableMessage HERE_IS_OTHER_CITY = new ErrorMessage("here_is_other_city");
    public static final CallableMessage YOU_CANT_DEPOSIT_THIS_TYPE = new ErrorMessage("you_cant_deposit_this_type");
    public static final CallableMessage YOU_ARE_ALREADY_REQUESTING_TO_THAT_CITY = new ErrorMessage("you_are_already_requesting_to_that_city");
    public static final CallableMessage HE_IS_ALREADY_INVITED_TO_YOUR_CITY = new ErrorMessage("he_is_already_invited_to_your_city");
    public static final CallableMessage YOUR_CITY_IS_ALREADY_REQUESTING_TO_THAT_NATION = new ErrorMessage("your_city_is_already_requesting_to_that_nation");
    public static final CallableMessage THAT_CITY_IS_ALREADY_INVITED_TO_YOUR_NATION = new ErrorMessage("that_city_is_already_invited_to_your_nation");
    public static final CallableMessage NOT_INVITING_IN_CITY = new ErrorMessage("not_inviting_in_city");
    public static final CallableMessage CANT_SEND_TO_OFFLINE_PLAYER = new ErrorMessage("cant_send_to_offline_player");
    public static final CallableMessage CANT_SEND_SO_FAR = new ErrorMessage("cant_send_so_far");

    private ErrorMessage(String path) {
        super(new LAPIMessage(
            AncapStates.class, "error.form",
            new Placeholder("message", new LAPIMessage(AncapStates.class, Path.dot("error", path)))
        ));
    }
    
}