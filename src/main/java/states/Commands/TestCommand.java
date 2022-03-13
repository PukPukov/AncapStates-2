package states.Commands;

import Database.Database;
import library.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import states.Dynmap.DynmapDrawer;
import states.Hexagons.AncapHexagonalGrid;
import states.Main.AncapStates;
import states.Player.AncapStatesPlayer;

import java.util.logging.Logger;

public class TestCommand implements CommandExecutor {

    Database statesDB = AncapStates.getMainDatabase();

    Logger log = Bukkit.getLogger();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        AncapStatesPlayer player = new AncapStatesPlayer(sender.getName());
        Player p = (Player) sender;
        if (args[0].equals("hexagon")) {
            if (args.length == 1) {
                sender.sendMessage("no args");
                return true;
            }
            if (args[1].equals("start")) {
                sender.sendMessage("Тестирование системы гексагонов начато.");
                HexagonalGrid hexagonGrid = new HexagonalGrid(Orientation.FLAT, new Point(0, 0), new Point(100, 100), new Morton64(2, 32));
                Hexagon hexagon = hexagonGrid.getHexagon(new Point(p.getLocation().getX(), p.getLocation().getZ()));
                Point[] corners = hexagon.getVertexPositions();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addset id:ancap world:world");
                for (int i = 0; i < 6; i++) {
                    String x = String.valueOf(corners[i].getX());
                    String z = String.valueOf(corners[i].getY());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker add icon:cross set:ancap x:"+x+" y:64 z:"+z+" world:world");
                }
                Hexagon[] neighbours = hexagon.getNeighbors(1);
                Point[] centers = new Point[6];
                for (int i = 0; i<neighbours.length; i++) {
                    centers[i] = neighbours[i].getCenter();
                    String x = String.valueOf(centers[i].getX());
                    String z = String.valueOf(centers[i].getY());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker add icon:cross set:ancap x:"+x+" y:64 z:"+z+" world:world");
                }
                sender.sendMessage("Процедуры тестирования системы гексагонов завершены.");
                return true;
            }
            if (args[1].equals("draw")) {
                sender.sendMessage("Тестирование системы отрисовки гексагонов начато.");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addset id:ancap world:world");
                Hexagon[] hexagons = new Hexagon[9];
                DynmapDrawer drawer = new DynmapDrawer();
                AncapHexagonalGrid grid = new AncapHexagonalGrid(Orientation.FLAT, new Point(0, 0), new Point(100, 100), new Morton64(2, 32));
                hexagons[0] = grid.getHexagon(new Point(p.getLocation().getX(), p.getLocation().getZ()));
                hexagons[1] = hexagons[0].getNeighbor(1);
                hexagons[2] = hexagons[0].getNeighbor(2);
                hexagons[3] = hexagons[0].getNeighbor(3);
                hexagons[4] = hexagons[0].getNeighbor(4);
                hexagons[5] = hexagons[1].getNeighbor(1);
                hexagons[6] = hexagons[5].getNeighbor(2);
                hexagons[7] = hexagons[6].getNeighbor(3);
                hexagons[8] = hexagons[7].getNeighbor(4);
                drawer.drawFigure(hexagons, "c9c9c9", null);
                return true;
            }
            if (args[1].equals("stop")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker deleteset id:ancap world:world");
                sender.sendMessage("Тестирование системы гексагонов закончено.");
                return true;
            }
        }
        if (args[0].equals("redraw")) {
            DynmapDrawer.redrawDynmap();
            return true;
        }
        if (args[0].equals("chunk")) {
            Chunk chunk = p.getLocation().getChunk();
            sender.sendMessage(chunk.getX()+";"+chunk.getZ());
        }
        if (args[0].equals("msg1")) {
            Bukkit.broadcastMessage("§6§lГосударства >> §fНация Сотрапия была разрушена нацией Бретонь");
        }
        if (args[0].equals("on")) {
            AncapStates.getInstance().enableTest();
        }
        if (args[0].equals("off")) {
            AncapStates.getInstance().disableTest();
        }

        return true;
    }
}
