package ru.ancap.states.data.ram;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.ancap.commons.map.GuaranteedMap;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.states.states.Nation.Nation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Accessors(fluent = true) @Getter
public class RAMData {
    
    public boolean test;
    public Nation TEST_NATION = null;
    
    public Map<String, String> debugMayors;
    
    public final Map<Hexagon, Set<UUID>> playerHexagons = new GuaranteedMap<>(HashSet::new);
    
}