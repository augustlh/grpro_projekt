package behaviours.plants;

import datatypes.Plant;
import datatypes.Species;
import help.Utils;
import itumulator.executable.DisplayInformation;
import itumulator.world.World;
import itumulator.world.Location;

import java.awt.Color;

/**
 * Represents a Bush, a specific type of plant that can grow berries.
 * The Bush has a limited capacity for berry growth and can be consumed by other organisms
 * in the world.
 */
public class Bush extends Plant {
    private int berryCount;

    /**
     * Constructs a new Bush object with the specified world and location.
     * The bush is initialized with species type BerryBush, and with default growth
     * values. The initial berry count for the bush is set to zero.
     *
     * @param world the world in which the bush resides.
     * @param location the location where this bush is planted.
     */
    public Bush(World world, Location location) {
        super(Species.BerryBush,.25, 1.5);
        world.setTile(location, this);
        berryCount = 0;
    }

    /**
     * Executes the behavior of the Bush.
     * This involves triggering its growth logic.
     *
     * @param world the world in which the bush exists.
     */
    @Override
    public void act(World world) {
        grow(world);
    }

    /**
     * Controls the growth behavior of a Bush.
     * The Bush attempts to grow berries up to its maximum capacity,
     * based on its spread probability.
     *
     * @param world The world in which the bush resides.
     */
    @Override
    public void grow(World world) {
        super.grow(world);

        if(berryCount < 4){
            if(Utils.random.nextDouble() <= this.spreadProbability){
                berryCount++;
            }
        }
    }

    /**
     * Resets the berry count of the bush to zero when the bush is consumed
     * by another organism.
     *
     * @param world The world in which the bush resides.
     */
    @Override
    public void onConsume(World world) {
        berryCount = 0;
    }

    /**
     * Determines if the Bush can currently be eaten.
     * The Bush is considered edible if it has one or more berries.
     *
     * @return true if the berry count is greater than zero, false otherwise.
     */
    @Override
    public boolean canBeEaten() {
        return berryCount > 0;
    }

    /**
     * Calculates the total nutritional value of the Bush based on the number of berries it has.
     *
     * @return the total nutritional value.
     */
    @Override
    public double getNutritionalValue() {
        return this.nutritionalValue * berryCount;
    }

    /**
     * Retrieves the current count of berries on the bush.
     *
     * @return the number of berries on the bush.
     */
    public int getBerryCount() {
        return berryCount;
    }

    /**
     * Provides display information for the bush, including its color and image key.
     * Display information changes depending on the bush's current number of berries.
     *
     * @return a DisplayInformation object representing the bush with specific color and image key.
     */
    @Override
    public DisplayInformation getInformation() {
        if(berryCount > 3) {
            return new DisplayInformation(Color.green, "mc-bush-many");
        }
        if(berryCount > 0) {
            return new DisplayInformation(Color.green, "mc-bush-few");
        }
        return new DisplayInformation(Color.magenta, "mc-bush");
    }

}
