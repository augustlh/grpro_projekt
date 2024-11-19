package itumulator.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The World class provides an abstraction for our simulated worlds. A world
 * contains beneficial abstractions for interacting with the world, some of
 * which are used by the Simulator itself.
 * World contains a landscape / 'map' (a three-dimensional array) wherein
 * objects
 * can be placed. The world class / object provides abstractions for
 * manipulating this.
 * Furthermore, World can contain objects not currently existing on the map,
 * e.g., when objects are not visible. This is primarily used by the simulator
 * to ensure all {@link itumulator.simulator.Actor} can act without being on the
 * 'map'.
 */
public class World {
    private static int DAY_DURATION = 20;
    private static int IS_DAY_TIME = 10;

    private Object[][][] tiles;
    private Map<Object, Location> entities;
    private int size;
    private Location current;
    private int time = 0;

    /**
     * initializes a World with a map of size. A day is defined in steps (i.e.,
     * 20) and day time is decided by before the first 10 steps.
     * 
     * @param size of the map within the world (size defines both x and y).
     */
    public World(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Size must greater than zero.");
        this.tiles = new Object[size][size][2];
        this.size = size;
        this.entities = new HashMap<>();
    }

    /**
     * Increases the step of the world to progress time.
     */
    public void step() {
        time++;
        time = time % DAY_DURATION;
    }

    /**
     * Provides an indicator of whether it is day.
     * 
     * @return true if it is day time.
     */
    public boolean isDay() {
        return time < IS_DAY_TIME;
    }

    /**
     * Provides an indicator of whether it is night.
     * 
     * @return true if it is night time.
     */
    public boolean isNight() {
        return time >= IS_DAY_TIME;
    }

    /**
     * Provides the size of the world (it is square)
     * 
     * @return the x and y of the world.
     */
    public int getSize() {
        return size;
    };

    /**
     * Sets the time to day.
     */
    public void setDay() {
        time = 0;
    }

    /**
     * Sets the time of the world to beginning of night, according to IS_DAY_TIME. 
     */
    public void setNight() {
        time = IS_DAY_TIME;
    }

    
    /**
     * Provides the current time of day
     * 
     * @return the current time as an integer
     */
    public int getCurrentTime() {
        return time;
    }

    /**
     * Provides the total amount of ticks of a day
     * 
     * @return the total amount of ticks of a day
     */
    public static int getTotalDayDuration() {
        return DAY_DURATION;
    };

    /**
     * Provides the amount of day time ticks
     * 
     * @return the amount of ticks the world is in day time
     */
    public static int getDayDuration() {
        return IS_DAY_TIME;
    };

    /**
     * Sets the location which is deemed as the 'current' location. This is the
     * location used when calling parameterless methods normally requiring a
     * parameter (e.g., {@link getSurroundingTiles() getSurroundingTiles}). Please note that methods like  {@link move(Object, Location) move} and {@link setTile(Location, Object) setTile} do not update this location.
     * You have to do that manually using {@link setCurrentLocation(Location)} if you want to keep using the parameterless methods.
     * 
     * @param current the location which will be set as currently under examination
     *                (can be null). Eg. when the actor is not on the map but exists in the world.
     * @throws IllegalArgumentException
     *                                  thrown if the location is out of bounds.
     */
    public void setCurrentLocation(Location current) {
        if (current != null)
            validateCoordinates(current);
        this.current = current;
    }

    /**
     * Provides the current location of the world. This is the location which the world uses
     * for the overloaded methods (e.g., {@link getSurroundingTiles()}). This does not provide any object.
     * @return the current location.
     */
    public Location getCurrentLocation() {
        return current;
    }


    /**
     * Retrieves the current location of a specified object within the world. If the object is in the world but
     * not currently on the map (i.e., its location is null), this method throws an
     * exception.
     * 
     * @param object The object whose location is to be retrieved.
     * @return The current location of the object on the map.
     * @throws IllegalArgumentException if the object does not exist in the world,
     *                                  or if it exists but is not currently placed on the map.
     */
    public Location getLocation(Object object) {
        validateLocation(object);
        return entities.get(object);
    }

    /**
     * Get immediate tiles surrounding the current location (as defined by
     * {@link getCurrentLocation() getCurrentLocation} and {@link setCurrentLocation(Location current)
     * setLocation}). The method only returns existing locations within the map.
     * 
     * @return set of immediate surrounding locations.
     * @throws IllegalStateException
     *                               thrown if current location not set.
     */
    public Set<Location> getSurroundingTiles() {
        validateCurrent();
        return getSurroundingTiles(current);
    }

    /**
     * Get tiles (up to the radius provided) surrounding the current location which
     * are not filled (as defined by {@link getCurrentLocation() getCurrentLocation} and
     * {@link setCurrentLocation(Location current) setCurrentLocation}). 
     * The method only returns existing locations within the map.
     * 
     * @param radius the number of locations to include in each direction.
     * @return set of surrounding locations.
     * @throws IllegalStateException
     *                               thrown if current location not set.
     */
    public Set<Location> getSurroundingTiles(int radius) {
        validateCurrent();
        return getSurroundingTiles(current, radius);
    }

    /**
     * Get tiles which are not filled surrounding the current location (as defined
     * by {@link getCurrentLocation() getCurrentLocation} and {@link setCurrentLocation(Location current)
     * setLocation}) and {@link #isTileEmpty(Location)}). The method only returns existing locations within the map.
     * 
     * @return set of surrounding tiles which are empty.
     * @throws IllegalStateException
     *                               thrown if current location not set.
     */
    public Set<Location> getEmptySurroundingTiles() {
        validateCurrent();
        return getEmptySurroundingTiles(current);
    }


    /**
     * Removes an object from its current location on the map, but retains its existence in the world. 
     * This method is particularly useful for temporarily 'hiding' an object from the map without 
     * deleting it (if an actor, its act method will continuously be called). 
     * Note: this does not delete the object from the world's entity list. Furthermore, remove does not update the current location. Thus, calling overloaded methods
     * (e.g., {@link #getEmptySurroundingTiles}) will still provide the tiles surrounding the location you removed from.
     * 
     * @param object The object to be removed from the map.
     * @throws IllegalArgumentException if the object does not exist in the world,
     *                                  or is not currently placed on the map.
     */
    public void remove(Object object) {
        Location l = getLocation(object);
        this.tiles[l.getX()][l.getY()][getLayer(object)] = null;
        entities.put(object, null);
    }

    /**
     * Completely removes an object from the world, both from the map and the world's entity list. 
     * This method is used when an object is no longer needed in the simulation and should be 
     * permanently deleted. If the object is currently on the map, it is first removed using
     * {@link #remove(Object)} method. Then, the object is removed from the world's entity list,
     * ensuring that it no longer exists within the world context. 
     * 
     * @param object The object to delete from the world.
     * @throws IllegalArgumentException if the object does not exist in the world.
     */
    public void delete(Object object) {
        if (!entities.containsKey(object))
            throw new IllegalArgumentException("No such object exists in the world.");
        Location l = entities.get(object);
        if (l != null) {
            remove(object);
        }
        entities.remove(object);
    }

    /**
     * Places a given object at a specified location on the map. 
     * If the location already contains an object of the same type (blocking/non-blocking), an exception is thrown.
     * Additionally, if the object already exists elsewhere in the world, it cannot be placed again.
     * Note that setTile does not update the current location. Thus, one cannot after using setTile use the overloaded methods (e.g., {@link #getEmptySurroundingTiles}) as the current location is not set.
     * 
     * @param location The location where the object will be placed.
     * @param object   The object to place at the specified location.
     * @throws IllegalArgumentException if the tile is occupied by the same type of object, or if the object already exists on the map, or if the location is out of bounds.
     */
    public void setTile(Location location, Object object) {
        if (entities.get(object) != null) {
            throw new IllegalArgumentException("Entity already exists in the world.");
        }
        validateCoordinates(location);

        Object[] tile = this.tiles[location.getX()][location.getY()];

        if (object instanceof NonBlocking) {
            if (tile[0] != null)
                throw new IllegalArgumentException("Tile cannot contain more than one non-blocking object");

            tile[0] = object;
        } else {
            if (tile[1] != null)
                throw new IllegalArgumentException("Tile cannot contain more than one blocking object");
            tile[1] = object;
        }

        entities.put(object, location);
    }

    /**
     * Moves an object from its current location to a new location. 
     *
     * @param object The object to be moved.
     * @param to     The destination location for the object.
     * @throws IllegalArgumentException if the object is not in the world, is not on the map, or if the destination tile is occupied by a blocking object.
     */
    public void move(Object object, Location to) {
        validateCoordinates(to);

        remove(object);
        setTile(to, object);

    }

    /**
     * Retrieves an object from a specified location, prioritizing blocking objects over non-blocking ones. 
     * If a blocking object exists at the location, it is returned; otherwise, the non-blocking object is returned.
     * If multiple objects are present (one blocking and one non-blocking), the blocking one is always returned.
     * If no objects are present at the location, null is returned.
     *
     * @param location The location from which to retrieve the object.
     * @return The object at the specified location, prioritizing blocking objects. Returns null if no object is present.
     */
    public Object getTile(Location location) {
        validateCoordinates(location);
        Object[] tile = this.tiles[location.getX()][location.getY()];
        if (tile[1] != null)
            return tile[1];
        return tile[0];
    }

    /**
     * Adds an object to the world without placing it on the map. The object's location is set to null, indicating it is not currently on the map.
     * This method is useful for adding objects that exist in the world but are not visible or interactable on the map.
     *
     * @param object The object to add to the world.
     * @throws IllegalArgumentException if the object already exists in the world.
     */
    public void add(Object object) {
        if (entities.containsKey(object))
            throw new IllegalArgumentException("Object already exists in the world.");
        entities.put(object, null);
    }

    /**
     * Retrieves a non-blocking object from a specified location. If a non-blocking object is present at the location, it is returned.
     * If no non-blocking object is present, an exception is thrown.
     *
     * @param location The location to check for a non-blocking object.
     * @return The non-blocking object at the specified location.
     */
    public Object getNonBlocking(Location location) {
        validateCoordinates(location);
        Object o = tiles[location.getX()][location.getY()][0];
        return o;
    }

    /**
     * Checks if a specified location is empty. A tile is considered empty if it does not contain any blocking objects.
     * Non-blocking objects do not affect the emptiness status of a tile. 
     *
     * @param location The location to check.
     * @return true if the tile is empty (no blocking objects present), false otherwise.
     */
    public boolean isTileEmpty(Location location) {
        validateCoordinates(location);
        return tiles[location.getX()][location.getY()][1] == null;
    }

    /**
     * Determines whether a specified location contains a non-blocking object. This method checks if there is an object at the location
     * that implements the NonBlocking interface.
     *
     * @param location The location to check.
     * @return true if a non-blocking object is present, false otherwise.
     */
    public boolean containsNonBlocking(Location location) {
        validateCoordinates(location);
        return this.tiles[location.getX()][location.getY()][0] != null;
    }


    /**
     * Retrieves a set of locations immediately surrounding the specified location,
     * including diagonals. The method calculates the surrounding tiles within a
     * radius of 1 tile. Tiles outside the bounds of the world are excluded.
     * 
     * @param location The central location for which surrounding tiles are to be found.
     * @return A set of locations immediately surrounding the specified location.
     * @throws IllegalArgumentException if the specified location is out of bounds.
     */
    public Set<Location> getSurroundingTiles(Location location) {
        // give a list of surrounding tiles (including diagonals)
        return getSurroundingTiles(location, 1);
    }

    /**
     * Retrieves a set of locations surrounding the specified location within a given
     * radius. This method includes all tiles within the radius.
     * Tiles outside the world's bounds are excluded.
     * 
     * @param location The  location for which surrounding tiles are to be found.
     * @param radius   The radius within which to find surrounding tiles. Must be non-negative.
     * @return A set of locations surrounding the specified location within the radius.
     * @throws IllegalArgumentException if the specified location is out of bounds or
     *                                  if the radius is negative.
     */
    public Set<Location> getSurroundingTiles(Location location, int radius) {
        validateCoordinates(location);
        // give a list of surrounding tiles (including diagonals)
        Set<Location> surroundingTiles = new HashSet<Location>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                int xCoord = location.getX() + x;
                int yCoord = location.getY() + y;
                if (xCoord == location.getX() && yCoord == location.getY())
                    continue; // skip the tile itself (the center)
                if (xCoord >= 0 && xCoord < tiles.length && yCoord >= 0 && yCoord < tiles[0].length) {
                    surroundingTiles.add(new Location(xCoord, yCoord));
                }
            }
        }
        return surroundingTiles;
    }

    /**
     * Retrieves a set of locations immediately surrounding the specified location that are 'empty'.
     * A location is considered 'empty' based on the criteria defined in {@link #isTileEmpty(Location)} method,
     * typically meaning it does not contain a blocking object. This method is useful for determining
     * potential movement or placement options around a given location, particularly in scenarios where
     * blocking objects restrict certain actions. It includes all adjacent tiles, both orthogonal and diagonal,
     * within a radius of 1 tile from the given location. Locations outside the bounds of the world
     * are not included in the returned set.
     * 
     * @param location The central location from which to find empty surrounding tiles.
     * @return A set of locations around the specified location that are empty, as per the definition in
     *         {@link #isTileEmpty(Location)}.
     * @throws IllegalArgumentException if the specified location is out of bounds.
     */
    public Set<Location> getEmptySurroundingTiles(Location location) {
        Set<Location> surroundingTiles = getSurroundingTiles(location);
        Iterator<Location> it = surroundingTiles.iterator();
        while (it.hasNext()) {
            Location tile = it.next();
            if (!isTileEmpty(tile))
                it.remove();
        }
        return surroundingTiles;
    }


    /**
     * Retrieves a copy of the three-dimensional array representing the current state of the world map. 
     * Each element in this array corresponds to a specific location in the world, 
     * with the first two dimensions representing the x and y coordinates, 
     * and the third dimension representing layers (0 for non-blocking, 1 for blocking objects). 
     * Note that the returned array is a copy, ensuring that modifications to it 
     * do not affect the actual world state.
     * 
     * @return A copy of the three-dimensional array representing the world's map.
     */
    public Object[][][] getTiles() {
        return tiles.clone();
    }


    /**
     * Gets all objects within the world (including those who do not currently
     * reside on the map).
     * 
     * @return Map where keys are the objects and their value is the location they
     *         are currently placed on (can be null if not currently on the map).
     *         The map returned is a copy of the internal map to avoid concurrent
     *         modification exceptions.
     */
    public java.util.Map<Object, Location> getEntities() {
        return new HashMap<>(entities);
    }

    /**
     * Determines whether an object exists in world
     * @param o object to check
     * @return true if the object exists in the world
     */
    public boolean contains(Object o){
        if(o == null) throw new IllegalArgumentException("Object cannot be null");
        return entities.containsKey(o);
    }

    /**
     * Determines whether an object is placed on a tile
     * @param o object to check
     * @return true if the object exists on a tile
     */
    public boolean isOnTile(Object o){
        if(!contains(o)) throw new IllegalArgumentException("Object must exist in world");
        return entities.get(o) != null;
    }

    /**
     * Returns all objects at a location which is an instance of the type given, or an empty set if none matches the type.
     * @param type the class we are looking for.
     * @param locations the locations from where we want to collect all objects of a certain instance
     * @return a set of the type we are looking for (decided by the parameter 'type') containing all (if any) objects that are instances of this.
     */
    public <T> Set<T> getAll(Class<T> type, Collection<Location> locations){
        Set<T> objects = new HashSet<>();
        for(Location l : locations){
            Object o = getTile(l);
            if(o != null && type.isInstance(o)) objects.add((T)o);
        }
        return objects;
    }

    // Private methods

    private void validateCurrent() {
        if (current == null)
            throw new IllegalStateException("Currently no location is set");
    }

    private void validateCoordinates(Location l) {
        if (l.getX() < 0 || l.getX() >= size || l.getY() < 0 || l.getY() >= size)
            throw new IllegalArgumentException("Tile out of bounds");
    }

    private void validateLocation(Object o) {
        if (!entities.containsKey(o))
            throw new IllegalArgumentException("Object does not exist in the world.");
        Location l = entities.get(o);
        if (l == null)
            throw new IllegalArgumentException("Object is not on the map.");
    }


    private int getLayer(Object o) {
        if (o instanceof NonBlocking)
            return 0;
        return 1;
    }

}
