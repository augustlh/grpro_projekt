import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

abstract class Animal implements Eatable, Actor, DynamicDisplayInformationProvider {
    protected int age;
    protected double energy;
    protected double metabolism;
    protected double energyDecay;

    public Animal(double metabolism, double energyDecay) {
        this.metabolism = metabolism;
        this.energyDecay = energyDecay;
    }

    abstract void eat(Eatable other);

    @Override
    public Type getType() {
        return Type.ANIMAL;
    }
}