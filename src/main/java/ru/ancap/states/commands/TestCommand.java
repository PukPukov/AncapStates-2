package ru.ancap.states.commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.hexagon.GridOrientation;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.hexagon.HexagonVertex;
import ru.ancap.hexagon.HexagonalGrid;
import ru.ancap.hexagon.common.Point;
import ru.ancap.states.AncapStates;
import ru.ancap.states.dynmap.DynmapDrawer;
import ru.ancap.states.hexagons.AncapHexagonalGrid;
import ru.ancap.states.player.AncapStatesPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TestCommand implements CommandExecutor {

    PathDatabase statesDB = AncapStates.getMainDatabase();

    Logger log = Bukkit.getLogger();

    @Override
    public boolean onCommand(@NotNull CommandSender sender_, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender_; 
        AncapStatesPlayer caller = AncapStatesPlayer.get(player);
        if (!player.isOp()) return false;
        if (args[0].equals("hexagon")) {
            if (args.length == 1) {
                player.sendMessage("no args");
                return true;
            }
            if (args[1].equals("start")) {
                player.sendMessage("Тестирование системы гексагонов начато.");
                HexagonalGrid hexagonGrid = new HexagonalGrid(GridOrientation.FLAT, new Point(0, 0), new Point(100, 100));
                Hexagon hexagon = hexagonGrid.hexagon(new Point(player.getLocation().getX(), player.getLocation().getZ()));
                List<HexagonVertex> corners = hexagon.vertexes();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addset id:ancap world:world");
                for (int i = 0; i < 6; i++) {
                    Point position = corners.get(i).position();
                    String x = String.valueOf(position.x());
                    String z = String.valueOf(position.y());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker add icon:cross set:ancap x:"+x+" y:64 z:"+z+" world:world");
                }
                Set<Hexagon> neighbours = hexagon.neighbors(1);
                neighbours.stream().map(Hexagon::center).forEach(point -> Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(), 
                    "dmarker add icon:cross set:ancap x:"+point.x()+" y:64 z:"+point.y()+" world:world"
                ));
                player.sendMessage("Процедуры тестирования системы гексагонов завершены.");
                return true;
            }
            if (args[1].equals("draw")) {
                player.sendMessage("Тестирование системы отрисовки гексагонов начато.");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addset id:ancap world:world");
                Hexagon[] hexagons = new Hexagon[9];
                DynmapDrawer drawer = new DynmapDrawer();
                AncapHexagonalGrid grid = new AncapHexagonalGrid(GridOrientation.FLAT, new Point(0, 0), new Point(100, 100));
                hexagons[0] = grid.hexagon(new Point(player.getLocation().getX(), player.getLocation().getZ()));
                hexagons[1] = hexagons[0].neighbor(1);
                hexagons[2] = hexagons[0].neighbor(2);
                hexagons[3] = hexagons[0].neighbor(3);
                hexagons[4] = hexagons[0].neighbor(4);
                hexagons[5] = hexagons[1].neighbor(1);
                hexagons[6] = hexagons[5].neighbor(2);
                hexagons[7] = hexagons[6].neighbor(3);
                hexagons[8] = hexagons[7].neighbor(4);
                drawer.drawFigure(Arrays.stream(hexagons).collect(Collectors.toSet()), "c9c9c9", null);
                return true;
            }
            if (args[1].equals("stop")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker deleteset id:ancap world:world");
                player.sendMessage("Тестирование системы гексагонов закончено.");
                return true;
            }
        }
        if (args[0].equals("redraw")) {
            DynmapDrawer.redrawDynmap();
            return true;
        }
        if (args[0].equals("chunk")) {
            Chunk chunk = player.getLocation().getChunk();
            player.sendMessage(chunk.getX()+";"+chunk.getZ());
        }
        if (args[0].equals("msg1")) {
            Bukkit.broadcastMessage("§6§lГосударства >> §fНация Сотрапия была разрушена нацией Бретонь");
        }
        if (args[0].equals("on")) {
            AncapStates.instance().enableTest();
        }
        if (args[0].equals("off")) {
            AncapStates.instance().disableTest();
        }

        return true;
    }
}
