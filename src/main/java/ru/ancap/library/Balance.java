package ru.ancap.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import ru.ancap.commons.map.SafeMap;
import ru.ancap.framework.database.nosql.SerializeWorker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ToString @EqualsAndHashCode
public class Balance {

    public static final SerializeWorker<Balance> SERIALIZE_WORKER = new SerializeWorker.AbstractSerializeWorker<>(Balance.class) {
        
        private final ObjectMapper objectMapper = new ObjectMapper();
        private final TypeReference<HashMap<String, Double>> typeRef = new TypeReference<>() {};
        
        @Override
        @SneakyThrows
        public String serialize(Balance balance) {
            return this.objectMapper.writeValueAsString(balance.data);
        }
        
        @Override
        @SneakyThrows
        public Balance deserialize(String json) {
            return new Balance(this.objectMapper.readValue(json, this.typeRef));
        }
        
    };
    
    public static final String IRON = "iron";
    public static final String DIAMOND = "diamond";
    public static final String NETHERITE = "netherite";

    private final Map<String, Double> data;
    
    public Balance(BalanceHolder holder) {
        this(holder.database().read("balance", SERIALIZE_WORKER).data());
    }
    
    public Balance(Map<String, Double> data) {
        this.data = SafeMap.builder(new ConcurrentHashMap<>(data))
            .guaranteed(() -> 0D)
            .build();
    }
    
    public Balance(double iron, double diamond, double netherite) {
        this(Map.of(
            Balance.IRON, iron,
            Balance.DIAMOND, diamond,
            Balance.NETHERITE, netherite
        ));
    }
    
    public Map<String, Double> data() {
        return this.data;
    }

    public double getIron() {
        return this.get("iron");
    }
    
    public double getDiamond() {
        return this.get("diamond");
    }
    
    public double getNetherite() {
        return this.get("netherite");
    }

    public double get(String key) {
        return this.data.get(key);
    }

    public void remove(String type, double amount) {
        if (!this.have(type, amount)) {
            throw new IllegalStateException(
                "Tried to remove "+InMarks.of(amount)+" "+InMarks.of(type)+", " +
                "but have only this balance: "+InMarks.of(SERIALIZE_WORKER.serialize(this))
            );
        }
        this.putBalance(type, this.data.get(type) - amount);
    }

    public void add(String type, double amount) {
        if (amount <= 0) throw new IllegalStateException("Tried to add "+InMarks.of(amount)+" "+InMarks.of(type)+" to balance");
        this.putBalance(type, this.data.get(type) + amount);
    }
    
    private void putBalance(String type, double amount) {
        if (amount == 0) this.data.remove(type);
        else this.data.put(type, amount);
    }

    public boolean have(String type, double amount) {
        return this.data.get(type) >= amount;
    }
    
    public boolean have(Balance amount) {
        return amount.data.entrySet().stream()
            .allMatch(entry -> this.have(entry.getKey(), entry.getValue()));
    }

    public void remove(Balance amount) {
        amount.data.forEach(this::remove);
    }
    
    public void add(Balance amount) {
        amount.data.forEach(this::add);
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }
    
}
