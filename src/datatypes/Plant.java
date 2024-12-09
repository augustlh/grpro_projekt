package datatypes;

import itumulator.world.NonBlocking;
import itumulator.world.World;

/**
 * Represents an abstract plant organism.
 * Plants have a probability of spreading to new locations and a defined nutritional value.
 * This class provides basic functionalities for plant-specific behaviors such as spreading,
 * being consumed, and providing nutritional value.
 */
public abstract class Plant extends Organism implements NonBlocking {
    protected double spreadProbability;
    protected double nutritionalValue;

    /**
     * Constructs a new Plant with the specified spread probability and nutritional value.
     *
     * @param spreadProbability the probability that this plant will spread to a new location
     * @param nutritionalValue the nutritional value of this plant when consumed
     */
    public Plant(Species species, double spreadProbability, double nutritionalValue) {
        super(species);
        this.spreadProbability = spreadProbability;
        this.nutritionalValue = nutritionalValue;
    }

    /**
     * Defines the behavior of a plant growing within the simulation world.
     *
     * @param world the simulation world where the plant is attempting to grow
     */
    protected void grow(World world) {
        if(world.isNight()) {
            return;
        }
    }

    /**
     * Defines the behavior of the plant when it is consumed within the simulation world.
     *
     * @param world the simulation world where the plant is consumed
     */
    @Override
    public void onConsume(World world) {
        world.delete(this);
    }

    /**
     * Retrieves the nutritional value of the plant.
     *
     * @return the nutritional value of this plant.
     */
    @Override
    public double getNutritionalValue() {
        return this.nutritionalValue;
    }


    /**
     * Sets the probability for the plant to spread to a new location.
     * This method is primarily intended for testing purposes to modify
     * the spread probability of a plant instance.
     *
     * @param spreadProbability the new spread probability value to set
     */
    public void setSpreadProbability(double spreadProbability) {
        this.spreadProbability = spreadProbability;
    }
}
