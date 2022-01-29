package states.Name;

import java.util.ArrayList;
import java.util.Collections;

public class Name {

    public static boolean canBeDefinedWith(String str) {
        return str.length() >= 2 && str.matches("[a-zA-Z0-9а-яА-Я-_]+") && (!str.startsWith("nation_") || !str.startsWith("city_"));
    }

    public static String getName(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        list.remove(0);
        String name = list.get(0);
        list.remove(0);
        if (list.size() > 0){
            for (String str : list) {
                name = name+"_"+str;
            }
        }
        return name;
    }

    public static String getName(String[] args, int t) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        for (int i = 0; i<t; i++) {
            list.remove(0);
        }
        String name = list.get(0);
        list.remove(0);
        if (list.size() > 0){
            for (String str : list) {
                name = name+"_"+str;
            }
        }
        return name;
    }
}
