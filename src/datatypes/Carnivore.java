package datatypes;

import help.Utils;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Carnivore extends Animal {

    public Carnivore (Species species, double metabolism, double energyDecay, int searchRadius) {
        super(species, metabolism, energyDecay, searchRadius);
    }

    // Hunt and eat other animals method
    // Eat adjacent eatable organisms
    protected void eat(World world) {
        List<Location> neighbours = new ArrayList<>(world.getSurroundingTiles());
        for (Location loc : neighbours) {
            if (world.getTile(loc) instanceof Organism o) {
                if ((this.canEat(o))) {
                    System.out.println("Eating");
                    this.consume(o, world);
                    world.move(this, loc);
                    return;
                }
            }
        }
    }

    protected void hunt(World world) {
        // Pursue nearest eatable organism
        Location loc = Utils.closestConsumableEntity(world, this, searchRadius);
        if(loc != null) pursue(world, loc);
    }

}
