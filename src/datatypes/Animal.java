package datatypes;

import behaviours.Carcass;
import behaviours.Cordyceps;
import help.Utils;
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
    protected Cordyceps cordyceps;

    /**
     * Constructs a new Animal with the specified species, metabolism, energy decay, search radius, and maximum energy.
     * There's a chance the animal becomes infested with a cordyceps.
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
        this.cordyceps = null;

    }

    /**
     * Handles constructing animals specified to be infested with a cordyceps.
     * Constructs a new Animal with the specified species, metabolism, energy decay, search radius, maximum energy and cordyceps.
     *
     * @param species      the species of the animal
     * @param metabolism   the rate at which the animal metabolizes energy
     * @param energyDecay  the rate at which the animal's energy decreases over time
     * @param searchRadius the radius in which the animal searches for food or other entities
     * @param cordyceps    the cordyceps it is infested by
     */
    public Animal(Species species, double metabolism, double energyDecay, int searchRadius, double maxEnergy, Cordyceps cordyceps) {
        super(species);
        this.age = 0;
        this.metabolism = metabolism;
        this.energyDecay = energyDecay;
        this.searchRadius = searchRadius;
        this.maxEnergy = maxEnergy;
        this.energy = this.maxEnergy;
        this.cordyceps = cordyceps;
        this.cordyceps.onInfect(this);
    }

    /**
     * Defines the behavior an animal exhibits during daytime within the given world.
     * This method is intended to be implemented by subclasses to determine
     * species-specific day time actions.
     *
     * @param world the world in which the animal resides
     */
    abstract protected void dayTimeBehaviour(World world);

    /**
     * Defines the behavior an animal exhibits during nighttime within the given world.
     * This method is intended to be implemented by subclasses to determine
     * species-specific day time actions.
     *
     * @param world the world in which the animal resides
     */
    abstract protected void nightTimeBehaviour(World world);

    /**
     * Performs the actions of an animal within the world, including aging,
     * and performing appropriate behavior based on the time of day.
     *
     * @param world the world in which the animal resides
     */
    @Override
    public void act(World world) {
        if(this.isDead) {
            return;
        }

        if(this.isInfected()) {
            age(world);
            this.cordyceps.act(world);
            return;
        }

        if(world.isDay()) {
            dayTimeBehaviour(world);
        } else if(world.isNight()) {
            nightTimeBehaviour(world);
        }

        age(world);
    }

    /**
     * Ages the animal by increasing its age and decreasing its energy.
     * If the animal's energy falls to zero or below, or its age reaches
     * a certain point, it triggers the animal's death.
     *
     * @param world the world in which the animal resides
     */
    private void age(World world) {
        this.age = this.age + 1;
        if (age%5==0){
            this.maxEnergy--;
            if(maxEnergy<energy)energy = this.maxEnergy;
        }
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
     * @return the nutritional value as a double, calculated by multiplying the animal's energy
     *         by the ratio of its age.
     */
    @Override
    public double getNutritionalValue() {
        return this.energy * (Math.min(this.age, 6) / 6.0);
    }

    /**
     * Consumes another organism, increasing this animal's energy by the nutritional value of the other organism
     * and triggering the consumption effects on the other organism.
     *
     * @param other the organism to be consumed
     * @param world the world in which the animal resides
     */
    public void consume(Organism other, World world) {
        this.energy += other.getNutritionalValue() * this.metabolism;
        other.onConsume(world);
    }

    /**
     * Handles the event when the animal is consumed by another organism.
     * This method will delete the current instance of the animal from the world
     * and construct a new carcass on its former location.
     * If the animal was infested with a cordyceps, it instead passes the
     * cordyceps onto the organism that consumed it.
     *
     * @param world the world in which the animal resides
     */
    @Override
    public void onConsume(World world) {
        if(!world.isOnTile(this)) {
            world.delete(this);
        } else {
            Location temp = world.getLocation(this);
            world.delete(this);

            if(!this.isInfected()) {
                if(temp != null) {
                    new Carcass(world, temp, this.energy);
                }
            } else {
                this.cordyceps.onHostDeath(world, temp);
            }
        }
    }

    /**
     * Causes the animal to wander to a random empty neighboring tile in the given world.
     * The method selects an empty neighboring tile at random and moves the animal to
     * that location if such a tile exists. If all surrounding tiles are already occupied
     * the animal does not move.
     *
     * @param world the world in which the animal is wandering
     */
    protected void wander(World world) {
        Set<Location> emptyNeighbours = world.getEmptySurroundingTiles();
        if(emptyNeighbours.isEmpty()) {
            return;
        }

        Location newLocation = (Location) emptyNeighbours.toArray()[new Random().nextInt(emptyNeighbours.size())];
        world.move(this, newLocation);
    }

    /**
     * Causes the animal to pursue a specified location within the given world.
     * The animal will move one tile closer to the target location if the tile is empty.
     *
     * @param world the world in which the animal resides
     * @param location the target location the animal is trying to move towards
     */
    public void pursue(World world, Location location) {
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

    /**
     * Determines whether the animal is currently infested with a cordyceps.
     *
     * @return true if the animal is infected with a cordyceps, false otherwise.
     */
    public boolean isInfected() {
        return this.cordyceps != null;
    }

    public void infect(Cordyceps cordyceps) {
        if(this.cordyceps == null) {
            this.cordyceps = cordyceps;
            cordyceps.onInfect(this);
        }
    }

    /**
     * Reduces the energy level of the animal by 1.5 times the energy decay rate.
     * Used by cordyceps to gradually eat away at its host
     */
    public void devour() {
        this.energy -= this.energyDecay * 1.5;
    }

    /**
     * Causes the animal to move towards a specified target location within the given world.
     * This method utilizes the pursue method to adjust the animal's position.
     *
     * @param world the world in which the animal resides
     * @param target the target location the animal is trying to move towards
     */
    public void moveTowards(World world, Location target) {
        pursue(world, target);
    }

}