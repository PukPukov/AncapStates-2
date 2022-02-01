package states.Top;

import states.States.City.City;
import states.States.Nation.Nation;

import java.util.*;

public class AncapTop {

    public Nation[] getBiggestNations(int amount) {
        return null;
    }

    public Nation[] getMostPopulatedNations(int amount) {
        return null;
    }

    public City[] getBiggestCities(int amount) {
        return null;
    }

    public City[] getMostPopulatedCities(int amount) {
        return null;
    }

    public void shrinkTo(List list, int newSize) {
        int size = list.size();
        if (newSize >= size) return;
        for (int i = newSize; i < size; i++) {
            list.remove(list.size() - 1);
        }
    }
}
