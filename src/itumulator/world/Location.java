package itumulator.world;

/**
 * Provides an abstraction to deal with coordinates (x,y) and compare them.
 */
public class Location {
    private int x, y;

    /**
     * Creates a new location
     * @param x coordinate
     * @param y coordinate
     */
    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x coordinate of the location
     * @return the x coordinate (int)
     */
    public int getX(){
        return this.x;
    }

    /**
     * Gets the y coordinate of the location
     * @return the y coordinate (int)
     */
    public int getY(){
        return this.y;
    }

    /**
     * Provides the location in a string format of (x, y).
     */
    @Override
    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }

    /**
     * Allows comparison of two locations according to their x,y coordinates
     */
    @Override
    public boolean equals(Object o){
        if(this == o) return true;

        if(o == null || o.getClass() != this.getClass()) return false;

        Location l = (Location) o;
        return this.x == l.getX() && this.y == l.getY();
    
    }

    /**
     * Provides a hash value of the location based on their x,y coordinates (assuming no larger coordinates than 1000).
     */
    @Override
    public int hashCode(){
        return this.x * 1000 + this.y;
    }
}
