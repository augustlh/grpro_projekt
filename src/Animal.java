import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

abstract class Animal implements Eatable, Actor, DynamicDisplayInformationProvider {
    protected int age;
    protected double energy;
    protected double metabolism;
    protected double energyDecay;
    protected Location location;

    public Animal(double metabolism, double energyDecay) {
        this.metabolism = metabolism;
        this.energyDecay = energyDecay;
    }

    abstract void eat(Eatable other, World world);

    public void eaten(World world) {
        world.delete(this);
    }

    @Override
    public Type getType() {
        return Type.ANIMAL;
    }

    public Location getLocation() {return location;}

    public void setLocation(Location location) {this.location = location;}
}