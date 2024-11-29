package behaviours;

import datatypes.Plant;
import datatypes.Species;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import help.Utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Grass class represents a type of Plant that can spread over time to new locations
 * within a World. Grass instances have a specific spread probability and nutritional value.
 */
public class Grass extends Plant {
    List<Location> neighbours;

    /**
     * Constructs a new Grass object at the specified location in the given world.
     * Initializes the spread probability and nutritional value.
     * Sets the tile at the given location to this Grass instance.
     *
     * @param world the world where the Grass instance will be placed
     * @param location the specific location within the world where the Grass instance will be set
     */
    public Grass(World world, Location location) {
        super(Species.Grass,.125, 1.2);
        world.setTile(location, this);
        neighbours = null;
    }

    /**
     * Act method is called to perform the Grass-specific action in the given world.
     * In this method, the Grass attempts to spread to a new location.
     *
     * @param world the world in which the Grass instance exists and where
     *              it will attempt to spread
     */
    @Override
    public void act(World world) {
        spread(world);
    }

    /**
     * Attempts to spread this Grass instance to a new location within the given world.
     * If the neighbours list is uninitialized, it retrieves the surrounding tiles.
     * If a random chance meets the predefined spread probability, it selects a random
     * empty non-blocking tile from the surrounding tiles and creates a new Grass instance there.
     *
     * @param world the world in which the Grass instance exists and where it will attempt to spread
     */
    @Override
    public void spread(World world) {
        if(this.neighbours == null) {
            this.neighbours = new ArrayList<>(world.getSurroundingTiles());
        }

        Random rand = new Random();
        if (rand.nextDouble() <= getSpreadProbability()){
            List<Location> surroundingEmptyNonBlockingTiles = Utils.getSurroundingEmptyNonBlockingTiles(world, neighbours);

            if(surroundingEmptyNonBlockingTiles.isEmpty()) return;
            Location newLocation = surroundingEmptyNonBlockingTiles.get(rand.nextInt(surroundingEmptyNonBlockingTiles.size()));
            new Grass(world, newLocation);
        }
    }

    /**
     * Provides display information for the Grass instance.
     *
     * @return DisplayInformation object containing the color and imageKey specific to Grass.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.green, "mc-grass");
    }
}
