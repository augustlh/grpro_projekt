import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Grass implements Spreadable, NonBlocking, DynamicDisplayInformationProvider {
    private final double spreadChance;
    private List<Location> surroundingTiles;
    private final Random rand;


    public Grass() {
       spreadChance = 0.15; // The change of a grass spreading each step of the simulation
       surroundingTiles = null;
       rand = new Random();
    }

    /**
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void spread(World world) {
        // If there is at least one empty tile around this object,
        // save a list of said surrounding tiles in variable 'surroundingTiles'.
        if(this.surroundingTiles == null) {
            this.surroundingTiles = new ArrayList<>(world.getSurroundingTiles());
        }

        // Get surrounding tiles that doesn't contain a non-blocking object
        if (rand.nextDouble() <= spreadChance){
            List<Location> surroundingEmptyNonBlockingTiles = getSurroundingEmptyNonBlockingTiles(world);

        // If all surrounding tiles already have a non-blocking object,
        // the grass can't spread and the method returns.
        if(surroundingEmptyNonBlockingTiles.isEmpty()) {
            return;
        }

        // Place the grass on a random surrounding tile
        Location newLocation = surroundingEmptyNonBlockingTiles.get(rand.nextInt(surroundingEmptyNonBlockingTiles.size()));
        world.setTile(newLocation, new Grass());
        }
    }

    @Override
    public void act(World world) {
        spread(world);
    }

    // Defines the appearance of this object
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.green, "grass");
    }

    // Returns a list of the surrounding tiles that doesn't contain a non-blocking object
    private List<Location> getSurroundingEmptyNonBlockingTiles(World world) {
        List<Location> freeTiles = new ArrayList<>();
        for(Location location : this.surroundingTiles) {
            if(!world.containsNonBlocking(location)) {
                freeTiles.add(location);
            }
        }
        return freeTiles;
    }

}
