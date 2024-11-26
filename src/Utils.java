import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public class Utils {
    public static int manhattanDistance(Location a, Location b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());

        return dx + dy;
    }

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

    public static List<Location> getSurroundingEmptyNonBlockingTiles(World world, List<Location> tiles) {
        List<Location> freeTiles = new ArrayList<>();
        for(Location location : tiles) {
            if(!world.containsNonBlocking(location)) {
                freeTiles.add(location);
            }
        }
        return freeTiles;
    }

    public static Location getValidRandomLocation(World world) {
        if(world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }

        int size = world.getSize();
        Random rand = new Random();
        Location location = new Location(rand.nextInt(size), rand.nextInt(size));

        while (world.getTile(location) != null) {
            location = new Location(rand.nextInt(size), rand.nextInt(size));
        }

        return location;

    }

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
