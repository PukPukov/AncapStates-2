package ru.ancap.states.fees;

import ru.ancap.library.Balance;

import java.util.Map;

public class ASFees {
    
    public static final Balance CITY_CREATION = new Balance(Map.of("iron", 32.0));
    
    public static final Balance NATION_CREATION = new Balance(Map.of("netherite", 1.0));
    
    public static final Balance HEXAGON_CLAIM = new Balance(Map.of("diamond", 1.0));
    
    public static final Balance CITY_TELEPORT = new Balance(Map.of("diamond", 0.5));
    
}
