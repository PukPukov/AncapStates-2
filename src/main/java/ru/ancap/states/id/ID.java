package ru.ancap.states.id;

import com.fasterxml.uuid.Generators;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.states.AncapStates;
import ru.ancap.states.main.AncapStatesDatabaseType;

public class ID {

    public static String getNationID(String name) {
        PathDatabase idDB = AncapStates.ancapStatesDatabase(AncapStatesDatabaseType.IDLINK_DATABASE);
        if (!idDB.isSet("ids.nation_"+name)) {
            String stringID = Generators.timeBasedGenerator().generate().toString();
            idDB.write("ids.nation_"+name, stringID);
        }
        return idDB.readString("ids.nation_"+name);
    }

    public static String getCityID(String name) {
        PathDatabase idDB = AncapStates.ancapStatesDatabase(AncapStatesDatabaseType.IDLINK_DATABASE);
        if (!idDB.isSet("ids.city_"+name)) {
            String stringID = Generators.timeBasedGenerator().generate().toString();
            idDB.write("ids.city_"+name, stringID);
        }
        return idDB.readString("ids.city_"+name);
    }
}