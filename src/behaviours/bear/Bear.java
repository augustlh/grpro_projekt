package behaviours.bear;

import datatypes.*;
import help.Utils;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

import java.awt.Color;
import java.util.Set;


public class Bear extends Carnivore {
    private final Set<Location> territory;
    private boolean hasActed;

    public Bear(World world, Location location) {
        super(Species.Bear, Utils.random.nextDouble(), Utils.random.nextDouble()*2, Utils.random.nextInt(1, 3), 75);
        this.territory = world.getSurroundingTiles(location, Utils.random.nextInt(3, 5));
        world.setTile(location, this);
        this.hasActed = false;
    }

    @Override
    protected void dayTimeBehaviour(World world) {
        // Failsafe hvis Bear somehow ender uden for territorie
        /*
        if(!this.territory.contains(world.getLocation(this)) && this.energy > this.maxEnergy / 2) {
            //Søg mod territorie
        }
         */
        System.out.println("ENERGY: " + this.energy + " P: " + this.energy/this.maxEnergy);
        hasActed = false;
        if(this.energy < this.maxEnergy / 1.3) {
            eat(world);
        }

        if (!hasActed) {
            hunt(world);
        }

        if (!hasActed) {
            wander(world);
        }

    }


//        for (Location loc : neighbours) {
//            if (world.getTile(loc) instanceof Organism o && territory.contains(loc)) {
//                if ((this.canEat(o))) {
//                    //System.out.println("Eating");
//                    this.consume(o, world);
//                    world.move(this, loc);
//                    hasActed = true;
//                    return;
//                }
//            }
//        }

    @Override
    protected void hunt(World world) {
        Location loc = Utils.closestConsumableEntity(world, this, searchRadius);
        if(loc != null && territory.contains(loc)) {
            if(world.getTile(loc) instanceof Organism o && o.canBeEaten()) {
                if(!(o instanceof Animal)) {
                    pursue(world, loc);
                    hasActed = true;
                } else if (this.energy < this.maxEnergy / 1.3) {
                    pursue(world, loc);
                    hasActed = true;
                }
            }
        }
    }

    private Set<Location> getValidEmptyLocationsWithinTerritory(World world){
        // Find the union of empty neighbours and the territory
        Set<Location> emptyNeighbours = world.getEmptySurroundingTiles();
        Set<Location> territoryNeighbours = new HashSet<>(territory);
        territoryNeighbours.retainAll(emptyNeighbours);
        //System.out.println(territoryNeighbours);
        return territoryNeighbours;
    }

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


    @Override
    protected void nightTimeBehaviour(World world) {
        // Sleepy time :)
    }

    /**
     * Retrieves display information for the bear, including its color and image key.
     *
     * @return a DisplayInformation object representing the rabbit with specific color and image key.
     */
    @Override
    public DisplayInformation getInformation() {
        if(this.isInfected()) {
            return new DisplayInformation(Color.WHITE, "mc-bear-large-infested");
        }

        if(age > 6) {
            return new DisplayInformation(Color.WHITE, "mc-bear-large");
        }
        return new DisplayInformation(Color.WHITE, "mc-bear-small");
    }

    @Override
    public void onConsume(World world) {
        super.onConsume(world);
    }
}
