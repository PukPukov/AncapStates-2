package ru.ancap.states.dynmap;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public record DynmapDescription(String name, String[] description) {
    
    private static final Safelist safelist = Safelist.none();

    public static String removeHtmlTags(String input) {
        return Jsoup.clean(input, safelist);
    }
    
}
