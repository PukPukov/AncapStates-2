package ru.ancap.states.name;

import java.util.ArrayList;
import java.util.Collections;

public class Name {

    public static boolean canBeDefinedWith(String string) {
        return !string.isEmpty() && string.matches("[a-zA-Z0-9а-яА-Я-_]+") && (!string.startsWith("nation_") || !string.startsWith("city_"));
    }

    public static String getName(String[] args) {
        return Name.getName(args, 1);
    }

    public static String getName(String[] args, int t) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        for (int i = 0; i<t; i++) list.remove(0);
        String name = list.get(0);
        list.remove(0);
        for (String str : list) name = name+"_"+str;
        return name;
    }
}