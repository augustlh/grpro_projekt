import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week1/t1-1a.txt", 800, 150);
        Program program = resourceManager.getProgram();

        Location obstacle = new Location(2,2);
        Rabbit grass = new Rabbit();
        program.getWorld().setCurrentLocation(obstacle);
        program.getWorld().setTile(obstacle, grass);

        program.show();


        List<Location> path = Utils.pathToTarget(program.getWorld(),  new Location(0,2), new Location(3,1));

        if (path != null) {
            System.out.println("Path found:");
            for (Location loc : path) {
                System.out.println("[" + loc.getX() + ", " + loc.getY() + "]");
            }
        } else {
            System.out.println("No path found.");
        }
    }


}