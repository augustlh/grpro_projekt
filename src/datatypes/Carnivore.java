package datatypes;

import help.Utils;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class Carnivore extends Animal {

    public Carnivore (Species species, double metabolism, double energyDecay, int searchRadius, double maxEnergy) {
        super(species, metabolism, energyDecay, searchRadius, maxEnergy);
    }

    protected void kill(World world, Animal other) {
        System.out.print("Killed your mom, Frederik");
        other.onConsume(world);
    }

    // Hunt and eat other animals method
    // Eat adjacent eatable organisms
    protected void eat(World world) {
        System.out.println("eat call");
        List<Location> neighbours = new ArrayList<>(world.getSurroundingTiles());

        for (Location location : neighbours) {
            if (world.getTile(location) instanceof Organism organism) {
                System.out.println("organism moment");
                if (this.canEat(organism)) {
                    System.out.println("can eat moment");
                    if (organism instanceof Animal) {
                        System.out.println("Should eat animal");
                        //hasacted bs
                        this.kill(world, (Animal) organism);
                        return;
                    }
                    this.consume(organism, world);
                    System.out.println("eat done");
                }
            }
        }
    }

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
