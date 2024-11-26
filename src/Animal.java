import itumulator.world.Location;
import itumulator.world.World;

import java.util.Currency;
import java.util.Random;
import java.util.Set;

public abstract class Animal extends Organism {
    protected int age;
    protected double energy;
    protected double metabolism;
    protected double energyDecay;
    protected int searchRadius;

    public Animal(Species species, double metabolism, double energyDecay, int searchRadius) {
        super(species);
        this.age = 0;
        this.energy = 20;
        this.metabolism = metabolism;
        this.energyDecay = energyDecay;
        this.searchRadius = searchRadius;
    }

    @Override
    public double getNutritionalValue() {
        return this.energy * (Math.min(this.age, 6) / 6.0);
    }

    public void consume(Organism other, World world) {
        this.energy += other.getNutritionalValue();
        other.onConsume(world);
    }

    @Override
    public void onConsume(World world) {
        world.delete(this);
    }

    protected void wander(World world) {
        Set<Location> emptyNeighbours = world.getEmptySurroundingTiles();
        Location newLocation = null;
        if(emptyNeighbours.isEmpty()) {
            return;
        }

        newLocation = (Location) emptyNeighbours.toArray()[new Random().nextInt(emptyNeighbours.size())];
        world.move(this, newLocation);
    }

    protected void pursue(World world, Location location) {
        Location currentLocation = world.getLocation(this);

        int x = currentLocation.getX();
        int y = currentLocation.getY();

        if(x < location.getX()) {
            x++;
        } else if(x > location.getX()) {
            x--;
        }

        if(y < location.getY()) {
            y++;
        } else if(y > location.getY()){
            y--;
        }

        Location newLocation = new Location(x, y);
        if(world.isTileEmpty(newLocation)) {
            world.move(this, newLocation);
        }
    }
}