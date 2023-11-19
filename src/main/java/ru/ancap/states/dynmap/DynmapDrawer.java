package ru.ancap.states.dynmap;


import com.fasterxml.uuid.Generators;
import org.bukkit.Bukkit;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerSet;
import org.dynmap.markers.PolyLineMarker;
import ru.ancap.commons.Pair;
import ru.ancap.hexagon.Hexagon;
import ru.ancap.hexagon.HexagonSide;
import ru.ancap.hexagon.HexagonVertex;
import ru.ancap.hexagon.common.Point;
import ru.ancap.states.AncapStates;
import ru.ancap.states.event.events.DynmapRedrawEvent;
import ru.ancap.states.states.city.City;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class DynmapDrawer {

    public void addPoints(Point[] points) {
        for (Point point : points) {
            double x = point.x();
            double y = point.y();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addcorner "+x+" 64 "+y+" world");
        }
    }

    public void drawLine(Pair<HexagonVertex, HexagonVertex> points, DynmapDescription description) {
        double[] x = new double[] {points.getKey().position().x(), points.getValue().position().x()};
        double[] y = new double[] {64, 64};
        double[] z = new double[] {points.getKey().position().y(), points.getValue().position().y()};
        String id = Generators.timeBasedGenerator().generate().toString();
        MarkerSet m = AncapStates.getDynmapMarkerSet();
        PolyLineMarker pl = m.createPolyLineMarker(id, description.name(), true, "world", x, y, z, false);
        pl.setLineStyle(7, 1.0D, Integer.parseInt("000".toUpperCase().substring(1), 16));
        this.setSideDescription(id, description);
    }

    public void draw(HexagonSide side, DynmapDescription description) {
        var points = side.ends();
        this.drawLine(points, description);
    }

    public void draw(Set<HexagonSide> sides, DynmapDescription description) {
        for (HexagonSide side : sides) {
            this.draw(side, description);
        }
    }

    public void draw(Hexagon hexagon, String color, DynmapDescription description) {
        List<HexagonVertex> points = hexagon.vertexes();
        double[] x = new double[points.size()];
        double[] z = new double[points.size()];
        for (int i = 0; i<points.size(); i++) {
            Point position = points.get(i).position();
            x[i] = position.x();
            z[i] = position.y();
        }
        String id = Generators.timeBasedGenerator().generate().toString();
        MarkerSet m = AncapStates.getDynmapMarkerSet();
        AreaMarker am = m.createAreaMarker(id, description.name(), true, "world", x, z, false);
        if (color.charAt(0) == '#') {
            color = color.substring(1);
        }
        am.setFillStyle(0.6D, Integer.parseInt(color.toUpperCase(), 16));
        am.setLineStyle(0, 0.01D, Integer.parseInt("000", 16));
        am.setBoostFlag(false);
        this.setHexagonDescription(id, description);
    }

    public void draw(Point point, String icon, DynmapDescription description) {
        String id = Generators.timeBasedGenerator().generate().toString();
        try {
            Bukkit.getScheduler().callSyncMethod(AncapStates.instance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker add " +
                "id:"+id+" " +
                description.name()+" " +
                "set:"+AncapStates.DYNMAP_MARKER_SET_ID+" "+
                "x:"+point.x()+" " +
                "y:64 " +
                "z:"+point.y()+" " +
                "icon:"+icon+" " +
                "world:world"
            )).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        this.setMarkerDescription(id, description);
    }

    public void draw(Set<Hexagon> hexagons, String color, DynmapDescription description) {
        for (Hexagon hexagon : hexagons) {
            this.draw(hexagon, color, description);
        }
    }

    public void drawFigure(Set<Hexagon> hexagons, String color, DynmapDescription description) {
        Set<HexagonSide> sides = AncapStates.grid.region(hexagons).bounds();
        this.draw(hexagons, color, description);
        this.draw(sides, description);
    }

    public void drawAllCities() {
        List<City> cities = AncapStates.getCityMap().getCities();
        for (City city : cities) try {
            city.draw();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHexagonDescription(String id, DynmapDescription description) {
        String descriptionString = this.generateDescriptionLine(description.description());
        MarkerSet m = AncapStates.getDynmapMarkerSet();
        AreaMarker am = m.findAreaMarker(id);
        am.setDescription(descriptionString);
    }

    public void setSideDescription(String id, DynmapDescription description) {
        String descriptionString = this.generateDescriptionLine(description.description());
        MarkerSet m = AncapStates.getDynmapMarkerSet();
        PolyLineMarker plm = m.findPolyLineMarker(id);
        plm.setDescription(descriptionString);
    }

    public void setMarkerDescription(String id, DynmapDescription description) {
        String descriptionString = this.generateDescriptionLine(description.description());
        MarkerSet m = AncapStates.getDynmapMarkerSet();
        Marker mm = m.findMarker(id);
        mm.setDescription(descriptionString);
    }

    private String generateDescriptionLine(String[] strings) {
        return String.join("<br>", strings);
    }

    public static void clearDynMap() {
        MarkerSet set = AncapStates.getDynmap().getMarkerAPI().getMarkerSet(AncapStates.DYNMAP_MARKER_SET_ID);

        if (set != null) set.deleteMarkerSet();

        AncapStates.getDynmap().getMarkerAPI().createMarkerSet(
                AncapStates.DYNMAP_MARKER_SET_ID,
                "Государства",
                null,
                true
        );
    }

    public static void redrawDynmap() {
        new DynmapRedrawEvent().callEvent();
    }

}
