import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * The Grass class represents a type of plant with specific properties for spreading probability
 * and energy. It extends the Plant class and provides implementations for the spreading behavior
 * and display information.
 */
public class Grass extends Plant {

    /**
     * Constructs a new instance of the Grass class, representing a type of plant with specific
     * properties for spreading probability and energy. Invokes the parent class Plant's
     * constructor with default values for spread probability and energy.
     */
    public Grass() {
        super(0.15, 1.4);//chance to something else later
    }

    /**
     * Executes the action of the Grass class within the given World context. This method
     * triggers the spreading behavior of the Grass.
     *
     * @param world the World object in which the Grass will act and potentially spread.
     */
    @Override
    public void act(World world) {spread(world);
    }

    /**
     * Spreads the Grass object to a new location within the given World based on certain
     * probabilities and available surrounding tiles.
     *
     * @param world the World object in which the Grass will potentially spread to a new location.
     */
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

    /**
     * Provides display information for the Grass class, indicating its color and type.
     *
     * @return a DisplayInformation object containing the color green and the image key "grass".
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.green, "grass");
    }

    /**
     * Retrieves a list of surrounding non-blocking and empty tiles from the given world.
     *
     * @param world the World object in which the search for non-blocking and empty tiles is performed.
     * @return a list of Locations representing the surrounding non-blocking and empty tiles.
     */
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
