import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grass extends Plant {
    List<Location> neighbours;

    public Grass(World world, Location location) {
        super(0.125, 1.2);
        world.setTile(location, this);
        neighbours = null;
    }

    @Override
    public void act(World world) {
        spread(world);
    }

    @Override
    void spread(World world) {
        if(this.neighbours == null) {
            this.neighbours = new ArrayList<>(world.getSurroundingTiles());
        }

        Random rand = new Random();
        if (rand.nextDouble() <= spreadProbability){
            List<Location> surroundingEmptyNonBlockingTiles = Utils.getSurroundingEmptyNonBlockingTiles(world, neighbours);

            if(surroundingEmptyNonBlockingTiles.isEmpty()) return;
            Location newLocation = surroundingEmptyNonBlockingTiles.get(rand.nextInt(surroundingEmptyNonBlockingTiles.size()));
            new Grass(world, newLocation);
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.green, "bush");
    }
}
