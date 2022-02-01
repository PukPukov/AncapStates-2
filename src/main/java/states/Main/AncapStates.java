package states.Main;

import library.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import states.Addons.AddonCore;
import states.Chunk.PrivateChunk;
import states.Commands.AncapStatesCommand;
import states.Listeners.AncapStatesEventsListeners.AncapStatesEventsListener;
import states.States.City.City;
import states.Commands.CityCommand;
import states.States.City.CityMap;
import states.Database.Database;
import states.Dynmap.DynmapDrawer;
import states.Commands.HereCommand;
import states.Hexagons.AncapHexagonalGrid;
import states.Listeners.PrimalListeners.*;
import states.Message.ErrorMessage;
import states.Message.Message;
import states.Metrics.Metrics;
import states.Commands.MigrationCommand;
import states.States.Nation.Nation;
import states.Commands.NationCommand;
import states.Player.AncapPlayer;
import states.Player.PlayerCommand;
import states.Commands.TestCommand;
import states.Timer.Heartbeat.AncapHeartbeat;
import states.Top.AncapTop;
import states.Wars.Listeners.TimerListeners.HeartbeatListener;

import java.util.HashMap;
import java.util.logging.Logger;

public class AncapStates extends JavaPlugin {


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

    private static class SingletonHolder {
        public static AncapStates HOLDER_INSTANCE;
    }

    public static AncapStates getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }



    public static City[] getCities() {
        String[] keys = statesDB.getStringList("states.city");
        City[] cities = new City[keys.length];
        for (int i = 0; i< cities.length; i++) {
            cities[i] = new City(keys[i]);
        }
        return cities;
    }

    public static Nation[] getNations() {
        String[] keys = statesDB.getStringList("states.nation");
        Nation[] nations = new Nation[keys.length];
        for (int i = 0; i< nations.length; i++) {
            nations[i] = new Nation(keys[i]);
        }
        return nations;
    }

    @Override
    public void onEnable() {
        this.setInstance();
        this.setUpDBs();
        this.registerCommands();
        this.registerEventsListeners();
        this.registerMetrics();
        this.startHeartbeat();
        this.registerCityMap();
        log.info("Done!");
    }

    private void registerCityMap() {
        cityMap = new CityMap();
    }
    private void startHeartbeat() {
        AncapHeartbeat heartbeat = new AncapHeartbeat();
        heartbeat.start();
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
        SingletonHolder.HOLDER_INSTANCE = this;
    }

    private void registerEventsListeners() {
        getServer().getPluginManager().registerEvents(new ProtectListener(), this);
        getServer().getPluginManager().registerEvents(new PVPListener(), this);
        getServer().getPluginManager().registerEvents(new CityMoveListener(), this);
        getServer().getPluginManager().registerEvents(new ExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new AncapStatesEventsListener(), this);
        getServer().getPluginManager().registerEvents(new HeartbeatListener(), this);
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



    @Override
    public void onDisable() {
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
    public static AncapPlayer[] getOnlinePlayers() {
        Player[] bukkitPlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        AncapPlayer[] players = new AncapPlayer[bukkitPlayers.length];
        for (int i = 0; i<players.length; i++) {
            players[i] = new AncapPlayer(bukkitPlayers[i].getName());
        }
        return players;
    }

    public static AddonCore getAddonCore() {
        return AddonCore.getInstance();
    }
    public static boolean canDamageBeDealedIn(Location loc1, Location loc2) {
        if (getCityMap().getCity(loc1) != null || getCityMap().getCity(loc2) != null) {
            return false;
        }
        return true;
    }

    public static void clearDynMap() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker deleteset id:ancap world:world");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addset Государства id:ancap world:world");
    }

    public static void redrawDynmap() {
        AncapStates.clearDynMap();
        long time0 = System.currentTimeMillis();
        Logger log = Bukkit.getLogger();
        DynmapDrawer drawer = new DynmapDrawer();
        drawer.drawAllCities();
        long time1 = System.currentTimeMillis();
        long estimatedTime = time1-time0;
        log.info("Dynmap redrawed. Estimated time: "+estimatedTime);
    }

    public static DynmapAPI getDynmap() {
        return dynmap;
    }

    public static void sendMessage(Message message) {
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        AncapPlayer[] ancapPlayers = new AncapPlayer[players.length];
        for (int i = 0; i<players.length; i++) {
            ancapPlayers[i] = new AncapPlayer(players[i]);
        }
        for (int i = 0; i< ancapPlayers.length; i++) {
            ancapPlayers[i].sendMessage(message);
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

    public String convertLocation(String string) {
        return string.replace(",", ";");
    }

    public Location getOldLocation(String string) {
        try {
            String[] locationString = string.split(",");
            World world = Bukkit.getWorld(locationString[0]);
            double x = Double.valueOf(locationString[1]);
            double y = Double.valueOf(locationString[2]);
            double z = Double.valueOf(locationString[3]);
            Location loc = new Location(world, x, y, z);
            return loc;
        } catch (Exception e) {
            return null;
        }
    }

    public Location getLocation(String string) {
        String[] locationString = string.split(";");
        World world = Bukkit.getWorld(locationString[0]);
        double x = Double.valueOf(locationString[1]);
        double y = Double.valueOf(locationString[2]);
        double z = Double.valueOf(locationString[3]);
        Location loc = new Location(world, x, y, z);
        return loc;
    }

    public String getStringFrom(Location location) {
        return location.getWorld().getName()+";"+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ();
    }

    public HexagonalGrid getGrid() {
        return this.grid;
    }

    public static boolean canInteract(AncapPlayer player, Location loc) {
        try {
            City city = getCityMap().getCity(loc);
            if (city == null) {
                return true;
            }
            PrivateChunk chunk = city.getPrivateChunk(loc);
            int remoteness = city.getRemoteness(player);
            int allowLevel = city.getAllowLevel().getInt();
            Message message = ErrorMessage.CANT_INTERACT_THIS_BLOCK;
            if (chunk.getOwner() == null) {
                if (remoteness>allowLevel) {
                    player.sendMessage(message);
                    return false;
                }
                return true;
            }
            if (remoteness>allowLevel) {
                player.sendMessage(message);
                return false;
            }
            if (chunk.getOwner() != null && !chunk.getOwner().equals(player) && !chunk.getOwner().isFriendOf(player)) {
                player.sendMessage(message);
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static AncapStatesInfo getInfo() {
        return new AncapStatesInfo();
    }
    }
