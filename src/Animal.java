import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class Animal implements Eatable, Actor, DynamicDisplayInformationProvider {
    protected int age;
    protected double energy;
    protected double metabolism;
    protected double energyDecay;

    protected List<Location> pathToTarget;

    protected Location location;

    public Animal(double metabolism, double energyDecay) {
        this.metabolism = metabolism;
        this.energyDecay = energyDecay;
        pathToTarget = null;
    }

    /**
     * Returns whether or not this entity can eat another eatable entity.
     * @param other The other eatable entity
     * @return boolean, false or true, indicating whether it could eat or not.
     */
    abstract boolean canEat(Eatable other);

    /**
     * Makes the animal wander within the world. The animal will look for a nearby food source and move towards it,
     * otherwise it will move to a random empty neighboring tile.
     *
     * @param world the simulation world where the animal exists, used to check surroundings and perform movements.
     */
    protected void wander(World world) {
        Set<Location> emptyNeighbours = world.getEmptySurroundingTiles();
        Location newLocation = null;
        if(emptyNeighbours.isEmpty()) {
            return;
        }

        // If food is nearby, move to food
        for (Location location : emptyNeighbours) {
            if (world.getNonBlocking(location) instanceof Grass) {
                world.move(this, location);
                this.location = location;
                return;
            }
        }

        // Move randomly
        newLocation = (Location) emptyNeighbours.toArray()[new Random().nextInt(emptyNeighbours.size())];
        world.move(this, newLocation);
        this.location = newLocation;
    }


    /**
     * Navigate the animal towards a target location within the world. If a valid path does not exist, it will compute a new path.
     * Once a valid path exists, it moves the animal step by step along the path towards the target. If the tile on the path
     * is not empty, it recomputes a new path.
     *
     * @param world the simulation world in which the animal exists and moves
     * @param target the target location to which the animal is attempting to navigate
     */
    protected void pursue(World world, Location target) {
        if(this.pathToTarget == null || this.pathToTarget.isEmpty()) {
            System.out.println("new path lol :)");
            this.pathToTarget = Utils.pathToTarget(world, world.getLocation(this), target);
        } else if (!world.isTileEmpty(pathToTarget.getFirst())) {
            System.out.println("new path lol");
            this.pathToTarget = Utils.pathToTarget(world, world.getLocation(this), target);

        }

        if(!this.pathToTarget.isEmpty()) {
            world.move(this, this.pathToTarget.getFirst());
            this.pathToTarget.removeFirst();
            return;
        }

        wander(world);

    }

    /**
     * Defines the action of eating another Eatable entity in a given world context.
     *
     * @param other the Eatable entity to be eaten.
     * @param world the simulation world where the eating action takes place.
     */
    abstract void eat(Eatable other, World world);

    public void onEaten(World world) {
        world.delete(this);
    }

    @Override
    public Type getType() {
        return Type.ANIMAL;
    }

    public Location getLocation() {return location;} // Location not updated on act

    public void setLocation(Location location) {this.location = location;} // Location not updated on act
}