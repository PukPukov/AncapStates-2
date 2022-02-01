package states.Economy;

import states.States.City.City;
import states.Database.Database;
import states.Message.Message;
import states.States.Nation.Nation;
import states.Player.AncapPlayer;

public class Balance {

    private double iron;
    private double diamond;
    private double netherite;

    private static Database statesDB = Database.STATES_DATABASE;

    private void generate(String type, String id) {
        if (!statesDB.isSet("states."+type+"."+id+".balance.iron")) {
            statesDB.write("states."+type+"."+id+".balance.iron", "0");
            statesDB.write("states."+type+"."+id+".balance.diamond", "0");
            statesDB.write("states."+type+"."+id+".balance.netherite", "0");
        }
    }

    public Balance(City city) {
        this.generate("city", city.getID());
        double iron = Double.parseDouble(statesDB.getString("states.city."+city.getID()+".balance.iron"));
        double diamond = Double.parseDouble(statesDB.getString("states.city."+city.getID()+".balance.diamond"));
        double netherite = Double.parseDouble(statesDB.getString("states.city."+city.getID()+".balance.netherite"));
        this.iron = iron;
        this.diamond = diamond;
        this.netherite = netherite;
    }

    public Balance(Nation nation) {
        this.generate("nation", nation.getIDString());
        double iron = Double.parseDouble(statesDB.getString("states.nation."+nation.getIDString()+".balance.iron"));
        double diamond = Double.parseDouble(statesDB.getString("states.nation."+nation.getIDString()+".balance.diamond"));
        double netherite = Double.parseDouble(statesDB.getString("states.nation."+nation.getIDString()+".balance.netherite"));
        this.iron = iron;
        this.diamond = diamond;
        this.netherite = netherite;
    }

    public Balance(double iron, double diamond, double netherite) {
        this.iron = iron;
        this.diamond = diamond;
        this.netherite = netherite;
    }

    public Balance(AncapPlayer ancapPlayer) {
        this.generate("player", ancapPlayer.getID());
        double iron = Double.parseDouble(statesDB.getString("states.player."+ancapPlayer.getID()+".balance.iron"));
        double diamond = Double.parseDouble(statesDB.getString("states.player."+ancapPlayer.getID()+".balance.diamond"));
        double netherite = Double.parseDouble(statesDB.getString("states.player."+ancapPlayer.getID()+".balance.netherite"));
        this.iron = iron;
        this.diamond = diamond;
        this.netherite = netherite;
    }

    public double getIron() {
        return this.iron;
    }

    public double getDiamond() {
        return this.diamond;
    }

    public double getNetherite() {
        return this.netherite;
    }

    public void merge(Balance balance) {
        this.iron = balance.getIron();
        this.diamond = balance.getDiamond();
        this.netherite = balance.getNetherite();
    }

    public void removeIron(double amount) {
        amount = Math.abs(amount);
        this.iron = this.iron - amount;
    }

    public void addIron(double amount) {
        amount = Math.abs(amount);
        this.iron = this.iron + amount;
    }

    public void removeDiamond(double amount) {
        amount = Math.abs(amount);
        this.diamond = this.diamond - amount;
    }

    public void addDiamond(double amount) {
        amount = Math.abs(amount);
        this.diamond = this.diamond + amount;
    }

    public void removeNetherite(double amount) {
        amount = Math.abs(amount);
        this.netherite = this.netherite - amount;
    }

    public void addNetherite(double amount) {
        amount = Math.abs(amount);
        this.netherite = this.netherite + amount;
    }

    public Message getMessage() {
        String[] strings = new String[4];
        strings[0] = "&6Казна:";
        strings[1] = "     &8Незерита&8: &f"+this.getNetherite();
        strings[2] = "     &bАлмазов&8: &f"+this.getDiamond();
        strings[3] = "     &7Железа&8: &f"+this.getIron();
        return new Message(strings);
    }

    public Message getTaxMessage() {
        String[] strings = new String[4];
        strings[0] = "&6Налоги:";
        strings[1] = "     &8Незерита&8: &f"+this.getNetherite();
        strings[2] = "     &bАлмазов&8: &f"+this.getDiamond();
        strings[3] = "     &7Железа&8: &f"+this.getIron();
        return new Message(strings);
    }

    public double getAmountForType(String type) {
        if (type.equals("diamond")) {
            return this.diamond;
        }
        if (type.equals("netherite")) {
            return this.netherite;
        }
        if (type.equals("iron")) {
            return this.iron;
        }
        return 0;
    }

    public boolean haveIron(double amount) {
        return this.iron>=amount;
    }

    public boolean haveDiamond(double amount) {
        return this.diamond>=amount;
    }

    public boolean haveNetherite(double amount) {
        return this.netherite>=amount;
    }

    public boolean haveForType(String type, double amount) {
        if (type.equals("iron")) {
            return this.haveIron(amount);
        }
        if (type.equals("diamond")) {
            return this.haveDiamond(amount);
        }
        if (type.equals("netherite")) {
            return this.haveNetherite(amount);
        }
        return false;
    }

    public boolean have(Balance balance) {
        return this.haveIron(balance.getIron()) && this.haveDiamond(balance.getDiamond()) && this.haveNetherite(balance.getNetherite());
    }

    public void remove(Balance balance) {
        this.removeIron(balance.getIron());
        this.removeDiamond(balance.getDiamond());
        this.removeNetherite(balance.getNetherite());
    }

    public void add(Balance balance) {
        this.addIron(balance.getIron());
        this.addDiamond(balance.getDiamond());
        this.addNetherite(balance.getNetherite());
    }
}
