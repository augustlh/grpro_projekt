package datatypes;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

/**
 * Represents a generic organism within a simulation, characterized by its species and its ability to be consumed, act, and provide dynamic display information.
 * This class provides basic functionalities for all organisms, such as determining species, mortality, and dietary rules.
 */
public abstract class Organism implements Consumable, Actor, DynamicDisplayInformationProvider {
    protected final Species species;
    protected boolean isDead;

    /**
     * Constructs a new Organism with the specified species.
     *
     * @param species the species of the organism
     */
    public Organism(Species species) {
        this.species = species;
    }

    /**
     * Retrieves the species of this organism.
     * @return the species of the organism.
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Marks this organism as dead by setting its 'isDead' flag to true.
     * This method should be invoked to update the organism's state within the simulation to reflect its demise.
     */
    protected void die() {
        this.isDead = true;
    }

    /**
     * Determines whether this organism can consume another specified organism.
     *
     * @param other the organism to check if it can be consumed
     * @return true if this organism can consume the other organism, false otherwise
     */
    public boolean canEat(Organism other) {
        return (this.species.getDietMask() & other.getSpecies().getValue()) != 0;
    }

}
