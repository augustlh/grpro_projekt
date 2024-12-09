package datatypes;

import itumulator.world.World;

/**
 * Herbivore is an abstract class that extends the Animal class,
 * representing animals that feed on plant material.
 * It provides the functionality for herbivores to
 * eat certain plants.
 */
public abstract class Herbivore extends Animal {

    /**
     * Constructs a new Herbivore with the specified species, metabolism rate, energy decay rate,
     * search radius, and maximum energy capacity.
     *
     * @param species      the species of the herbivore
     * @param metabolism   the rate at which the herbivore converts food to energy
     * @param energyDecay  the rate at which the herbivore's energy decreases over time
     * @param searchRadius the radius within which the herbivore searches for edible plants
     * @param maxEnergy    the maximum energy capacity of the herbivore
     */
    public Herbivore(Species species, double metabolism, double energyDecay, int searchRadius, double maxEnergy) {
        super(species, metabolism, energyDecay, searchRadius, maxEnergy);
    }

    /**
     * Determines whether the herbivore is standing on an edible organism and consumes it if possible.
     *
     * @param world The world in which the herbivore resides.
     */
    protected void eat(World world) {
        // If stands on eatable organism, eat it
        if (world.getNonBlocking(world.getLocation(this)) instanceof Organism o) {
            if(canEat(o)) {
                this.consume(o, world);
            }
        }
    }
}
