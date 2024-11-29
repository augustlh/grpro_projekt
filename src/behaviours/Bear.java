package behaviours;

import datatypes.Animal;
import datatypes.Carnivore;
import datatypes.Organism;
import datatypes.Species;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.*;


public class Bear extends Carnivore {

    private HashSet<Location> territory;
    private final int territoryRange;

    public Bear(World world, Location location) {
        super(Species.Bear, new Random().nextDouble(), new Random().nextDouble(1, 2), 1);
        world.setTile(location, this);
        territoryRange = 2;
    }

    @Override
    public void act(World world) {
        // Stops act if dead
        age(world);
        if(isDead){
            return;
        }

        // Set's the bear's territory
        setTerritory(world);

        // Eat adjacent eatable organisms
        List<Location> neighbours = new ArrayList<>(world.getSurroundingTiles());
        for (Location loc : neighbours){
            Set<Location> territoryNeighbours = getValidEmptyLocationsWithinTerritory(world);
            if (world.getTile(loc) instanceof Organism o && territoryNeighbours.contains(loc)){
                if((this.canEat(o))) {
                    System.out.println("Eating");
                    this.consume(o,world);
                    world.move(this,loc);
                    return;
                }
            }
        }

        // Pursue food in territory
        ArrayList<Location> territoryList = new ArrayList<>(territory);
        for (Location loc : territoryList){
            if (world.getTile(loc) instanceof Organism o){
                if(this.canEat(o)){
                    System.out.println("Pursuing");
                    pursue(world, loc);
                    return;
                }
            }
        }

        // Wander randomly within territory
        wander(world);

    }

    private void age(World world){
        this.age ++;
        this.energy -= energyDecay;
        if (energy <=0 || age >=100){
            die();
            world.delete(this);
        }
    }

    private void setTerritory(World world){
        if(territory == null) {
            territory = new HashSet<>(world.getSurroundingTiles(territoryRange));
        }
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
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.YELLOW, "mc-bear-large");
    }



}
