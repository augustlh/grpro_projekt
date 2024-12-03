package behaviours.plants;

import datatypes.Plant;
import datatypes.Species;
import help.Utils;
import itumulator.executable.DisplayInformation;
import itumulator.world.World;
import itumulator.world.Location;

import java.awt.Color;

public class Bush extends Plant {
    private int berryCount;

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
        super.grow(world);

        if(berryCount < 4){
            if(Utils.random.nextDouble() <= this.spreadProbability){
                berryCount++;
            }
        }
    }

    @Override
    public void onConsume(World world) {
        berryCount = 0;
    }

    @Override
    public boolean canBeEaten() {
        return berryCount > 0;
    }

    @Override
    public double getNutritionalValue() {
        return this.nutritionalValue * berryCount;
    }

    public int getBerryCount() {
        return berryCount;
    }

    @Override
    public DisplayInformation getInformation() {
        if(berryCount > 3) {
            return new DisplayInformation(Color.green, "mc-bush-many");
        }
        if(berryCount > 0) {
            return new DisplayInformation(Color.green, "mc-bush-few");
        }
        return new DisplayInformation(Color.magenta, "mc-bush");
    }

}
