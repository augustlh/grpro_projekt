package behaviours;

import datatypes.Animal;
import help.Utils;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;
import java.util.Set;

public class Cordyceps {
    private Animal host;

    public void act(World world) {
        if(this.host == null) return;

        this.host.devour();

        if(!world.isOnTile(this.host)) {
            return;
        }

        Location target = Utils.closestEqualAnimal(this.host, world, this.host.getSpecies(), world.getLocation(this.host));

        if(target == null) {
            //System.out.println("No target");
            Set<Location> emptyNeighbours = world.getEmptySurroundingTiles(world.getLocation(this.host));
            if(emptyNeighbours.isEmpty()) {
                return;
            }

            Location newLocation = (Location) emptyNeighbours.toArray()[new Random().nextInt(emptyNeighbours.size())];
            world.move(host, newLocation);
        } else {
            this.host.moveTowards(world, target);
        }
    }

    public void onHostDeath(World world, Location loc) {
        Set<Location> s = world.getSurroundingTiles(loc, 3);
        for(Location location : s) {
            this.host = null;
            if(world.getTile(location) instanceof Animal a) {
                a.infect(this);
                return;
            }
        }

        //System.out.println("Cordyceps died, sad moment!");
    }

    public void onInfect(Animal animal) {
        this.host = animal;
    }

}
