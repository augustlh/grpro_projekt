package itumulator.display.utility;

import java.awt.image.BufferedImage;

import itumulator.world.Location;

/**
 * Utility to generate iso perspective coordinates for squares.
 */
public class IsomorphicCoordinateFactory {
        /* This is Singleton pattern */
        private static IsomorphicCoordinateFactory instance;

        public static IsomorphicCoordinateFactory Instance(){
            if (instance == null){
                throw new IllegalStateException("IsomorphicCoordinateFactory has not been setup yet, use setupFactory");
            }
            return instance;
        }
        /* This is Singleton pattern */

        public static void setupFactory(int displaySize, int worldSize){
            if (instance == null){
                instance = new IsomorphicCoordinateFactory();
            }
            instance.displaySize = displaySize;
            instance.worldSize = worldSize;
        }

        public Point2DInt getIsoLocation(Location l){
            return IsomorphicUtility.getIsoLocation(l, (int)getTileWidth(), getDisplaySize());
        }
        
        public double getTileWidth(){
            return getDisplaySize()/worldSize;
        }

        public double getTileHeight(){
            return getTileWidth()/2;
        }

        public int getDisplaySize(){
            return displaySize;
        }

        public BufferedImage getScaledImage(BufferedImage image){
            return ImageUtility.getScaledImage(image, (int)getTileHeight(), (int)getTileHeight());
        }

        private int displaySize;
        private int worldSize;

        private IsomorphicCoordinateFactory(){
        }
}
