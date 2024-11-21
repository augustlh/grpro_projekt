import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.List;

public abstract class Plant implements Eatable, Actor, NonBlocking, DynamicDisplayInformationProvider {
    protected List<Location> neighbours;
    protected double spreadProbability;
    protected double energy;

    abstract void spread(World world);

    public Plant(double spreadProbability, double energy) {
        this.spreadProbability = spreadProbability;
        this.energy = energy;
        this.neighbours = null;
    }

    @Override
    public boolean canEat(Eatable other) {
        return false;
    }

    @Override
    public double onEaten() {
        return this.energy;
    }

    @Override
    public Type getType() {
        return Type.PLANT;
    }
}