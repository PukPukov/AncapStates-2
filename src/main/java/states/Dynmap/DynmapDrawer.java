package states.Dynmap;


import AncapLibrary.API.SMassiveAPI;
import com.fasterxml.uuid.Generators;
import library.Hexagon;
import library.HexagonComponents.HexagonSide;
import library.HexagonalGrid;
import library.Point;
import org.bukkit.Bukkit;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerSet;
import org.dynmap.markers.PolyLineMarker;
import states.Main.AncapStates;
import states.States.City.City;

import java.util.logging.Logger;

public class DynmapDrawer {

    public DynmapDrawer() {
    }

    public void addPoints(Point[] points) {
        for (Point point : points) {
            double x = point.getX();
            double y = point.getY();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addcorner "+x+" 64 "+y+" world");
        }
    }

    public void drawLine(Point[] points, DynmapDescription description) {
        double[] x = new double[points.length];
        double[] y = new double[points.length];
        double[] z = new double[points.length];
        for (int i = 0; i<points.length; i++) {
            x[i] = points[i].getX();
            y[i] = 64;
            z[i] = points[i].getY();
        }
        String id = Generators.timeBasedGenerator().generate().toString();
        MarkerSet m = AncapStates.getDynmap().getMarkerAPI().getMarkerSet("ancap");
        PolyLineMarker pl = m.createPolyLineMarker(id, description.getName(), true, "world", x, y, z, false);
        pl.setLineStyle(7, 1.0D, Integer.parseInt("000".toUpperCase().substring(1), 16));
        this.setSideDescription(id, description);
    }

    public void draw(HexagonSide side, DynmapDescription description) {
        Point[] points = side.getEnds();
        drawLine(points, description);
    }

    public void draw(HexagonSide[] sides, DynmapDescription description) {
        for (HexagonSide side : sides) {
            this.draw(side, description);
        }
    }

    public void draw(Hexagon hexagon, String color, DynmapDescription description) {
        Point[] points = hexagon.getVertexPositions();
        double[] x = new double[points.length];
        double[] z = new double[points.length];
        for (int i = 0; i<points.length; i++) {
            x[i] = points[i].getX();
            z[i] = points[i].getY();
        }
        String id = Generators.timeBasedGenerator().generate().toString();
        MarkerSet m = AncapStates.getDynmap().getMarkerAPI().getMarkerSet("ancap");
        AreaMarker am = m.createAreaMarker(id, description.getName(), true, "world", x, z, false);
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
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker add id:"+id+" "+description.getName()+" set:ancap x:"+point.getX()+" y:64 z:"+point.getY()+" icon:"+icon+" world:world");
        this.setMarkerDescription(id, description);
    }

    public void draw(Hexagon[] hexagons, String color, DynmapDescription description) {
        for (Hexagon hexagon : hexagons) {
            this.draw(hexagon, color, description);
        }
    }

    public void drawFigure(Hexagon[] hexagons, String color, DynmapDescription description) {
        if (hexagons.length == 0) {
            return;
        }
        HexagonalGrid grid = hexagons[0].getGrid();
        HexagonSide[] sides = grid.getBounds(hexagons);
        this.draw(hexagons, color, description);
        this.draw(sides, description);
    }

    public void drawAllCities() {
        City[] cities = AncapStates.getCityMap().getCities();
        if (cities.length == 0) {
            return;
        }
        for (int i = 0; i<cities.length; i++) {
            try {
                cities[i].draw();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setHexagonDescription(String id, DynmapDescription description) {
        String descriptionString = this.generateDescriptionLine(description.getStrings());
        DynmapAPI dynmap = AncapStates.getDynmap();
        MarkerSet m = dynmap.getMarkerAPI().getMarkerSet("ancap");
        AreaMarker am = m.findAreaMarker(id);
        am.setDescription(descriptionString);
        return;
    }

    public void setSideDescription(String id, DynmapDescription description) {
        String descriptionString = this.generateDescriptionLine(description.getStrings());
        DynmapAPI dynmap = AncapStates.getDynmap();
        MarkerSet m = dynmap.getMarkerAPI().getMarkerSet("ancap");
        PolyLineMarker plm = m.findPolyLineMarker(id);
        plm.setDescription(descriptionString);
        return;
    }

    public void setMarkerDescription(String id, DynmapDescription description) {
        String descriptionString = this.generateDescriptionLine(description.getStrings());
        DynmapAPI dynmap = AncapStates.getDynmap();
        MarkerSet m = dynmap.getMarkerAPI().getMarkerSet("ancap");
        Marker mm = m.findMarker(id);
        mm.setDescription(descriptionString);
        return;
    }

    private String generateDescriptionLine(String[] strings) {
        return SMassiveAPI.toString(strings, "<br>");
    }

    public static void clearDynMap() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker deleteset id:ancap world:world");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addset Государства id:ancap world:world");
    }

    public static void redrawDynmap() {
        DynmapDrawer.clearDynMap();
        long time0 = System.currentTimeMillis();
        Logger log = Bukkit.getLogger();
        DynmapDrawer drawer = new DynmapDrawer();
        drawer.drawAllCities();
        long time1 = System.currentTimeMillis();
        long estimatedTime = time1-time0;
        log.info("Dynmap redrawed. Estimated time: "+estimatedTime);
    }
}
