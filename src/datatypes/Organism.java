package datatypes;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

/**
 * The Organism class represents a living entity and acts as the super class for all living entities.
 * The class contains information about the organism's species, age and whether it's dead or infested.
 * It provides the functionality for species to recognize which other organisms they can consume.
 * It implements the Consumable and Actor interfaces as the behavior defined in those classes
 * are required to accurately represent organisms.
 * it also implements the DynamicDisplayInformationProvider interface, in order for each sub-organism
 * to be able to change appearance based on certain conditions.
 */
public abstract class Organism implements Consumable, Actor, DynamicDisplayInformationProvider {
    protected final Species species;
    protected boolean isDead;
    protected int age;

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
     * @param other The other organism we wish to see if it can consumed by this organism.
     * @return true if this organism can consume the other organism, false otherwise
     */
    public boolean canEat(Organism other) {
        return (this.species.getDietMask() & other.getSpecies().getValue()) != 0 && other.canBeEaten();
    }

    /**
     * Determines if the organism is alive or dead.
     *
     * @return A boolean indicating whether the organism is alive.
     */
    public boolean isAlive() {
        return !this.isDead;
    }

    /**
     * Determines if this organism can be eaten by another organism.
     * Acts as a helping method for certain subclasses.
     *
     * @return true if the organism can be eaten, false otherwise.
     */
    public boolean canBeEaten() {
        return true;
    }
}
