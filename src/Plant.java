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
    protected Location location;

    abstract void spread(World world);

    public Plant(double spreadProbability, double energy) {
        this.spreadProbability = spreadProbability;
        this.energy = energy;
        this.neighbours = null;
    }

    public void onEaten(World world) {
        world.delete(this);
    }

    @Override
    public double getNutritionalValue() {
        return this.energy;
    }

    @Override
    public Type getType() {
        return Type.PLANT;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}