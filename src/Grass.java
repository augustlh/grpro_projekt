import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Grass extends Plant {

    public Grass() {
        super(0.15, 1.4);//chance to something else later
    }

    @Override
    public void act(World world) {spread(world);
    }

    @Override
    public void spread(World world) {
        if(this.neighbours == null) {
            this.neighbours = new ArrayList<>(world.getSurroundingTiles());
        }

        Random rand = new Random();
        if (rand.nextDouble() <= spreadProbability){
            List<Location> surroundingEmptyNonBlockingTiles = getSurroundingEmptyNonBlockingTiles(world);

            if(surroundingEmptyNonBlockingTiles.isEmpty()) return;
            Location newLocation = surroundingEmptyNonBlockingTiles.get(rand.nextInt(surroundingEmptyNonBlockingTiles.size()));
            world.setTile(newLocation, new Grass());
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.green, "grass");
    }

    private List<Location> getSurroundingEmptyNonBlockingTiles(World world) {
        List<Location> freeTiles = new ArrayList<>();
        for(Location location : this.neighbours) {
            if(!world.containsNonBlocking(location)) {
                freeTiles.add(location);
            }
        }
        return freeTiles;
    }

}
