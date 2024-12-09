package help;
import behaviours.nests.RabbitHole;
import datatypes.Animal;
import datatypes.Organism;
import datatypes.Species;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

/**
 * Utility class providing various methods useful for working with simulation entities and locations.
 */
public class Utils {
    public static Random random = new Random();
    public static int manhattanDistance(Location a, Location b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());

        return dx + dy;
    }

    public static Location closestEqualAnimal(Animal c, World world, Species species, Location currentLocation) {
        Set<Location> tiles = world.getSurroundingTiles(currentLocation,world.getSize());

        int closestDistance = Integer.MAX_VALUE;
        Location closestLocation = null;

        for(Location location : tiles) {
            Object entity = world.getTile(location);

            if(entity instanceof Animal a && a.getSpecies() == species && a != c && !a.isInfected()) {
                int distance = manhattanDistance(currentLocation, location);

                if(distance < closestDistance) {
                    closestLocation = location;
                    closestDistance = distance;
                }
            }
        }

        return closestLocation;
    }

    /**
     * Finds the closest consumable entity (Organism) within a given search radius from a specified organism.
     *
     * @param world the world containing the organism and other entities
     * @param organism the organism searching for a consumable entity
     * @param searchRadius the radius within which to search for consumable entities
     * @return the location of the closest consumable organism if found, otherwise null
     */
    public static Location closestConsumableEntity(World world, Organism organism, int searchRadius) {
        Set<Location> neighbours = world.getSurroundingTiles(searchRadius);
        Location currentLocation = world.getLocation(organism);

        Organism closestOrganism = null;
        int closestDistance = Integer.MAX_VALUE;

        for(Location location : neighbours) {
            Object entity = world.getTile(location);

            if(entity instanceof Organism o) {
                if(organism.canEat(o)) {
                    int distance = manhattanDistance(currentLocation, location);
                    if(distance < closestDistance) {
                        closestOrganism = o;
                        closestDistance = distance;
                    }
                }
            }
        }

        if (closestOrganism == null) {
            return null;
        }

        return world.getLocation(closestOrganism);

    }

    /**
     * Finds the closest RabbitHole within a specified search radius from a given location.
     *
     * @param world the world containing the locations and entities
     * @param currentLocation the location from which the search begins
     * @param searchRadius the radius within which to search for RabbitHoles
     * @return the location of the closest RabbitHole if found, otherwise null
     */
    public static Location getClosestRabbitHole(World world, Location currentLocation, int searchRadius) {
        Set<Location> neighbours = world.getSurroundingTiles(searchRadius*2);
        int closestDistance = Integer.MAX_VALUE;
        RabbitHole closestHole = null;

        for(Location location : neighbours) {
            Object entity = world.getTile(location);

            if(entity instanceof RabbitHole hole) {
                int distance = manhattanDistance(currentLocation, location);
                if(distance < closestDistance) {
                    closestDistance = distance;
                    closestHole = hole;
                }
            }
        }

        if (closestHole == null) {
            return null;
        }

        return world.getLocation(closestHole);
    }

    /**
     * Retrieves a list of tiles from the provided list that are empty and non-blocking within the given world.
     *
     * @param world the world containing the tiles
     * @param tiles the list of locations to be checked
     * @return a list of locations that are empty and non-blocking
     */
    public static List<Location> getSurroundingEmptyNonBlockingTiles(World world, List<Location> tiles) {
        List<Location> freeTiles = new ArrayList<>();
        for(Location location : tiles) {
            if(!world.containsNonBlocking(location)) {
                freeTiles.add(location);
            }
        }
        return freeTiles;
    }

    /**
     * Generates a valid random location within the given world that does not overlap with existing tiles.
     *
     * @param world The world object within which the location is to be generated.
     * @return A valid random location within the world.
     * @throws IllegalArgumentException If the provided world is null.
     */
    public static Location getValidRandomLocation(World world) {
        if(world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }

        int ATTEMPTS = 0;
        int MAX_ATTEMPTS = 800;

        int size = world.getSize();
        Random rand = new Random();
        Location location = new Location(rand.nextInt(size), rand.nextInt(size));

        while (world.getTile(location) != null) {
            if(ATTEMPTS >= MAX_ATTEMPTS) {
                return null;
            }

            ATTEMPTS++;
            location = new Location(rand.nextInt(size), rand.nextInt(size));
        }

        return location;
    }

    /**
     * Returns the first empty tile found within a 2-tile radius surrounding a given location in the world.
     * If no empty tile is found, a valid random location within the world is returned.
     *
     * @param location the central location from which to search for surrounding empty tiles
     * @param world the world containing the tiles
     * @return the Location object of the first empty tile found, or a valid random location if no empty tile is found
     */
    public static Location getSurroundingEmptyTiles(Location location, World world) {
        List<Location> tiles = new ArrayList<>(world.getSurroundingTiles(location,2));
        for(Location loc : tiles) {
            if(world.isTileEmpty(loc)) {
                return loc;
            }
        }
        return getValidRandomLocation(world);
    }

}
