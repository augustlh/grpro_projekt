package behaviours.plants;

import datatypes.Plant;
import datatypes.Species;
import itumulator.executable.DisplayInformation;
import itumulator.world.World;
import itumulator.world.Location;

import java.awt.Color;
import java.util.Random;

public class Bush extends Plant {
    private int berryCount;
    Random rand = new Random();

    public Bush(World world, Location location) {
        super(Species.BerryBush,.25, 1.5);
        world.setTile(location, this);
        berryCount = 0;
    }

    @Override
    public void act(World world) {
        grow(world);
    }

    @Override
    public void grow(World world) {
        if(berryCount<4){
            if(rand.nextDouble() <= this.spreadProbability){
                berryCount++;
            }
        }
    }

    @Override
    public DisplayInformation getInformation() {
        if(berryCount > 0) {
            return new DisplayInformation(Color.green, "bush-berries");
        }
        return new DisplayInformation(Color.magenta, "bush");
    }
    @Override
    public void onConsume(World world) {
        berryCount--;
    }

    public int getBerryCount() {
        return berryCount;
    }
}
