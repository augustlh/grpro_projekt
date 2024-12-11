package behaviours.bear;

import datatypes.*;
import help.Utils;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

import java.awt.Color;
import java.util.Set;


/**
 * The Bear class represents a bear in the simulation, extending the Carnivore class.
 * Bears have a defined territory in which they operate and exhibit specific behaviors during the day and night.
 */
public class Bear extends Carnivore {
    private final Set<Location> territory;
    private boolean hasActed;
    private boolean isSleeping;

    /**
     * Constructs a new Bear in the specified world at the given location.
     * Calls super class to specify species, metabolism, energy decay, search radius and max energy,
     * which are defined in the {@link Animal Animal class}.
     * Sets the Bears territory with its spawn point as center and a radius between 3 and 5 tiles.
     * Specifies the Bear has not yet performed an action
     *
     * @param world the world in which the bear resides
     * @param location the Location within the world where the bear will be initially situated
     */
    public Bear(World world, Location location) {
        super(Species.Bear, Utils.random.nextDouble(), Utils.random.nextDouble()*2.5, Utils.random.nextInt(2, 4), 75);
        this.territory = world.getSurroundingTiles(location, Utils.random.nextInt(3, 5));
        world.setTile(location, this);
        this.hasActed = false;
    }

    /**
     * Defines the daytime behavior of a Bear within its territory.
     * The Bear first attempts to satisfy its energy requirements by eating.
     * If not successful and the Bear has not yet acted, it will try to hunt.
     * If hunting is also unsuccessful and no action has been performed,
     * the Bear will wander randomly within its territory.
     *
     * @param world the world in which the bear resides
     */
    @Override
    protected void dayTimeBehaviour(World world) {
        isSleeping = false;
        hasActed = false;
        if(this.energy < this.maxEnergy / 1.1) {
            eat(world);
        }

        if (!hasActed) {
            hunt(world);
        }

        if (!hasActed) {
            wander(world);
        }

    }

    /**
     * Defines the nighttime behavior of a Bear within the world.
     * The Bear does nothing during the night
     *
     * @param world the world in which the bear resides
     */
    @Override
    protected void nightTimeBehaviour(World world) {
        isSleeping = true;
    }

    /**
     * Executes the hunting behavior of the Bear within its defined territory.
     * The Bear searches for the closest entity that it can consume within its search radius.
     * If such an entity is found within its territory and can be eaten,
     * the Bear will pursue it under specific conditions.
     * The Bear prioritizes non-Animal organisms, but will also target Animals
     * if its energy level falls below a certain threshold.
     *
     * @param world the world in which the bear resides
     */
    @Override
    protected void hunt(World world) {
        Location loc = Utils.closestConsumableEntity(world, this, searchRadius);
        if(loc != null && territory.contains(loc)) {
            if(world.getTile(loc) instanceof Organism o && o.canBeEaten()) {
                if(!(o instanceof Animal)) {
                    pursue(world, loc);
                    hasActed = true;
                } else if (this.energy < this.maxEnergy / 1.1) {
                    pursue(world, loc);
                    hasActed = true;
                }
            }
        }
    }

    /**
     * Retrieves the set of valid empty locations within the bear's territory.
     * This method determines the intersection of the bear's territorial locations
     * and the surrounding empty tiles, providing a set of positions that are both
     * within the territory and currently unoccupied.
     *
     * @param world the world in which the bear resides.
     * @return a set of locations that are within the bear's territory and are empty.
     */
    private Set<Location> getValidEmptyLocationsWithinTerritory(World world){
        // Find the union of empty neighbours and the territory
        Set<Location> emptyNeighbours = world.getEmptySurroundingTiles();
        Set<Location> territoryNeighbours = new HashSet<>(territory);
        territoryNeighbours.retainAll(emptyNeighbours);
        return territoryNeighbours;
    }

    /**
     * Allows the bear to wander within its defined territory in the world.
     * The bear moves to a random empty neighboring location within its territory.
     * If there are no valid empty locations nearby, the bear will invoke
     * its default wandering behavior as defined in the {@link Animal Animal class}.
     * Marks the bear as having acted for the simulation step.
     *
     * @param world the world in which the bear is located
     */
    @Override
    protected void wander(World world) {
        Set<Location> territoryNeighbours = getValidEmptyLocationsWithinTerritory(world);

        //  Move the bear
        if(!territoryNeighbours.isEmpty()) {
            Location newLocation = (Location) territoryNeighbours.toArray()[new Random().nextInt(territoryNeighbours.size())];
            world.move(this, newLocation);
        }
        else {
            super.wander(world);
        }
        hasActed = true;
    }

    /**
     * Handles the event when the bear is consumed by another organism.
     * Calls the super class {@link Animal Animal's} onConsume method.
     *
     * @param world the world in which the bear resides
     */
    @Override
    public void onConsume(World world) {
        super.onConsume(world);
    }

    /**
     * Provides display information for the bear, including its color and image key.
     * Display information changes depending on the bear's age and whether
     * it's infested with a cordyceps.
     *
     * @return a DisplayInformation object representing the bear with specific color and image key.
     */
    @Override
    public DisplayInformation getInformation() {
        if(isSleeping) {
            return new DisplayInformation(Color.WHITE, "mc-bear-large-sleeping");
        }
        if(this.isInfected() && age > 6) {
            return new DisplayInformation(Color.WHITE, "mc-bear-large-infested");
        }
        if(this.isInfected() && age <= 6) {
            return new DisplayInformation(Color.WHITE, "mc-bear-small-infested");
        }
        if(!this.isInfested() && age > 6) {
            return new DisplayInformation(Color.WHITE, "mc-bear-large");
        }
        if(!this.isInfested() && age <= 6) {
            return new DisplayInformation(Color.WHITE, "mc-bear-small");
        }
        else {
            return new DisplayInformation(Color.WHITE, "mc-bear-large");
        }
    }

}
