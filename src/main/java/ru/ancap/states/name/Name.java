package ru.ancap.states.name;

import ru.ancap.commons.MultiAlphabetChecker;

import java.util.ArrayList;
import java.util.Collections;

public class Name {

    public static boolean canBeDefinedWith(String string) {
        return !string.isEmpty() &&
            string.matches("[a-zA-Zа-яА-Я0-9_\\u0590-\\u05FF\\uFB1D-\\uFB4F\\u0600-\\u06FF\\u0750-\\u077F\\u10D0-\\u10FFёЁҐґЄєІіЇї]*") &&
            !MultiAlphabetChecker.isMultiAlphabetic(string) &&
            (!string.startsWith("nation_") || !string.startsWith("city_"));
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