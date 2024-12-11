package datatypes;

import help.Utils;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The Carnivore class represents an abstract type of animal that
 * feeds on other animals. It extends the Animal class and
 * adds behaviors specific to carnivores such as hunting, killing and
 * eating other animals.
 */
public abstract class Carnivore extends Animal {

    /**
     * Constructs a new Carnivore with the specified species, metabolism, energy decay, search radius, and maximum energy.
     *
     * @param species       the species of the carnivore
     * @param metabolism    the rate at which the carnivore metabolizes energy
     * @param energyDecay   the rate at which the carnivore's energy decreases over time
     * @param searchRadius  the radius in which the carnivore searches for prey
     * @param maxEnergy     the maximum energy level of the carnivore
     */
    public Carnivore (Species species, double metabolism, double energyDecay, int searchRadius, double maxEnergy) {
        super(species, metabolism, energyDecay, searchRadius, maxEnergy);
    }

    /**
     * Perform the action of killing another animal.
     * Triggers the consumption behavior of the targeted animal.
     *
     * @param world the world in which the carnivore resides
     * @param other the other animal that is being killed by the carnivore
     */
    protected void kill(World world, Animal other) {
        //System.out.print("Killed");
        other.onConsume(world);
    }

    /**
     * Attempts to consume organisms located on the surrounding tiles within the world.
     * The method first checks the neighboring tiles to identify if they contain organisms
     * the carnivore can eat. If it can, the organism is killed and a carcass takes its place.
     *
     * @param world the world in which the carnivore resides.
     */
    protected void eat(World world) {
        //System.out.println("eat call");
        List<Location> neighbours = new ArrayList<>(world.getSurroundingTiles(world.getLocation(this)));

        for (Location location : neighbours) {
            if (world.getTile(location) instanceof Organism organism) {
                //System.out.println("organism moment");
                if (this.canEat(organism)) {
                    //System.out.println("can eat moment");
                    if (organism instanceof Animal) {
                        //System.out.println("Should eat animal");
                        //hasActed stuff
                        this.kill(world, (Animal) organism);
                        return;
                    }
                    this.consume(organism, world);
                    //System.out.println("eat done");
                }
            }
        }
    }

    /**
     * Performs the hunting behavior of the Carnivore. The Carnivore attempts to find
     * and pursue the nearest consumable entity within a specified search radius.
     * If a consumable entity is found, it is pursued. If no such entity is found,
     * the Carnivore wanders randomly.
     *
     * @param world the world in which the carnivore resides.
     */
    protected void hunt(World world) {
        // Pursue nearest eatable organism
        Location loc = Utils.closestConsumableEntity(world, this, searchRadius*2);
        if(loc != null){
            pursue(world, loc);
        } else {
            wander(world);
        }
    }

}
