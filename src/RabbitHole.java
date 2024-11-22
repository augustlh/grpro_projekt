import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

public class RabbitHole implements NonBlocking {
    private final List<Hole> holes;

    public RabbitHole() {
        holes = new ArrayList<>();
    }

    public void addHole(Hole hole) {

        holes.add(hole);
    }

    public Location getClosestHole(Location location) {
        if(this.holes.isEmpty()) {
            return null;
        }

        Hole closestHole = null;
        int closestDistance = Integer.MAX_VALUE;

        for(Hole hole : holes) {
            int distance = Math.abs(hole.getLocation().getX() - location.getX()) + Math.abs(hole.getLocation().getY() - location.getY());

            if(distance < closestDistance) {
                closestHole = hole;
                closestDistance = distance;
            }
        }

        if(closestHole != null) {
            return closestHole.getLocation();
        }

        return null;
    }
}