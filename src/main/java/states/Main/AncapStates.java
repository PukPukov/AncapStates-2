package states.Main;

import AncapLibrary.Message.Message;
import library.HexagonalGrid;
import library.Morton64;
import library.Orientation;
import library.Point;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import states.Commands.*;
import states.Config.AncapStatesConfiguration;
import states.Database.Database;
import states.Dynmap.DynmapDrawer;
import states.Hexagons.AncapHexagonalGrid;
import states.Listeners.AncapStatesEventsListeners.AncapStatesEventsListener;
import states.Listeners.TimerListeners.TimerListener;
import states.Player.AncapStatesPlayer;
import states.Player.PlayerCommand;
import states.States.City.City;
import states.States.CityMap;
import states.States.Nation.Nation;
import states.Top.AncapTop;
import states.Wars.Listeners.TimerListeners.HeartbeatListener;

import java.util.HashMap;
import java.util.logging.Logger;

public class AncapStates extends JavaPlugin {

    private static AncapStates INSTANCE;

    private static final int pluginId = 13812;

    private static boolean isWarsInstalled = false;

    private static String[] ancapAddons;

    private Logger log = Bukkit.getLogger();

    private static boolean test;

    private static Database statesDB;

    private static DynmapDrawer drawer = new DynmapDrawer();

    private static DynmapAPI dynmap = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("Dynmap");

    private static CityMap cityMap;

    public static final AncapHexagonalGrid grid = new AncapHexagonalGrid(Orientation.FLAT, new Point(0D,0D), new Point(100D, 100D), new Morton64(2L, 32L));

    public static HashMap<String, City> playerLocations = new HashMap<>();

    public static AncapTop getTop() {
        return new AncapTop();
    }

    public static CityMap getCityMap() {
        return cityMap;
    }

    public static AncapStatesConfiguration getConfiguration() {
        return AncapStatesConfiguration.getInstance();
    }

    public static AncapStatesStatesPlayerMap getPlayerMap() {
        return AncapStatesStatesPlayerMap.getInstance();
    }

    public HexagonalGrid getGrid() {
        return grid;
    }

    public static AncapStates getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        this.setInstance();
        this.setUpDBs();
        this.registerCommands();
        this.registerEventsListeners();
        this.registerMetrics();
        this.registerCityMap();
        log.info("Done!");
    }

    @Override
    public void onDisable() {
    }

    private void registerCityMap() {
        cityMap = new CityMap();
    }

    private void registerMetrics() {
        Metrics metrics = new Metrics(this, pluginId);
    }

    private void setUpDBs() {
        Database.setUp();
        statesDB = Database.STATES_DATABASE;
        saveDefaultConfig();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void registerEventsListeners() {
        getServer().getPluginManager().registerEvents(new AncapStatesEventsListener(), this);
        getServer().getPluginManager().registerEvents(new HeartbeatListener(), this);
        getServer().getPluginManager().registerEvents(new TimerListener(), this);
    }

    private void registerCommands() {
        PluginCommand city = Bukkit.getServer().getPluginCommand("city");
        city.setExecutor(new CityCommand());
        city.setTabCompleter(new CityCommand());
        PluginCommand nation = Bukkit.getServer().getPluginCommand("nation");
        nation.setExecutor(new NationCommand());
        nation.setTabCompleter(new NationCommand());
        PluginCommand ancapstates = Bukkit.getServer().getPluginCommand("ancapstates");
        ancapstates.setExecutor(new AncapStatesCommand());
        ancapstates.setTabCompleter(new AncapStatesCommand());
        getServer().getPluginCommand("test").setExecutor(new TestCommand());
        getServer().getPluginCommand("player").setExecutor(new PlayerCommand());
        getServer().getPluginCommand("here").setExecutor(new HereCommand());
        getServer().getPluginCommand("migration").setExecutor(new MigrationCommand());
    }

    public boolean isTest() {
        return test;
    }

    public void enableTest() {
        test = true;
    }

    public void disableTest() {
        test = false;
    }

    public static DynmapAPI getDynmap() {
        return dynmap;
    }

    public static void sendMessage(Message message) {
        AncapStatesPlayer[] ancapStatesPlayers = AncapStates.getPlayerMap().getOnlinePlayers();
        for (int i = 0; i< ancapStatesPlayers.length; i++) {
            ancapStatesPlayers[i].sendMessage(message);
        }
    }

    public static void startNewDay() {
        NewDayThread thread = new NewDayThread();
        thread.start();
    }

    public static void grabTaxes() {
        AncapStates.grabCitiesTaxes();
        AncapStates.grabNationTaxes();
    }

    private static void grabNationTaxes() {
        Nation[] nations = AncapStates.getCityMap().getNations();
        for (Nation nation : nations) {
            nation.grabTaxes();
        }
    }

    private static void grabCitiesTaxes() {
        City[] cities = AncapStates.getCityMap().getCities();
        for (City city : cities) {
            city.grabTaxes();
        }
    }

    public static void collectTaxes() {
        AncapStates.collectCityTaxes();
        AncapStates.collectNationTaxes();
    }

    private static void collectNationTaxes() {
        Nation[] nations = getCityMap().getNations();
        for (Nation nation : nations) {
            nation.collectTaxes();
        }
    }

    private static void collectCityTaxes() {
        City[] cities = getCityMap().getCities();
        for (City city : cities) {
                city.collectTaxes();
            }
        }

    public static AncapStatesInfo getInfo() {
        return new AncapStatesInfo();
    }
}
