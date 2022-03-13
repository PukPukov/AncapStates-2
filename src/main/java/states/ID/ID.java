package states.ID;

import Database.Database;
import com.fasterxml.uuid.Generators;
import states.Main.AncapStates;
import states.Main.AncapStatesDatabaseType;

public class ID {

    public static String getNationID(String name) {
        Database idDB = AncapStates.getAncapStatesDatabase(AncapStatesDatabaseType.IDLINK_DATABASE);
        if (!idDB.isSet("ids.nation_"+name)) {
            String stringID = Generators.timeBasedGenerator().generate().toString();
            idDB.write("ids.nation_"+name, stringID);
        }
        return idDB.getString("ids.nation_"+name);
    }

    public static String getCityID(String name) {
        Database idDB = AncapStates.getAncapStatesDatabase(AncapStatesDatabaseType.IDLINK_DATABASE);
        if (!idDB.isSet("ids.city_"+name)) {
            String stringID = Generators.timeBasedGenerator().generate().toString();
            idDB.write("ids.city_"+name, stringID);
        }
        return idDB.getString("ids.city_"+name);
    }
}
