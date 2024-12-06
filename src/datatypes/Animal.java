package datatypes;

import behaviours.Carcass;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.Random;
import java.util.Set;

/**
 * The Animal class represents a generic organism with characteristics such as age, energy, metabolism,
 * energy decay, and a search radius. It provides methods for consuming other organisms, wandering,
 * and pursuing targets in a given world.
 */
public abstract class Animal extends Organism {
    protected double energy;
    protected double maxEnergy;
    protected double metabolism;
    protected double energyDecay;
    protected int searchRadius;

    /**
     * Constructs a new Animal with the specified species, metabolism, energy decay, and search radius.
     *
     * @param species      the species of the animal
     * @param metabolism   the rate at which the animal metabolizes energy
     * @param energyDecay  the rate at which the animal's energy decreases over time
     * @param searchRadius the radius in which the animal searches for food or other entities
     */
    public Animal(Species species, double metabolism, double energyDecay, int searchRadius, double maxEnergy) {
        super(species);
        this.age = 0;
        this.metabolism = metabolism;
        this.energyDecay = energyDecay;
        this.searchRadius = searchRadius;
        this.maxEnergy = maxEnergy;
        this.energy = this.maxEnergy;
    }

    abstract protected void dayTimeBehaviour(World world);
    abstract protected void nightTimeBehaviour(World world);

    /**
     * Executes the actions of an animal within the given world, including aging,
     * and performing appropriate behavior based on the time of day.
     *
     * @param world The world in which the rabbit behaves.
     */
    @Override
    public void act(World world) {
        if(this.isDead) {
            return;
        }

        if(world.isDay()) {
            dayTimeBehaviour(world);
        } else if(world.isNight()) {
            nightTimeBehaviour(world);
        }

        this.age = this.age + 1;
        this.energy = this.energy - this.energyDecay;

        if (this.energy <= 0 || this.age >= 100){
            die();
            onConsume(world);
        }
    }


    /**
     * Calculates the nutritional value of the animal based on its current energy level
     * and its age, where the contribution of age is capped at 6.
     *
     * @return the nutritional value as a double, computed by multiplying the animal's energy
     *         by the ratio of its age (capped at 6) to 6.
     */
    @Override
    public double getNutritionalValue() {
        return this.energy * (Math.min(this.age, 6) / 6.0);
    }

    /**
     * Consumes another organism, increasing this organism's energy by the nutritional value of the other organism
     * and triggering the consumption effects on the other organism.
     *
     * @param other the organism to be consumed
     * @param world the world in which the consumption takes place, used to handle effects of the consumption
     */
    public void consume(Organism other, World world) {
        this.energy += other.getNutritionalValue() * this.metabolism;
        other.onConsume(world);
    }

//    /**
//     * Handles the event when the animal is consumed by another organism.
//     * This method will delete the current instance of the animal from the world.
//     *
//     * @param world the world in which the animal exists
//     */
//    @Override
//    public void onConsume(World world) {
//        Location temp = world.getLocation(this);
//        world.delete(this);
//
//        if(temp != null) {
//            new Carcass(world, temp, this.energy);
//        }
//    }

    /**
     * Causes the animal to wander to a random empty neighboring tile in the given world.
     * The method selects an empty neighboring tile at random and moves the animal to
     * that location if such a tile exists.
     *
     * @param world the world in which the animal is wandering
     */
    protected void wander(World world) {
        Set<Location> emptyNeighbours = world.getEmptySurroundingTiles();
        Location newLocation = null;
        if(emptyNeighbours.isEmpty()) {
            return;
        }

        newLocation = (Location) emptyNeighbours.toArray()[new Random().nextInt(emptyNeighbours.size())];
        world.move(this, newLocation);
    }

    /**
     * Causes the animal to pursue a specified location within the given world.
     * The animal will move one tile closer to the target location if the tile is empty.
     *
     * @param world the world in which the animal is pursuing its target
     * @param location the target location the animal is trying to move towards
     */
    protected void pursue(World world, Location location) {
        Location currentLocation = world.getLocation(this);

        int x = currentLocation.getX();
        int y = currentLocation.getY();

        if(x < location.getX()) {
            x++;
        } else if(x > location.getX()) {
            x--;
        }

        if(y < location.getY()) {
            y++;
        } else if(y > location.getY()){
            y--;
        }

        Location newLocation = new Location(x, y);
        if(world.isTileEmpty(newLocation)) {
            world.move(this, newLocation);
        }
    }
}