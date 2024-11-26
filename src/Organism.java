import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

public abstract class Organism implements Consumable, Actor, DynamicDisplayInformationProvider {
    protected final Species species;
    protected boolean isDead;

    public Organism(Species species) {
        this.species = species;
    }

    public Species getSpecies() {
        return species;
    }

    public void die() {
        this.isDead = true;
    }

    protected boolean canEat(Organism other) {
        return (this.species.getDietMask() & other.getSpecies().getValue()) != 0;
    }

}
