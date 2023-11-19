package ru.ancap.states.message;

import ru.ancap.commons.map.SafeMap;
import ru.ancap.framework.communicate.modifier.Modifier;
import ru.ancap.framework.communicate.modifier.ModifierBundle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayeredModifies {
    
    private final Map<String, Modifier> data;
    
    public LayeredModifies(Map<String, Modifier> data) {
        this.data = SafeMap.builder(new HashMap<>(data))
            .guaranteed(() -> new ModifierBundle(List.of()))
            .build();
    }
    
    public Modifier forLayer(String layerName) {
        return this.data.get(layerName);
    }
    
}
