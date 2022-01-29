package states.City;

public class LimitType {

    private String limitType;

    public LimitType(String limitTypeName) {
        this.limitType = limitTypeName;
    }

    @Override
    public String toString() {
        return this.limitType;
    }
}
