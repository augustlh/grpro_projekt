package behaviours;

import datatypes.Animal;
import help.Utils;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;
import java.util.Set;

/**
 * The Cordyceps class represents a parasitic entity that infects an Animal host
 * and manipulates its behavior. This class provides behavior for how the parasite acts,
 * how it spreads upon the death of its host.
 */
public class Cordyceps {
    private Animal host;

    /**
     * Executes the behavior of the parasitic entity within the given world.
     * The method defines how the parasite manipulates its host, determines where the host should move,
     * and interacts with surrounding entities based on the state of the world.
     *
     * @param world The world in which the cordyceps resides.
     */
    public void act(World world) {
        if(this.host == null) return;

        this.host.devour();

        if(!world.isOnTile(this.host)) {
            return;
        }

        Location target = Utils.closestEqualAnimal(this.host, world, this.host.getSpecies(), world.getLocation(this.host));
        if(target == null) {
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


    /**
     * Handles the behavior of the parasite when its host dies.
     * The method attempts to infect a new host by searching for nearby animals within
     * a specified range around the death location.
     *
     * @param world The world in which the cordyceps resides.
     * @param loc   The location where the host has died and the infection should attempt to spread.
     */
    public void onHostDeath(World world, Location loc) {
        Set<Location> s = world.getSurroundingTiles(loc, 3);
        for(Location location : s) {
            this.host = null;
            if(world.getTile(location) instanceof Animal a) {
                a.infect(this);
                return;
            }
        }
    }

    /**
     * Sets a new host for the parasitic entity when infection occurs.
     *
     * @param animal The animal that becomes the new host of the parasite.
     */
    public void onInfect(Animal animal) {
        this.host = animal;
    }

}
