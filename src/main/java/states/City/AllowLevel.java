package states.City;

public class AllowLevel {

    private int allowLevel;

    public static boolean canBeDefinedWith(String allow) {
        if (allow.equals("mayor")) {return true;}
        if (allow.equals("assistants")) {return true;}
        if (allow.equals("residents")) {return true;}
        if (allow.equals("nation-leader")) {return true;}
        if (allow.equals("nation-ministers")) {return true;}
        if (allow.equals("nation")) {return true;}
        if (allow.equals("licentiate")) {return true;}
        if (allow.equals("everyone")) {return true;}
        return false;
    }

    public AllowLevel(String allowLevelName) {
        if (allowLevelName.equals("mayor")) {this.allowLevel=1;}
        if (allowLevelName.equals("assistants")) {this.allowLevel=2;}
        if (allowLevelName.equals("residents")) {this.allowLevel=3;}
        if (allowLevelName.equals("nation-leader")) {this.allowLevel=4;}
        if (allowLevelName.equals("nation-ministers")) {this.allowLevel=5;}
        if (allowLevelName.equals("nation")) {this.allowLevel=6;}
        if (allowLevelName.equals("licentiate")) {this.allowLevel=7;}
        if (allowLevelName.equals("everyone")) {this.allowLevel=8;}
    }

    public AllowLevel(int i) {
        this.allowLevel = i;
    }

    public int getInt() {
        return this.allowLevel;
    }
}
