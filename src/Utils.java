import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public class Utils {
    //Made form pseudo-code from https://en.wikipedia.org/wiki/Breadth-first_search
    public static List<Location> pathToTarget(World world, Location start, Location end) {
        int[] xDirections = {-1, 1, 0, 0, -1, 1, -1, 1};
        int[] yDirections = {0, 0, -1, 1, -1, -1, 1, 1};

        if(start == null || end == null) {
            return new ArrayList<>();
        }

        Queue<Location> queue = new LinkedList<>();
        queue.add(start);

        Set<Location> explored = new HashSet<>();
        explored.add(start);

        Map<Location, Location> parentMap = new HashMap<>();

        while(!queue.isEmpty()) {
            Location currentLocation = queue.poll();
            if(currentLocation.getX() == end.getX() && currentLocation.getY() == end.getY()) {
                List<Location> path = new ArrayList<>();
                Location current = end;

                while(current != null){
                    path.add(current);
                    current = parentMap.get(current);
                }

                Collections.reverse(path);
                path.removeFirst();
                return path;
            }

            for (int i = 0; i < 4; i++) {
                Location neighbour = new Location(
                        currentLocation.getX() + xDirections[i],
                        currentLocation.getY() + yDirections[i]
                );

                if (neighbour.getX() >= 0 && neighbour.getX() < world.getSize() && neighbour.getY() >= 0 && neighbour.getY() < world.getSize() && !explored.contains(neighbour) && world.isTileEmpty(neighbour)) {
                    explored.add(neighbour);
                    parentMap.put(neighbour, currentLocation);
                    queue.add(neighbour);
                }
            }
        }

        return new ArrayList<>();
    }

    public static int manhattanDistance(Location a, Location b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());

        return dx + dy;
    }

    public static Location closestConsumableEntity(World world, Animal a, int searchRadius) {
        Set<Location> neighbours = world.getSurroundingTiles(searchRadius);
        Location currentLocation = world.getLocation(a);

        Eatable closestOrganism = null;
        int closestDistance = Integer.MAX_VALUE;

        for(Location location : neighbours) {
            Object entity = world.getTile(location);

            if(entity instanceof Eatable o) {
                if(a.canEat(o)) {
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

    public static List<Location> getSurroundingEmptyNonBlockingTiles(World world, List<Location> tiles) {
        List<Location> freeTiles = new ArrayList<>();
        for(Location location : tiles) {
            if(!world.containsNonBlocking(location)) {
                freeTiles.add(location);
            }
        }
        return freeTiles;
    }
}
