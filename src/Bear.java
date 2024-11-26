import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.Random;
import java.util.List;


public class Bear extends Animal {

    public Bear(World world, Location location) {
        super(Species.Bear, new Random().nextDouble(), new Random().nextDouble(1, 2), 1);
        world.setTile(location, this);
    }

    @Override
    public void act(World world) {

    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.YELLOW, "bear");
    }



}
