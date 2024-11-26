import itumulator.world.NonBlocking;
import itumulator.world.World;

public abstract class Plant extends Organism implements NonBlocking {
    protected double spreadProbability;
    protected double nutritionalValue;

    public Plant(double spreadProbability, double nutritionalValue) {
        super(Species.Plant);
        this.spreadProbability = spreadProbability;
        this.nutritionalValue = nutritionalValue;
    }

    abstract void spread(World world);
    @Override
    public void onConsume(World world) {
        world.delete(this);
    }
    @Override
    public double getNutritionalValue() {
        return this.nutritionalValue;
    }
}
