package datatypes;

import itumulator.world.World;

public abstract class Herbivore extends Animal {

    public Herbivore(Species species, double metabolism, double energyDecay, int searchRadius) {
        super(species, metabolism, energyDecay, searchRadius);
    }

    /**
     * Determines whether the rabbit is standing on an edible organism and consumes it if possible.
     *
     * @param world The world in which the rabbit resides and interacts with the environment.
     */
    protected void eat(World world) {
        // If stands on eatable organism, eat it
        if (world.getNonBlocking(world.getLocation(this)) instanceof Organism o) {
            if(canEat(o)) {
                this.consume(o, world);
            }
        }
    }
}
