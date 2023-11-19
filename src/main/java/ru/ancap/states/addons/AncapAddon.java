package ru.ancap.states.addons;

import java.util.List;

public class AncapAddon {

    private static final List<String> OfficialAddonsList = List.of(new String[]{"AncapWars"});

    public String name;

    public AncapAddon(String name) {
        if (!OfficialAddonsList.contains(name)) {
            throw new IllegalAddonException();
        }
    }
}
