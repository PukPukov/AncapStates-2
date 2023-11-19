package ru.ancap.states.addons;

import java.util.ArrayList;

public class AddonCore {

    private static ArrayList<AncapAddon> addons;

    private static ArrayList<ThirdPartyAddon> thirdPartyAddons;

    private AddonCore() {
    }

    private static class SingletonHolder {
        public static final AddonCore HOLDER_INSTANCE = new AddonCore();
    }

    public static AddonCore getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public ArrayList<AncapAddon> getAddons() {
        return addons;
    }

    public ArrayList<ThirdPartyAddon> getThirdPartyAddons() {
        return thirdPartyAddons;
    }

    public boolean isAddonInstalled(String name) {
        AncapAddon addon = new AncapAddon(name);
        return addons.contains(addon);
    }

    public boolean isThirdPartyAddonInstalled(String name) {
        ThirdPartyAddon addon = new ThirdPartyAddon(name);
        return thirdPartyAddons.contains(addon);
    }

    public void registerAddon(String name) {
        addons.add(new AncapAddon(name));
    }

    public void registerThirdPartyAddon(String name) {
        thirdPartyAddons.add(new ThirdPartyAddon(name));
    }
}