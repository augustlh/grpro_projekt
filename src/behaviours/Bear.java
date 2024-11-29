package behaviours;

import datatypes.Carnivore;
import datatypes.Organism;
import datatypes.Species;
import help.Utils;
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
        this.energy = 30;
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

        // Eats adjacent food if within territory
        eat(world);

        // Pursue food in territory
        hunt(world);

        // Wander randomly within territory
        wander(world);

    }

    private void setTerritory(World world){
        if(territory == null) {
            territory = new HashSet<>(world.getSurroundingTiles(territoryRange));
        }
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
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.YELLOW, "mc-bear-large");
    }



}
