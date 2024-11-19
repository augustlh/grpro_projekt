package itumulator.display.utility;

import java.awt.Polygon;
import itumulator.world.Location;

/**
 * Utility for producing isomorphic coordinates (in the form of polygons and points)
 */
public final class IsomorphicUtility {
    public static Polygon getIsoPolygon(int x, int y, int width, int height){
        Polygon p = new Polygon();
        p.addPoint(x - width, y);
        p.addPoint(x, y - height);
        p.addPoint(x + width, y);
        p.addPoint(x, y + height);
        return p;
    }
    public static Point2DInt getIsoLocation(Location l, int tileSize, int fullSize){
        return new Point2DInt(
            ((l.getX() - l.getY()) * (tileSize / 2)) + (fullSize/2), 
            ((l.getX() + l.getY()) * (tileSize / 4)) + (fullSize/4));
    }
}