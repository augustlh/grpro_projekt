package behaviours.bear;

import datatypes.Carnivore;
import datatypes.Organism;
import datatypes.Species;
import help.Utils;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

import java.awt.Color;
import java.util.Set;


public class Bear extends Carnivore {
    private Set<Location> territory;

    public Bear(World world, Location location) {
        super(Species.Bear, Utils.random.nextDouble(), Utils.random.nextDouble(), Utils.random.nextInt(1, 3), 75);
        this.territory = world.getSurroundingTiles(location, Utils.random.nextInt(3, 5));
        world.setTile(location, this);
    }

    @Override
    protected void dayTimeBehaviour(World world) {
        //Hvis den er virkelig sulsten, så skal den nok søge langt?

        //bare tanker?
//        if(!this.territory.contains(world.getLocation(this)) && this.energy > this.maxEnergy / 2) {
//            //Søg mod territorie
//            System.out.println("mad");
//        } else if(this.energy < this.maxEnergy / 2) {
//            //Søg mad på hele mappet (lidt snyd, I know);
//        }

        eat(world);
        hunt(world);
        wander(world);
    }

    @Override
    protected void eat(World world) {
        List<Location> neighbours = new ArrayList<>(world.getSurroundingTiles());
        for (Location loc : neighbours) {
            Set<Location> territoryNeighbours = getValidEmptyLocationsWithinTerritory(world);
            if (world.getTile(loc) instanceof Organism o && territoryNeighbours.contains(loc)) {
                if ((this.canEat(o))) {
                    System.out.println("Eating");
                    this.consume(o, world);
                    world.move(this, loc);
                    return;
                }
            }
        }
    }

    @Override
    protected void hunt(World world) {
        Location loc = Utils.closestConsumableEntity(world, this, searchRadius);
        if(loc != null && territory.contains(loc)) pursue(world, loc);
    }

    private Set<Location> getValidEmptyLocationsWithinTerritory(World world){
        // Find the union of empty neighbours and the territory
        Set<Location> emptyNeighbours = world.getEmptySurroundingTiles();
        Set<Location> territoryNeighbours = new HashSet<>(territory);
        territoryNeighbours.retainAll(emptyNeighbours);
        return territoryNeighbours;
    }

    @Override
    protected void wander(World world) {
        Set<Location> territoryNeighbours = getValidEmptyLocationsWithinTerritory(world);

        //  Move the bear
        Location newLocation = null;
        if(!territoryNeighbours.isEmpty()) {
            newLocation = (Location) territoryNeighbours.toArray()[new Random().nextInt(territoryNeighbours.size())];
            world.move(this, newLocation);
        }
        else {
            super.wander(world);
        }
    }


    @Override
    protected void nightTimeBehaviour(World world) {
        //Her skal den blot sove eller w/e :)
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.YELLOW, "mc-bear-large");
    }
}
